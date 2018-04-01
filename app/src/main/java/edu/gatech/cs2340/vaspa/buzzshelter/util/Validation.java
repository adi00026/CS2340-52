package edu.gatech.cs2340.vaspa.buzzshelter.util;

import java.util.List;

/**
 * Class definition for util class Validation.
 *
 * @author Sanath Nagaraj
 * @version 6.9
 */
@SuppressWarnings("UtilityClass")
public class Validation {
    /**
     * Method to validate an entered date.
     *
     * @param date list formatted date to validate. Date is in the format {month, day, year}.
     * @return Whether the date is valid
     */
    public static boolean isValidDate(List<Integer> date) {
        int[] dateArr = {-1, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int month = date.get(0);
        int day = date.get(1);
        int year = date.get(2);
        if ((year % 4) == 0) {
            dateArr[2] = 29;
        }
        return !((month < 1) || (month > 12)) && !((day < 1) || (day > dateArr[month]));
    }
}
