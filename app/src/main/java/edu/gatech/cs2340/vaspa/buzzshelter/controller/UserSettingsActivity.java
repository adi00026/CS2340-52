package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;
import edu.gatech.cs2340.vaspa.buzzshelter.model.ShelterEmployee;
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

        userIDText.setText(Model.getInstance().getCurrentUser().getUserId());
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
                startActivity(new Intent(UserSettingsActivity.this,
                        MainPageActivity.class));
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

    }
}
