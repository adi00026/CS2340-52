package edu.gatech.cs2340.vaspa.buzzshelter.model;

/**
 * Class definition for abstract class AccountHolder.
 *
 * @author Aniruddha Das, Sanath Nagaraj
 * @version 6.9
 */
public abstract class AccountHolder {
    private String name;
    private String userId;
    private String password;
    private boolean lockedOut;
    private boolean deleted;
    private String contactInfo;

    /**
     * 5 Argument constructor for AccountHolder.
     *
     * @param name name of account holder
     * @param userId userId of account holder
     * @param password password of account holder
     * @param lockedOut whether the account holder is locked out.
     * @param contactInfo the contact info of the account holder.
     */
    public AccountHolder(String name, String userId, String password, boolean lockedOut,
                         String contactInfo) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.lockedOut = lockedOut;
        this.contactInfo = contactInfo;
        this.deleted = false;
    }

    /**
     * No args constructor for AccountHolder. This is in place for Firebase purposes.
     */
    public AccountHolder() {
        // no args constructor
    }

    /**
     * Method to get the userId of this account holder.
     *
     * @return UserId of account holder
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Method to set the userId of this account holder.
     *
     * @param userId UserId of account holder
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Method to get the password of this account holder.
     *
     * @return password of account holder
     */
    public String getPassword() {
        return password;
    }

    /**
     * Method to set the password of this account holder.
     *
     * @param password password of account holder
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Method to get the locked out status of this account holder.
     *
     * @return whether the account holder is locked out.
     */
    public boolean isLockedOut() {
        return lockedOut;
    }

    /**
     * Method to set the locked out status of this account holder.
     *
     * @param lockedOut whether the account holder is locked out.
     */
    public void setLockedOut(boolean lockedOut) {
        this.lockedOut = lockedOut;
    }

    /**
     * Method to get the contactInfo of this account holder.
     *
     * @return contactInfo of account holder
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Method to set the contactInfo of this account holder.
     *
     * @param contactInfo contactInfo of account holder
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * Method to get the name of this account holder.
     *
     * @return name of account holder
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set the name of this account holder.
     *
     * @param name name of account holder
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method to get the deleted status of this account holder.
     *
     * @return whether the account holder is deleted.
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Method to set the deleted status of this account holder.
     *
     * @param deleted whether the account holder is deleted.
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
