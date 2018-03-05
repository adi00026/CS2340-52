package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.gatech.cs2340.vaspa.buzzshelter.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button back;
    private Button send;
    private EditText recovery_emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        back = (Button) findViewById(R.id.button_back);
        send = (Button) findViewById(R.id.button_send);
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
    private void sendClicked() {
        String recovery_email = recovery_emailText.getText().toString().trim();
        if (recovery_email.length() == 0) {
            Toast.makeText(ForgotPasswordActivity.this,
              "Please fill in recovery email", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(recovery_email).matches()) {
            Toast.makeText(ForgotPasswordActivity.this,
              "Please fill in valid email", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(ForgotPasswordActivity.this,
          "Check your email!", Toast.LENGTH_SHORT).show();
    }
}
