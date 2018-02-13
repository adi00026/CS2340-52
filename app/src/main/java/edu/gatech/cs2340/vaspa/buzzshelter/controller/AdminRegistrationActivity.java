package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.LinkedList;
import java.util.List;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Admin;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;

public class AdminRegistrationActivity extends AppCompatActivity {

    private EditText keyText;
    private Button backButton;
    private Button finish;

    private String adminKey = "temp_admin_key";

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration);

        keyText = (EditText) findViewById(R.id.admin_Key);
        backButton = (Button) findViewById(R.id.admin_back);
        finish = (Button) findViewById(R.id.Finish_Button);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminRegistrationActivity.this,
                  RegistrationActivity.class);
                startActivity(intent);
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
        String key = keyText.getText().toString().trim();
        if (!adminKey.equals(key)) {
            Toast.makeText(this, "The inputted key is incorrect",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String username = getIntent().getExtras().getString("username");
        String password = getIntent().getExtras().getString("password");
        String contactInfo = getIntent().getExtras().getString("contactInfo");
        String name = getIntent().getExtras().getString("name");
        final Admin admin = new Admin(name, username, password, contactInfo);
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
                            Toast.makeText(AdminRegistrationActivity.this,
                                    admin.getUserId() + " added!", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String UID = firebaseUser.getUid();
                            myRef.child("account_holders").child("admins").child(UID)
                                    .setValue(admin);
                            Intent intent = new Intent(AdminRegistrationActivity.this,
                                    WelcomePageActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(AdminRegistrationActivity.this,
                                        admin.getUserId() + " already exists",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AdminRegistrationActivity.this,
                                        task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
