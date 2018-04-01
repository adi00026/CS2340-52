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

    /**
     * Getter
     *
     * @return specialNotes
     */
    public String getSpecialNotes() {
        return specialNotes;
    }

    /**
     * Setter
     *
     * @param specialNotes new special notes
     */
    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
    }

    private String specialNotes;

    /**
     * Getter
     *
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Setter
     *
     * @param address new address
     */
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

    @SuppressWarnings("ConstructorWithTooManyParameters")
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

    /**
     * Getter
     *
     * @return unique key
     */
    public String getUniqueKey() {
        return uniqueKey;
    }

    /**
     * Getter
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter
     *
     * @return capacity
     */
    public String getCapacity() {
        return capacity;
    }

    /**
     * Getter
     *
     * @return vacancy
     */
    public int getVacancies() {
        return vacancies;
    }

    /**
     * Getter
     *
     * @return restrictions
     */
    public String getRestrictions() {
        return restrictions;
    }

    /**
     * Getter
     *
     * @return contact info
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Getter
     *
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Getter
     *
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Setter
     *
     * @param key new key
     */
    public void setUniqueKey(String key) {
        uniqueKey = key;
    }

    /**
     * Setter
     *
     * @param n new name
     */
    public void setName(String n) {
        name = n;
    }

    /**
     * Setter
     *
     * @param n set capacity
     */
    public void setCapacity(String n) {
        capacity = n;
    }

    /**
     * Setter
     *
     * @param n new vacancies
     */
    public void setVacancies(int n) {
        vacancies = n;
    }

    /**
     * Setter
     *
     * @param d new latitude
     */
    public void setLatitude(double d) {
        latitude = d;
    }

    /**
     * Setter
     *
     * @param d new longitude
     */
    public void setLongitude(double d) {
        longitude = d;
    }

    /**
     *  Setter
     *
     * @param restr new restrictions
     */
    public void setRestrictions(String restr) {
        restrictions = restr;
    }

    /**
     * Setter
     * @param contact new contact info
     */
    public void setContactInfo(String contact) {
        contactInfo = contact;
    }


    /**
     * Used internally
     * @param p something
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
        @Override
        public Shelter createFromParcel(Parcel in) {
            return new Shelter(in);
        }

        @Override
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


