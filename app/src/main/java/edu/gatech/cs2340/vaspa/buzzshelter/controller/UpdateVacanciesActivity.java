package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import edu.gatech.cs2340.vaspa.buzzshelter.model.ShelterEmployee;
import edu.gatech.cs2340.vaspa.buzzshelter.util.StringOps;

/**
 * Class to handle the Update Vacancies page activity. This page is only accessible to Shelter
 * Employees.
 *
 * @author Sanath Nagaraj
 * @version 6.9
 */
@SuppressWarnings("ConstantConditions")
public class UpdateVacanciesActivity extends AppCompatActivity {
    private TextView shelterText;
    private EditText vacanciesEditText;

    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vacancies);

        Button backButton = findViewById(R.id.button_back);
        Button updateButton = findViewById(R.id.button_update);
        shelterText = findViewById(R.id.textView_shelterName);
        vacanciesEditText = findViewById(R.id.editText_vacancies);

        //mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressWarnings("ProhibitedExceptionCaught")
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

    /**
     * Method to handle what happens when the update vacancies button is pressed. If the entered
     * number of vacancies is not numeric, an error is displayed. If the entered number is less than
     * 0 or larger than this employee's respective shelter's capacity, an error is displayed. If
     * no error, this activity is added to the local log and then the the vacancies are updated on
     * both firebase and locally.
     */
    private void updatePressed() {
        ShelterEmployee currentUser = (ShelterEmployee) Model.getInstance().getCurrentUser();
        int vacancies;
        try {
            String vacancyText = vacanciesEditText.getText().toString().trim();
            vacancies = Integer.parseInt(vacancyText);
        } catch (NumberFormatException e) {
            Toast.makeText(UpdateVacanciesActivity.this, "Vacancies must be a number",
              Toast.LENGTH_SHORT).show();
            return;
        }
        if (vacancies < 0) {
            Toast.makeText(UpdateVacanciesActivity.this,
                    "Vacancies must be non-negative", Toast.LENGTH_SHORT).show();
            return;
        }
        Shelter thisShelter = Model.getInstance().getShelters().get(currentUser.getShelterID());
        int capacity = StringOps.parseCapacity(thisShelter.getCapacity());
        if (vacancies > capacity) {
            Toast.makeText(UpdateVacanciesActivity.this, "Vacancy cannot exceed "
                    + "capacity", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO remove once updating of vacancies is implemented
        if (false) {
            final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
              format(Calendar.getInstance().getTime()); // Current date and time
            String log = date + ", " + "SHELTER EMPLOYEE: " + currentUser.getUserId() + ", " +
              "updated vacancies for: " + currentUser.getShelterID();
            Model.getInstance().updateLogs(log);
        }

        final int newVacancies = vacancies;
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressWarnings("ProhibitedExceptionCaught")
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
