package edu.gatech.cs2340.vaspa.buzzshelter.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class definition for shelter that defines a shelter object.
 *
 * @author Aniruddha Das, Sanath Nagaraj
 * @version 6.9
 */

public class Shelter implements Parcelable {
    private String uniqueKey;
    private String name;
    private String capacity;
    private int vacancies;
    private double latitude;
    private double longitude;

    public String getSpecialNotes() {
        return specialNotes;
    }

    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
    }

    private String specialNotes;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;
    // private float rating;
    private String restrictions;
    // private float cost;
    private String contactInfo;
    // private User[] blacklist;
    // private User[] currentResidents;


    public Shelter() {
        this(null, null, null, null, 0, 0, null, null, null, 0);
    }

    public Shelter(String key, String name, String cap, String restrics, double lon, double lat,
                   String addr, String specNotes, String contact, int vac) {
        uniqueKey = key;
        this.name = name;
        capacity = cap;
        vacancies = vac;
        latitude = lat;
        longitude = lon;
        restrictions = restrics;
        contactInfo = contact;
        address = addr;
        specialNotes = specNotes;

    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public String getName() {
        return name;
    }

    public String getCapacity() {
        return capacity;
    }

    public int getVacancies() {
        return vacancies;
    }

    public String getRestrictions() {
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

    public void setCapacity(String n) {
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

    public void setRestrictions(String restr) {
        restrictions = restr;
    }

    public void setContactInfo(String contact) {
        contactInfo = contact;
    }


    /**
     * Used internally
     * @param p
     */
    private Shelter(Parcel p) {
        uniqueKey = p.readString();
        name = p.readString();
        capacity = p.readString();
        vacancies = p.readInt();
        longitude = p.readDouble();
        latitude = p.readDouble();
        restrictions = p.readString();
        contactInfo = p.readString();
        address = p.readString();
        specialNotes = p.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uniqueKey);
        parcel.writeString(name);
        parcel.writeString(capacity);
        parcel.writeInt(vacancies);
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
        parcel.writeString(restrictions);
        parcel.writeString(contactInfo);
        parcel.writeString(address);
        parcel.writeString(specialNotes);


    }

    /**
     * Should not have to edit this method if the constructor and write method are
     * working correctly.
     */
    public static final Parcelable.Creator<Shelter> CREATOR
            = new Parcelable.Creator<Shelter>() {
        public Shelter createFromParcel(Parcel in) {
            return new Shelter(in);
        }

        public Shelter[] newArray(int size) {
            return new Shelter[size];
        }
    };

    @Override
    public String toString() {
        return getName();
    }

    // The other getters / setters will be done at a suitable time.
}


