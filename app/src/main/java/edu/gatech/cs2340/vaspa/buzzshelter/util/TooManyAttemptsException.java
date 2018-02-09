package edu.gatech.cs2340.vaspa.buzzshelter.util;

/**
 * Created by Sanath on 2/8/2018.
 */

public class TooManyAttemptsException extends RuntimeException {
    public TooManyAttemptsException() {
        super("Too many attempts to log in by user");
    }
}
