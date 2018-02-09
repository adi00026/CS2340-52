package edu.gatech.cs2340.vaspa.buzzshelter.backend;

import android.content.Context;

import java.util.HashMap;

import edu.gatech.cs2340.vaspa.buzzshelter.model.AccountHolder;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;
import edu.gatech.cs2340.vaspa.buzzshelter.util.PersonNotInDatabaseException;
import edu.gatech.cs2340.vaspa.buzzshelter.util.TooManyAttemptsException;
import edu.gatech.cs2340.vaspa.buzzshelter.util.WrongPasswordException;


/**
 * Class representation for a DatabaseHandler.
 * **NOTE**: You SHOULD call this class when interacting with the database.
 *
 * Methods other classes will need:
 *  - Declaring a handler:
 *          DatabaseHandler dh = new DatabaseHandler(<context>);
 *          NOTE: <context> is "this" when calling from an Activity class.
 *  - Adding a user/registration:
 *          dh.putUser(<user object>);
 *  - Attempting log in:
 *          dh.attemptLogin(<username>, <password>);
 *
 * @author Aniruddha Das
 * @version 1.0
 */
public class DatabaseHandler {
    private static HashMap<String, AccountHolder> holderElems;
    private DatabaseBackend db;
    private Context context;
    /******************************************************************************************
     * METHODS NEEDED TO IMPLEMENT M4 and M5
     *******************************************************************************************/

    /**
     * Constructor for database handler
     *
     * @param context context of current execution. In most cases, you pass in "this" as the context
     */
    public DatabaseHandler(Context context) {
        this.context = context;
        db = new DatabaseBackend(this.context);
        holderElems = new HashMap<>();
        populate();
    }

    /**
     * Adds accountholder to the backend
     *
     * @param accountHolder the Superhero to be added
     */
    boolean putUser(AccountHolder accountHolder) {
        boolean success = db.addUser((User) accountHolder);
        if (success) {
            holderElems.put(accountHolder.getUserId(), accountHolder);
        }
        return success;
    }

    /**
     * Method to get attempt the login of a user with userID and password. Checks for correct
     * userID and password. If no exceptions are thrown, it means the login was successful and
     * an AccountHolder object representing the user who just logged in is returned. If an exception
     * is thrown, there was a problem logging in (detailed below).
     *
     * NOTE: For now, the only non-null attributes of the returned AccountHolder will be username,
     * password, lockedOut and contactInfo. More will be added as it is needed.
     *
     * @param userID the username of the user trying to log in.
     * @param password the password of above user.
     * @return an AccountHolder object representing the user who just logged in
     * @throws NullPointerException if username or password are null
     * @throws PersonNotInDatabaseException if the username is not found in the database.
     * @throws TooManyAttemptsException if the user is locked out, regardless whether the password
     *      is correct.
     * @throws WrongPasswordException if the username is found, but the password is wrong.
     */
    public AccountHolder attemptLogin(String userID, String password) {
        if (holderElems.containsKey(userID)) {
            AccountHolder ret = holderElems.get(userID);
            if (ret.getPassword().equals(password) && !ret.isLockedOut()) {
                return ret;
            } else if (ret.isLockedOut()) {
                throw new TooManyAttemptsException();
            }
        } else {
            throw new PersonNotInDatabaseException();
        }
        try {
            return db.attemptLogin(userID, password);
        } catch (TooManyAttemptsException e) {
            holderElems.get(userID).setLocketOut(true);
            throw e;
        }
    }

    /******************************************************************************************
     * METHODS YOU CAN IGNORE WHILE IMPLEMENTING M4 and M5
     *******************************************************************************************/
    /**
     * Populates the hashmap with the backend database
     */
    private void populate() {
        holderElems = db.getHashDatabase();
    }

    /**
     * Clears the existing database
     */
    public void clearDatabase() {
        db.clearTables();
    }

    /**
     * Gets the accountholder with the userID
     *
     * @param userID The name of the superhero to be got
     * @return Superhero with the desired name
     */
    AccountHolder getHolder(String userID) {
        return holderElems.get(userID);
    }

    /**
     * method to reset the attempts of logins for all users. For debugging purposes only.
     */
    public void resetLogins() {
        db.resetLogins();
        populate();
    }

    /**
     * Debugging method to view the database table in its current state
     *
     * @return a String representation of the database
     */
    public String viewDatabase() {
        return db.viewDatabase();
    }
}
