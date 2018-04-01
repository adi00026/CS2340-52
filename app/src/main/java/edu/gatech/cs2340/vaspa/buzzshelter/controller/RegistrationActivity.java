package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import edu.gatech.cs2340.vaspa.buzzshelter.R;

/**
 * Class to handle the registration activity. Everyone can access this page
 * from the welcome page.
 *
 * @author Aniruddha Das
 * @version 6.9
 */

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

        arrayAdapter.setDropDownViewResource(R.layout.spinner_layout_1);
        typeReg.setAdapter(arrayAdapter);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 nextClicked();
            }
        });

        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        typeReg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                ((TextView) parent.getChildAt(0)).setTextSize(23);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Method called when listener for next button is called
     */
    void nextClicked() {
        String name = nameBox.getText().toString().trim();
        String username = usernameBox.getText().toString().trim();
        String password = passwordBox.getText().toString().trim();
        String contactInfo = contactInfoBox.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            Toast.makeText(RegistrationActivity.this, "Please enter a valid email",
              Toast.LENGTH_SHORT).show();
            return;
        }

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


        if (password.length() < 8) {
            Toast.makeText(RegistrationActivity.this, "Password too short. " +
                 "Must be atleast 8 characters long", Toast.LENGTH_SHORT).show();
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
