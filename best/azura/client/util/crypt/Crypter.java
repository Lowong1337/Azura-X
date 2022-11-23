package best.azura.client.util.crypt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Class used to make Strings secure.
 */
public class Crypter {

    /**
     * Encode the Message to make it secure.
     * @param string the Message
     * @return the secure Message.
     */
    public static String encode(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decode the Message to get the original String.
     * @param string the encoded String.
     * @return the original String.
     */
    public static String decode(String string) {
        return new String(Base64.getDecoder().decode(string));
    }

}
