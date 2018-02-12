package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.gatech.cs2340.vaspa.buzzshelter.R;

public class MainPageActivity extends AppCompatActivity {
    TextView welcomeTextview;
    Button logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        welcomeTextview = (TextView) findViewById(R.id.textview_welcome);
        logoutButton = (Button) findViewById(R.id.button_logout);

        String username = getIntent().getStringExtra("USERNAME");
        String display = "Welcome, " + username + "!";
        welcomeTextview.setText(display);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this,
                        WelcomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
