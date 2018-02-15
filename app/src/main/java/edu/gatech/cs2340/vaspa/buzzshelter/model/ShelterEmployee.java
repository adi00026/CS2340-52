package edu.gatech.cs2340.vaspa.buzzshelter.model;

/**
 * Created by aniruddhadas on 12/02/18.
 */

public class ShelterEmployee extends AccountHolder {

    private String shelterID;

    public ShelterEmployee() {

    }

    public ShelterEmployee(String name, String userId, String password, String contactInfo) {
        this(name, userId, password, contactInfo, null);
    }

    public ShelterEmployee(String name, String userId, String password, String contactInfo, String shelterID) {
        super(name, userId, password, false, contactInfo);
        this.shelterID = shelterID;
    }

    public String getShelterID() {
        return shelterID;
    }

    public void setShelterID(String shelterID) {
        this.shelterID = shelterID;
    }

    @Override
    public String toString() {
        return "---SHELTER EMPLOYEE---\n" +
                "Name: " + getName() + "\n" +
                "Username: " + getUserId() + "\n" +
                "Password: " + getPassword() + "\n" +
                "Contact Info: " + getContactInfo() + "\n" +
                "Shelter: " + getShelterID();
    }
}
