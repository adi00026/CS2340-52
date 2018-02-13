package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;

public class WelcomePageActivity extends AppCompatActivity {
    Model model;
    Button loginButton;
    Button cancelButton;
    Button registrationButton;
    EditText usernameEditText;
    EditText passwordEditText;

    private FirebaseAuth mAuth;

    private static final String TAG = "WELCOME PAGE ACTIVITY";

    /**
     * method called when welcome page is loaded.
     *
     * @param savedInstanceState auto generated
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_welcome_page);

        model = Model.getInstance();

        loginButton = (Button) findViewById(R.id.button_login);
        cancelButton = (Button) findViewById(R.id.button_cancel);
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
        String email = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email += "@temp.com";
        }

        mAuth.signInWithEmailAndPassword(email, password)
          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                  if (task.isSuccessful()) {
                      // Sign in success, update UI with the signed-in user's information
                      FirebaseUser user = mAuth.getCurrentUser();
                      Toast.makeText(WelcomePageActivity.this, "Login succeeded.",
                        Toast.LENGTH_SHORT).show();
                      Intent intent = new Intent(WelcomePageActivity.this,
                        MainPageActivity.class);
                      startActivity(intent);
                  } else {
                      // If sign in fails, display a message to the user.
                      Toast.makeText(WelcomePageActivity.this, "Incorrect username" +
                          " or password.", Toast.LENGTH_SHORT).show();
                  }
              }
          });
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
