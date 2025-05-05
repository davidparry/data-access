package ai.qodo.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class Utils {

    public byte[] getBytesOfTwoString(String first, String second) {
        try {
            byte[] firstArray = IOUtils.toByteArray(first);
            byte[] secondArray = IOUtils.toByteArray(second);
            return new byte[firstArray.length + secondArray.length];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
