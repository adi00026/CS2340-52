package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;

public class LogActivity extends AppCompatActivity {

    EditText user_id;
    EditText dest_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        user_id = findViewById(R.id.editText_userID);
        dest_email = findViewById(R.id.editText_email);
        Button send_log = findViewById(R.id.button_sendLogs);
        Button back_button = findViewById(R.id.button_back);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        send_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendPressed();
            }
        });
    }

    /**
     * Listener method to work send logs of user_id to dest_email
     * Has validation checking to.
     */
    private void onSendPressed() {
        final String userIdText = user_id.getText().toString().trim();
        final String emailText = dest_email.getText().toString().trim();

        if (userIdText.length() == 0 || emailText.length() == 0) {
            Toast.makeText(LogActivity.this, "Please fill in all fields",
              Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            Toast.makeText(LogActivity.this, "Please enter a valid email id",
              Toast.LENGTH_SHORT).show();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userIdText).matches()) {
            Toast.makeText(LogActivity.this, "Please enter a valid user id",
              Toast.LENGTH_SHORT).show();
            return;
        }

        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String path = userIdText.replace(".", ",");
                if (dataSnapshot.child("logging").child(path).exists()) {

                    String retrieved_log = dataSnapshot.child("logging").child(path).getValue()
                      .toString();
                    Log.d("LOG_ACTIVITY", retrieved_log);

                    final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                      format(Calendar.getInstance().getTime()); // Current date and time
                    String log = date + ", " + "ADMIN: " + Model.getInstance().getCurrentUser()
                      .getUserId() + ", sent logs of " + userIdText + " to " + emailText;
                    Model.getInstance().updateLogs(log);

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{emailText});
                    i.putExtra(Intent.EXTRA_SUBJECT, "Logs for user: " + userIdText);
                    i.putExtra(Intent.EXTRA_TEXT   , retrieved_log);

                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(LogActivity.this, "There are no email " +
                          "clients installed.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LogActivity.this, "Logs for this user do not exist",
                      Toast.LENGTH_SHORT).show();
                }
                myRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
