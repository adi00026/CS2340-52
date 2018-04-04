package edu.gatech.cs2340.vaspa.buzzshelter.util;

/**
 * Class definition for util class Encryption.
 *
 * @author Aniruddha Das
 * @version 6.9
 */
@SuppressWarnings("UtilityClass")
class Encryption {
    private static final byte encryptionKey = 0x7A;
    /**
     * Takes in a string, encodes it and returns encoded string
     * Returns null of passed in parameter is null
     *
     * @param to_encode String to be encoded
     * @return encoded string
     */
    public static String encode(String to_encode) {
        if (to_encode == null) {
            return null;
        }
        char[] encodedString = new char[to_encode.length()];
        for (int i = 0; i < to_encode.length(); i++) {
            encodedString[i] = (char) ((byte)to_encode.charAt(i) ^ encryptionKey);
        }
        return new String(encodedString);
    }

    /**
     * Takes in a string, decodes it and returns decoded string.
     * Returns null if passed in parameter is null
     *
     * @param to_decode String to be decoded
     * @return decoded string
     */
    public static String decode(String to_decode) {
        if (to_decode == null) {
            return null;
        }
        char[] decodedString = new char[to_decode.length()];
        for (int i = 0; i < to_decode.length(); i++) {
            decodedString[i] = (char) ((byte)to_decode.charAt(i) ^ encryptionKey);
        }
        return new String(decodedString);
    }
}
