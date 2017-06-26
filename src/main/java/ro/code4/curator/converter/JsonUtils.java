package ro.code4.curator.converter;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created on 4/30/17.
 */
public class JsonUtils {

    public static <T> T parseJsonObj(String json, Class<T> clazz)  throws IOException {
        return new ObjectMapper().readValue(json, clazz);
    }

}
