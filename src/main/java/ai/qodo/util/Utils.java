package ai.qodo.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Utils {
    public static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    public static String getResourceAsString(String resource) {
        try {
            return IOUtils.toString(Utils.class.getResourceAsStream(resource), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Failed to read resource: {}", resource, e);
            return "";
        }
    }
}