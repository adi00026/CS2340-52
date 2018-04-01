package edu.gatech.cs2340.vaspa.buzzshelter.model;

/**
 * Class definition for class that defines a ShelterEmployee user.
 *
 * @author Aniruddha Das, Sanath Nagaraj
 * @version 6.9
 */
public class ShelterEmployee extends AccountHolder {

    private String shelterID;

    /**
     * No args constructor for ShelterEmployee. This is in place for Firebase purposes.
     */
    public ShelterEmployee() {

    }

    /**
     * 4 Argument constructor for ShelterEmployee.
     *
     * @param name name of ShelterEmployee
     * @param userId userId of ShelterEmployee
     * @param password password of ShelterEmployee
     * @param contactInfo the contact info of the ShelterEmployee
     */
    public ShelterEmployee(String name, String userId, String password, String contactInfo) {
        this(name, userId, password, contactInfo, null);
    }

    /**
     * 5 Argument constructor for ShelterEmployee.
     *
     * @param name name of ShelterEmployee
     * @param userId userId of ShelterEmployee
     * @param password password of ShelterEmployee
     * @param contactInfo the contact info of the ShelterEmployee
     * @param shelterID the unique ID of the shelter this ShelterEmployee works at.
     */
    public ShelterEmployee(String name, String userId, String password, String contactInfo,
                           String shelterID) {
        super(name, userId, password, false, contactInfo);
        this.shelterID = shelterID;
    }

    /**
     * Method to get the unique ID of shelter this ShelterEmployee works at.
     *
     * @return unique ID of shelter this ShelterEmployee works at.
     */
    public String getShelterID() {
        return shelterID;
    }

    /**
     * Method to set the unique ID of shelter this ShelterEmployee works at.
     *
     * @param shelterID unique ID of shelter this ShelterEmployee works at.
     */
    @SuppressWarnings("unused")
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
