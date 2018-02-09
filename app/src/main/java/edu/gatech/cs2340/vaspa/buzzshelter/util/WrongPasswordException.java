package edu.gatech.cs2340.vaspa.buzzshelter.util;

/**
 * Created by Sanath on 2/8/2018.
 */

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("Wrong password for inputted username");
    }
}
