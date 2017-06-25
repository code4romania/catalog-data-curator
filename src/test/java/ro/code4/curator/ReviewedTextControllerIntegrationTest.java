package ro.code4.curator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
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
import ro.code4.curator.entity.ReviewedText;
import ro.code4.curator.transferObjects.ParsedTextTO;
import ro.code4.curator.transferObjects.ShallowReviewedTextTO;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ro.code4.curator.TestUtils.parseJsonObj;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@org.springframework.transaction.annotation.Transactional(noRollbackFor = {})
public class ReviewedTextControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockData testData;

    @Test
    @WithMockUser
    public void postThenGetFinding_verifyValidDataIsPersisted() throws Exception {
        ParsedTextTO finding1 = TestUtils.getParsedInputTO(
                "/testData/DNA_1100_parseResult_parser1.json");

        final ParsedTextTO[] persistedF1 = addFinding(finding1);

        ReviewedText review = TestUtils.buildReviewedInputFromFile(
                "/testData/DNA_1100_parseResult_reviewed_usr1.json");

        final ParsedTextTO[] persistedR1 = doReview(persistedF1, toJson(review));

        final ShallowReviewedTextTO[] foundR1 = getReview(persistedR1);

//        assertEquals(finding1.getText().getFullText(), persistedR1[0].getText().getFullText());
//        assertEquals(review.getText().getFullText(), persistedR1[0].getText().getFullText());
        assertEquals(foundR1[0].getTextSourceId(), persistedR1[0].getTextSourceId());
        assertEquals(foundR1[0].getTextType(), persistedR1[0].getTextType());

        // delete review
        deleteReview(foundR1[0]);
    }


    @Test
    @WithMockUser
    @Ignore
    public void postThenGetFinding_verifyValidDataIsPersisted_reviewMergeNotSupportedYet() throws Exception {
        ParsedTextTO finding1 = TestUtils.getParsedInputTO(
                "/testData/DNA_1100_parseResult_parser1.json");
        final ParsedTextTO[] persistedF1 = addFinding(finding1);
        ParsedTextTO finding2 = TestUtils.getParsedInputTO(
                "/testData/DNA_1100_parseResult_parser2.json");
        final ParsedTextTO[] persistedF2 = addFinding(finding2);

        ReviewedText review = TestUtils.buildReviewedInputFromFile(
                "/testData/DNA_1100_parseResult_reviewed_usr1.json");

        String content = toJson(review);
        final ParsedTextTO[] persistedR1 = doReview(persistedF1, content);
        final ParsedTextTO[] persistedR2 = doReview(persistedF2, content);
        final ShallowReviewedTextTO[] foundR1 = getReview(persistedR1);
        final ShallowReviewedTextTO[] foundR2 = getReview(persistedR2);

//        assertEquals(finding1.getText().getFullText(), persistedR1[0].getText().getFullText());
//        assertEquals(review.getText().getFullText(), persistedR1[0].getText().getFullText());
//        assertEquals(review.getText().getFullText(), persistedR2[0].getText().getFullText());
        assertEquals(foundR1[0].getTextSourceId(), persistedR1[0].getTextSourceId());
        assertEquals(foundR1[0].getTextType(), persistedR1[0].getTextType());
        assertEquals(foundR1[1].getTextSourceId(), persistedR1[1].getTextSourceId());
        assertEquals(foundR1[1].getTextType(), persistedR1[1].getTextType());

        // delete review
        deleteReview(foundR1[0]);
        deleteReview(foundR2[0]);
    }

    private ShallowReviewedTextTO[] getReview(ParsedTextTO[] persistedR1) throws Exception {
        ShallowReviewedTextTO[] foundR1 = {null};
        mockMvc.perform(
                get("/input/reviewed/" + persistedR1[0].getEntityId()))
                .andDo(result ->
                        foundR1[0] = parseJsonObj(getContentAsString(result), ShallowReviewedTextTO.class))
                .andExpect(status().isOk());
        return foundR1;
    }

    private void deleteReview(ShallowReviewedTextTO shallowReviewedInputTO) throws Exception {
        mockMvc.perform(
                delete("/input/reviewed/" + shallowReviewedInputTO.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }

    private ParsedTextTO[] doReview(ParsedTextTO[] persistedF1, String content) throws Exception {
        ParsedTextTO[] persistedReview = {null};
        mockMvc.perform(
                post("/input/reviewed/"+persistedF1[0].getEntityId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(result -> persistedReview[0] = parseJsonObj(getContentAsString(result), ParsedTextTO.class))
                .andExpect(status().isOk());
        return persistedReview;
    }

    private ParsedTextTO[] addFinding(ParsedTextTO originalFinding) throws Exception {
        final ParsedTextTO[] persistedFinding = {null};
        mockMvc.perform(
                post("/input/parsed/")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(originalFinding)))
                .andDo(result -> persistedFinding[0] = parseJsonObj(getContentAsString(result), ParsedTextTO.class))
                .andExpect(status().isOk());
        return persistedFinding;
    }

    private String getContentAsString(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    private String toJson(Object finding) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(finding);
    }

}
