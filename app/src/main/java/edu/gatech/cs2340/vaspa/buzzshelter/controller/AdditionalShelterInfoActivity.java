package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Shelter;

public class AdditionalShelterInfoActivity extends AppCompatActivity {

    private Button back;
    private Button finish;
    private EditText specialNotesText;
    private EditText capacityText;
    private CheckBox[] restrictionsCheckboxes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_shelter_info);

        back = (Button) findViewById(R.id.button_back);
        finish = (Button) findViewById(R.id.button_finish);
        specialNotesText = (EditText) findViewById(R.id.shelter_special_notes);
        capacityText = (EditText) findViewById(R.id.shelter_capacity);

        restrictionsCheckboxes = new CheckBox[6];

        restrictionsCheckboxes[0] = findViewById(R.id.checkBox_male);
        restrictionsCheckboxes[1] = findViewById(R.id.checkBox_female);
        restrictionsCheckboxes[2] = findViewById(R.id.checkBox_famAndNewborns);
        restrictionsCheckboxes[3] = findViewById(R.id.checkBox_children);
        restrictionsCheckboxes[4] = findViewById(R.id.checkBox_youngAdults);
        restrictionsCheckboxes[5] = findViewById(R.id.checkBox_anyone);

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
        String capacity_string = capacityText.getText().toString().trim();
        String restrictions = "";
        for (CheckBox restriction : restrictionsCheckboxes) {
            restrictions = restrictions
                    + (restriction.isChecked() ? restriction.getText().toString().toLowerCase()
                    + ", " : "");
        }
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
                    myRef.removeEventListener(this);
                } else {
                    // Adds shelter to database
                    myRef.child("shelters").child(id).setValue(newShelter);
                    Toast.makeText(AdditionalShelterInfoActivity.this,
                      "Shelter added", Toast.LENGTH_SHORT).show();
                    final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                      format(Calendar.getInstance().getTime()); // Current date and time
                    String log = date + ", " + "ADMIN: " + Model.getInstance().getCurrentUser()
                      .getUserId() + ", " + "added new shelter: " + newShelter.getUniqueKey();
                    Model.getInstance().updateLogs(log);
                    myRef.removeEventListener(this);
                    Intent intent = new Intent(AdditionalShelterInfoActivity.this,
                            ManageUsersActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
