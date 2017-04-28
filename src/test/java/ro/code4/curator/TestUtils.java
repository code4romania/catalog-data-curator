package ro.code4.curator;

import com.fasterxml.jackson.databind.ObjectMapper;
import ro.code4.curator.entity.ReviewedInput;
import ro.code4.curator.transferObjects.ParsedInputTO;

import java.io.File;
import java.io.IOException;

/**
 * Created on 4/27/17.
 */
public class TestUtils {

    private TestUtils() {}

    public static ParsedInputTO getParsedInputTO(String name) throws IOException {
        File file = TestUtils.getFile(name);
        return new ObjectMapper().readValue(file, ParsedInputTO.class);
    }

    public static ReviewedInput buildReviewedInputFromFile(String name) throws IOException {
        File file = getFile(name);
        return new ObjectMapper().readValue(file, ReviewedInput.class);
    }

    public static File getFile(String name) {
        String path = ReviewedTextControllerIntegrationTest.class.getResource(name).getFile();
        return new File(path);
    }

    public static <T> T parseJsonObj(String json, Class<T> clazz)  throws IOException {
        return new ObjectMapper().readValue(json, clazz);
    }

    public static ParsedInputTO[] parseJsonArray(String json) throws IOException {
        return new ObjectMapper().readValue(json, ParsedInputTO[].class);
    }




}
