package edu.gatech.cs2340.vaspa.buzzshelter.model;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import edu.gatech.cs2340.vaspa.buzzshelter.util.StringOps;

/**
 * "Model Facade" class
 *
 * @author All
 * @version 6.9
 */
@SuppressWarnings({"ConstantConditions", "ClassWithTooManyDependents"})
public class Model {
    /**
     * Singleton instance
     */
    private static final Model instance = new Model();

    private Collection<Shelter> filteredShelters;
    private HashMap<String, Shelter> shelters;
    private AccountHolder currentUser;
    private final HashMap<String, Integer> loginAttempts;

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
     * This method assumes ageRange is not null.
     *
     * @param ageRange string to search for.
     * @return a collection of shelters containing str in their names
     */
    public Collection<Shelter> filterShelterByAge(String ageRange) {
        List<Shelter> list = new LinkedList<>();
        for (Shelter sh: shelters.values()) {
            if (sh.getRestrictions().toLowerCase().
                    contains(ageRange.toLowerCase())) {
                list.add(sh);
            }
        }
        return list;
    }

    /**
     * Function to search shelter that have str in their name.
     *
     * @param gender string to search for.
     * @return a collection of shelters containing str in their names
     */
    public Collection<Shelter> filterShelterByGender(String gender) {
        List<Shelter> list = new LinkedList<>();
        for (Shelter sh: shelters.values()) {
            if (sh.getRestrictions().contains(gender)) {
                list.add(sh);
            }
        }
        return list;
    }

    /**
     * Function to filter shelters that have name in their name.
     *
     * @param name to search for.
     * @return a collection of shelters containing str in their names
     */
    public Collection<Shelter> filterShelterByName(String name) {
        List<Shelter> list = new LinkedList<>();
        for (Shelter sh: shelters.values()) {
            if (StringOps.fuzzySearchContains(sh.getName().toLowerCase(), name.toLowerCase())) {
                list.add(sh);
            }
        }
        return list;
    }

    /**
     * Method to return a list of all shelters currently in the database
     *
     * @return a Collection of all the shelters.
     */
    public Collection<Shelter> allShelters() {
        return shelters.values();
    }

    /**
     * Function to search shelter that have str in their name.
     *
     * @param ageRange the age range
     * @param gender string to search for.
     * @return a list of shelters containing str in their names
     */
    public List<Shelter> filterShelterByAgeGender(String ageRange, String gender) {
        List<Shelter> list = new LinkedList<>();
        for (Shelter sh: shelters.values()) {
            if (sh.getRestrictions().toLowerCase().
                    contains(gender.toLowerCase())
                    && sh.getRestrictions().toLowerCase()
                    .contains(ageRange.toLowerCase())) {
                list.add(sh);
            }
        }
        return list;
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
        shelters = new HashMap<>();
        FirebaseDatabase.getInstance().getReference().child("shelters").orderByKey()
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Shelter shelter = dataSnapshot.getValue(Shelter.class);
                shelters.put(shelter.getUniqueKey(), shelter);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                Shelter shelter = dataSnapshot.getValue(Shelter.class);
                shelters.put(shelter.getUniqueKey(), shelter);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
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

    /**
     * Getter for filtered Shelters.
     *
     * @return the List of filtered shelters
     */
    public Collection<Shelter> getFilteredShelters() {
        return filteredShelters;
    }

    /**
     * Getter for filtered Shelters.
     *
     * @param filteredShelters the List of filtered shelters
     */
    public void setFilteredShelters(Collection<Shelter> filteredShelters) {
        this.filteredShelters = filteredShelters;
    }
}
