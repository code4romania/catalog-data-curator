package ro.code4.curator;

import com.fasterxml.jackson.databind.ObjectMapper;
import ro.code4.curator.entity.ReviewedText;
import ro.code4.curator.transferObjects.ParsedTextTO;

import java.io.File;
import java.io.IOException;

/**
 * Created on 4/27/17.
 */
public class TestUtils {

    private TestUtils() {}

    public static ParsedTextTO getParsedInputTO(String name) throws IOException {
        File file = TestUtils.getFile(name);
        return new ObjectMapper().readValue(file, ParsedTextTO.class);
    }

    public static ReviewedText buildReviewedInputFromFile(String name) throws IOException {
        File file = getFile(name);
        return new ObjectMapper().readValue(file, ReviewedText.class);
    }

    public static File getFile(String name) {
        String path = ReviewedTextControllerIntegrationTest.class.getResource(name).getFile();
        return new File(path);
    }

    public static <T> T parseJsonObj(String json, Class<T> clazz)  throws IOException {
        return new ObjectMapper().readValue(json, clazz);
    }

    public static ParsedTextTO[] parseJsonArray(String json) throws IOException {
        return new ObjectMapper().readValue(json, ParsedTextTO[].class);
    }




}
