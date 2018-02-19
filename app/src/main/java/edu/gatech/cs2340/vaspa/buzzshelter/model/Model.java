package edu.gatech.cs2340.vaspa.buzzshelter.model;

import android.accounts.Account;

import java.util.HashSet;

/**
 * "Model Facade" class
 */
public class Model {
    /**
     * Singleton instance
     */
    private static final Model instance = new Model();

    private HashSet<Shelter> shelters;
    private AccountHolder currentUser;

    /**
     * returns the static instance of the model to use in the controller
     *
     * @return an instance of the Model to interact with.
     */
    public static Model getInstance() {
        return instance;
    }

    /**
     * method to set the current app user to the one defined by currentUser.
     *
     * @param currentUser is the user to be set as the current user.
     */
    public void setCurrentUser(AccountHolder currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * method to get the current app user.
     *
     * @return an AccountHolder to represent the current user of the app
     */
    public AccountHolder getCurrentUser() {
        return this.currentUser;
    }

    /**
     * make a new model
     */
    private Model() {
        // empty for now
    }

}
