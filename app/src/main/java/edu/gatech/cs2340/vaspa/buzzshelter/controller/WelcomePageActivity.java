package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;
import edu.gatech.cs2340.vaspa.buzzshelter.model.ShelterEmployee;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;

public class WelcomePageActivity extends AppCompatActivity {
    private Model model;
    private Button loginButton;
    private Button cancelButton;
    private Button registrationButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

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
        database  = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        setContentView(R.layout.activity_welcome_page);

        model = Model.getInstance();

        loginButton = (Button) findViewById(R.id.button_login);
        cancelButton = (Button) findViewById(R.id.button_cancel);
        registrationButton = (Button) findViewById(R.id.button_registration);
        usernameEditText = (EditText) findViewById(R.id.editText_username);
        passwordEditText = (EditText) findViewById(R.id.editText_password);
        progressDialog = new ProgressDialog(this);

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
        try {
            if (getIntent().getExtras().containsKey("username")) {
                usernameEditText.setText(getIntent().getExtras().getString("username"));
            }
            if (getIntent().getExtras().containsKey("password")) {
                passwordEditText.setText(getIntent().getExtras().getString("password"));
            }
        } catch (NullPointerException e) {
            // Intent does not exist
        }
    }

    /**
     * Method to handle what happens when the "login" button is pressed
     */
    private void loginPressed() {
        loginButton.setEnabled(false);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        String email = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
          format(Calendar.getInstance().getTime()); // Current date and time

        if (email.isEmpty() || password.isEmpty()) {
            return;
        }

        final String uid = email;  // Done because inner classes need final variables

        final int loginAttempts = model.incrementLoginAttempts(uid);
        if (loginAttempts == 4) {
            // implies this uids login attempts have exceeded 3 for the first time
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean done = false;
                    for (DataSnapshot ds : dataSnapshot.child("account_holders").child("users")
                      .getChildren()) {
                        String key = ds.getKey();
                        // TODO update that user was locked out
                        User user = ds.getValue(User.class);
                        if (user.getUserId().equals(uid)) {
                            done = true;
                            user.setLockedOut(true);
                            // sets locked out to true
                            // adds user to database with locked out value to true
                            myRef.child("account_holders").child("users").child(key).setValue(user);
                            Toast.makeText(WelcomePageActivity.this, "Locked out. Too many attempts",
                              Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (!done) {
                        for (DataSnapshot ds : dataSnapshot.child("account_holders").child("shelter_employees")
                          .getChildren()) {
                            // TODO update that user was locked out
                            String key = ds.getKey();
                            ShelterEmployee user = ds.getValue(ShelterEmployee.class);
                            if (user.getUserId().equals(uid)) {
                                user.setLockedOut(true);
                                // sets locked out to true
                                // adds user to database with locked out value to true
                                myRef.child("account_holders").child("shelter_employees").child(key).setValue(user);
                                Toast.makeText(WelcomePageActivity.this, "Locked out. Too many attempts",
                                  Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    myRef.removeEventListener(this);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            progressDialog.dismiss();
            loginButton.setEnabled(true);
            return;
        } else if (loginAttempts > 4) {
            Toast.makeText(WelcomePageActivity.this, "Locked out. Too many attempts",
              Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            loginButton.setEnabled(true);
            return;
        }

        mAuth.signInWithEmailAndPassword(uid, password)
          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                  if (task.isSuccessful()) {

                      String log = date + ", " + uid + ", " + "Logged In"; // LOG message

                      // Sign in success, update UI with the signed-in user's information
                      FirebaseUser user = mAuth.getCurrentUser();
                      Toast.makeText(WelcomePageActivity.this, "Login succeeded.",
                        Toast.LENGTH_SHORT).show();
                      Intent intent = new Intent(WelcomePageActivity.this,
                        MainPageActivity.class);

                      // Clears login attempts on successful login
                      model.clearLoginAttempts(uid);
                      // Updates logs
                      model.updateLogs(log);
                      startActivity(intent);
                  } else {
                      // If sign in fails, display a message to the user.
                      Toast.makeText(WelcomePageActivity.this, "Incorrect username" +
                          " or password.", Toast.LENGTH_SHORT).show();
                  }
                  loginButton.setEnabled(true);
                  progressDialog.dismiss();
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
