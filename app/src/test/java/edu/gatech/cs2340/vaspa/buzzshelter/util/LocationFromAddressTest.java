package edu.gatech.cs2340.vaspa.buzzshelter.util;

import android.content.Context;
import android.test.ServiceTestCase;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.lang.reflect.Method;

/**
 * Created by aniruddhadas on 21/03/18.
 */
public class LocationFromAddressTest {
    public static final int TIMEOUT = 200;
    @Test
    public void checkLatLng() {
        String address = "University House Midtown";
        LatLng latLng = LocationFromAddress
          .getLocationFromAddress(getTestContext(), address);
        assert latLng != null;
        System.out.println(latLng.latitude + latLng.longitude);
    }

    private Context getTestContext()
    {
        try
        {
            Method getTestContext = ServiceTestCase.class.getMethod("getTestContext");
            return (Context) getTestContext.invoke(this);
        }
        catch (final Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
    }
}