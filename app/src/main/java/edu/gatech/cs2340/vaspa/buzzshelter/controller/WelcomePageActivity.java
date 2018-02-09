package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.backend.DatabaseHandler;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;
import edu.gatech.cs2340.vaspa.buzzshelter.util.PersonNotInDatabaseException;
import edu.gatech.cs2340.vaspa.buzzshelter.util.TooManyAttemptsException;
import edu.gatech.cs2340.vaspa.buzzshelter.util.WrongPasswordException;

public class WelcomePageActivity extends AppCompatActivity {
    Button loginButton;
    Button cancelButton;
    Button resetButton;
    Button viewDBButton;
    EditText usernameEditText;
    EditText passwordEditText;
    DatabaseHandler dh;

    /**
     * method called when welcome page is loaded.
     *
     * @param savedInstanceState auto generated
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        loginButton = (Button) findViewById(R.id.button_login);
        cancelButton = (Button) findViewById(R.id.button_cancel);
        resetButton = (Button) findViewById(R.id.button_reset);
        viewDBButton = (Button) findViewById(R.id.button_viewDB);
        usernameEditText = (EditText) findViewById(R.id.editText_username);
        passwordEditText = (EditText) findViewById(R.id.editText_password);
        dh = new DatabaseHandler(this);

        // TODO: Remove this after M4
        User tempUser = new User("user", "9082376913", "pass");
        dh.putUser(tempUser);
        dh.resetLogins();

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
                dh.resetLogins();
            }
        });
        viewDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("WelcomePageActivity", dh.viewDatabase());
            }
        });
    }

    /**
     * Method to handle what happens when the "login" button is pressed
     */
    private void loginPressed() {
        String text;
        try {
            dh.attemptLogin(usernameEditText.getText().toString().trim(),
                    passwordEditText.getText().toString().trim());
            text = "Login successful!";
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
