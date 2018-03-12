package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Shelter;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;
import edu.gatech.cs2340.vaspa.buzzshelter.util.StringSearch;

public class ViewAvailableSheltersActivity extends AppCompatActivity {

    TextView shelterInfoTextView;
    Button backButton;
    Button checkInButton;
    Button checkOutButton;
    Button mapButton;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    Shelter selectedShelter;

    String infoString;

    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shelters);

        model = Model.getInstance();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        shelterInfoTextView = (TextView) findViewById(R.id.shelterInfoTextView);
        backButton = (Button) findViewById(R.id.button_back);
        checkOutButton = (Button) findViewById(R.id.button_checkOut);
        checkInButton = (Button) findViewById(R.id.button_checkIn);
        mapButton = (Button) findViewById(R.id.button_map);

        if (((User) Model.getInstance().getCurrentUser()).getShelterID() == null) {
            checkOutButton.setEnabled(false);
            checkInButton.setEnabled(true);
        } else {
            checkOutButton.setEnabled(true);
            checkInButton.setEnabled(false);
        }

        selectedShelter = getIntent().getParcelableExtra("shelter");

        try {
            if (getIntent().getExtras().containsKey("shelter")) {
                Shelter sh = (Shelter) getIntent().getExtras().getParcelable("shelter");
                infoString = sh.getName() + "\n" + sh.getAddress() + "\n" +
                        "Capacity: " + sh.getCapacity() + "\n"
                        + "Vacancies: ";
                shelterInfoTextView.setText(sh.getName() + "\n" + sh.getAddress() + "\n" +
                        "Capacity: " + sh.getCapacity() + "\n"
                        + "Vacancies: " + sh.getVacancies() + "\n"
                        + "Restrictions: " + sh.getRestrictions());
                if (sh.getVacancies() == 0) {
                    checkInButton.setEnabled(false);
                } else {
                    //checkInButton.setEnabled(true);
                }
            }
        } catch (NullPointerException e) {
            Toast.makeText(ViewAvailableSheltersActivity.this,
                    "Sorry, the shelter you requested wasn't found", Toast.LENGTH_LONG).show();
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInPressed();
            }
        });
        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOutPressed();
            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapPressed();
            }
        });
    
    }

    private void checkOutPressed() {
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
                String BSCap = dataSnapshot.child("shelters").child(currentID).child("capacity")
                        .getValue(String.class);
                int capacity = StringSearch.parseCapacity(BSCap);
                if (size + 1 <= capacity) {
                    myRef.child("shelters").child(currentID).child("vacancies").setValue(size + 1);
                    shelterInfoTextView.setText(infoString + (size + 1));
                }
                Model.getInstance().setCurrentUser(user);
                checkOutButton.setEnabled(false);
                checkInButton.setEnabled(true);
                Toast.makeText(ViewAvailableSheltersActivity.this,
                        "Check-out successful!", Toast.LENGTH_SHORT).show();
                myRef.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {            }
        });
    }

    private void checkInPressed() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child("account_holders").child("users")
                        .child(mAuth.getCurrentUser().getUid()).getValue(User.class);
                String currentID = user.getShelterID();
                int size = dataSnapshot.child("shelters").child(selectedShelter.getUniqueKey())
                        .child("vacancies").getValue(Integer.class);
                if (size > 0) {
                    myRef.child("shelters").child(selectedShelter.getUniqueKey()).child("vacancies")
                            .setValue(size - 1);
                    user.setShelterID(selectedShelter.getUniqueKey());
                    myRef.child("account_holders").child("users")
                            .child(mAuth.getCurrentUser().getUid()).setValue(user);
                    Model.getInstance().setCurrentUser(user);
                    checkOutButton.setEnabled(true);
                    checkInButton.setEnabled(false);
                    shelterInfoTextView.setText(infoString + (size - 1));
                    Toast.makeText(ViewAvailableSheltersActivity.this,
                            "Check-in successful!", Toast.LENGTH_SHORT).show();
                } else {
                    shelterInfoTextView.setText(infoString + (size));
                    Toast.makeText(ViewAvailableSheltersActivity.this,
                            "Sorry, this shelter is full!", Toast.LENGTH_SHORT).show();
                }
                myRef.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {            }
        });
    }
    
    private void mapPressed() {
        startActivity(new Intent(ViewAvailableSheltersActivity.this,
                MapsActivity.class));
    }

}
