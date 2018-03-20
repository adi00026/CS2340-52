package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Admin;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;

/**
 * Class to handle the Admin Settings page activity. This is only accessible to Admins.
 *
 * @author Sanath Nagaraj
 * @version 6.9
 */
public class AdminSettingsActivity extends AppCompatActivity {
    Button backButton;
    Button updateButton;
    Button changePasswordButton;
    TextView userIDText;
    EditText nameEditText;
    EditText contactEditText;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        backButton = (Button) findViewById(R.id.button_back);
        updateButton = (Button) findViewById(R.id.button_update);
        changePasswordButton = (Button) findViewById(R.id.button_password);
        userIDText = (TextView) findViewById(R.id.textView_userid);
        nameEditText = (EditText) findViewById(R.id.editText_name);
        contactEditText = (EditText) findViewById(R.id.editText_contact);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        userIDText.setText("User ID: " + Model.getInstance().getCurrentUser().getUserId());
        nameEditText.setText(Model.getInstance().getCurrentUser().getName());
        contactEditText.setText(Model.getInstance().getCurrentUser().getContactInfo());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminSettingsActivity.this,
                        ChangePasswordActivity.class));
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePressed();
            }
        });
    }

    /**
     * Method to handle what happens when the update button is pressed. If the data in the inputs
     * are different from what the Admin's current attribute values are, it updates his/her info on
     * firebase and locally on the app. This action is logged locally.
     */
    private void updatePressed() {
        Admin admin = (Admin) Model.getInstance().getCurrentUser();
        if (!(nameEditText.getText().toString().trim().equals(admin.getName())
                && contactEditText.getText().toString().trim().equals(admin.getContactInfo()))) {
            admin.setName(nameEditText.getText().toString().trim());
            admin.setContactInfo(contactEditText.getText().toString().trim());
            Model.getInstance().setCurrentUser(admin);
            String UID = mAuth.getCurrentUser().getUid();
            myRef.child("account_holders").child("admins").child(UID).setValue(admin);
            final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
              format(Calendar.getInstance().getTime()); // Current date and time
            String log = date + ", " + "ADMIN: " + admin.getUserId() + ", updated settings";
            Model.getInstance().updateLogs(log);
        }
        Toast.makeText(AdminSettingsActivity.this, "Information updated!",
                Toast.LENGTH_SHORT).show();
    }
}
