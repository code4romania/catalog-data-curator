package ro.code4.curator.converter;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created on 4/30/17.
 */
public class FileUtils {

    public static String readFile(String name) {
        URL resource = FileUtils.class.getResource(name);
        if (resource == null)
            resource = FileUtils.class.getResource("/" + name);
        String path = resource.getFile();
        try {
            return org.apache.commons.io.FileUtils.readFileToString(new File(path), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file " + name);
        }
    }

}
