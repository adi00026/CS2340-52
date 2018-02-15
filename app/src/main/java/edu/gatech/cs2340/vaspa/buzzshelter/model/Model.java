package edu.gatech.cs2340.vaspa.buzzshelter.model;

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
     * make a new model
     */
    private Model() {
        // empty for now
    }

}
