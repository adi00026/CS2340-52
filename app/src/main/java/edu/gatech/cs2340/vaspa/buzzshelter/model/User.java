package edu.gatech.cs2340.vaspa.buzzshelter.model;

import java.util.List;

/**
 * Class definition for class that defined a Homeless User.
 *
 * @author Aniruddha Das, Sanath Nagaraj
 * @version 6.9
 */
public class User extends AccountHolder {
    private String gender;
    private List<Integer> dateOfBirth;
    private boolean isVeteran;
    private String shelterID;
    private int numCheckedIn;

    /**
     * 8 Argument constructor for User.
     *
     * @param name name of Homeless User
     * @param userID userId of Homeless User
     * @param password password of Homeless User
     * @param lockedOut whether the Homeless User is locked out.
     * @param contactInfo the contact info of the Homeless User.
     * @param gender The gender of the Homeless User.
     * @param dateOfBirth the date of birth of the Homeless User
     * @param isVeteran whether the Homeless User is a US Veteran.
     */
    public User(String name, String userID, String password, boolean lockedOut, String contactInfo,
                String gender, List<Integer> dateOfBirth, boolean isVeteran) {
        super(name, userID, password, lockedOut, contactInfo);
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.isVeteran = isVeteran;
        this.shelterID = null;
        this.numCheckedIn = 0;
    }

    /**
     * 4 Argument constructor for User.
     *
     * @param name name of Homeless User
     * @param userID userId of Homeless User
     * @param password password of Homeless User
     * @param contactInfo the contact info of the Homeless User.
     */
    public User(String name, String userID, String contactInfo, String password) {
        this(name, userID, password, false, contactInfo, "Male", null,
                false);
    }

    /**
     * No args constructor for User. This is in place for Firebase purposes.
     */
    public User() {
        // No args constructor.
    }

    /**
     * Method to get the gender of the Homeless User.
     *
     * @return the gender of the Homeless User.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Method to set the gender of the Homeless User.
     *
     * @param gender the gender of the Homeless User.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Method to get the dateOfBirth of the Homeless User.
     *
     * @return the dateOfBirth of the Homeless User.
     */
    private List<Integer> getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Method to set the dateOfBirth of the Homeless User.
     *
     * @param dateOfBirth the dateOfBirth of the Homeless User.
     */
    public void setDateOfBirth(List<Integer> dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Method to get the US Veteran status of the Homeless User.
     *
     * @return whether the Homeless user is a veteran.
     */
    public boolean isVeteran() {
        return isVeteran;
    }

    /**
     * Method to set the US Veteran status of the Homeless User.
     *
     * @param veteran whether the Homeless user is a veteran.
     */
    public void setVeteran(boolean veteran) {
        isVeteran = veteran;
    }

    /**
     * Method to get the unique ID of the current shelter this Homeless User is staying at.
     * Will return null if no current shelter exists.
     *
     * @return The Unique ID of the current shelter of this Homeless user.
     */
    public String getShelterID() {
        return shelterID;
    }

    /**
     * Method to set the unique ID of the current shelter this Homeless User is staying at.
     *
     * @param shelterID The Unique ID of the current shelter of this Homeless user.
     */
    public void setShelterID(String shelterID) {
        this.shelterID = shelterID;
    }

    /**
     * Method to return the number of people checked in by this Homeless user.
     *
     * @return the number of people checked in by this Homeless User.
     */
    public int getNumCheckedIn() {
        return numCheckedIn;
    }

    /**
     * Method to set the number of people checked in by this Homeless user.
     *
     * @param numCheckedIn the number of people checked in by this Homeless User.
     */
    public void setNumCheckedIn(int numCheckedIn) {
        this.numCheckedIn = numCheckedIn;
    }

    @Override
    public String toString() {
        return "------USER------\n" +
                "Name: " + getName() + "\n" +
                "Username: " + getUserId() + "\n" +
                "Password: " + getPassword() + "\n" +
                "Contact Info: " + getContactInfo() + "\n" +
                "Gender: " + getGender() + "\n" +
                "Shelter ID: " + getShelterID() + "\n" +
                "DOB: " + getDateOfBirth().get(0) + "/" + getDateOfBirth().get(1) + "/"
                + getDateOfBirth().get(2) + "\n" +
                "Veteran: " + isVeteran();
    }
}
