package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.gatech.cs2340.vaspa.buzzshelter.R;

public class ChangePasswordActivity extends AppCompatActivity {
    Button backButton;
    Button changePasswordButton;
    EditText oldPasswordText;
    EditText newPasswordText;
    EditText newPasswordRepeatText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        backButton = (Button) findViewById(R.id.button_back);
        changePasswordButton = (Button) findViewById(R.id.button_changePassword);
        oldPasswordText = (EditText) findViewById(R.id.editText_oldPassword);
        newPasswordText = (EditText) findViewById(R.id.editText_newPassword);
        newPasswordRepeatText = (EditText) findViewById(R.id.editText_newPassword_repeat);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePasswordActivity.this,
                        AdminSettingsActivity.class));
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordPressed();
            }
        });
    }

    private void changePasswordPressed() {

    }
}
