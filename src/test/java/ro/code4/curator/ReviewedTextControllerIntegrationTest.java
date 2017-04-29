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
import ro.code4.curator.entity.ReviewedInput;
import ro.code4.curator.transferObjects.ParsedInputTO;
import ro.code4.curator.transferObjects.ShallowReviewedInputTO;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
        ParsedInputTO finding1 = TestUtils.getParsedInputTO(
                "/testData/parsedInput_DNA_1100_parser1.json");

        final ParsedInputTO[] persistedF1 = addFinding(finding1);

        ReviewedInput review = TestUtils.buildReviewedInputFromFile(
                "/testData/reviewedInput_DNA_1100.json");

        final ParsedInputTO[] persistedR1 = doReview(persistedF1, toJson(review));

        final ShallowReviewedInputTO[] foundR1 = getReview(persistedR1);

        assertEquals(finding1.getFullText(), persistedR1[0].getFullText());
        assertEquals(review.getFullText(), persistedR1[0].getFullText());
        assertEquals(foundR1[0].getTextSourceId(), persistedR1[0].getTextSourceId());
        assertEquals(foundR1[0].getTextType(), persistedR1[0].getTextType());

        // delete review
        deleteReview(foundR1[0]);
    }


    @Test
    @WithMockUser
    @Ignore
    public void postThenGetFinding_verifyValidDataIsPersisted_reviewMergeNotSupportedYet() throws Exception {
        ParsedInputTO finding1 = TestUtils.getParsedInputTO(
                "/testData/parsedInput_DNA_1100_parser1.json");
        final ParsedInputTO[] persistedF1 = addFinding(finding1);
        ParsedInputTO finding2 = TestUtils.getParsedInputTO(
                "/testData/parsedInput_DNA_1100_parser2.json");
        final ParsedInputTO[] persistedF2 = addFinding(finding2);

        ReviewedInput review = TestUtils.buildReviewedInputFromFile(
                "/testData/reviewedInput_DNA_1100.json");

        String content = toJson(review);
        final ParsedInputTO[] persistedR1 = doReview(persistedF1, content);
        final ParsedInputTO[] persistedR2 = doReview(persistedF2, content);
        final ShallowReviewedInputTO[] foundR1 = getReview(persistedR1);
        final ShallowReviewedInputTO[] foundR2 = getReview(persistedR2);

        assertEquals(finding1.getFullText(), persistedR1[0].getFullText());
        assertEquals(review.getFullText(), persistedR1[0].getFullText());
        assertEquals(review.getFullText(), persistedR2[0].getFullText());
        assertEquals(foundR1[0].getTextSourceId(), persistedR1[0].getTextSourceId());
        assertEquals(foundR1[0].getTextType(), persistedR1[0].getTextType());
        assertEquals(foundR1[1].getTextSourceId(), persistedR1[1].getTextSourceId());
        assertEquals(foundR1[1].getTextType(), persistedR1[1].getTextType());

        // delete review
        deleteReview(foundR1[0]);
        deleteReview(foundR2[0]);
    }

    private ShallowReviewedInputTO[] getReview(ParsedInputTO[] persistedR1) throws Exception {
        ShallowReviewedInputTO[] foundR1 = {null};
        mockMvc.perform(
                get("/input/reviewed/" + persistedR1[0].getEntityId()))
                .andDo(result ->
                        foundR1[0] = parseJsonObj(getContentAsString(result), ShallowReviewedInputTO.class))
                .andExpect(status().isOk());
        return foundR1;
    }

    private void deleteReview(ShallowReviewedInputTO shallowReviewedInputTO) throws Exception {
        mockMvc.perform(
                delete("/input/reviewed/" + shallowReviewedInputTO.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }

    private ParsedInputTO[] doReview(ParsedInputTO[] persistedF1, String content) throws Exception {
        ParsedInputTO[] persistedReview = {null};
        mockMvc.perform(
                post("/input/reviewed/"+persistedF1[0].getEntityId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(result -> persistedReview[0] = parseJsonObj(getContentAsString(result), ParsedInputTO.class))
                .andExpect(status().isOk());
        return persistedReview;
    }

    private ParsedInputTO[] addFinding(ParsedInputTO originalFinding) throws Exception {
        final ParsedInputTO[] persistedFinding = {null};
        mockMvc.perform(
                post("/input/parsed/")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(originalFinding)))
                .andDo(result -> persistedFinding[0] = parseJsonObj(getContentAsString(result), ParsedInputTO.class))
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
