package edu.gatech.cs2340.vaspa.buzzshelter.util;

/**
 * Class definition for util class Validation.
 *
 * @author Sanath Nagaraj
 * @version 6.9
 */
public class Validation {
    /**
     * Method to validate an entered date.
     *
     * @param month month of date to validate
     * @param day day of date to validate
     * @param year year of date to validate
     * @return Whether the date is valid
     */
    public static boolean isValidDate(int month, int day, int year) {
        int[] dateArr = {-1, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (year % 4 == 0) {
            dateArr[2] = 29;
        }
        if (month < 1 || month > 12) {
            return false;
        }
        if (day < 1 || day > dateArr[month]) {
            return false;
        }
        return true;
    }
}
