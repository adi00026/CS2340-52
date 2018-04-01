package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;
import edu.gatech.cs2340.vaspa.buzzshelter.model.ShelterEmployee;

/**
 * Class to handle the shelter employees registration activity. Non-AccountHolders will access this
 * page when they want to add shelter employees. This page is shared by all users.
 *
 * @author Aniruddha Das
 * @version 6.9
 */
@SuppressWarnings("ConstantConditions")
public class ShelterEmployeeRegistrationActivity extends AppCompatActivity {

    private EditText keyText;
    private EditText shelterId;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_employee_registration);

        keyText = findViewById(R.id.Shelter_Employee_Key);
        shelterId = findViewById(R.id.shelter_ID);
        Button finish = findViewById(R.id.Finish_Button_Shelter);
        Button backButton = findViewById(R.id.shelter_registration_back);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    /**
     *  Helper method for registering the user
     */
    private void register() {
        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        String key = keyText.getText().toString().trim();
        String shelterID = shelterId.getText().toString().trim();
        String shelterKey = "temp_shelter_key";
        if (shelterKey.equals(key)) {
            if (!Model.getInstance().getShelters().containsKey(shelterID)) {
                progressDialog.dismiss();
                Toast.makeText(this, "The inputted shelter ID does not exist",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(this, "The inputted key is incorrect",
                    Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        String username = getIntent().getExtras().getString("username");
        String password = getIntent().getExtras().getString("password");
        String contactInfo = getIntent().getExtras().getString("contactInfo");
        String name = getIntent().getExtras().getString("name");
        final ShelterEmployee shemp = new ShelterEmployee(name, username, password, contactInfo,
                shelterID);
        // Auth Create this guy
        String email = username;
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email += "@temp.com";
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ShelterEmployeeRegistrationActivity.this,
                                    shemp.getUserId() + " added!", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String UID = firebaseUser.getUid();
                            myRef.child("account_holders").child("shelter_employees").child(UID)
                                    .setValue(shemp);

                            // TODO move to seperate logging class
                            // Logging creation of new account holder
                            String uid = shemp.getUserId();
                            uid = uid.replace('.', ',');
                            final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                              format(Calendar.getInstance().getTime()); // Current date and time
                            String log = date + ", " + shemp.getUserId() + ", " + "created account";
                            myRef.child("logging").child(uid).setValue(log);

                            progressDialog.dismiss();
                            Intent intent = new Intent(
                                    ShelterEmployeeRegistrationActivity.this,
                                    WelcomePageActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("username", shemp.getUserId());
                            intent.putExtra("password", shemp.getPassword());
                            startActivity(intent);
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(ShelterEmployeeRegistrationActivity.this,
                                        shemp.getUserId() + " already exists",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ShelterEmployeeRegistrationActivity.this,
                                        task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });
    }

}
