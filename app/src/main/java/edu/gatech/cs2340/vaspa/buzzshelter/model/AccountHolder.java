package edu.gatech.cs2340.vaspa.buzzshelter.model;

/**
 * Created by Sanath on 2/8/2018.
 */

public abstract class AccountHolder {
    private String name;
    private String userId;
    private String password;
    private boolean lockedOut;
    private boolean deleted;
    private String contactInfo;

    public AccountHolder(String name, String userId, String password, boolean lockedOut, String contactInfo) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.lockedOut = lockedOut;
        this.contactInfo = contactInfo;
        this.deleted = false;
    }

    public AccountHolder() {
        // no args constructor
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLockedOut() {
        return lockedOut;
    }

    public void setLockedOut(boolean lockedOut) {
        this.lockedOut = lockedOut;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
