package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import edu.gatech.cs2340.vaspa.buzzshelter.model.Guest;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Shelter;
import edu.gatech.cs2340.vaspa.buzzshelter.model.ShelterEmployee;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;

/**
 * Class to handle the Main page activity. All Account holders are directed to this page after
 * logging in. Depending on the type of user. certain buttons are visible and certain buttons are
 * not.
 *
 * @author Aniruddha Das, Sanath Nagaraj
 * @version 6.9
 */
@SuppressWarnings({"ConstantConditions", "ClassWithTooManyDependencies", "CyclicClassDependency"})
public class MainPageActivity extends AppCompatActivity {
    private TextView welcomeTextview;
    private Button logoutButton;
    private Button settingsButton;
    private Button searchSheltersButton;
    private Button manageUsersButton;
    private Button updateVacanciesButton;
    private ProgressDialog progressDialog;

    private static final String TAG = "MAIN PAGE ACTIVITY";

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @SuppressWarnings("LawOfDemeter")
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resumed");
        String shelterText = "";
        //noinspection LawOfDemeter
        AccountHolder currentlyLoggedIn = Model.getInstance().getCurrentUser();
        if (currentlyLoggedIn instanceof User) {
            //noinspection LawOfDemeter
            Shelter curr = Model.getInstance().getShelters()
                    .get(((User) currentlyLoggedIn).getShelterID());
            shelterText = "\nCurrent shelter:\n"
                    + (curr == null ? "NONE" : curr.getName());
        }
        welcomeTextview.setText((currentlyLoggedIn == null) ?
                "No user currently logged in" : ("Welcome, "
                + currentlyLoggedIn.getName() + "!\n" + shelterText));
    }

    @SuppressWarnings("LawOfDemeter")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_page);
        
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading your information...");
        progressDialog.show();

        welcomeTextview = findViewById(R.id.textview_welcome);
        logoutButton = findViewById(R.id.button_logout);
        settingsButton = findViewById(R.id.button_settings);
        searchSheltersButton = findViewById(R.id.button_search);
        manageUsersButton = findViewById(R.id.button_manage_users);
        updateVacanciesButton = findViewById(R.id.button_update_vacancies);

        setUpButtons(Model.getInstance().getCurrentUser());

        final String password = getIntent().getStringExtra("password");
        // gets it in case password changed as a result of recovery

        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod", "LawOfDemeter"})
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mAuth.getCurrentUser() != null) {
                    String UID = mAuth.getCurrentUser().getUid();
                    AccountHolder currentlyLoggedIn = null;
                    if (dataSnapshot.child("account_holders").child("admins").child(UID)
                            .exists()) {
                        currentlyLoggedIn = dataSnapshot.child("account_holders")
                                .child("admins").child(UID).getValue(Admin.class);
                        if (!currentlyLoggedIn.getPassword().equals(password)) {
                            myRef.child("account_holders").child("admins").child(UID)
                                    .child("password").setValue(password);
                        }
                    } else if (dataSnapshot.child("account_holders").child("shelter_employees")
                            .child(UID).exists()) {
                        currentlyLoggedIn = dataSnapshot.child("account_holders")
                                .child("shelter_employees").child(UID)
                                .getValue(ShelterEmployee.class);
                        if (!currentlyLoggedIn.getPassword().equals(password)) {
                            myRef.child("account_holders").child("shelter_employees").child(UID)
                                    .child("password").setValue(password);
                        }
                    } else if (dataSnapshot.child("account_holders").child("users").child(UID).exists()) {
                        currentlyLoggedIn = dataSnapshot.child("account_holders").child("users")
                                .child(UID).getValue(User.class);
                        if (!currentlyLoggedIn.getPassword().equals(password)) {
                            myRef.child("account_holders").child("users").child(UID)
                                    .child("password").setValue(password);
                        }
                    } else {
                        currentlyLoggedIn = new Guest();
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
                        String shelterText = "";
                        if (currentlyLoggedIn instanceof User) {
                            //noinspection LawOfDemeter
                            Shelter curr = Model.getInstance().getShelters()
                                    .get(((User) currentlyLoggedIn).getShelterID());
                            shelterText = "\n\nCurrent shelter:\n"
                                    + (curr == null ? "NONE" : curr.getName());
                        }
                        welcomeTextview.setText((currentlyLoggedIn == null) ?
                                "No user currently logged in" : ("Welcome, "
                                + currentlyLoggedIn.getName() + "!" + shelterText));
                        setUpButtons(currentlyLoggedIn);
                    }
                    Model.getInstance().setCurrentUser(currentlyLoggedIn);
                } else {
                    welcomeTextview.setText("No current user!");
                }
                progressDialog.dismiss();
                myRef.removeEventListener(this);
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

    /**
     * Method to handle what happens when the logout button is pressed. All logged data is pushed
     * up to firebase. User is logged out and returned to the Welcome Page.
     */
    @SuppressWarnings("LawOfDemeter")
    private void logoutPressed() {
        Intent intent = new Intent(MainPageActivity.this,
                WelcomePageActivity.class);
        //noinspection LawOfDemeter
        final AccountHolder currUser = Model.getInstance().getCurrentUser();
        if (!(currUser instanceof Guest)) {
            myRef.addValueEventListener(new ValueEventListener() {
                @SuppressWarnings("LawOfDemeter")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // TODO move to seperate logging class

                    // done because .'s cannot be withing filepath
                    //noinspection LawOfDemeter
                    String path = currUser.getUserId().replace(".", ",");
                    final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                            format(Calendar.getInstance().getTime()); // Current date and time
                    //noinspection LawOfDemeter
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
        }
        mAuth.signOut();
        Model.getInstance().setCurrentUser(null);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * Method to handle what happens when the settings button is pressed. Depending on what type of
     * AccountHolder is logged in, they are directed to the respective settings page.
     */
    @SuppressWarnings("LawOfDemeter")
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

    /**
     * Method to handle what happens when the Search Shelters button is pressed. This method can
     * only be accessed by a Homeless User. The Homeless User is directed to the Search Shelter
     * Activity
     */
    private void searchShelterClicked() {
        startActivity(new Intent(MainPageActivity.this,
                SearchShelterActivity.class));
    }

    /**
     * Method to handle what happens when the Update Vacancies button is pressed. This method can
     * only be accessed by a Shelter Employee. The Shelter Employee is directed to the Update
     * Vacancies Activity
     */
    private void updateVacanciesPressed() {
        startActivity(new Intent(MainPageActivity.this,
                UpdateVacanciesActivity.class));
    }

    /**
     * Method to handle what happens when the Manage Users button is pressed. This method can
     * only be accessed by a Admin. The Admiin is directed to the Manage Users Activity.
     */
    private void manageUsersPressed() {
        startActivity(new Intent(MainPageActivity.this,
                ManageUsersActivity.class));
    }

    /**
     * Method to set up which buttons are visible based on the currently logged in user. If no one
     * is logged in, all buttons are invisible. In all cases of some valid AccountHolder logged in,
     * Settings and Logout are both visible and enabled. The other three buttons are enabled
     * exclusively for the following type of AccountHolder:
     *  - Homeless User : Search Shelters
     *  - Shelter Employee : Update Vacancies
     *  - Admin : Manage Users
     *
     * @param currentlyLoggedIn is the AccountHolder object representing the currently logged in
     *                          user.
     */
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
            settingsButton.setEnabled(!(currentlyLoggedIn instanceof Guest));
            searchSheltersButton.setEnabled(currentlyLoggedIn instanceof User
                    || currentlyLoggedIn instanceof Guest);
            manageUsersButton.setEnabled(currentlyLoggedIn instanceof Admin);
            updateVacanciesButton.setEnabled(currentlyLoggedIn instanceof ShelterEmployee);
    
            logoutButton.setVisibility(View.VISIBLE);
            settingsButton.setVisibility(View.VISIBLE);
            searchSheltersButton.setVisibility((currentlyLoggedIn instanceof User
                    || currentlyLoggedIn instanceof Guest) ?
                    View.VISIBLE : View.INVISIBLE);
            manageUsersButton.setVisibility((currentlyLoggedIn instanceof Admin) ?
                    View.VISIBLE : View.INVISIBLE);
            updateVacanciesButton
                    .setVisibility((currentlyLoggedIn instanceof ShelterEmployee) ?
                            View.VISIBLE : View.INVISIBLE);
        }
    }
}
