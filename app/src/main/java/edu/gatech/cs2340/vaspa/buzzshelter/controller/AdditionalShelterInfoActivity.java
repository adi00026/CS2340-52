package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
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
import edu.gatech.cs2340.vaspa.buzzshelter.util.LocationFromAddress;

/**
 * Class to handle the additional shelter info activity. Admins will access this page
 * from their manage users page. This page is shared by all Admins.
 *
 * @author Aniruddha Das
 * @version 6.9
 */

@SuppressWarnings("CyclicClassDependency")
public class AdditionalShelterInfoActivity extends AppCompatActivity {

    private EditText specialNotesText;
    private EditText capacityText;
    private CheckBox[] restrictionsCheckboxes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_shelter_info);

        Button back = findViewById(R.id.button_back);
        Button finish = findViewById(R.id.button_finish);
        specialNotesText = findViewById(R.id.shelter_special_notes);
        capacityText = findViewById(R.id.shelter_capacity);

        restrictionsCheckboxes = new CheckBox[7];

        restrictionsCheckboxes[0] = findViewById(R.id.checkBox_male);
        restrictionsCheckboxes[1] = findViewById(R.id.checkBox_female);
        restrictionsCheckboxes[2] = findViewById(R.id.checkBox_famAndNewborns);
        restrictionsCheckboxes[3] = findViewById(R.id.checkBox_children);
        restrictionsCheckboxes[4] = findViewById(R.id.checkBox_youngAdults);
        restrictionsCheckboxes[5] = findViewById(R.id.checkBox_anyone);
        restrictionsCheckboxes[6] = findViewById(R.id.checkBox_other);



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

    /**
     * Listener method for finish button
     */
    private void finishPressed() {
        @SuppressWarnings("ConstantConditions") final String name = getIntent().getExtras()
                .getString("name");
        final String id = getIntent().getExtras().getString("id");
        final String contact_info = getIntent().getExtras().getString("contact_info");
        final String address = getIntent().getExtras().getString("address");

        String specialNotes = specialNotesText.getText().toString().trim();
        String capacity_string = capacityText.getText().toString().trim();
        StringBuilder restrictions = new StringBuilder();
        for (CheckBox restriction : restrictionsCheckboxes) {
            restrictions.append(restriction.isChecked() ? restriction.getText().toString()
                    .toLowerCase() + ", " : "");
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
        LatLng latLng = LocationFromAddress.getLocationFromAddress(this, address);
        final Shelter newShelter;
        if (latLng != null) {
            Log.d("ADDITIONAL_SHELTER_INFO", latLng.latitude + ", " + latLng.longitude);
            newShelter = new Shelter(id, name, "" + capacity, restrictions.toString(),
                    latLng.longitude, latLng.latitude, address, specialNotes, contact_info,
                    capacity);
        } else {
            Log.d("ADDITIONAL_SHELTER_INFO", "latlng was null. Default values placed");
            newShelter = new Shelter(id, name, "" + capacity, restrictions.toString(), 69.0,
              69.0, address, specialNotes, contact_info, capacity);
        }

        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();


        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressWarnings("LawOfDemeter")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                assert id != null;
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
                    //noinspection LawOfDemeter,LawOfDemeter
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
