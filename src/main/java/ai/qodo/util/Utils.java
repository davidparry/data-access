package ai.qodo.util;

import java.nio.charset.StandardCharsets;

public class Utils {

    public byte[] getBytesOfTwoString(String first, String second) {
        try {
            // Convert strings to byte arrays using UTF-8 encoding
            byte[] firstArray = first.getBytes(StandardCharsets.UTF_8);
            byte[] secondArray = second.getBytes(StandardCharsets.UTF_8);

            // Concatenate the two arrays
            byte[] result = new byte[firstArray.length + secondArray.length];
            System.arraycopy(firstArray, 0, result, 0, firstArray.length);
            System.arraycopy(secondArray, 0, result, firstArray.length, secondArray.length);

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
