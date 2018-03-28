package edu.gatech.cs2340.vaspa.buzzshelter.util;

/**
 * Class definition for util class Encryption.
 *
 * @author Aniruddha Das
 * @version 6.9
 */
public class Encryption {
    private static final byte encryptionKey = 0x7A;
    /**
     * Takes in a string, encodes it and returns encoded string
     *
     * @param password String to be encoded
     * @return encoded string
     */
    public static String encode(String password) {
        char[] encodedString = new char[password.length()];
        for (int i = 0; i < password.length(); i++) {
            encodedString[i] = (char) ((byte)password.charAt(i) ^ encryptionKey);
        }
        return new String(encodedString);
    }

    /**
     * Takes in a string, decodes it and returns decoded string
     *
     * @param password String to be decoded
     * @return decoded string
     */
    public static String decode(String password) {
        char[] decodedString = new char[password.length()];
        for (int i = 0; i < password.length(); i++) {
            decodedString[i] = (char) ((byte)password.charAt(i) ^ encryptionKey);
        }
        return new String(decodedString);
    }
}
