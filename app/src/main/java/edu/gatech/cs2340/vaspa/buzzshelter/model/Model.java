package edu.gatech.cs2340.vaspa.buzzshelter.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import edu.gatech.cs2340.vaspa.buzzshelter.util.StringSearch;

/**
 * "Model Facade" class
 */
public class Model {
    /**
     * Singleton instance
     */
    private static final Model instance = new Model();

    private HashMap<String, Shelter> shelters;
    private AccountHolder currentUser;
    private HashMap<String, Integer> loginAttempts;

    private String logs;

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
     * Getter for shelters.
     *
     * @return the shelters.
     */
    public HashMap<String, Shelter> getShelters() {
        return shelters;
    }

    /**
     * Setter for shelters
     *
     * @param shelters shelter to set shelters to.
     */
    public void setShelters(HashMap<String, Shelter> shelters) {
        this.shelters = shelters;
    }

    /**
     * Function to search shelter that have str in their name.
     *
     * @param str string to search for.
     * @return a list of shelters containing str in their names
     */
    public List<Shelter> searchShelterByName(String str) {
        LinkedList<Shelter> output = new LinkedList<>();

        // Go through the shelters, and searching by name. Unfortunately, because
        // the shelters are stored in the HashMap by their unique key, we can't
        // make use of quick indexing by name.
        // This may have to be rewritten at some point to take that into account
        // if this app is to scale.
        for (Shelter sh : shelters.values()) {
            if (StringSearch.contains(sh.getName().trim(), str)) {
                output.addLast(sh);
            }
        }
        return output;
    }

    /**
     * Function to search shelter that have str in their name.
     *
     * @param ageRange string to search for.
     * @return a list of shelters containing str in their names
     */
    public List<Shelter> searchShelterByAge(String ageRange) {
        return null;
    }

    /**
     * Function to search shelter that have str in their name.
     *
     * @param gender string to search for.
     * @return a list of shelters containing str in their names
     */
    public List<Shelter> searchShelterByGender(String gender) {
        return null;
    }

    /**
     * Function that clears the number of login attempts the user has made
     *
     * @param uid The UID of the user that has successfully logged in
     */
    public void clearLoginAttempts(String uid) {
        loginAttempts.remove(uid);
    }

    /**
     * Function that updates number of login attempts associated with uid
     *
     * @param uid The UID of the user that has attempted to login
     * @return the number of login attempts associated with the uid
     */
    public int incrementLoginAttempts(String uid) {
        if (loginAttempts.containsKey(uid)) {
            loginAttempts.put(uid, loginAttempts.get(uid) + 1);
        } else {
            loginAttempts.put(uid, 1);
        }
        return loginAttempts.get(uid);
    }

    /**
     * make a new model
     */
    private Model() {
        // empty for now
        loginAttempts = new HashMap<>();
        logs = "";
    }

    /**
     * Updates Model log string
     *
     * @param update is the update to be made to the log
     */
    public void updateLogs(String update) {
        logs += update + "\n";
    }

    /**
     * Getter method for the logs
     *
     * @return the logs
     */
    public String getLogs() {
        return logs;
    }

    /**
     * Clears logs
     */
    public void clearLog() {
        logs = "";
    }
}
