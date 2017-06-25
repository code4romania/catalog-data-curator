package ro.code4.curator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ro.code4.curator.config.MockData;
import ro.code4.curator.converter.FileUtils;
import ro.code4.curator.converter.JsonUtils;
import ro.code4.curator.transferObjects.ParsedTextTO;

import java.io.File;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@org.springframework.transaction.annotation.Transactional(noRollbackFor = {})
public class ParsedTextControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockData testData;

    @Test
    @WithMockUser
    public void getAllFindings() throws Exception {
        mockMvc.perform(
                get("/input/parsed"))
                .andDo(result -> {
                    ParsedTextTO[] results = parseJsonArray(getContentAsString(result));
                    // depends on the mock data size
//                    assertEquals("all test data should be retrieved",
//                            testData.mockParsedInputs.size(), results.length);
                    assertTrue("all test data should be retrieved",
                            results.length > 1);
                    assertTrue("all parsed fields should be returned",
                            results[0].getParsedFields().size() > 0);
                })
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void getFindings_byId_found() throws Exception {
        int id = testData.mockParsedInputs.get(0).getId();
        mockMvc.perform(
                get("/input/parsed/" + id))
                .andDo(result -> {
                    ParsedTextTO to = parseJsonObj(getContentAsString(result));
                    assertEquals("review should be found",
                            id, to.getEntityId());
                })
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void getFindings_byId_notFound() throws Exception {
        int nonExistingId = testData.mockParsedInputs.get(0).getId() + 1000;
        mockMvc.perform(
                get("/input/parsed/" + nonExistingId))
                .andDo(result -> {
                    String json = getContentAsString(result);
                    assertTrue("review should be found", isEmptyString(json));
                })
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void postThenGetFinding_verifyValidDataIsPersisted() throws Exception {
        String json = FileUtils.readFile("/testData/DIICOT_1100_parseResult_parser1.json");
        ParsedTextTO originalFinding = JsonUtils.parseJsonObj(json, ParsedTextTO.class);
        final ParsedTextTO[] persistedFinding = {null};
        final ParsedTextTO[] foundFinding = {null};

        // add new finding
        mockMvc.perform(
                post("/input/parsed/")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(originalFinding)))
                .andDo(result -> persistedFinding[0] = parseJsonObj(getContentAsString(result)))
                .andExpect(status().isOk());

        // get finding
        int id = persistedFinding[0].getEntityId();
        mockMvc.perform(
                get("/input/parsed/" + id))
                .andDo(result -> {
                    foundFinding[0] = parseJsonObj(getContentAsString(result));
                })
                .andExpect(status().isOk());

        assertEquals("should be fully equal",
                foundFinding[0], persistedFinding[0]);
//        assertEquals("all equal except entityId",
//                foundFinding[0].getFullText(), originalFinding.getFullText());
        assertEquals("all equal except entityId",
                3, foundFinding[0].getParsedFields().size());
        assertEquals("all equal except entityId",
                foundFinding[0].getParsedFields(), originalFinding.getParsedFields());
        assertEquals("all equal except entityId",
                foundFinding[0].getTextSourceId(), originalFinding.getTextSourceId());
        assertEquals("all equal except entityId",
                foundFinding[0].getTextType(), originalFinding.getTextType());


        // delete finding
        mockMvc.perform(
                delete("/input/parsed/" + id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }

    private String getContentAsString(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    private File getFile(String name) {
        String path = ParsedTextControllerIntegrationTest.class.getResource(name).getFile();
        return new File(path);
    }

    private String toJson(ParsedTextTO finding) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(finding);
    }

    private boolean isEmptyString(String json) {
        return json == null || "".equals(json.trim());
    }

    private ParsedTextTO parseJsonObj(String json) throws java.io.IOException {
        return new ObjectMapper().readValue(json, ParsedTextTO.class);
    }

    private ParsedTextTO[] parseJsonArray(String json) throws java.io.IOException {
        return new ObjectMapper().readValue(json, ParsedTextTO[].class);
    }
}
