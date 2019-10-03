package ai.bitflow.bitwise.wallet.utils;

import java.nio.charset.StandardCharsets;

public class CipherUtil {

    /**
     * Converts String to UTF8 bytes
     * @param input the input string
     * @return UTF8 bytes
     */
    public static byte[] getUTF8Bytes(String input) {
        return input.getBytes(StandardCharsets.UTF_8);
    }
    
}
