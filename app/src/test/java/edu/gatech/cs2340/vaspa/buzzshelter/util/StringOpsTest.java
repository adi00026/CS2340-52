package edu.gatech.cs2340.vaspa.buzzshelter.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Class definition for test of util class StringOps. Used to test some of the StringOps methods.
 *
 * @author Vishnu Kaushik
 * @version 0.1
 */
public class StringOpsTest {

    @Test(expected = NullPointerException.class)
    public void testFuzzySearchContainsNull() {
        assertTrue(StringOps.fuzzySearchContains(null, null));
    }

    @Test
    public void testFuzzySearchContains() {
        // This method has full branch coverage.

        String text = "mr blue";
        String pattern = "blue";
        assertTrue(StringOps.fuzzySearchContains(text, pattern));

        pattern = "";
        assertTrue(StringOps.fuzzySearchContains(text, pattern));

        pattern = "this is a long pattern my friend.";
        assertFalse(StringOps.fuzzySearchContains(text, pattern));

        text = "mr blue sky is living in the day";
        pattern = "ms blue sky is the beast day";
        assertTrue(StringOps.fuzzySearchContains(text, pattern));


        text = "this isn't blue enough the heck.";
        assertFalse(StringOps.fuzzySearchContains(text, pattern));
    }

    @Test(expected = NullPointerException.class)
    public void testparseCapacityNull() {
        assertTrue(StringOps.parseCapacity(null));
    }

    @Test
    public void testparseCapacity() {
        //Checking to see if empty string returns 1000
        String emptyStr = "";
        assertEquals(1000, StringOps.parseCapacity(emptyStr));

        //Checking with valid strings
        String text = "cs2340 is the number 1 class";
        assertEquals(2341, StringOps.parseCapacity(text));

        text = "2340 5 1    ";
        assertEquals(2346, StringOps.parseCapacity(text));

        text = "2340 5 100 puttingnumbers312931inwords";
        assertEquals(2445, StringOps.parseCapacity(text));

    }

}