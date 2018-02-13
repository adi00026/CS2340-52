package edu.gatech.cs2340.vaspa.buzzshelter.model;

/**
 * Created by aniruddhadas on 12/02/18.
 */

public class Admin extends AccountHolder {
    Admin(String userId, String password, String contactInfo) {
        super(userId, password, false, contactInfo);
    }
    public Admin() {
        // no args constructor
    }
}
