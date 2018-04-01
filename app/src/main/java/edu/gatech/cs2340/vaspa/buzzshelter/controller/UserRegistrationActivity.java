package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;
import edu.gatech.cs2340.vaspa.buzzshelter.util.Validation;

/**
 * Class to handle the User Registration page activity. This page is only accessible to Registering
 * Homeless users.
 *
 * @author Sanath Nagaraj
 * @version 6.9
 */
@SuppressWarnings({"ConstantConditions", "CyclicClassDependency"})
public class UserRegistrationActivity extends AppCompatActivity {

    private Spinner genderSpinner;
    private CheckBox vetCheckbox;
    private Button backButton;
    private Button registerButton;
    private EditText dayText;
    private EditText monthText;
    private EditText yearText;
    private ProgressDialog progressDialog;


    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private final String TAG = "UserRegistrationAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        Log.d(TAG, "onCreate");

        genderSpinner = findViewById(R.id.spinner_gender);
        vetCheckbox = findViewById(R.id.checkBox_vets);
        backButton = findViewById(R.id.button_back);
        registerButton = findViewById(R.id.button_register);
        dayText = findViewById(R.id.editText_day);
        monthText = findViewById(R.id.editText_month);
        yearText = findViewById(R.id.editText_year);
        progressDialog = new ProgressDialog(this);


        // Firebase stuff initializers
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        // Set listeners and adapters:
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        String[] genderArray = {"Male", "Female", "Other"};
        ArrayAdapter<String> adapterGender = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Arrays.asList(genderArray));
        adapterGender.setDropDownViewResource(R.layout.spinner_layout_1);
        genderSpinner.setAdapter(adapterGender);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                ((TextView) parent.getChildAt(0)).setTextSize(23);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Method to handle what happens when the register button is pressed. If the entered date of
     * birth is an invalid date, an error is shown. If no error, a new user is created on Firebase
     * Auth and added to Firebase Database in the . --> AccountHolders --> Users branch.
     */
    private void register() {
        Log.d(TAG, "register");
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        int month = Integer.parseInt(monthText.getText().toString());
        int day = Integer.parseInt(dayText.getText().toString());
        int year = Integer.parseInt(yearText.getText().toString());
        final String username = getIntent().getExtras().getString("username");

        List<Integer> dob = new LinkedList<>();
        dob.add(month);
        dob.add(day);
        dob.add(year);

        boolean isValid = Validation.isValidDate(dob);

        if (!isValid) {
            progressDialog.dismiss();
            Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
            return;
        }

        String gender = genderSpinner.getSelectedItem().toString();
        boolean isVeteran = vetCheckbox.isChecked();
        String password = getIntent().getExtras().getString("password");
        String contactInfo = getIntent().getExtras().getString("contactInfo");
        String name = getIntent().getExtras().getString("name");

        final User user = new User(name, username, password, false, contactInfo, gender, dob,
                isVeteran);
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UserRegistrationActivity.this,
                            user.getUserId() + " added!", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String UID = firebaseUser.getUid();

                    // Add to DB
                    myRef.child("account_holders").child("users").child(UID).setValue(user);

                    // TODO move to seperate logging class
                    // Logging creation of new account holder
                    String uid = user.getUserId();
                    uid = uid.replace('.', ',');
                    final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                      format(Calendar.getInstance().getTime()); // Current date and time
                    String log = date + ", " + user.getUserId() + ", " + "created account";
                    myRef.child("logging").child(uid).setValue(log);

                    Intent intent = new Intent(UserRegistrationActivity.this,
                            WelcomePageActivity.class);
                    progressDialog.dismiss();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("username", user.getUserId());
                    intent.putExtra("password", user.getPassword());
                    startActivity(intent);
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(UserRegistrationActivity.this,
                                user.getUserId() + " already exists",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UserRegistrationActivity.this,
                                task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                progressDialog.dismiss();
            }
        });
    }
}
