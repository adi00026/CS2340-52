package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.gatech.cs2340.vaspa.buzzshelter.R;

public class AdminRegistrationActivity extends AppCompatActivity {

    private EditText keyText;
    private Button backButton;
    private Button finish;

    private String adminKey = "temp_admin_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration);

        keyText = (EditText) findViewById(R.id.admin_Key);
        backButton = (Button) findViewById(R.id.admin_back);
        finish = (Button) findViewById(R.id.Finish_Button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminRegistrationActivity.this,
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
        if (adminKey.equals(key)) {
            Toast.makeText(this, "Welcome new admin " + getIntent()
              .getStringExtra("name"), Toast.LENGTH_SHORT).show();
            // CODE TO ADD NEW ADMIN
        } else {
            Toast.makeText(this, "The inputted key is incorrect",
              Toast.LENGTH_SHORT).show();
        }
    }
}
