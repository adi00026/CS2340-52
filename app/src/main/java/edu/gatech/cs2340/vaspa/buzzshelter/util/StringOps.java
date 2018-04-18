package edu.gatech.cs2340.vaspa.buzzshelter.util;

import android.util.Log;

/**
 * Class definition for util class StringOps. Class contains String parse and searcb methods.
 *
 * @author Sanath Nagaraj
 * @version 6.9
 */
@SuppressWarnings("UtilityClass")
public class StringOps {
    /**
     * Method to parse the capacity string from the csv. If the entry is empty,
     * -1 is returned. Else the sum of all numbers in the string are returned.
     *
     * @param bullshitCapacity is the BS string from CSV
     * @return an int that is (hopefully) the capacity
     */
    public static int parseCapacity(String bullshitCapacity) {
        // If bullshitCapacity is empty...
        if ("".equals(bullshitCapacity.trim())) {
            return 1000;
        }
        // If bullshitCapacity is not empty...
        String[] split = bullshitCapacity.split(" ");
        int capacity = 0;
        for (String s : split) {
            try {
                capacity += Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                // Log.i("TAG", "Exception caught");
            }
        }
        return capacity;
    }

    /**
     * Method to somewhat fuzzy search text for pattern.
     * This function checks within some probabilistic tolerance
     * whether pattern is potentially intended to search for text.
     *
     * @param text the string sequence to search in
     * @param pattern the string sequence to search for
     * @return whether the string text likely contains pattern
     */
    public static boolean fuzzySearchContains(String text, String pattern) {
        final double FUZZY_THRESHOLD = 0.72; // Fuzzy threshold
        final double ALPHA = 0.85; // Pattern control parameter
        if (text.contains(pattern)) {
            return true;
        }
        if (pattern.isEmpty()) {
            return true;
        }
        if (pattern.length() > text.length()) {
            return false;
        }

        double num = 1.0 * lcs(text, pattern);
        double probMatch = num / (((1 - ALPHA) * text.length()) + (ALPHA * pattern.length()));
        return probMatch > FUZZY_THRESHOLD;
    }

    /**
     * Method to find the largest common subsequence between
     * text and pattern. It returns the size of this subsequence.
     *
     * @param text the string sequence to search in
     * @param pattern the string sequence to search for
     * @return the size of the largest common subsequence of text
     *   and pattern.
     */
    static int lcs(String text, String pattern) {
        int[][] matrix = new int[text.length() + 1][pattern.length() + 1];
        for (int i = 1; i <= text.length(); i++) {
            for (int j = 1; j <= pattern.length(); j++) {
                if (text.charAt(i - 1) == pattern.charAt(j - 1)) {
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                } else {
                    matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);
                }
            }
        }
        return matrix[text.length()][pattern.length()];
    }
}
