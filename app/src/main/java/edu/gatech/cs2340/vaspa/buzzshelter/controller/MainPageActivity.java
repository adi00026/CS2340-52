package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import edu.gatech.cs2340.vaspa.buzzshelter.R;

public class MainPageActivity extends AppCompatActivity {
    TextView welcomeTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        welcomeTextview = (TextView) findViewById(R.id.textview_welcome);
        String username = getIntent().getStringExtra("USERNAME");
        String display = "Welcome, " + username + "!";
        welcomeTextview.setText(display);
    }
}
