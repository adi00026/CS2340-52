package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Shelter;

public class ViewAvailableSheltersActivity extends AppCompatActivity {

    TextView shelterInfoTextView;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shelters);

        model = Model.getInstance();

        shelterInfoTextView = (TextView) findViewById(R.id.shelterInfoTextView);

        try {
            if (getIntent().getExtras().containsKey("shelter")) {
                Shelter sh = (Shelter) getIntent().getExtras().getParcelable("shelter");
                shelterInfoTextView.setText(sh.getName() + "\n" + sh.getAddress() + "\n" +
                        "Capacity: " + sh.getCapacity() + "\n"
                        + "Vacancies: " + sh.getVacancies());
            }
        } catch (NullPointerException e) {
            Toast.makeText(ViewAvailableSheltersActivity.this,
                    "Sorry, the shelter you requested wasn't found", Toast.LENGTH_LONG).show();
        }
    }

}
