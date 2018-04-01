package edu.gatech.cs2340.vaspa.buzzshelter.controller;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.gatech.cs2340.vaspa.buzzshelter.R;

/**
 * Class to handle the forgot password activity. Everyone can access this page
 * from the welcome page.
 *
 * @author Aniruddha Das
 * @version 6.9
 */

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText recovery_emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Button back = (Button) findViewById(R.id.button_back);
        Button send = (Button) findViewById(R.id.button_send);
        recovery_emailText = (EditText) findViewById(R.id.editText_recover_email);

        recovery_emailText.setText(getIntent().getStringExtra("username"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendClicked();
            }
        });
    }

    /**
     * Helper method called when send is clicked
     *
     * Sends email from firebase for password recovery
     */
    private void sendClicked() {
        final String recovery_email = recovery_emailText.getText().toString().trim();
        if (recovery_email.length() == 0) {
            Toast.makeText(ForgotPasswordActivity.this,
              "Please fill in recovery email", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(recovery_email).matches()) {
            Toast.makeText(ForgotPasswordActivity.this,
              "Please fill in valid email", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(recovery_email)
          .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String prevLog = dataSnapshot.child("logging").child(recovery_email
                              .replace('.', ',')).getValue(String.class);
                            final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                              format(Calendar.getInstance().getTime()); // Current date and time
                            String log = date + ", " + recovery_email + ", sent password recovery email\n";
                            // appends new log to original log and places into database
                            myRef.child("logging").child(recovery_email
                              .replace('.', ',')).setValue(prevLog + log);
                            myRef.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Toast.makeText(ForgotPasswordActivity.this,
                      "Check your email!", Toast.LENGTH_SHORT).show();

                    onBackPressed(); // takes them back once recovery email is set
                } else {
                    Toast.makeText(ForgotPasswordActivity.this,
                      "An error has occured", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
