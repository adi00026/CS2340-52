package edu.gatech.cs2340.vaspa.buzzshelter.model;

/**
 * Created by aniruddhadas on 12/02/18.
 */

public class ShelterEmployee extends AccountHolder {

    private String shelterID;

    public ShelterEmployee() {
        super();
    }

    ShelterEmployee(String name, String userId, String password, String contactInfo) {
        this(name, userId, password, contactInfo, null);
    }

    ShelterEmployee(String name, String userId, String password, String contactInfo, String shelterID) {
        super(name, userId, password, false, contactInfo);
        this.shelterID = shelterID;
    }

    public String getShelterID() {
        return shelterID;
    }

    public void setShelterID(String shelterID) {
        this.shelterID = shelterID;
    }
}