package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Shelter;

/**
 * Class to handle the google maps activity. Users can access this page
 * from the search shelter page when they press the map button
 *
 * @author Aniruddha Das
 * @version 6.9
 */
public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback {
    private List<Shelter> shelters;

    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private Map<Marker, Shelter> transferMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shelters = getIntent().getParcelableArrayListExtra("shelters");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Button backButton = findViewById(R.id.button_back);
        transferMap = new HashMap<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    public  void  onResume(){
        super.onResume();
        if (mMap != null){
            mMap.clear();

            mUiSettings = mMap.getUiSettings();
            mUiSettings.setZoomControlsEnabled(true);


            for (Shelter shelter : shelters) {
                LatLng shelterLL = new LatLng(shelter.getLatitude(), shelter.getLongitude());
                Marker m = mMap.addMarker(new MarkerOptions().position(shelterLL)
                        .title(shelter.getName()).snippet("Vacancies: " + shelter.getVacancies()));
                transferMap.put(m, shelter);
            }



            LatLng atl = new LatLng(33.7490, -84.3880);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(atl));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
            mMap.animateCamera(zoom);
            mMap.setOnMarkerClickListener(this);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng atl = new LatLng(33.7490, -84.3890);
        mMap = googleMap;
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);

        for (Shelter shelter : shelters) {
            LatLng shelterLL = new LatLng(shelter.getLatitude(), shelter.getLongitude());
            Marker m = mMap.addMarker(new MarkerOptions().position(shelterLL)
                    .title(shelter.getName()).snippet("Vacancies: " + shelter.getVacancies()));
            transferMap.put(m, shelter);
        }

        LatLng currLocation = atl;
        // TODO: cleanup code later
        Log.d("MAPS_ACTIVITY", "Starting location attempt.");
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            Log.d("MAPS_ACTIVITY", "Location manager not null.");
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            if (provider != null) {
                Log.d("MAPS_ACTIVITY", "Provider not null.");
                LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
                if (manager != null && manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    try {
                        //Location location = locationManager.getLastKnownLocation(provider);
                        Location location = getLastKnownLocation();
                        if (location != null) {
                            LatLng user_loc = new LatLng(location.getLatitude(), location.getLongitude());
                            currLocation = user_loc;
                            Marker m = mMap.addMarker(new MarkerOptions().position(user_loc)
                              .title("Current Location"));

                            // attempts to set icon of current marker to azure color
                            m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
                                    .HUE_AZURE));

                            transferMap.put(m, null);
                            Log.d("MAPS_ACTIVITY", "Adding current location: "
                              + location.getLatitude() + ", " + location.getLongitude());
                        } else {
                            Log.d("MAPS_ACTIVITY", "Got wrecked. Location was null");
                        }
                    } catch (SecurityException e) {
                        // TODO request permission for GPS
                        Log.d("MAPS_ACTIVITY", "Got wrecked. Security exception");
                        Toast.makeText(MapsActivity.this,
                                "Please give BuzzShelter permission to use location",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                // provider null implies turned off GPS
                Log.d("MAPS_ACTIVITY", "Got wrecked. Provider is null");
            }
        } else {
            Log.d("MAPS_ACTIVITY", "Got wrecked. LocationManager is null");
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
        mMap.animateCamera(zoom);

        mMap.setOnMarkerClickListener(this);
    }

    /**
     * Creates a location manager, iterates through providers and finds the best provider that
     * is valid and gives us an accurate location.
     *
     * @return Location that was last known
     */
    private Location getLastKnownLocation() {
        LocationManager mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            try {
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            } catch (SecurityException e) {
                Log.d("MAPS_ACTIVITY", "getLast killed");
            }
        }
        return bestLocation;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        // get check_in_prompt.xml view
        if ("Current Location".equals(marker.getTitle())) {
            return false;
        }
        LayoutInflater li = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View promptsView = li.inflate(R.layout.you_sure_prompt,
                null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final TextView infoTextView = promptsView
                .findViewById(R.id.textView1);
        infoTextView.setText("More details about:\n" + marker.getTitle());
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("View Details",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Shelter selectedShelter = transferMap.get(marker);
                                Intent intent = new Intent(MapsActivity.this,
                                        ViewAvailableSheltersActivity.class);
                                if (selectedShelter != null) {
                                    intent.putExtra("shelter", selectedShelter);
                                    startActivity(intent);
                                } else {
                                    // This shouldn't EVER happen:
                                    Toast.makeText(MapsActivity.this, "An error occurred: " +
                                                    "the shelter you requested was not found in our database.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
}