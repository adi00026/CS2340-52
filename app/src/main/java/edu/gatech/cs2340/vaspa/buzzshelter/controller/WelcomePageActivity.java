package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;
import edu.gatech.cs2340.vaspa.buzzshelter.util.PersonNotInDatabaseException;
import edu.gatech.cs2340.vaspa.buzzshelter.util.TooManyAttemptsException;
import edu.gatech.cs2340.vaspa.buzzshelter.util.WrongPasswordException;

public class WelcomePageActivity extends AppCompatActivity {
    Model model;
    Button loginButton;
    Button cancelButton;
    Button resetButton;
    Button registrationButton;
    Button viewDBButton;
    EditText usernameEditText;
    EditText passwordEditText;

    /**
     * method called when welcome page is loaded.
     *
     * @param savedInstanceState auto generated
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        model = Model.getInstance();

        loginButton = (Button) findViewById(R.id.button_login);
        cancelButton = (Button) findViewById(R.id.button_cancel);
        resetButton = (Button) findViewById(R.id.button_reset);
        viewDBButton = (Button) findViewById(R.id.button_viewDB);
        registrationButton = (Button) findViewById(R.id.button_registration);
        usernameEditText = (EditText) findViewById(R.id.editText_username);
        passwordEditText = (EditText) findViewById(R.id.editText_password);

        model.createDummyLogin(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPressed();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelPressed();
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.resetLogins(WelcomePageActivity.this);
            }
        });
        viewDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("WelcomePageActivity",
                        model.viewDatabase(WelcomePageActivity.this));
            }
        });
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomePageActivity.this,
                        RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Method to handle what happens when the "login" button is pressed
     */
    private void loginPressed() {
        String text;
        try {
            model.attemptLogin(this,
                    usernameEditText.getText().toString().trim(),
                    passwordEditText.getText().toString().trim());
            text = "Login successful!";
            Intent intent = new Intent(WelcomePageActivity.this, MainPageActivity.class);
            intent.putExtra("USERNAME", usernameEditText.getText().toString().trim());
            startActivity(intent);
        } catch (TooManyAttemptsException e) {
            text = "Too many log-in attempts!";
        } catch (PersonNotInDatabaseException e) {
            text = "Username not found";
        } catch (WrongPasswordException e) {
            text = "Wrong password!";
        }
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method to handle what happens when the "cancel" button is pressed
     */
    private void cancelPressed() {
        usernameEditText.setText("");
        passwordEditText.setText("");
        Toast.makeText(getApplicationContext(), "Login Canceled!",
                Toast.LENGTH_SHORT).show();
    }
}
