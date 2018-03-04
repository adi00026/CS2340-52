package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.AccountHolder;
import edu.gatech.cs2340.vaspa.buzzshelter.model.ShelterEmployee;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;

public class ManageUsersActivity extends AppCompatActivity {
    private Button backButton;
    private Button removeButton;
    private Button addButton;
    private Button disableButton;
    private Button enableButton;
    private EditText userIDText;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    private AccountHolder toManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        backButton = (Button) findViewById(R.id.button_back);
        removeButton = (Button) findViewById(R.id.button_remove);
        addButton = (Button) findViewById(R.id.button_add);
        disableButton = (Button) findViewById(R.id.button_disable);
        enableButton = (Button) findViewById(R.id.button_enable);
        userIDText = (EditText) findViewById(R.id.editText);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Method 1: Search database. If not there, Toast it. If there, record UID. Then,
                 * get username and password of User. Log in and delete account.
                 * Then, remove node UID and all its children from database.
                 *
                 * Method 2: Search database. If not there, Toast it. If there, remove node UID
                 * and all its children from database. Upon next login, alert user their account
                 * has been deleted and delete it then.
                 */
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Do we really need this?
            }
        });
        disableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserEnabled(false);
            }
        });
        enableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserEnabled(true);
            }
        });
    }

    /**
     * Method to set a certain homeless user's account to either enabled or
     * disabled.
     *
     * @param status what you want the User's enabled status to be.
     */
    private void setUserEnabled(final boolean status) {
        /**
         * Search database. If not found, Toast it. If found, set locked out to status.
         */

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("account_holders").child("users")
                        .getChildren()) {
                    String key = ds.getKey();
                    toManage = ds.getValue(User.class);
                    if (toManage != null && toManage.getUserId().equals(userIDText
                            .getText().toString())) {
                        toManage.setLockedOut(!status);
                        myRef.child("account_holders").child("users").child(key)
                                .setValue(toManage);
                        Toast.makeText(ManageUsersActivity.this, "User "
                                + (status ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT)
                                .show();
                        myRef.removeEventListener(this);
                        return;
                    }
                }
                for (DataSnapshot ds : dataSnapshot.child("account_holders")
                        .child("shelter_employees").getChildren()) {
                    String key = ds.getKey();
                    toManage = ds.getValue(ShelterEmployee.class);
                    if (toManage != null && toManage.getUserId().equals(userIDText
                            .getText().toString())) {
                        toManage.setLockedOut(!status);
                        myRef.child("account_holders").child("shelter_employees").child(key)
                                .setValue(toManage);
                        Toast.makeText(ManageUsersActivity.this, "User "
                                + (status ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT)
                                .show();
                        myRef.removeEventListener(this);
                        return;
                    }
                }
                Toast.makeText(ManageUsersActivity.this, "\"" + userIDText.getText()
                                .toString() + "\" does not exist",
                        Toast.LENGTH_SHORT).show();
                myRef.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
