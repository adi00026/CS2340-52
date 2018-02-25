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
    private double latitude;
    private double longitude;
    // private float rating;
    private Set<String> restrictions;
    // private float cost;
    private String contactInfo;
    // private User[] blacklist;
    // private User[] currentResidents;


    public Shelter() {
        this(null, null, 0, 0, 0, 0, null, null);
    }

    public Shelter(String key, String name, int cap, int vac, double lat, double lon,
                   Set<String> restrics, String contact) {
        uniqueKey = key;
        this.name = name;
        capacity = cap;
        vacancies = vac;
        latitude = lat;
        longitude = lon;
        restrictions = restrics;
        contactInfo = contact;
    }


    public String getUniqueKey() {
        return uniqueKey;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getVacancies() {
        return vacancies;
    }

    public Set<String> getRestrictions() {
        return restrictions;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setUniqueKey(String key) {
        uniqueKey = key;
    }

    public void setName(String n) {
        name = n;
    }

    public void setCapacity(int n) {
        capacity = n;
    }

    public void setVacancies(int n) {
        vacancies = n;
    }

    public void setLatitude(double d) {
        latitude = d;
    }

    public void setLongitude(double d) {
        longitude = d;
    }

    public void setRestrictions(Set<String> restr) {
        restrictions = restr;
    }

    public void setContactInfo(String contact) {
        contactInfo = contact;
    }

    // The other getters / setters will be done at a suitable time.
}
