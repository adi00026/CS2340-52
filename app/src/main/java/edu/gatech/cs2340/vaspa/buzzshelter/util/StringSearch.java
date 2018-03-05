package edu.gatech.cs2340.vaspa.buzzshelter.util;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sanath on 2/26/2018.
 */

public class StringSearch {
    /**
     * Method returns whether text is contained in pattern. It is case sensitive.
     * contains("Male/Female", "Male") --> true
     * contains("Female", "Male") --> false
     *
     * @param pattern the text to search
     * @param text the text to search for
     * @return whether text is in pattern.
     */
    public static boolean contains(String pattern, String text) {
        return kmp(pattern, text,
                new CharacterComparator()).size() != 0;
    }

    /**
     * Knuth-Morris-Pratt (KMP) algorithm that relies on the failure table (also
     * called failure function). Works better with small alphabets.
     *
     * Make sure to implement the failure table before implementing this method.
     *
     * @throws IllegalArgumentException if the pattern is null or of length 0
     * @throws IllegalArgumentException if text or comparator is null
     * @param pattern the pattern you are searching for in a body of text
     * @param text the body of text where you search for pattern
     * @return list containing the starting index for each match found
     */
    private static List<Integer> kmp(CharSequence pattern, CharSequence text,
                                    CharacterComparator comparator) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("inputted pattern is null or of "
                    + "length 0");
        }
        if (text == null) {
            throw new IllegalArgumentException("Inputted text is null");
        }
        LinkedList<Integer> matchList = new LinkedList<>();
        if (pattern.length() > text.length()) {
            return matchList;
        }
        int[] failureTable = buildFailureTable(pattern, comparator);
        int i = 0;
        int jStart = 0;
        while (i + pattern.length() <= text.length()) {
            boolean misMatch = false;
            int jTemp = jStart;
            for (int j = jTemp; !misMatch && j < pattern.length(); j++) {
                if (comparator.compare(pattern.charAt(j), text.charAt(i + j))
                        != 0) {
                    misMatch = true;
                    if (j == 0) {
                        i++;
                        jStart = 0;
                    } else {
                        i = i + j - failureTable[j - 1];
                        jStart = failureTable[j - 1];
                    }
                }
            }
            if (!misMatch) {
                matchList.add(i);
                i = i + pattern.length() - failureTable[pattern.length() - 1];
                jStart = failureTable[pattern.length() - 1];
            }
        }
        return matchList;
    }

    /**
     * Builds failure table that will be used to run the Knuth-Morris-Pratt
     * (KMP) algorithm.
     *
     * The table built should be the length of the input text.
     *
     * Note that a given index i will be the largest prefix of the pattern
     * indices [0..i] that is also a suffix of the pattern indices [1..i].
     * This means that index 0 of the returned table will always be equal to 0
     *
     * Ex. ababac
     *
     * table[0] = 0
     * table[1] = 0
     * table[2] = 1
     * table[3] = 2
     * table[4] = 3
     * table[5] = 0
     *
     * If the pattern is empty, return an empty array.
     *
     * @throws IllegalArgumentException if the pattern or comparator is null
     * @param pattern a {@code CharSequence} you're building a failure table for
     * @param comparator the comparator to use when checking character equality
     * @return integer array holding your failure table
     */
    private static int[] buildFailureTable(CharSequence pattern,
                                          Comparator<Character> comparator) {
        if (pattern == null || comparator == null) {
            throw new IllegalArgumentException("pattern or comparator is null");
        }
        int i = 0;
        int j = 1;
        int[] failureTable = new int[pattern.length()];
        if (pattern.length() == 0) {
            return failureTable;
        }
        failureTable[0] = 0;
        while (j < pattern.length()) {
            if (comparator.compare(pattern.charAt(i), pattern.charAt(j)) == 0) {
                failureTable[j++] = ++i;
            } else {
                if (i == 0) {
                    failureTable[j++] = 0;
                } else {
                    i = failureTable[i - 1];
                }
            }
        }
        return failureTable;
    }

    /**
     * Method to parse the capacity string from the csv. If the entry is empty,
     * -1 is returned. Else the sum of all numbers in the string are returned.
     *
     * @param bullshitCapacity is the BS string from CSV
     * @returns an int that is (hopefully) the capacity
     */
    public int parseCapacity(String bullshitCapacity) {
        // If bullshitCapacity is empty...
        if (bullshitCapacity.trim().equals("")) {
            return 1000;
        }
        // If bullshitCapacity is not empty...
        String[] split = bullshitCapacity.split(" ");
        int capacity = 0;
        for (String s : split) {
            try {
                capacity += Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {}
        }
        return capacity;
    }
}
