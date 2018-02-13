package edu.gatech.cs2340.vaspa.buzzshelter.model;

import java.util.List;

/**
 * Created by Sanath on 2/8/2018.
 */

public class User extends AccountHolder {
    private String gender;
    private List<Integer> dateOfBirth;
    private boolean isVeteran;

    public User(String userID, String password, boolean lockedOut, String contactInfo, String gender,
         List<Integer> dateOfBirth, boolean isVeteran) {
        super(userID, password, lockedOut, contactInfo);
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.isVeteran = isVeteran;
    }
    public User(String userID, String contactInfo, String password) {
        this(userID, password, false, contactInfo, "Male", null, false);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Integer> getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(List<Integer> dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isVeteran() {
        return isVeteran;
    }

    public void setVeteran(boolean veteran) {
        isVeteran = veteran;
    }
}
