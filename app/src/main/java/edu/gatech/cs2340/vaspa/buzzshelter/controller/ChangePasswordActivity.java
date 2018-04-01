package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Admin;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;

/**
 * Class to handle the Change Password page activity. All AccountHolders will access this page from
 * their respective settings page. This page is shared by all types of AccountHolders.
 *
 * @author Sanath Nagaraj
 * @version 6.9
 */
public class ChangePasswordActivity extends AppCompatActivity {
    private Button backButton;
    private Button changePasswordButton;
    private EditText oldPasswordText;
    private EditText newPasswordText;
    private EditText newPasswordRepeatText;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        backButton = findViewById(R.id.button_back);
        changePasswordButton = findViewById(R.id.button_changePassword);
        oldPasswordText = findViewById(R.id.editText_oldPassword);
        newPasswordText = findViewById(R.id.editText_newPassword);
        newPasswordRepeatText = findViewById(R.id.editText_newPassword_repeat);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordPressed();
            }
        });
    }

    /**
     * Method to handle what happens when the change password button is pressed. If the old
     * password is wrong or if the two entries of the new passwords do not match, it will display
     * the error. If the new password is less than 8 characters, it will display the error. If all
     * is fine, it changes the user's details both on firebase database and firebase auth.
     */
    private void changePasswordPressed() {
        if (!oldPasswordText.getText().toString().equals(Model.getInstance().getCurrentUser()
                .getPassword())) {
            Toast.makeText(ChangePasswordActivity.this,
                    "wrong old password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPasswordText.getText().toString().equals(newPasswordRepeatText.getText()
                .toString())) {
            Toast.makeText(ChangePasswordActivity.this,
                    "New passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPasswordText.getText().toString().length() < 8) {
            Toast.makeText(ChangePasswordActivity.this, "Password too short. " +
                    "Must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return;
        }
        final FirebaseUser user = mAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(Model.getInstance().getCurrentUser().getUserId(),
                        Model.getInstance().getCurrentUser().getPassword());
        assert user != null;
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPasswordText.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        String newPass = newPasswordText.getText().toString();
                                        if (Model.getInstance().getCurrentUser() instanceof User) {
                                            myRef.child("account_holders").child("users")
                                                    .child(user.getUid()).child("password")
                                                    .setValue(newPass);
                                        } else if (Model.getInstance().getCurrentUser()
                                                instanceof Admin) {
                                            myRef.child("account_holders").child("admins")
                                                    .child(user.getUid()).child("password")
                                                    .setValue(newPass);
                                        } else {
                                            myRef.child("account_holders")
                                                    .child("shelter_employees").child(user.getUid())
                                                    .child("password")
                                                    .setValue(newPass);
                                        }
                                        Toast.makeText(ChangePasswordActivity.this,
                                                "Password Changed!", Toast.LENGTH_SHORT).show();
                                        final String date
                                                = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                                .format(Calendar.getInstance().getTime());
                                                // Current date and time
                                        String log = date + ", " + Model.getInstance()
                                          .getCurrentUser().getUserId() + ", " + "changed password";
                                        Model.getInstance().updateLogs(log);
                                        onBackPressed();
                                    } else {
                                        Toast.makeText(ChangePasswordActivity.this,
                                                "Error changing password!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(ChangePasswordActivity.this,
                                    "Error changing password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
