package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;

import edu.gatech.cs2340.vaspa.buzzshelter.R;

public class RegistrationActivity extends AppCompatActivity {

    private EditText usernameBox;
    private EditText nameBox;
    private EditText contactInfoBox;
    private EditText passwordBox;
    private EditText passwordConfirmBox;
    private Spinner typeReg;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        usernameBox = (EditText) findViewById(R.id.registration_username);
        nameBox = (EditText) findViewById(R.id.registration_name);
        contactInfoBox = (EditText) findViewById(R.id.registration_contact_info);
        passwordBox = (EditText) findViewById(R.id.password_field);
        passwordConfirmBox = (EditText) findViewById(R.id.password_confirm);
        typeReg = (Spinner) findViewById(R.id.spinner_type);
        nextButton = (Button) findViewById(R.id.next_button);

        String[] userType = {"User", "Admin", "Shelter Employee"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
          android.R.layout.simple_spinner_item, Arrays.asList(userType));

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeReg.setAdapter(arrayAdapter);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 nextClicked();
            }
        });
    }
    void nextClicked() {
        String name = nameBox.getText().toString().trim();
        String username = usernameBox.getText().toString().trim();
        String password = passwordBox.getText().toString().trim();
        String contactInfo = contactInfoBox.getText().toString().trim();

        // checks for password text equality

        if (!password.equals(passwordConfirmBox.getText().toString().trim())) {
            Toast.makeText(RegistrationActivity.this, "Passwords do not match",
              Toast.LENGTH_SHORT).show();

            return;
        }

        // implies that passwords are equal

        if (name.length() == 0 || username.length() == 0 || password.length() == 0
          || contactInfo.length() == 0) {
            Toast.makeText(RegistrationActivity.this, "One or more of the inputted values" +
              " is empty", Toast.LENGTH_SHORT).show();
            return;
        }


        if (password.length() < 6) {
            Toast.makeText(RegistrationActivity.this, "Password too short. " +
                 "Must be atleast 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedItem = typeReg.getSelectedItem().toString();

        Intent nextIntent;

        if (selectedItem.equalsIgnoreCase("User")) {
            nextIntent = new Intent(RegistrationActivity.this,
              UserRegistrationActivity.class);
        } else if (selectedItem.equalsIgnoreCase("Admin")) {
            nextIntent = new Intent(RegistrationActivity.this,
              AdminRegistrationActivity.class);
        } else {
            nextIntent = new Intent(RegistrationActivity.this,
              ShelterEmployeeRegistrationActivity.class);
        }

        nextIntent.putExtra("name", name);
        nextIntent.putExtra("username", username);
        nextIntent.putExtra("password", password);
        nextIntent.putExtra("contactInfo", contactInfo);

        startActivity(nextIntent);

    }
}
