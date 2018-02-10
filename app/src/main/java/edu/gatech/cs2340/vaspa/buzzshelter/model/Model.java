package edu.gatech.cs2340.vaspa.buzzshelter.model;

import android.content.Context;
import java.util.HashSet;
import edu.gatech.cs2340.vaspa.buzzshelter.backend.DatabaseHandler;

/**
 * "Model Facade" class
 */
public class Model {
    /** Singleton instance */
    private static final Model instance = new Model();

    /**
     * returns the static instance of the model to use in the controller
     *
     * @return an instance of the Model to interact with.
     */
    public static Model getInstance() {
        return instance;
    }

    /**
     * make a new model
     */
    private Model() {
        // empty for now
    }
    /***********************************************************************************
     *                             MODEL METHODS
     ***********************************************************************************/
    private HashSet<Shelter> shelters;
    private AccountHolder currentUser;
    /***********************************************************************************
     *                             DATABASE METHODS
     ***********************************************************************************/

    /**
     * Interface for controller to attempt login
     *
     * @param context context for database to work in. Usually "this".
     * @param userID userID of person attempting to log in.
     * @param password password of person attempting to log in.
     * @return an AccountHolder object representing the person who just logged in.
     */
    public boolean attemptLogin(Context context, String userID,
                                String password) {
        DatabaseHandler dh = new DatabaseHandler(context);
        currentUser = dh.attemptLogin(userID, password);
        return true;
    }

    /**
     * Method to create dummy login
     *
     * @param context context for database to work in. Usually "this".
     */
    public void createDummyLogin(Context context) {
        DatabaseHandler dh = new DatabaseHandler(context);
        User tempUser = new User("user", "9082376913", "pass");
        dh.putUser(tempUser);
    }

    /**
     * Method to reset locked out for all users
     *
     * @param context context for database to work in. Usually "this".
     */
    public void resetLogins(Context context) {
        DatabaseHandler dh = new DatabaseHandler(context);
        dh.resetLogins();
    }

    /**
     * Method to view database
     *
     * @param context context for database to work in. Usually "this".
     */
    public String viewDatabase(Context context) {
        DatabaseHandler dh = new DatabaseHandler(context);
        return dh.viewDatabase();
    }
}
