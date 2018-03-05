package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;
import edu.gatech.cs2340.vaspa.buzzshelter.model.ShelterEmployee;

public class UpdateVacanciesActivity extends AppCompatActivity {
    Button backButton;
    Button updateButton;
    TextView shelterText;
    EditText vacanciesEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vacancies);

        backButton = (Button) findViewById(R.id.button_back);
        updateButton = (Button) findViewById(R.id.button_update);
        shelterText = (TextView) findViewById(R.id.textView_shelterName);
        vacanciesEditText = (EditText) findViewById(R.id.editText_vacancies);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePressed();
            }
        });
    }

    private void updatePressed() {
        ShelterEmployee currentUser = (ShelterEmployee) Model.getInstance().getCurrentUser();
        String uKey = shelterText.getText().toString().trim();
        int vacancies;
        try {
            String vacancyText = vacanciesEditText.getText().toString().trim();
            vacancies = Integer.parseInt(vacancyText);
        } catch (NumberFormatException e) {
            Toast.makeText(UpdateVacanciesActivity.this, "Vacancies must be a number",
              Toast.LENGTH_SHORT).show();
            return;
        }
        if (vacancies < 0) {
            Toast.makeText(UpdateVacanciesActivity.this, "Vacancies must be non-negative",
              Toast.LENGTH_SHORT).show();
            return;
        }

        if (uKey.length() == 0) {
            Toast.makeText(UpdateVacanciesActivity.this, "Enter a unique key",
              Toast.LENGTH_SHORT).show();
        }

        // TODO setup actual updating of logs

        // TODO remove once updating of vacancies is implemented
        if (false) {
            final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
              format(Calendar.getInstance().getTime()); // Current date and time
            String log = date + ", " + "SHELTER EMPLOYEE: " + currentUser.getUserId() + ", " +
              "updated vacancies for: " + uKey;
            Model.getInstance().updateLogs(log);
        }
    }
}
