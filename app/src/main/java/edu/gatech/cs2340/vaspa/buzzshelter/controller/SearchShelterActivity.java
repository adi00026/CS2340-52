package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Shelter;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;


public class SearchShelterActivity extends AppCompatActivity {

    Button viewShelterButton;
    Button backButton;
    Button checkoutButton;
    Spinner shelterSpinner;
    Spinner genderSpinner;
    Spinner ageSpinner;
    EditText nameEditText;
    Button goButton;
    Button mapButton;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    Collection<Shelter> filteredS = new ArrayList<>();
	Collection<Shelter> unfilteredS = new ArrayList<>();
    
    int currCheckedIn;

    private Model model;
    
    private ArrayAdapter<String> shelterAdapter;
    
	
    @Override
    public void onResume() {
        super.onResume();
        if (((User) Model.getInstance().getCurrentUser()).getShelterID() == null) {
            checkoutButton.setEnabled(false);
        } else {
            checkoutButton.setEnabled(true);
        }
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shelter);

        model = Model.getInstance();

        viewShelterButton = (Button) findViewById(R.id.view_shelter_button);
        backButton = (Button) findViewById(R.id.button_back);
        checkoutButton = (Button) findViewById(R.id.button_checkOut);
        goButton = (Button) findViewById(R.id.gobutton);
        mapButton = (Button) findViewById(R.id.button_map);
        shelterSpinner = (Spinner) findViewById(R.id.shelter_spinner);
        genderSpinner = (Spinner) findViewById(R.id.gender_spinner);
        ageSpinner = (Spinner) findViewById(R.id.age_spinner);
        nameEditText = (EditText) findViewById(R.id.namePlainText);

        initFirebaseComponents();
        
        if (((User) Model.getInstance().getCurrentUser()).getShelterID() == null) {
            checkoutButton.setEnabled(false);
        } else {
            checkoutButton.setEnabled(true);
        }

        //ShelterSpinner
	    shelterAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item);
        shelterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //GenderSpinner
        final ArrayAdapter<String> genderAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //AgeSpinner
        final ArrayAdapter<String> ageAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //ShelterSpinner set up
        shelterAdapter.setDropDownViewResource(R.layout.spinner_layout_2);
        shelterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                ((TextView) parent.getChildAt(0)).setTextSize(23);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //GenderSpinner set up
        genderAdapter.setDropDownViewResource(R.layout.spinner_layout_2);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                ((TextView) parent.getChildAt(0)).setTextSize(23);
                repopulateShelterSpinner();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //AgeSpinner set up
        ageAdapter.setDropDownViewResource(R.layout.spinner_layout_2);
        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                ((TextView) parent.getChildAt(0)).setTextSize(23);
                repopulateShelterSpinner();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //ShelterSpinner
        boolean set = false;
        int selected = 0;
        for (Shelter shelter : Model.getInstance().getShelters().values()) {
            Log.d("SEARCHDEBUG", "user shelter id: " + ((User) Model.getInstance().getCurrentUser()).getShelterID());
            Log.d("SEARCHDEBUG", "actual shelter id: " + shelter.getUniqueKey());
            Log.d("SEARCHDEBUG", selected + "\n--------------");
            if (((User) Model.getInstance().getCurrentUser()).getShelterID() != null
                    && ((User) Model.getInstance().getCurrentUser()).getShelterID()
                    .equals(shelter.getUniqueKey())) {
                set = true;
            }
            if (!set) {
                selected++;
            }
            shelterAdapter.add(filter(shelter.getName()));
        }
        if (!set) {
            selected = 0;
        }
        currCheckedIn = selected;
        //GenderSpinner
        genderAdapter.add("Any");
        genderAdapter.add("Men");
        genderAdapter.add("Women");
        genderAdapter.add("Other");

        //AgeSpinner
        ageAdapter.add("Anyone");
        ageAdapter.add("Children");
        ageAdapter.add("Young Adults");
        ageAdapter.add("Families");

        shelterSpinner.setAdapter(shelterAdapter);
        Log.d("SEARCHDEBUG", "selected: " + selected);
        genderSpinner.setAdapter(genderAdapter);
        ageSpinner.setAdapter(ageAdapter);
        shelterSpinner.setSelection(selected);

        viewShelterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewShelterPressed();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkoutPressed();
            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapPressed();
            }
        });
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillShelterSpinnerAfterNameSearch();
            }
        });
    }

    private void checkoutPressed() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child("account_holders").child("users")
                        .child(mAuth.getCurrentUser().getUid()).getValue(User.class);
                String currentID = user.getShelterID();
                user.setShelterID(null);
                user.setNumCheckedIn(0);
                myRef.child("account_holders").child("users")
                        .child(mAuth.getCurrentUser().getUid()).setValue(user);
                int size = dataSnapshot.child("shelters").child(currentID).child("vacancies")
                        .getValue(Integer.class);
                myRef.child("shelters").child(currentID).child("vacancies").setValue(size + 1);
                Model.getInstance().setCurrentUser(user);
                checkoutButton.setEnabled(false);
                myRef.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {            }
        });
    }

    private void fillShelterSpinnerAfterNameSearch() {
        final ArrayAdapter<String> shelterAdapter =
                new ArrayAdapter(this,android.R.layout.simple_spinner_item);
        shelterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Collection<Shelter> list = model.filterShelterByName(nameEditText.getText().toString().trim());
        model.setFilteredShelters(list);
        for (Shelter sh: list) {
            shelterAdapter.add(sh.getName());
        }
        shelterSpinner.setAdapter(shelterAdapter);
    }

    private void repopulateShelterSpinner() {
        final ArrayAdapter<String> shelterAdapter =
                new ArrayAdapter(this,android.R.layout.simple_spinner_item);
        shelterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (ageSpinner.getSelectedItem().equals("Anyone")
                && genderSpinner.getSelectedItem().equals("Any")) {
            model.setFilteredShelters(model.allShelters());
        } else if (ageSpinner.getSelectedItem().equals("Anyone")) {
            model.setFilteredShelters(model.filterShelterByGender((String) genderSpinner
                    .getSelectedItem()));
        } else if (genderSpinner.getSelectedItem().equals("Any")) {
            model.setFilteredShelters(model.filterShelterByAge((String) ageSpinner
                    .getSelectedItem()));
        } else {
            model.setFilteredShelters(model.filterShelterByAgeGender((String) ageSpinner
                            .getSelectedItem(), (String) genderSpinner.getSelectedItem()));
        }
        for (Shelter shelter : model.getFilteredShelters()) {
            shelterAdapter.add(filter(shelter.getName()));
        }
        //ShelterSpinner set up
        shelterAdapter.setDropDownViewResource(R.layout.spinner_layout_2);
        shelterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                ((TextView) parent.getChildAt(0)).setTextSize(23);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        shelterSpinner.setAdapter(shelterAdapter);
        if (ageSpinner.getSelectedItem().equals("Anyone")
                && genderSpinner.getSelectedItem().equals("Any")) {
            shelterSpinner.setSelection(currCheckedIn);
        }
    }

    private void viewShelterPressed() {
        try {
            Intent intent = new Intent(SearchShelterActivity.this,
                    ViewAvailableSheltersActivity.class);
            String name = unfilter(shelterSpinner.getSelectedItem().toString());
            List<Shelter> matches = Model.getInstance().searchShelterByName(name);
            if (!matches.isEmpty()) {
                intent.putExtra("shelter", matches.get(0));
                startActivity(intent);
                return;
            }
            // This shouldn't EVER happen:
            Toast.makeText(SearchShelterActivity.this, "An error occurred: " +
                            "the shelter you requested was not found in our database.",
                    Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {

        }
    }
    
    private void mapPressed() {
        Intent intent = new Intent(SearchShelterActivity.this,
                MapsActivity.class);
	
	    filteredS.addAll(model.getFilteredShelters());
	    unfilteredS.addAll(model.getShelters().values());
	    
        if (filteredS.size() < unfilteredS.size()) {
	        intent.putParcelableArrayListExtra("shelters", (ArrayList<? extends Parcelable>) filteredS);
        } else {
	        intent.putParcelableArrayListExtra("shelters", (ArrayList<? extends Parcelable>
			        ) unfilteredS);
        }
        startActivity(intent);
    }

    private void initFirebaseComponents() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    private String filter(String inStr) {
        Log.d("SEARCHSHELTERS", inStr);
        String[] arr = inStr.split(" ");
        String outStr = "";
        for (int i = 0; i < arr.length; i++) {
            outStr += arr[i];
            if ((i + 1) % 5 == 0) {
                outStr += "\n";
            } else {
                outStr += " ";
            }
        }
        return outStr;
    }

    private String unfilter(String inStr) {
        return inStr.replace("\n", " ")
                .trim();
    }
}
