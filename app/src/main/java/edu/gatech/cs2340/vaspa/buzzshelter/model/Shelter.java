package edu.gatech.cs2340.vaspa.buzzshelter.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by aniruddhadas on 08/02/18.
 */

public class Shelter {
    // TODO: No args constructor that does nothing
    // TODO: getters and setters for everthing
    // TODO: make sure all shits are JSON objects
    private String uniqueKey;
    private String name;
    private int capacity;
    private int vacancies;
    private int latitude;
    private int longitude;
    // private float rating;
    private Set<String> restrictions;
    // private float cost;
    private String contactInfo;
    // private User[] blacklist;
    // private User[] currentResidents;
}
