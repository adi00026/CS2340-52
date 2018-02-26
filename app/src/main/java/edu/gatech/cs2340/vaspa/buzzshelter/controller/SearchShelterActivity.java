package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.AccountHolder;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Admin;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Shelter;
import edu.gatech.cs2340.vaspa.buzzshelter.model.ShelterEmployee;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;

import java.util.HashMap;



public class SearchShelterActivity extends AppCompatActivity {

    Button viewShelterButton;
    Spinner shelterSpinner;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    HashMap<String, Shelter> sheltersMap;

    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shelter);

        model = Model.getInstance();

        viewShelterButton = (Button) findViewById(R.id.view_shelter_button);
        shelterSpinner = (Spinner) findViewById(R.id.shelter_spinner);

        sheltersMap = new HashMap<String, Shelter>();

        initFirebaseComponents();


        final ArrayAdapter<String> shelterAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item);
        shelterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        myRef.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Shelter shelter = dataSnapshot.getValue(Shelter.class);
                sheltersMap.put(shelter.getUniqueKey(), shelter);
                shelterAdapter.add(shelter.getName());

                // Maybe move the line below outside the listener?
                shelterSpinner.setAdapter(shelterAdapter);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        viewShelterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewShelterPressed();
            }
        });

    }

    private void viewShelterPressed() {
        Intent intent = new Intent(SearchShelterActivity.this,
                ViewAvailableSheltersActivity.class);

        // Go through the shelters, and searching by name. Unfortunately, because
        // the shelters are stored in the HashMap by their unique key, we can't
        // make use of quick indexing by name.
        // This may have to be rewritten at some point to take that into account
        // if this app is to scale.
        for (Shelter sh: sheltersMap.values()) {
            if (shelterSpinner.getSelectedItem().equals(sh.getName())) {
                intent.putExtra("shelter", sh);
                startActivity(intent);
                return;
            }
        }
        // This shouldn't EVER happen:
        Toast.makeText(SearchShelterActivity.this ,"An error occurred: " +
                "the shelter you requested was not found in our database.",
                Toast.LENGTH_LONG).show();
    }

    private void initFirebaseComponents() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("shelters");
    }
}