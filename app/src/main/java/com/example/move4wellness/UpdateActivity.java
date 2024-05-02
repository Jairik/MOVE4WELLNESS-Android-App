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

public class UpdateActivity extends AppCompatActivity {

    TextView exerciseName;
    TextView timeEditText; //Will correlate to time widget
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
        //Update the firebase database(I need to get info from text-field to do this)

        //Once completed, show an animation or something then go back
        onClickGoBack(view);
    }

    //If there is somehow no user signed in, will redirect to login/register page
    private void noUserDetected() {
        Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
    }


}
