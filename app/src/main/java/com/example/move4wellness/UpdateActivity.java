package com.example.move4wellness;

/* Update Activity page class
 * Description: Page allows users to manually enter exercise data and update it
 * Redirects to: MainHomepage*/

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.regex.*;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class UpdateActivity extends AppCompatActivity {

    TextView exerciseName;
    TextView dEditText;
    //DATE WIDGET DECLARATION HERE
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
    DocumentReference userRef; //Reference to current user
    String uniqueUserID;
    String ENAME; //Exercise name, received from previous activity

    String exerciseDate;
    TextView date;
    // Regular expression pattern for MM/DD/YYYY format
    String regex = "^\\d{2}/\\d{2}/\\d{4}$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity_screen);
        //Getting the exercise name
        Intent intent = getIntent();
        ENAME = intent.getStringExtra("STRING_KEY");
        //Setting the top text to the current exercise
        exerciseName = findViewById(R.id.textView9);
        exerciseName.setText(ENAME);
        //Getting the edit text-field
        dEditText = findViewById(R.id.editTextTime);
        date = findViewById(R.id.editTextDate);
        //Getting the current user
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        /*Getting the current userUID, getting the database, and establishing a reference to the
        * current user in the 'user' collection */
        if(user != null) {
            uniqueUserID = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            userRef = db.collection("users").document(uniqueUserID);
        }
        else { //No user is signed in, redirect to main activity
            noUserDetected(); //Go back to first welcome screen
        }
    }

    //Back button redirect
    public void onClickGoBack(View view) {
        Intent intent = new Intent(UpdateActivity.this, MainHomepage.class);
        startActivity(intent);
    }

    //method skeleton for updating activity button
    public void onClickUpdateData(View view) {
        //Get the time from the editfield
        String durationString = dEditText.getText().toString().trim();
        int duration = timeStringConversion(durationString);

        String dateString = date.getText().toString().trim();
        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regex);
        // Match the date string against the pattern
        Matcher matcher = pattern.matcher(dateString);

        //Validating time & date from user
        if(duration == -999) { //signal value generated by timeStringConversion function
            dEditText.setError("Invalid Time");
            return;
        }
        else if(!matcher.matches()){
            date.setError("Invalid must be MM/DD/YYYY");
            return;
        }
        //Add it to the Firestore database activities collection
        addActivityToDatabase(duration, dateString);

        //Once completed, show an animation or something then go back
        onClickGoBack(view);
    }

    //Converts and validates a string time to an int, returning a signal value if invalid
    private int timeStringConversion(String timeString) {
        int timeAsInt;
        try {
            timeAsInt = Integer.parseInt(timeString);
        }
        catch (NumberFormatException e) {
            return -999; //Setting to signal value
        }
        if(timeAsInt < 1 || timeAsInt > 240) {
            return -999;
        }
        return timeAsInt; //At this point, it should be certified that this is valid
    }

    //If there is somehow no user signed in, will redirect to login/register page
    private void noUserDetected() {
        Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //Adds a given activity to the database, also updating the user's totals
    private void addActivityToDatabase(int duration, String date) {
        //Storing data for the activity in a hashmap
        Map<String, Object> activityData = new HashMap<>();
        activityData.put("activity_name", ENAME);
        activityData.put("duration", duration); //Minutes on the activity
        activityData.put("date", date); // exercise date format MM/DD/YYYY

        //Adding activityData to the database
        userRef.collection("activities").add(activityData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //If successfully added, Update the totals
                        updateTotals(duration);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dEditText.setError("Unexpected Error - Please Try Again");
                    }
                });
    }

    //Increments the user's num_exercises and total_minutes in the fireside database
    private void updateTotals(int currentDuration) {
        //Hold the changes in a hashmap
        Map<String, Object> totalUpdates = new HashMap<>();
        totalUpdates.put("num_exercises", FieldValue.increment(1));
        totalUpdates.put("total_minutes", FieldValue.increment(currentDuration));

        //Make these changes in the database
        userRef.update(totalUpdates);
    }



}
