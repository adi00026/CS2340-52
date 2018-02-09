package edu.gatech.cs2340.vaspa.buzzshelter.model;

/**
 * Created by Sanath on 2/8/2018.
 */

public abstract class AccountHolder {
    private String userId;
    private String password;
    private boolean lockedOut;
    private String contactInfo;
    public AccountHolder(String userId, String password, boolean lockedOut, String contactInfo) {
        this.userId = userId;
        this.password = password;
        this.lockedOut = lockedOut;
        this.contactInfo = contactInfo;
    }
    public String getUserId() {
        return userId;
    }
    public String getPassword() {
        return password;
    }
    public boolean isLockedOut() {
        return lockedOut;
    }
    public void setLocketOut(boolean isLockedOut) {
        lockedOut = isLockedOut;
    }
    public String getContactInfo() {
        return contactInfo;
    }
}
