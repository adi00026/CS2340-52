package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.gatech.cs2340.vaspa.buzzshelter.R;

public class AdminAddShelterActivity extends AppCompatActivity {

    private EditText id_text;
    private EditText name_text;
    private EditText contact_text;
    private EditText address_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button back;
        Button next;

        setContentView(R.layout.activity_admin_add_shelter);
        back = findViewById(R.id.button_back);
        next = findViewById(R.id.button_next);
        id_text = findViewById(R.id.registration_shelter_id);
        name_text = findViewById(R.id.shelter_name);
        contact_text = findViewById(R.id.shelter_contact_info);
        address_text = findViewById(R.id.registration_shelter_address);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextClicked();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void nextClicked() {
        String id = id_text.getText().toString().trim();
        String name = name_text.getText().toString().trim();
        String contact_info = contact_text.getText().toString().trim();
        String address = address_text.getText().toString().trim();
        if (name.length() == 0 || id.length() == 0 || contact_info.length() == 0
          || address.length() == 0) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent nextIntent = new Intent(AdminAddShelterActivity.this,
          AdditionalShelterInfoActivity.class);
        nextIntent.putExtra("name", name);
        nextIntent.putExtra("id", id);
        nextIntent.putExtra("contact_info", contact_info);
        nextIntent.putExtra("address", address);
        startActivity(nextIntent);
    }
}
