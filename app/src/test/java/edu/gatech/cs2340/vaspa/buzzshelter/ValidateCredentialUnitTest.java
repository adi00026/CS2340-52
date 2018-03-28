package edu.gatech.cs2340.vaspa.buzzshelter;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import edu.gatech.cs2340.vaspa.buzzshelter.util.Validation;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ValidateCredentialUnitTest {
    @Test
    public void monthAboveBounds() throws Exception {
        assertFalse(isValidDate(13, 0, 1990));
        assertFalse(isValidDate(14, 5, 1990));
        assertFalse(isValidDate(15, 22, 1990));
        assertFalse(isValidDate(16, 33, 1990));
    }

    @Test
    public void monthBelowBounds() throws Exception {
        assertFalse(isValidDate(0, 0, 1990));
        assertFalse(isValidDate(-1, 5, 1990));
        assertFalse(isValidDate(-2, 22, 1990));
        assertFalse(isValidDate(-3, 33, 1990));
    }

    @Test
    public void monthValidBounds() throws Exception {
        assertFalse(isValidDate(1, 0, 1990));
        assertTrue(isValidDate(2, 2, 1990));
        assertTrue(isValidDate(3, 22, 1990));
        assertFalse(isValidDate(4, 33, 1990));
    }

    @Test
    public void dayBelowBounds() throws Exception {
        assertFalse(isValidDate(0, 0, 1990));
        assertFalse(isValidDate(2, -1, 1990));
        assertFalse(isValidDate(3, -2, 1990));
        assertFalse(isValidDate(14, -3, 1990));
    }

    @Test
    public void dayAboveBounds() throws Exception {
        // Non leap year
        assertFalse(isValidDate(0, 32, 1991));
        assertFalse(isValidDate(1, 32, 1991));
        assertFalse(isValidDate(2, 29, 1991));
        assertFalse(isValidDate(3, 32, 1991));
        assertFalse(isValidDate(4, 31, 1991));
        assertFalse(isValidDate(5, 32, 1991));
        assertFalse(isValidDate(6, 31, 1991));
        assertFalse(isValidDate(7, 32, 1991));
        assertFalse(isValidDate(8, 32, 1991));
        assertFalse(isValidDate(9, 31, 1991));
        assertFalse(isValidDate(10, 32, 1991));
        assertFalse(isValidDate(11, 31, 1991));
        assertFalse(isValidDate(12, 32, 1991));

        // Leap year
        assertFalse(isValidDate(0, 32, 1992));
        assertFalse(isValidDate(1, 32, 1992));
        assertFalse(isValidDate(2, 30, 1992));
        assertFalse(isValidDate(3, 32, 1992));
        assertFalse(isValidDate(4, 31, 1992));
        assertFalse(isValidDate(5, 32, 1992));
        assertFalse(isValidDate(6, 31, 1992));
        assertFalse(isValidDate(7, 32, 1992));
        assertFalse(isValidDate(8, 32, 1992));
        assertFalse(isValidDate(9, 31, 1992));
        assertFalse(isValidDate(10, 32, 1992));
        assertFalse(isValidDate(11, 31, 1992));
        assertFalse(isValidDate(12, 32, 1992));
    }

    @Test
    public void dayValidBoundsEdge() throws Exception {
        // Non leap year
        assertFalse(isValidDate(0, 31, 1991));
        assertTrue(isValidDate(1, 31, 1991));
        assertTrue(isValidDate(2, 28, 1991));
        assertTrue(isValidDate(3, 31, 1991));
        assertTrue(isValidDate(4, 30, 1991));
        assertTrue(isValidDate(5, 31, 1991));
        assertTrue(isValidDate(6, 30, 1991));
        assertTrue(isValidDate(7, 31, 1991));
        assertTrue(isValidDate(8, 31, 1991));
        assertTrue(isValidDate(9, 30, 1991));
        assertTrue(isValidDate(10, 31, 1991));
        assertTrue(isValidDate(11, 30, 1991));
        assertTrue(isValidDate(12, 31, 1991));

        // Leap year
        assertFalse(isValidDate(0, 31, 1992));
        assertTrue(isValidDate(1, 31, 1992));
        assertTrue(isValidDate(2, 29, 1992));
        assertTrue(isValidDate(3, 31, 1992));
        assertTrue(isValidDate(4, 30, 1992));
        assertTrue(isValidDate(5, 31, 1992));
        assertTrue(isValidDate(6, 30, 1992));
        assertTrue(isValidDate(7, 31, 1992));
        assertTrue(isValidDate(8, 31, 1992));
        assertTrue(isValidDate(9, 30, 1992));
        assertTrue(isValidDate(10, 31, 1992));
        assertTrue(isValidDate(11, 30, 1992));
        assertTrue(isValidDate(12, 31, 1992));
    }

    @Test
    public void dayValidBoundsNonEdge() throws Exception {
        // Non leap year
        assertFalse(isValidDate(0, 1, 1991));
        assertTrue(isValidDate(1, 1, 1991));
        assertTrue(isValidDate(2, 1, 1991));
        assertTrue(isValidDate(3, 1, 1991));
        assertTrue(isValidDate(4, 1, 1991));
        assertTrue(isValidDate(5, 1, 1991));
        assertTrue(isValidDate(6, 1, 1991));
        assertTrue(isValidDate(7, 1, 1991));
        assertTrue(isValidDate(8, 1, 1991));
        assertTrue(isValidDate(9, 1, 1991));
        assertTrue(isValidDate(10, 1, 1991));
        assertTrue(isValidDate(11, 1, 1991));
        assertTrue(isValidDate(12, 1, 1991));

        // Leap year
        assertFalse(isValidDate(0, 1, 1992));
        assertTrue(isValidDate(1, 1, 1992));
        assertTrue(isValidDate(2, 1, 1992));
        assertTrue(isValidDate(3, 1, 1992));
        assertTrue(isValidDate(4, 1, 1992));
        assertTrue(isValidDate(5, 1, 1992));
        assertTrue(isValidDate(6, 1, 1992));
        assertTrue(isValidDate(7, 1, 1992));
        assertTrue(isValidDate(8, 1, 1992));
        assertTrue(isValidDate(9, 1, 1992));
        assertTrue(isValidDate(10, 1, 1992));
        assertTrue(isValidDate(11, 1, 1992));
        assertTrue(isValidDate(12, 1, 1992));
    }

    /**
     * Helper method to check Validation's isValidDate method. Method was changed, but it is too
     * much to change every line to reflect this.
     * 
     * @param month
     * @param day
     * @param year
     * @return
     */
    private boolean isValidDate(int month, int day, int year) {
        List<Integer> dob = new LinkedList<>();
        dob.add(month);
        dob.add(day);
        dob.add(year);
        return Validation.isValidDate(dob);
    }
}