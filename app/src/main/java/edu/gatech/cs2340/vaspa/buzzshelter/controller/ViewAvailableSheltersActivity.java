package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.gatech.cs2340.vaspa.buzzshelter.R;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Model;
import edu.gatech.cs2340.vaspa.buzzshelter.model.Shelter;
import edu.gatech.cs2340.vaspa.buzzshelter.model.User;
import edu.gatech.cs2340.vaspa.buzzshelter.util.StringOps;

public class ViewAvailableSheltersActivity extends AppCompatActivity {

    private TextView shelterInfoTextView;
    private TextView currentShelterTextView;
    private Button backButton;
    private Button checkInButton;
    private Button checkOutButton;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Shelter selectedShelter;

    private String infoString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shelters);

        Model model = Model.getInstance();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        shelterInfoTextView = (TextView) findViewById(R.id.shelterInfoTextView);
        currentShelterTextView = (TextView) findViewById(R.id.textView_current);
        backButton = (Button) findViewById(R.id.button_back);
        checkOutButton = (Button) findViewById(R.id.button_checkOut);
        checkInButton = (Button) findViewById(R.id.button_checkIn);

        if (((User) Model.getInstance().getCurrentUser()).getShelterID() == null) {
            checkOutButton.setEnabled(false);
            checkInButton.setEnabled(true);
            currentShelterTextView.setText("Current Shelter:\nNONE");
        } else {
            checkOutButton.setEnabled(true);
            checkInButton.setEnabled(false);
            currentShelterTextView.setText("Current Shelter:\n"
                    + Model.getInstance().getShelters().get(((User) Model
                    .getInstance().getCurrentUser()).getShelterID()));
        }

        selectedShelter = getIntent().getParcelableExtra("shelter");

        try {
            if (getIntent().getExtras().containsKey("shelter")) {
                Shelter sh = (Shelter) getIntent().getExtras().getParcelable("shelter");
                infoString = sh.getName() + "\n" + sh.getAddress() + "\n" +
                        "Capacity: " + sh.getCapacity() + "\n"
                        + "Vacancies: ";
                shelterInfoTextView.setText(sh.getName() + "\n" + sh.getAddress() + "\n" +
                        "Capacity: " + sh.getCapacity() + "\n"
                        + "Vacancies: " + sh.getVacancies() + "\n"
                        + "Restrictions: " + sh.getRestrictions());
                if (sh.getVacancies() == 0) {
                    checkInButton.setEnabled(false);
                } else {
                    //checkInButton.setEnabled(true);
                }
            }
        } catch (NullPointerException e) {
            Toast.makeText(ViewAvailableSheltersActivity.this,
                    "Sorry, the shelter you requested wasn't found", Toast.LENGTH_LONG).show();
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInPressed();
            }
        });
        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOutPressed();
            }
        });
    }

    private void checkOutPressed() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child("account_holders").child("users")
                        .child(mAuth.getCurrentUser().getUid()).getValue(User.class);
                String currentID = user.getShelterID();
                int numCheckedIn = user.getNumCheckedIn();
                String prevShelterID = user.getShelterID();
                user.setShelterID(null);
                user.setNumCheckedIn(0);
                myRef.child("account_holders").child("users")
                        .child(mAuth.getCurrentUser().getUid()).setValue(user);
                int size = dataSnapshot.child("shelters").child(currentID).child("vacancies")
                        .getValue(Integer.class);
                String BSCap = dataSnapshot.child("shelters").child(currentID).child("capacity")
                        .getValue(String.class);
                int capacity = StringOps.parseCapacity(BSCap);
                if (size + numCheckedIn <= capacity
                        && selectedShelter.getUniqueKey().equals(prevShelterID)) {
                    myRef.child("shelters").child(currentID).child("vacancies")
                            .setValue(size + numCheckedIn);
                    shelterInfoTextView.setText(infoString + (size + numCheckedIn));
                }
                Model.getInstance().setCurrentUser(user);
                checkOutButton.setEnabled(false);
                checkInButton.setEnabled(true);
                currentShelterTextView.setText("Current Shelter:\nNONE");
                Toast.makeText(ViewAvailableSheltersActivity.this,
                        "Check-out successful!", Toast.LENGTH_SHORT).show();
                myRef.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {            }
        });
    }

    private void checkInFirebaseUpdate(final int numCheckIn) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child("account_holders").child("users")
                        .child(mAuth.getCurrentUser().getUid()).getValue(User.class);
                String currentID = user.getShelterID();
                int size = dataSnapshot.child("shelters").child(selectedShelter.getUniqueKey())
                        .child("vacancies").getValue(Integer.class);
                if ((size - numCheckIn) >= 0) {
                    myRef.child("shelters").child(selectedShelter.getUniqueKey()).child("vacancies")
                            .setValue(size - numCheckIn);
                    user.setShelterID(selectedShelter.getUniqueKey());
                    user.setNumCheckedIn(numCheckIn);
                    myRef.child("account_holders").child("users")
                            .child(mAuth.getCurrentUser().getUid()).setValue(user);
                    Model.getInstance().setCurrentUser(user);
                    checkOutButton.setEnabled(true);
                    checkInButton.setEnabled(false);
                    shelterInfoTextView.setText(infoString + (size - numCheckIn));
                    currentShelterTextView.setText("Current Shelter:\n"
                            + Model.getInstance().getShelters().get(((User) Model
                            .getInstance().getCurrentUser()).getShelterID()));
                    Toast.makeText(ViewAvailableSheltersActivity.this,
                            "Check-in successful!", Toast.LENGTH_SHORT).show();
                } else {
                    shelterInfoTextView.setText(infoString + (size));
                    Toast.makeText(ViewAvailableSheltersActivity.this,
                            "Sorry, this shelter is full!", Toast.LENGTH_SHORT).show();
                }
                myRef.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {            }
        });
    }

    private void checkInPressed() {
        // get check_in_prompt.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.check_in_prompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Check In",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                int numCheckIn = Integer.parseInt(userInput.getText().toString());
                                if (numCheckIn < 1 || numCheckIn > 5) {
                                    Toast.makeText(ViewAvailableSheltersActivity.this,
                                            "Number of bed claims cannot exceed 5!",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    checkInFirebaseUpdate(numCheckIn);
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

}
