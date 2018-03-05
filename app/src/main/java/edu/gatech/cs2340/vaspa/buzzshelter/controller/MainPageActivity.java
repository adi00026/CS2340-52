package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.AccountHolder;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Admin;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;
import edu.gatech.cs2340.vaspa.buzzshelter.model.ShelterEmployee;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;

public class MainPageActivity extends AppCompatActivity {
    TextView welcomeTextview;
    Button logoutButton;
    Button settingsButton;
    Button searchSheltersButton;
    Button manageUsersButton;
    Button updateVacanciesButton;
    ProgressDialog progressDialog;

    private static final String TAG = "MAIN PAGE ACTIVITY";

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_page);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading your information...");
        progressDialog.show();

        welcomeTextview = (TextView) findViewById(R.id.textview_welcome);
        logoutButton = (Button) findViewById(R.id.button_logout);
        settingsButton = (Button) findViewById(R.id.button_settings);
        searchSheltersButton = (Button) findViewById(R.id.button_search);
        manageUsersButton = (Button) findViewById(R.id.button_manage_users);
        updateVacanciesButton = (Button) findViewById(R.id.button_update_vacancies);

        setUpButtons(Model.getInstance().getCurrentUser());

        final String password = getIntent().getStringExtra("password");
        // gets it in case password changed as a result of recovery

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mAuth.getCurrentUser() != null) {
                    String UID = mAuth.getCurrentUser().getUid();
                    AccountHolder currentlyLoggedIn = null;
                    if (dataSnapshot.child("account_holders").child("admins").child(UID)
                            .exists()) {
                        myRef.child("account_holders").child("admins").child(UID)
                          .child("password").setValue(password);
                        currentlyLoggedIn = dataSnapshot.child("account_holders")
                                .child("admins").child(UID).getValue(Admin.class);
                    }
                    if (dataSnapshot.child("account_holders").child("shelter_employees")
                            .child(UID).exists()) {
                        myRef.child("account_holders").child("shelter_employees").child(UID)
                          .child("password").setValue(password);
                        currentlyLoggedIn = dataSnapshot.child("account_holders")
                                .child("shelter_employees").child(UID)
                                .getValue(ShelterEmployee.class);
                    }
                    if (dataSnapshot.child("account_holders").child("users").child(UID).exists()) {
                        myRef.child("account_holders").child("users").child(UID)
                          .child("password").setValue(password);
                        currentlyLoggedIn = dataSnapshot.child("account_holders").child("users")
                                .child(UID).getValue(User.class);
                    }
                    if (currentlyLoggedIn.isLockedOut()) {
                        welcomeTextview.setText("ACCOUNT IS LOCKED");
                        settingsButton.setEnabled(false);
                        searchSheltersButton.setEnabled(false);
                        manageUsersButton.setEnabled(false);
                        updateVacanciesButton.setEnabled(false);
                        logoutButton.setVisibility(View.VISIBLE);
                        logoutButton.setEnabled(true);
                    } else if (currentlyLoggedIn.isDeleted()) {
                        welcomeTextview.setText("ACCOUNT IS DELETED");
                        settingsButton.setEnabled(false);
                        searchSheltersButton.setEnabled(false);
                        manageUsersButton.setEnabled(false);
                        updateVacanciesButton.setEnabled(false);
                        logoutButton.setVisibility(View.VISIBLE);
                        logoutButton.setEnabled(true);
                    } else {
                        Model.getInstance().setCurrentUser(currentlyLoggedIn);
                        welcomeTextview.setText(currentlyLoggedIn == null ?
                          "No user currently logged in" : "Welcome, "
                          + currentlyLoggedIn.getName() + "!");
                        setUpButtons(currentlyLoggedIn);
                    }
                } else {
                    welcomeTextview.setText("No current user!");
                }
                progressDialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutPressed();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsPressed();
            }
        });

        searchSheltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchShelterClicked();
            }
        });

        updateVacanciesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateVacanciesPressed();
            }
        });

        manageUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageUsersPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        logoutPressed();
    }

    private void logoutPressed() {
        Intent intent = new Intent(MainPageActivity.this,
                WelcomePageActivity.class);
        final AccountHolder currUser = Model.getInstance().getCurrentUser();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // done because .'s cannot be withing filepath
                String path = currUser.getUserId().replace(".", ",");
                final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                        format(Calendar.getInstance().getTime()); // Current date and time
                String log = date + ", " + currUser.getUserId() + ", "
                        + "logged out";
                Model.getInstance().updateLogs(log);
                if (dataSnapshot.child("logging").child(path).exists()) {
                    // gets earlier logs
                    String prevLog = dataSnapshot.child("logging")
                            .child(path).getValue(String.class);
                    // appends latest logs to earlier logs
                    prevLog += Model.getInstance().getLogs();
                    myRef.child("logging").child(path).setValue(prevLog);
                } else {
                    // as no logs are available, the current logs are put up
                    myRef.child("logging").child(path)
                            .setValue(Model.getInstance().getLogs());
                }
                progressDialog.dismiss();
                Model.getInstance().clearLog();
                myRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mAuth.signOut();
        Model.getInstance().setCurrentUser(null);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void settingsPressed() {
        Intent intent;
        if (Model.getInstance().getCurrentUser() instanceof User) {
            intent = new Intent(MainPageActivity.this, UserSettingsActivity.class);
        } else if (Model.getInstance().getCurrentUser() instanceof ShelterEmployee) {
            intent = new Intent(MainPageActivity.this,
                    ShelterEmployeeSettingsActivity.class);
        } else if (Model.getInstance().getCurrentUser() instanceof Admin) {
            intent = new Intent(MainPageActivity.this, AdminSettingsActivity.class);
        } else {
            Toast.makeText(MainPageActivity.this, "No user currently logged in!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
    }

    private void searchShelterClicked() {
        startActivity(new Intent(MainPageActivity.this,
                SearchShelterActivity.class));
    }

    private void updateVacanciesPressed() {
        startActivity(new Intent(MainPageActivity.this,
                UpdateVacanciesActivity.class));
    }

    private void manageUsersPressed() {
        startActivity(new Intent(MainPageActivity.this,
                ManageUsersActivity.class));
    }

    private void setUpButtons(AccountHolder currentlyLoggedIn) {
        if (currentlyLoggedIn == null) {
            // Disabling all buttons
            logoutButton.setEnabled(false);
            settingsButton.setEnabled(false);
            searchSheltersButton.setEnabled(false);
            manageUsersButton.setEnabled(false);
            updateVacanciesButton.setEnabled(false);

            // Setting visibilities all to false
            logoutButton.setVisibility(View.INVISIBLE);
            settingsButton.setVisibility(View.INVISIBLE);
            searchSheltersButton.setVisibility(View.INVISIBLE);
            manageUsersButton.setVisibility(View.INVISIBLE);
            updateVacanciesButton.setVisibility(View.INVISIBLE);
        } else {
            logoutButton.setEnabled(true);
            settingsButton.setEnabled(true);
            searchSheltersButton.setEnabled(currentlyLoggedIn instanceof User);
            manageUsersButton.setEnabled(currentlyLoggedIn instanceof Admin);
            updateVacanciesButton.setEnabled(currentlyLoggedIn instanceof ShelterEmployee);

            logoutButton.setVisibility(View.VISIBLE);
            settingsButton.setVisibility(View.VISIBLE);
            searchSheltersButton.setVisibility(currentlyLoggedIn instanceof User ?
                    View.VISIBLE : View.INVISIBLE);
            manageUsersButton.setVisibility(currentlyLoggedIn instanceof Admin ?
                    View.VISIBLE : View.INVISIBLE);
            updateVacanciesButton
                    .setVisibility(currentlyLoggedIn instanceof ShelterEmployee ?
                            View.VISIBLE : View.INVISIBLE);
        }
    }
}
