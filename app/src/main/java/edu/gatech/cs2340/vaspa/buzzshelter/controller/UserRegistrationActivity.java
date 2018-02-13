package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.LinkedList;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;

public class UserRegistrationActivity extends AppCompatActivity {
    Spinner genderSpinner;
    EditText DOBText;
    Spinner vetSpinner;
    Button backButton;
    Button registerButton;

    private final String TAG = "UserRegistrationAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        genderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        vetSpinner = (Spinner) findViewById(R.id.spinner_vetStatus);
        DOBText = (EditText) findViewById(R.id.editText_dob);
        backButton = (Button) findViewById(R.id.button_back);
        registerButton = (Button) findViewById(R.id.button_registration);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserRegistrationActivity.this,
                        RegistrationActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        String[] genderArray = {"Male", "Female", "Other"};
        ArrayAdapter<String> adapterGender = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Arrays.asList(genderArray));
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapterGender);

        String[] vetArray = {"US Veteran", "Not US Veteran"};
        ArrayAdapter<String> adapterVet = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Arrays.asList(vetArray));
        adapterVet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapterVet);


    }
    private void register() {
        Log.d(TAG, DOBText.getText().toString());
    }
}
