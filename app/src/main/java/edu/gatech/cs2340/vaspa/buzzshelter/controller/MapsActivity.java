package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.gatech.cs2340.vaspa.buzzshelter.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
	
	private GoogleMap mMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
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
		mMap = googleMap;
		googleMap = googleMap;
		LatLng atl = new LatLng(33.7490, -84.3880);
		mMap.addMarker(new MarkerOptions().position(atl).title("Shelter 1"));
		
		LatLng shelt2 = new LatLng(33.775710, -84.399041);
		mMap.addMarker(new MarkerOptions().position(shelt2).title("Shelter 2"));
		
		LatLng shelt3 = new LatLng(33.752696, -84.385508);
		mMap.addMarker(new MarkerOptions().position(shelt3).title("Shelter 3"));
		
		mMap.moveCamera(CameraUpdateFactory.newLatLng(atl));
		
		//Temp Map Stuff goes below
		/**
		ArrayList<MarkerData> markersArray = new ArrayList<MarkerData>();
		
		for(int i = 0 ; i < markersArray.size() ; i++ ) {
			
			createMarker(markersArray.get(i).getLatitude(), markersArray.get(i).getLongitude(), markersArray.get(i).getTitle(), markersArray.get(i).getSnippet(), markersArray.get(i).getIconResID());
		}

...
		
		protected Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID) {
			
			return googleMap.addMarker(new MarkerOptions()
					.position(new LatLng(latitude, longitude))
					.anchor(0.5f, 0.5f)
					.title(title)
					.snippet(snippet);
            .icon(BitmapDescriptorFactory.fromResource(iconResID)))
		 **/
		//Temp Map Stuff goes above
	}
}
