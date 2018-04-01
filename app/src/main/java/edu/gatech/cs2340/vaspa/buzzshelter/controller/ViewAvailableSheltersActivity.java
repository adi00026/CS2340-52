package edu.gatech.cs2340.vaspa.buzzshelter.controller;

import android.annotation.SuppressLint;
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

/**
 * Class to handle the viewing available shelters activity. Users will access this page
 * when they want to view shelter according to criteria. This page is shared by all users.
 *
 * @author Aniruddha Das
 * @version 6.9
 */
@SuppressWarnings("ConstantConditions")
public class ViewAvailableSheltersActivity extends AppCompatActivity {

    private TextView shelterInfoTextView;
    private TextView currentShelterTextView;
    private Button checkInButton;
    private Button checkOutButton;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    private Shelter selectedShelter;

    private String infoString;

    @SuppressWarnings({"ProhibitedExceptionCaught", "LawOfDemeter"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shelters);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        shelterInfoTextView = findViewById(R.id.shelterInfoTextView);
        currentShelterTextView = findViewById(R.id.textView_current);
        Button backButton = findViewById(R.id.button_back);
        checkOutButton = findViewById(R.id.button_checkOut);
        checkInButton = findViewById(R.id.button_checkIn);

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
                Shelter sh = getIntent().getExtras().getParcelable("shelter");
                infoString = sh.getName() + "\n" + sh.getAddress() + "\n" +
                        "Capacity: " + sh.getCapacity() + "\n"
                        + "Vacancies: ";
                shelterInfoTextView.setText(sh.getName() + "\n" + sh.getAddress() + "\n" +
                        "Capacity: " + sh.getCapacity() + "\n"
                        + "Vacancies: " + sh.getVacancies() + "\n"
                        + "Restrictions: " + sh.getRestrictions());
                if (sh.getVacancies() == 0) {
                    checkInButton.setEnabled(false);
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

    /**
     *  Helper method that handles checking out of users
     */
    private void checkOutPressed() {
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressWarnings("LawOfDemeter")
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
                if (((size + numCheckedIn) <= capacity)
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

    /**
     * Updates firebase database vacancies for shelter
     *
     * @param numCheckIn number checked in
     */
    private void checkInFirebaseUpdate(final int numCheckIn) {
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressWarnings("LawOfDemeter")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child("account_holders").child("users")
                        .child(mAuth.getCurrentUser().getUid()).getValue(User.class);
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

    /**
     *  Helper method for when the check in button is clicked
     */
    private void checkInPressed() {
        // get check_in_prompt.xml view
        LayoutInflater li = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View promptsView = li.inflate(R.layout.check_in_prompt,
                null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = promptsView
                .findViewById(R.id.editTextDialogUserInput);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Check In",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                int numCheckIn = Integer.parseInt(userInput.getText().toString());
                                if ((numCheckIn < 1) || (numCheckIn > 5)) {
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
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

}
