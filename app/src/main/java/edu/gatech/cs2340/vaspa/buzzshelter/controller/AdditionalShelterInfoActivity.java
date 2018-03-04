package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Shelter;

public class AdditionalShelterInfoActivity extends AppCompatActivity {

    private Button back;
    private Button finish;
    private EditText specialNotesText;
    private EditText restrictionsText;
    private EditText capacityText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_shelter_info);

        back = (Button) findViewById(R.id.button_back);
        finish = (Button) findViewById(R.id.button_finish);
        specialNotesText = (EditText) findViewById(R.id.shelter_special_notes);
        restrictionsText = (EditText) findViewById(R.id.shelter_restrictions);
        capacityText = (EditText) findViewById(R.id.shelter_capacity);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishPressed();
            }
        });

    }

    private void finishPressed() {
        final String name = getIntent().getExtras().getString("name");
        final String id = getIntent().getExtras().getString("id");
        final String contact_info = getIntent().getExtras().getString("contact_info");
        final String address = getIntent().getExtras().getString("address");

        String specialNotes = specialNotesText.getText().toString().trim();
        String restrictions = restrictionsText.getText().toString().trim();
        String capacity_string = capacityText.getText().toString().trim();

        if (specialNotes.length() == 0 || restrictions.length() == 0
          || capacity_string.length() == 0) {
            Toast.makeText(AdditionalShelterInfoActivity.this, "Please fill all fields",
              Toast.LENGTH_SHORT).show();
            return;
        }
        int capacity;
        try {
            capacity = Integer.parseInt(capacity_string);
        } catch (NumberFormatException e) {
            Toast.makeText(AdditionalShelterInfoActivity.this, "Enter a number in capacity field",
              Toast.LENGTH_SHORT).show();
            return;
        }
        final Shelter newShelter = new Shelter(id, name, "" + capacity, restrictions, 69.0,
          69.0, address, specialNotes, contact_info, capacity);

        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("shelters").child(id).exists()) {
                    Toast.makeText(AdditionalShelterInfoActivity.this,
                      "Shelter with this UID exists", Toast.LENGTH_SHORT).show();
                } else {
                    // Adds shelter to database
                    myRef.child("shelters").child(id).setValue(newShelter);
                    Toast.makeText(AdditionalShelterInfoActivity.this,
                      "Shelter added", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
