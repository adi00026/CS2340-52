package edu.gatech.cs2340.vaspa.buzzshelter.util;

/**
 * Created by Sanath on 2/8/2018.
 */

public class PersonNotInDatabaseException extends RuntimeException {
    public PersonNotInDatabaseException() {
        super("Account holder not found in Database!");
    }
}
