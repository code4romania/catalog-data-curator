package ro.code4.curator.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created on 4/30/17.
 */
public class JsonUtils {

    public static <T> T parseJsonObj(String json, Class<T> clazz)  throws IOException {
        return new ObjectMapper().readValue(json, clazz);
    }

}
