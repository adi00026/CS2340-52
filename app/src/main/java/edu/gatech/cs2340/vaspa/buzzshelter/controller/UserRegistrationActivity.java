package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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

import java.time.Year;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;

public class UserRegistrationActivity extends AppCompatActivity {
    Spinner genderSpinner;
    CheckBox vetCheckbox;
    Button backButton;
    Button registerButton;
    EditText dayText;
    EditText monthText;
    EditText yearText;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    private final String TAG = "UserRegistrationAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        genderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        vetCheckbox = (CheckBox) findViewById(R.id.checkBox_vets);
        backButton = (Button) findViewById(R.id.button_back);
        registerButton = (Button) findViewById(R.id.button_register);
        dayText = (EditText) findViewById(R.id.editText_day);
        monthText = (EditText) findViewById(R.id.editText_month);
        yearText = (EditText) findViewById(R.id.editText_year);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

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
    private void register() {
        int month = Integer.parseInt(monthText.getText().toString());
        int day = Integer.parseInt(dayText.getText().toString());
        int year = Integer.parseInt(yearText.getText().toString());
        if (!isValidDate(month, day, year)) {
            Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Integer> dob = new LinkedList<>();
        dob.add(month);
        dob.add(day);
        dob.add(year);
        String gender = genderSpinner.getSelectedItem().toString();
        boolean isVeteran = vetCheckbox.isChecked();
        String username = getIntent().getExtras().getString("username");
        String password = getIntent().getExtras().getString("password");
        String contactInfo = getIntent().getExtras().getString("contactInfo");
        String name = getIntent().getExtras().getString("name");
        final User user = new User(name, username, password, false, contactInfo, gender, dob,
                isVeteran);
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
                    Toast.makeText(UserRegistrationActivity.this,
                            user.getUserId() + " added!", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String UID = firebaseUser.getUid();
                    /**
                     * CODE TO ADD STUDD TO DB
                     */
                    myRef.child("account_holders").child("users").child(UID).setValue(user);
                    /**
                     *
                     */
                    Intent intent = new Intent(UserRegistrationActivity.this,
                            WelcomePageActivity.class);
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
            }
        });
    }

    private boolean isValidDate(int month, int day, int year) {
        int[] dateArr = {-1, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (year % 4 == 0) {
            dateArr[2] = 29;
        }
        if (month < 1 || month > 12) {
            return false;
        }
        if (day < 1 || day > dateArr[month]) {
            return false;
        }
        return true;
    }
}
