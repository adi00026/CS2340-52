package edu.gatech.cs2340.vaspa.buzzshelter.model;

import java.util.HashSet;

/**
 * Created by aniruddhadas on 08/02/18.
 */

public class Shelter {
    private String uniqueKey;
    private String name;
    private int capacity;
    private int vacancies;
    private int latitude;
    private int longitude;
    private float rating;
    private HashSet<String> restrictions;
    private float cost;
    private String contactInfo;
    private User[] blacklist;
    private User[] currentResidents;
}
