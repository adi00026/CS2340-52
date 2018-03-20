package edu.gatech.cs2340.vaspa.buzzshelter.model;

/**
 * Class definition for class that defines an Admin User.
 *
 * @author Aniruddha Das, Sanath Nagaraj
 * @version 6.9
 */
public class Admin extends AccountHolder {

    /**
     * 4 Argument constructor for Admin object.
     *
     * @param name name of Admin
     * @param userId userId of Admin
     * @param password password of Admin
     * @param contactInfo the contact info of the Admin
     */
    public Admin(String name, String userId, String password, String contactInfo) {
        super(name, userId, password, false, contactInfo);
    }

    /**
     * No args constructor for Admin. This is in place for Firebase purposes.
     */
    public Admin() {
        // no args constructor
    }

    @Override
    public String toString() {
        return "------ADMIN------\n" +
                "Name: " + getName() + "\n" +
                "Username: " + getUserId() + "\n" +
                "Password: " + getPassword() + "\n" +
                "Contact Info: " + getContactInfo();
    }
}
