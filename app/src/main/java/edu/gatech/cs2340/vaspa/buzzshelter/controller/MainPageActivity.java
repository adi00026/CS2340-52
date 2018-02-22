package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.AccountHolder;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Admin;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Shelter;
import edu.gatech.cs2340.vaspa.buzzshelter.model.ShelterEmployee;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;

public class MainPageActivity extends AppCompatActivity {
    TextView welcomeTextview;
    Button logoutButton;

    private static final String TAG = "MAIN PAGE ACTIVITY";

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        setContentView(R.layout.activity_main_page);

        welcomeTextview = (TextView) findViewById(R.id.textview_welcome);
        logoutButton = (Button) findViewById(R.id.button_logout);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mAuth.getCurrentUser() != null) {
                    String UID = mAuth.getCurrentUser().getUid();
                    AccountHolder currentlyLoggedIn = null;
                    String desig = "";
                    if (dataSnapshot.child("account_holders").child("admins").child(UID)
                            .exists()) {
                        currentlyLoggedIn = dataSnapshot.child("account_holders")
                                .child("admins").child(UID).getValue(Admin.class);
                        desig = "Admin ";
                    }
                    if (dataSnapshot.child("account_holders").child("shelter_employees")
                            .child(UID).exists()) {
                        currentlyLoggedIn = dataSnapshot.child("account_holders")
                                .child("shelter_employees").child(UID)
                                .getValue(ShelterEmployee.class);
                        desig = "Account Holder ";
                    }
                    if (dataSnapshot.child("account_holders").child("users").child(UID).exists()) {
                        currentlyLoggedIn = dataSnapshot.child("account_holders").child("users")
                                .child(UID).getValue(User.class);
                    }
                    Model.getInstance().setCurrentUser(currentlyLoggedIn);
                    /*welcomeTextview.setText(currentlyLoggedIn == null ? "NULL" : currentlyLoggedIn
                            .toString());*/
                    welcomeTextview.setText(currentlyLoggedIn == null ?
                            "No user currently logged in" : "Welcome, " + desig
                            + currentlyLoggedIn.getName() + "!");
                } else {
                    welcomeTextview.setText("No current user!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this,
                        WelcomePageActivity.class);
                mAuth.signOut();
                Model.getInstance().setCurrentUser(null);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
