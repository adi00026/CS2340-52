package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;
import edu.gatech.cs2340.vaspa.buzzshelter.model.ShelterEmployee;

/**
 * Class to handle the Shelter Employee Settings page activity. This is only accessible to Shelter
 * Employees.
 *
 * @author Sanath Nagaraj
 * @version 6.9
 */
public class ShelterEmployeeSettingsActivity extends AppCompatActivity {
    private Button backButton;
    private Button updateButton;
    private Button changePasswordButton;
    private TextView userIDText;
    private EditText nameEditText;
    private EditText contactEditText;
    private EditText shelterIDEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_employee_settings);

        backButton = (Button) findViewById(R.id.button_back);
        updateButton = (Button) findViewById(R.id.button_update);
        changePasswordButton = (Button) findViewById(R.id.button_password);
        userIDText = (TextView) findViewById(R.id.textView_userid);
        nameEditText = (EditText) findViewById(R.id.editText_name);
        contactEditText = (EditText) findViewById(R.id.editText_contact);
        shelterIDEditText = (EditText) findViewById(R.id.editText_shelterID);

        userIDText.setText("User ID: " + Model.getInstance().getCurrentUser().getUserId());
        nameEditText.setText(Model.getInstance().getCurrentUser().getName());
        contactEditText.setText(Model.getInstance().getCurrentUser().getContactInfo());
        shelterIDEditText.setText(((ShelterEmployee) Model.getInstance().getCurrentUser())
                .getShelterID());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShelterEmployeeSettingsActivity.this,
                        ChangePasswordActivity.class));
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
     * Method to handle what happens when the update button is pressed. If the data in the inputs
     * are different from what the Shelter Employee's current attribute values are, it updates
     * his/her info on firebase and locally on the app. If the entered unique shelter key is not
     * valid, it says so.
     */
    private void updatePressed() {
        final ShelterEmployee shelterEmployee = (ShelterEmployee) Model.getInstance().getCurrentUser();
        String name = nameEditText.getText().toString().trim();
        String contactInfo = contactEditText.getText().toString().trim();
        String shelterID = shelterIDEditText.getText().toString().trim();
        if (!Model.getInstance().getShelters().containsKey(shelterID)) {
            Toast.makeText(ShelterEmployeeSettingsActivity.this, "Invalid Shelter ID",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!name.equals(shelterEmployee.getName()) || !contactInfo.equals(shelterEmployee
          .getContactInfo()) || !shelterID.equals(shelterEmployee.getShelterID())) {
            ShelterEmployee newEmployee = new ShelterEmployee(name, shelterEmployee.getUserId(),
              shelterEmployee.getPassword(), contactInfo, shelterID);
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
            myRef.child("account_holders").child("shelter_employees").child(uid).setValue(newEmployee);
            Model.getInstance().setCurrentUser(newEmployee);
            final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
              format(Calendar.getInstance().getTime()); // Current date and time
            String log = date + ", " + "SHELTER EMPLOYEE: " + shelterEmployee.getUserId() + ", "
              + "updated settings";
            Model.getInstance().updateLogs(log);
            Toast.makeText(ShelterEmployeeSettingsActivity.this, "Information updated!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ShelterEmployeeSettingsActivity.this,
              "No attributes are different", Toast.LENGTH_SHORT).show();
        }
    }
}
