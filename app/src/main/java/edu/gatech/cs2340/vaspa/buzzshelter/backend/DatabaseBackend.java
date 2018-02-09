package edu.gatech.cs2340.vaspa.buzzshelter.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

import edu.gatech.cs2340.vaspa.buzzshelter.model.AccountHolder;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;
import edu.gatech.cs2340.vaspa.buzzshelter.util.Encryption;
import edu.gatech.cs2340.vaspa.buzzshelter.util.PersonNotInDatabaseException;
import edu.gatech.cs2340.vaspa.buzzshelter.util.TooManyAttemptsException;
import edu.gatech.cs2340.vaspa.buzzshelter.util.WrongPasswordException;

/**
 * Class representation for backend of Database.
 * **NOTE**: This class should never directly be called by any class other than DatabaseHandler.
 *
 * @author Sanath Nagaraj
 * @version 1.0
 */
public class DatabaseBackend extends SQLiteOpenHelper {
    private Context mcontext;

    // All Static variables for the SQLite Database
    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "TESTDB";
    // Contacts table name
    private static final String TABLE_USER = "userTable";
    // Contacts Table Columns names for users
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ATTEMPTS = "attempts";
    private static final String KEY_CONTACT_INFO = "contactInfo";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_DOB = "dob";
    private static final String KEY_VETSTATUS = "veteran";

    private final String TAG = "DatabaseBackend";
    /**
     * Constructor for database backend
     *
     * @param context context of current execution
     */
    public DatabaseBackend(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mcontext = context;
        createDB();
    }

    /**
     * onCreate method to create db and satisfy superclass constraints. Check superclass
     * documentation for further information
     *
     * @param db is the SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    /**
     * onUpgrade method to create db and satisfy superclass constraints. Check superclass
     * documentation for further information.
     *
     * @param db is the SQLiteDatabase
     * @param oldVersion is the old version of the table
     * @param newVersion is the new version of the table
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If you need to add a column
        if (newVersion > oldVersion) {
            //clearTables();
            //createDB();
        }
    }
    /**
     * Private method to create a Database. Creates a single table to hold User object data
     * NOTE: currently only stores: userID|password|lockedOut|contactInfo|gender|DOB|isVeteran|
     * Other attributes will be added over time - This is all that is needed for M4.
     */
    private void createDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Creating Database
        String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
                + KEY_USERNAME + " TEXT PRIMARY KEY," +
                KEY_PASSWORD + " TEXT," +
                KEY_ATTEMPTS + " INTEGER," +
                KEY_CONTACT_INFO + " TEXT," +
                KEY_GENDER + " TEXT," +
                KEY_DOB + " TEXT," +
                KEY_VETSTATUS + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }
    /**
     * Private method to clear the created table and remake it.
     */
    public void clearTables() {
        SQLiteDatabase db = this.getReadableDatabase();
        //db.execSQL("DROP TABLE IF EXISTS " + "superheroes");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        createDB();
    }
    /**
     * Method to add user to the user database.
     * NOTE: Currently this only adds the values for userID, password, locked out and contact info.
     * other info is left as "TBD" for now - will implement as we need it.
     *
     * @param user The user to add to the database
     * @return whether the add was successful.
     * @throws NullPointerException if superhero is null
     */
    public boolean addUser(User user) {
        if (user == null) {
            throw new NullPointerException("Inputted user cannot be null");
        }
        String name = user.getUserId();
        String password = Encryption.encode(user.getPassword());
        String contactInfo = user.getContactInfo();

        if (checkExists(name, TABLE_USER)) {
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        // Storing values for user
        ContentValues newUser = new ContentValues();
        newUser.put(KEY_USERNAME, name);
        newUser.put(KEY_PASSWORD, password);
        newUser.put(KEY_ATTEMPTS, 0);
        newUser.put(KEY_CONTACT_INFO, contactInfo);
        newUser.put(KEY_GENDER, "TBD");
        newUser.put(KEY_DOB, "TBD");
        newUser.put(KEY_VETSTATUS, "TBD");
        // Inserting Row
        db.insert(TABLE_USER, null, newUser);
        db.close();
        return true;
    }

    /**
     * Private method to see if name exists as a key in tableName
     *
     * @param name the name to check for in the database
     * @param tableName the table to check in for this name
     * @return whether name is a key in tableName
     */
    private boolean checkExists(String name, String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + tableName;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if (name.equals(cursor.getString(0))) {
                    db.close();
                    return true;
                }
            } while (cursor.moveToNext());
        }
        db.close();
        return false;
    }

    /**
     * Method to get attempt the login of a user with username and password. Checks for correct
     * username and password. If no exceptions are thrown, it means the login was successful and
     * an AccountHolder object representing the user who just logged in is returned. If an exception
     * is thrown, there was a problem logging in (detailed below).
     *
     * NOTE: For now, the only non-null attributes of the returned AccountHolder will be username,
     * password, lockedOut and contactInfo. More will be added as it is needed.
     *
     * @param username the username of the user trying to log in.
     * @param password the password of above user.
     * @return an AccountHolder object representing the user who just logged in
     * @throws NullPointerException if username or password are null
     * @throws PersonNotInDatabaseException if the username is not found in the database.
     * @throws TooManyAttemptsException if the user is locked out, regardless whether the password
     *      is correct.
     * @throws WrongPasswordException if the username is found, but the password is wrong.
     */
    public AccountHolder attemptLogin(String username, String password) {
        if (username == null || password == null) {
            throw new NullPointerException("You have entered a null username or password!");
        }
        password = Encryption.encode(password);
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USER;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        AccountHolder holder = null;
        if (cursor.moveToFirst()) {
            do {
                if (username.equals(cursor.getString(0))) {
                    if (Integer.parseInt(cursor.getString(2)) > 3) {
                        throw new TooManyAttemptsException();
                    }
                    if (password.equals(cursor.getString(1))) {
                        holder = new User(cursor.getString(0),
                                Encryption.decode(cursor.getString(1)),
                                Integer.parseInt(cursor.getString(2)) > 3,
                                cursor.getString(3),
                                null, null, false);
                        String command = "UPDATE " + TABLE_USER + " SET "
                                + KEY_ATTEMPTS + " = 0 WHERE "
                                + KEY_USERNAME + "=?";
                        db.execSQL(command, new String[] {username});
                        break;
                    } else {
                        String command = "UPDATE " + TABLE_USER + " SET "
                                + KEY_ATTEMPTS + " = " + KEY_ATTEMPTS + " +1 WHERE "
                                + KEY_USERNAME + "=?";
                        db.execSQL(command, new String[] {username});
                        throw new WrongPasswordException();
                    }
                }
            } while (cursor.moveToNext());
        }
        if (holder == null) {
            throw new PersonNotInDatabaseException();
        }
        db.close();
        return holder;
    }

    /**
     * Debugging method to view the database table in its current state
     *
     * @return a String representation of the database
     */
    public String viewDatabase() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USER;
        Cursor cursor = db.rawQuery(selectQuery, null);
        String output = String.format("|%-20s|%-20s|%-20s|%-20s|\n", "Username",
                "Password",
                "Num Attempts",
                "Contact Info");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                output = output + String.format("|%-20s|%-20s|%-20s|%-20s|\n", cursor.getString(0),
                        Encryption.decode(cursor.getString(1)),
                        cursor.getString(2),
                        cursor.getString(3));
            } while (cursor.moveToNext());
        }
        db.close();
        return output;
    }
    /**
     * method to reset the lockedOut status of logins for all users.
     * NOTE: For debugging purposes only.
     */
    public void resetLogins() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USER;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String command = "UPDATE " + TABLE_USER + " SET "
                        + KEY_ATTEMPTS + " = 0 WHERE "
                        + KEY_USERNAME + "=?";
                db.execSQL(command, new String[] {name});
            } while (cursor.moveToNext());
        }
        db.close();
    }

    /**
     * Method to return a hashmap representation of the current database.
     *
     * @return a hashmap representation of database
     */
    public HashMap<String, AccountHolder> getHashDatabase() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USER;
        Cursor cursor = db.rawQuery(selectQuery, null);
        HashMap<String, AccountHolder> output = new HashMap<>();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User(cursor.getString(0),
                        Encryption.decode(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)) > 3,
                        cursor.getString(3),
                        null, null, false);
                output.put(cursor.getString(0), user);
            } while (cursor.moveToNext());
        }
        return output;
    }
}
