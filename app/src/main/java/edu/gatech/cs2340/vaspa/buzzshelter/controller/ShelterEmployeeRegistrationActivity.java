package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.gatech.cs2340.vaspa.buzzshelter.R;

public class ShelterEmployeeRegistrationActivity extends AppCompatActivity {

    private EditText keyText;
    private EditText shelterId;
    private Button backButton;
    private Button finish;

    private String shelterKey = "temp_shelter_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_employee_registration);

        keyText = (EditText) findViewById(R.id.Shelter_Employee_Key);
        shelterId = (EditText) findViewById(R.id.shelter_ID);
        finish = (Button) findViewById(R.id.Finish_Button_Shelter);
        backButton = (Button) findViewById(R.id.shelter_registration_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShelterEmployeeRegistrationActivity.this,
                  RegistrationActivity.class);
                startActivity(intent);
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishClicked();
            }
        });
    }

    private void finishClicked() {
        String key = keyText.getText().toString().trim();
        String shelterID = shelterId.getText().toString().trim();
        if (shelterKey.equals(key)) {
            if (shelterID.length() != 0) { // TODO will be replaced by .contains() of structure containing Shelter objects
                Toast.makeText(this, "Welcome new shelter employee " + getIntent()
                  .getStringExtra("name"), Toast.LENGTH_SHORT).show();
                // CODE TO ADD NEW SHELTER EMPLOYEE
            } else {
                Toast.makeText(this, "The inputted shelter ID does not exist",
                  Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "The inputted key is incorrect",
              Toast.LENGTH_SHORT).show();
        }
    }
}
