package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import edu.gatech.cs2340.vaspa.buzzshelter.model.ShelterEmployee;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;

public class UpdateVacanciesActivity extends AppCompatActivity {
    Button backButton;
    Button updateButton;
    TextView shelterText;
    EditText vacanciesEditText;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vacancies);

        backButton = (Button) findViewById(R.id.button_back);
        updateButton = (Button) findViewById(R.id.button_update);
        shelterText = (TextView) findViewById(R.id.textView_shelterName);
        vacanciesEditText = (EditText) findViewById(R.id.editText_vacancies);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Shelter shelt = dataSnapshot.child("shelters").child(((ShelterEmployee) Model
                            .getInstance().getCurrentUser()).getShelterID())
                            .getValue(Shelter.class);
                    shelterText.setText("Shelter: " + shelt.getName());
                    vacanciesEditText.setText("" + shelt.getVacancies());
                } catch (NullPointerException e) {
                    shelterText.setText("Shelter: ####");
                }
                myRef.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePressed();
            }
        });
    }

    private void updatePressed() {
        final int newVacancies;
        try {
            newVacancies = Integer.parseInt(vacanciesEditText.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(UpdateVacanciesActivity.this, "Vacancy must be a number",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO: Replace with if (vancancies > capacity)
        if (false) {
            Toast.makeText(UpdateVacanciesActivity.this, "Vacancy cannot exceed "
                            + "capacity", Toast.LENGTH_SHORT).show();
            return;
        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ShelterEmployee shemp = (ShelterEmployee) Model.getInstance().getCurrentUser();
                Shelter shelter = dataSnapshot.child("shelters").child(shemp.getShelterID())
                        .getValue(Shelter.class);
                try {
                    shelter.setVacancies(newVacancies);
                    myRef.child("shelters").child(shemp.getShelterID()).setValue(shelter);
                } catch (NullPointerException e) {
                    Toast.makeText(UpdateVacanciesActivity.this, "Your Shelter does "
                            + "not exist!", Toast.LENGTH_SHORT).show();
                }
                myRef.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
