package com.example.move4wellness;

/* Update Activity page class
 * Description: Page allows users to manually enter exercise data and update it
 * Redirects to: MainHomepage*/

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateActivity extends AppCompatActivity {

    TextView exerciseName;
    TextView timeEditText;
    FirebaseAuth auth;
    FirebaseUser user;
    String uniqueUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity_screen);
        //Getting the exercise name
        Intent intent = getIntent();
        String ENAME = intent.getStringExtra("STRING_KEY");
        //Setting the top text to the current exercise
        exerciseName = findViewById(R.id.textView9);
        exerciseName.setText(ENAME);
        //Getting the edit text-field
        timeEditText = findViewById(R.id.editTextTime);
        //Getting the current user
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        //Getting the current user's unique ID
        if(user != null) {
            uniqueUserID = user.getUid();
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
        String timeString = timeEditText.getText().toString().trim();
        int time = timeStringConversion(timeString);
        //Validating time from user
        if(time == -999) { //signal value generated by timeStringConversion function
            timeEditText.setError("Invalid Time");
            return;
        }
        //Add it to the Firebase activities collection
        FirebaseDatabase database =  FirebaseDatabase.getInstance();
        //DatabaseReference activitiesRef = database.child("users").child(uniqueUserID).child("activities");
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
    }

}
