package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.List;


public class SearchShelterActivity extends AppCompatActivity {

    Button viewShelterButton;
    Button backButton;
    Button checkoutButton;
    Spinner shelterSpinner;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    HashMap<String, Shelter> sheltersMap;

    private Model model;

    @Override
    public void onResume() {
        super.onResume();
        if (((User) Model.getInstance().getCurrentUser()).getShelterID() == null) {
            checkoutButton.setEnabled(false);
        } else {
            checkoutButton.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shelter);

        model = Model.getInstance();

        viewShelterButton = (Button) findViewById(R.id.view_shelter_button);
        backButton = (Button) findViewById(R.id.button_back);
        checkoutButton = (Button) findViewById(R.id.button_checkOut);
        shelterSpinner = (Spinner) findViewById(R.id.shelter_spinner);

        sheltersMap = new HashMap<String, Shelter>();

        initFirebaseComponents();

        if (((User) Model.getInstance().getCurrentUser()).getShelterID() == null) {
            checkoutButton.setEnabled(false);
        } else {
            checkoutButton.setEnabled(true);
        }

        final ArrayAdapter<String> shelterAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item);
        shelterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Spinner set up
        shelterAdapter.setDropDownViewResource(R.layout.spinner_layout_2);
        shelterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                ((TextView) parent.getChildAt(0)).setTextSize(23);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        for (Shelter shelter : Model.getInstance().getShelters().values()) {
            shelterAdapter.add(filter(shelter.getName()));
        }
        shelterSpinner.setAdapter(shelterAdapter);

        /*myRef.child("shelters").orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Shelter shelter = dataSnapshot.getValue(Shelter.class);
                sheltersMap.put(shelter.getUniqueKey(), shelter);
                shelterAdapter.add(filter(shelter.getName()));
                // Maybe move the line below outside the listener?
                shelterSpinner.setAdapter(shelterAdapter);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                Shelter shelter = dataSnapshot.getValue(Shelter.class);
                sheltersMap.put(shelter.getUniqueKey(), shelter);
                shelterAdapter.add(filter(shelter.getName()));
                // Maybe move the line below outside the listener?
                shelterSpinner.setAdapter(shelterAdapter);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });*/

        viewShelterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewShelterPressed();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkoutPressed();
            }
        });
    }

    private void checkoutPressed() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child("account_holders").child("users")
                        .child(mAuth.getCurrentUser().getUid()).getValue(User.class);
                String currentID = user.getShelterID();
                user.setShelterID(null);
                myRef.child("account_holders").child("users")
                        .child(mAuth.getCurrentUser().getUid()).setValue(user);
                int size = dataSnapshot.child("shelters").child(currentID).child("vacancies")
                        .getValue(Integer.class);
                myRef.child("shelters").child(currentID).child("vacancies").setValue(size + 1);
                Model.getInstance().setCurrentUser(user);
                checkoutButton.setEnabled(false);
                myRef.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {            }
        });
    }

    private void viewShelterPressed() {
        Intent intent = new Intent(SearchShelterActivity.this,
                ViewAvailableSheltersActivity.class);
        model.setShelters(sheltersMap);

        String name = unfilter(shelterSpinner.getSelectedItem().toString());
        List<Shelter> matches = Model.getInstance().searchShelterByName(name);
        if (!matches.isEmpty()) {
            intent.putExtra("shelter", matches.get(0));
            startActivity(intent);
            return;
        }
        // This shouldn't EVER happen:
        Toast.makeText(SearchShelterActivity.this ,"An error occurred: " +
                "the shelter you requested was not found in our database.",
                Toast.LENGTH_LONG).show();
    }

    private void initFirebaseComponents() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    private String filter(String inStr) {
        Log.d("SEARCHSHELTERS", inStr);
        String[] arr = inStr.split(" ");
        String outStr = "";
        for (int i = 0; i < arr.length; i++) {
            outStr += arr[i];
            if ((i + 1) % 5 == 0) {
                outStr += "\n";
            } else {
                outStr += " ";
            }
        }
        return outStr;
    }

    private String unfilter(String inStr) {
        return inStr.replace("\n", " ")
                .trim();
    }
}
