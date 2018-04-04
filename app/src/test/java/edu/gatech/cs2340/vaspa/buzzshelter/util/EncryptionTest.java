package edu.gatech.cs2340.vaspa.buzzshelter.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Class definition for test of util class Encryption. Used to check encryption and
 * decryption methods
 *
 * @author Aniruddha Das
 * @version 6.9
 */
public class EncryptionTest {
    @Test
    public void checkEncode() {
        String test_string = "Bob Waters";
        String output;
        output = Encryption.encode(test_string);
        assertNotEquals(output, null);
    }
    @Test
    public void checkEncodeEmpty() {
        String test_string = "";
        String output;
        output = Encryption.encode(test_string);
        assertEquals(output, "");
    }
    @Test
    public void checkEncodeNull() {
        assertEquals(Encryption.encode(null), null);
    }
    @Test
    public void checkDecode() {
        String test_string = "Bob Waters";
        String output;
        output = Encryption.decode(test_string);
        assertNotEquals(output, null);
    }
    @Test
    public void checkDecodeEmpty() {
        String test_string = "";
        String output;
        output = Encryption.decode(test_string);
        assertEquals(output, "");
    }
    @Test
    public void checkDecodeNull() {
        assertEquals(Encryption.decode(null), null);
    }
    @Test
    public void checkEncodeDecode() {
        String test_string = "Bob Waters";
        String output = Encryption.encode(test_string);
        assertEquals(test_string, Encryption.decode(output));
    }
}