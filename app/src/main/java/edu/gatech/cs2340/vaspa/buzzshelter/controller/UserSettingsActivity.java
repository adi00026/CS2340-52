package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;

public class UserSettingsActivity extends AppCompatActivity {
    Button backButton;
    Button updateButton;
    Button changePasswordButton;
    TextView userIDText;
    EditText nameEditText;
    EditText contactEditText;
    Spinner genderSpinner;
    CheckBox vetStatusCheckbox;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        backButton = (Button) findViewById(R.id.button_back);
        updateButton = (Button) findViewById(R.id.button_update);
        changePasswordButton = (Button) findViewById(R.id.button_password);
        userIDText = (TextView) findViewById(R.id.textView_userid);
        nameEditText = (EditText) findViewById(R.id.editText_name);
        contactEditText = (EditText) findViewById(R.id.editText_contact);
        genderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        vetStatusCheckbox = (CheckBox) findViewById(R.id.checkBox_vets);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        userIDText.setText("User ID: " + Model.getInstance().getCurrentUser().getUserId());
        nameEditText.setText(Model.getInstance().getCurrentUser().getName());
        contactEditText.setText(Model.getInstance().getCurrentUser().getContactInfo());
        vetStatusCheckbox.setChecked(((User) Model.getInstance().getCurrentUser()).isVeteran());

        // Spinner set up
        String[] genderArray = {"Male", "Female", "Other"};
        ArrayAdapter<String> adapterGender = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Arrays.asList(genderArray));
        adapterGender.setDropDownViewResource(R.layout.spinner_layout_1);
        genderSpinner.setAdapter(adapterGender);
        String gender = ((User) Model.getInstance().getCurrentUser()).getGender();
        genderSpinner.setSelection(gender.equals("Male") ? 0 : (gender.equals("Female") ? 1 : 2));
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                ((TextView) parent.getChildAt(0)).setTextSize(23);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Button events
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserSettingsActivity.this,
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

    private void updatePressed() {
        User user = (User) Model.getInstance().getCurrentUser();
        String name = nameEditText.getText().toString().trim();
        String contactInfo = contactEditText.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString().trim();
        boolean isVeteran = vetStatusCheckbox.isChecked();
        if (!(user.getName().equals(name) && user.getContactInfo().equals(contactInfo)
                && user.getGender().equals(gender) && user.isVeteran() == isVeteran)) {
            user.setName(name);
            user.setContactInfo(contactInfo);
            user.setGender(gender);
            user.setVeteran(isVeteran);
            Model.getInstance().setCurrentUser(user);
            String UID = mAuth.getCurrentUser().getUid();
            myRef.child("account_holders").child("users").child(UID).setValue(user);
            final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
              format(Calendar.getInstance().getTime()); // Current date and time
            String log = date + ", " + "USER: " + user.getUserId() + ", updated settings";
            Model.getInstance().updateLogs(log);
        }
        Toast.makeText(UserSettingsActivity.this, "Information updated!",
                Toast.LENGTH_SHORT).show();
    }
}
