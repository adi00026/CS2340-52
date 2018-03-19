package edu.gatech.cs2340.vaspa.buzzshelter;

import org.junit.Test;

import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;
import edu.gatech.cs2340.vaspa.buzzshelter.util.Validation;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ValidateCredentialUnitTest {
    @Test
    public void monthAboveBounds() throws Exception {
        assertFalse(Validation.isValidDate(13, 0, 1990));
        assertFalse(Validation.isValidDate(14, 5, 1990));
        assertFalse(Validation.isValidDate(15, 22, 1990));
        assertFalse(Validation.isValidDate(16, 33, 1990));
    }

    @Test
    public void monthBelowBounds() throws Exception {
        assertFalse(Validation.isValidDate(0, 0, 1990));
        assertFalse(Validation.isValidDate(-1, 5, 1990));
        assertFalse(Validation.isValidDate(-2, 22, 1990));
        assertFalse(Validation.isValidDate(-3, 33, 1990));
    }

    @Test
    public void monthValidBounds() throws Exception {
        assertFalse(Validation.isValidDate(1, 0, 1990));
        assertTrue(Validation.isValidDate(2, 2, 1990));
        assertTrue(Validation.isValidDate(3, 22, 1990));
        assertFalse(Validation.isValidDate(4, 33, 1990));
    }

    @Test
    public void dayBelowBounds() throws Exception {
        assertFalse(Validation.isValidDate(0, 0, 1990));
        assertFalse(Validation.isValidDate(2, -1, 1990));
        assertFalse(Validation.isValidDate(3, -2, 1990));
        assertFalse(Validation.isValidDate(14, -3, 1990));
    }

    @Test
    public void dayAboveBounds() throws Exception {
        // Non leap year
        assertFalse(Validation.isValidDate(0, 32, 1991));
        assertFalse(Validation.isValidDate(1, 32, 1991));
        assertFalse(Validation.isValidDate(2, 29, 1991));
        assertFalse(Validation.isValidDate(3, 32, 1991));
        assertFalse(Validation.isValidDate(4, 31, 1991));
        assertFalse(Validation.isValidDate(5, 32, 1991));
        assertFalse(Validation.isValidDate(6, 31, 1991));
        assertFalse(Validation.isValidDate(7, 32, 1991));
        assertFalse(Validation.isValidDate(8, 32, 1991));
        assertFalse(Validation.isValidDate(9, 31, 1991));
        assertFalse(Validation.isValidDate(10, 32, 1991));
        assertFalse(Validation.isValidDate(11, 31, 1991));
        assertFalse(Validation.isValidDate(12, 32, 1991));

        // Leap year
        assertFalse(Validation.isValidDate(0, 32, 1992));
        assertFalse(Validation.isValidDate(1, 32, 1992));
        assertFalse(Validation.isValidDate(2, 30, 1992));
        assertFalse(Validation.isValidDate(3, 32, 1992));
        assertFalse(Validation.isValidDate(4, 31, 1992));
        assertFalse(Validation.isValidDate(5, 32, 1992));
        assertFalse(Validation.isValidDate(6, 31, 1992));
        assertFalse(Validation.isValidDate(7, 32, 1992));
        assertFalse(Validation.isValidDate(8, 32, 1992));
        assertFalse(Validation.isValidDate(9, 31, 1992));
        assertFalse(Validation.isValidDate(10, 32, 1992));
        assertFalse(Validation.isValidDate(11, 31, 1992));
        assertFalse(Validation.isValidDate(12, 32, 1992));
    }

    @Test
    public void dayValidBoundsEdge() throws Exception {
        // Non leap year
        assertFalse(Validation.isValidDate(0, 31, 1991));
        assertTrue(Validation.isValidDate(1, 31, 1991));
        assertTrue(Validation.isValidDate(2, 28, 1991));
        assertTrue(Validation.isValidDate(3, 31, 1991));
        assertTrue(Validation.isValidDate(4, 30, 1991));
        assertTrue(Validation.isValidDate(5, 31, 1991));
        assertTrue(Validation.isValidDate(6, 30, 1991));
        assertTrue(Validation.isValidDate(7, 31, 1991));
        assertTrue(Validation.isValidDate(8, 31, 1991));
        assertTrue(Validation.isValidDate(9, 30, 1991));
        assertTrue(Validation.isValidDate(10, 31, 1991));
        assertTrue(Validation.isValidDate(11, 30, 1991));
        assertTrue(Validation.isValidDate(12, 31, 1991));

        // Leap year
        assertFalse(Validation.isValidDate(0, 31, 1992));
        assertTrue(Validation.isValidDate(1, 31, 1992));
        assertTrue(Validation.isValidDate(2, 29, 1992));
        assertTrue(Validation.isValidDate(3, 31, 1992));
        assertTrue(Validation.isValidDate(4, 30, 1992));
        assertTrue(Validation.isValidDate(5, 31, 1992));
        assertTrue(Validation.isValidDate(6, 30, 1992));
        assertTrue(Validation.isValidDate(7, 31, 1992));
        assertTrue(Validation.isValidDate(8, 31, 1992));
        assertTrue(Validation.isValidDate(9, 30, 1992));
        assertTrue(Validation.isValidDate(10, 31, 1992));
        assertTrue(Validation.isValidDate(11, 30, 1992));
        assertTrue(Validation.isValidDate(12, 31, 1992));
    }

    @Test
    public void dayValidBoundsNonEdge() throws Exception {
        // Non leap year
        assertFalse(Validation.isValidDate(0, 1, 1991));
        assertTrue(Validation.isValidDate(1, 1, 1991));
        assertTrue(Validation.isValidDate(2, 1, 1991));
        assertTrue(Validation.isValidDate(3, 1, 1991));
        assertTrue(Validation.isValidDate(4, 1, 1991));
        assertTrue(Validation.isValidDate(5, 1, 1991));
        assertTrue(Validation.isValidDate(6, 1, 1991));
        assertTrue(Validation.isValidDate(7, 1, 1991));
        assertTrue(Validation.isValidDate(8, 1, 1991));
        assertTrue(Validation.isValidDate(9, 1, 1991));
        assertTrue(Validation.isValidDate(10, 1, 1991));
        assertTrue(Validation.isValidDate(11, 1, 1991));
        assertTrue(Validation.isValidDate(12, 1, 1991));

        // Leap year
        assertFalse(Validation.isValidDate(0, 1, 1992));
        assertTrue(Validation.isValidDate(1, 1, 1992));
        assertTrue(Validation.isValidDate(2, 1, 1992));
        assertTrue(Validation.isValidDate(3, 1, 1992));
        assertTrue(Validation.isValidDate(4, 1, 1992));
        assertTrue(Validation.isValidDate(5, 1, 1992));
        assertTrue(Validation.isValidDate(6, 1, 1992));
        assertTrue(Validation.isValidDate(7, 1, 1992));
        assertTrue(Validation.isValidDate(8, 1, 1992));
        assertTrue(Validation.isValidDate(9, 1, 1992));
        assertTrue(Validation.isValidDate(10, 1, 1992));
        assertTrue(Validation.isValidDate(11, 1, 1992));
        assertTrue(Validation.isValidDate(12, 1, 1992));
    }
}