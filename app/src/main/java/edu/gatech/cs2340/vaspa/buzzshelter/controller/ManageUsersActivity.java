package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import edu.gatech.cs2340.vaspa.buzzshelter.R;

public class ManageUsersActivity extends AppCompatActivity {
    private Button backButton;
    private Button removeButton;
    private Button addButton;
    private Button disableButton;
    private Button enableButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        backButton = (Button) findViewById(R.id.button_back);
        removeButton = (Button) findViewById(R.id.button_remove);
        addButton = (Button) findViewById(R.id.button_add);
        disableButton = (Button) findViewById(R.id.button_disable);
        enableButton = (Button) findViewById(R.id.button_enable);

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
    private void setUserEnabled(boolean status) {
        /**
         * Search database. If not found, Toast it. If found, set enabled to false.
         */

    }
}
