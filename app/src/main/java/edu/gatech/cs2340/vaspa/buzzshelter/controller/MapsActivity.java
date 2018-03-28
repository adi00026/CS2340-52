package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Shelter;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {
	
	private ArrayList<Shelter> shelters;
	private Button backButton;
	
	private GoogleMap mMap;
	private UiSettings mUiSettings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		shelters = getIntent().getParcelableArrayListExtra("shelters");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		
		backButton = findViewById(R.id.button_back);
		
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
				mMap.addMarker(new MarkerOptions().position(shelterLL).title(shelter.getName()));
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
			mMap.addMarker(new MarkerOptions().position(shelterLL).title(shelter.getName()));
			atl = new LatLng(shelter.getLatitude(), shelter.getLongitude());
		}
		
		mMap.moveCamera(CameraUpdateFactory.newLatLng(atl));
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
		mMap.animateCamera(zoom);
		
		mMap.setOnMarkerClickListener(this);
	}
	
	
	
	@Override
	public boolean onMarkerClick(final Marker marker) {
		for (Shelter shelter : shelters) {
			Toast.makeText(this,
					" Phone number: " + shelter.getContactInfo(),
					Toast.LENGTH_SHORT).show();
		}
		
		// Return false to indicate that we have not consumed the event and that we wish
		// for the default behavior to occur (which is for the camera to move such that the
		// marker is centered and for the marker's info window to open, if it has one).
		return false;
	}
}