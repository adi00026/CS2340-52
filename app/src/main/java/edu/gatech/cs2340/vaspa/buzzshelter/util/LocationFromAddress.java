package edu.gatech.cs2340.vaspa.buzzshelter.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by aniruddhadas on 21/03/18.
 */

public class LocationFromAddress {
    private static final String TAG = "LocationFromAddress";
    /**
     * Static method uses Google Maps API to return latitude and longitude based on passed in address
     *
     * @param context context to be passed in
     * @param strAddress address lf location to find latitude and longitude for
     * @return LatLng object containing Latitude and Longitude of passed in address
     */
    public static LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng latLng = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null || address.size() == 0) {
                // implies backend service is down or no results were found for the address
                Log.d(TAG, "backend service is down or no results were found for the address");
                return null;
            }

            Address location = address.get(0);
            latLng = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {
            Log.d(TAG, ex.getMessage());
            Log.getStackTraceString(ex);
        }
        // latitude and longitude are public data members of LatLng
        return latLng;
    }
}
