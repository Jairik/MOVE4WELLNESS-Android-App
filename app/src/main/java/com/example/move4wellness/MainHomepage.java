package com.example.move4wellness;

/* Main Homepage Class
 * Description:
 * Redirects to: notification, activity tracker, activity updater, user profile*/

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainHomepage extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
    DocumentReference userRef;
    String uniqueUserID;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_homepage);
        //Getting the current user
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        /*Getting the current userUID, getting the database, and establishing a reference to the
         * current user in the 'user' collection */
        if(user != null) {
            uniqueUserID = user.getUid();
            userRef = db.collection("users").document(uniqueUserID);
        }
        else { //No user is signed in, redirect to main activity
            noUserDetected(); //Go back to first welcome screen
        }

        //grab the username from the user document
        if (userRef != null) {
            userRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                // Document exists, fetch username
                                username = documentSnapshot.getString("username");
                            }
                            //set text to user's username
                            TextView name = findViewById(R.id.username);
                            name.setText(username);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Error getting document");
                        }
                    });
        } else {
            System.out.println("userRef is null");
        }

    }

    //If there is somehow no user signed in, will redirect to login/register page
    private void noUserDetected() {
        Intent intent = new Intent(MainHomepage.this, MainActivity.class);
    }

    //redirects to noti screen
    public void onClickNotification(View view) {
        Intent intent = new Intent(MainHomepage.this, NotificationScreen.class);
        startActivity(intent);
    }

    //redirects to profile
    public void onClickProfile(View view) {
        Intent intent = new Intent(MainHomepage.this, UserProfile.class);
        startActivity(intent);
    }


    //redirects to activity tracker screen
    public void onClickActivityTracker(View view) {
        Intent intent = new Intent(MainHomepage.this, ActivityTracker.class);
        startActivity(intent);
    }


    /*------------------------------------------------------------------------
      Redirects to update activity screen, passing in a string exercise name
    -------------------------------------------------------------------------- */

    public void onClickActivityUpdateSitUps(View view) {
        Intent intent = new Intent(MainHomepage.this, UpdateActivity.class);
        intent.putExtra("STRING_KEY", "Sit-ups"); //Pass in exercise name
        startActivity(intent);
    }

    public void onClickActivityUpdateWeightlifting(View view) {
        Intent intent = new Intent(MainHomepage.this, UpdateActivity.class);
        intent.putExtra("STRING_KEY", "Weightlifting"); //Pass in exercise name
        startActivity(intent);
    }

    public void onClickActivityUpdateJumpRope(View view) {
        Intent intent = new Intent(MainHomepage.this, UpdateActivity.class);
        intent.putExtra("STRING_KEY", "Jump Rope"); //Pass in exercise name
        startActivity(intent);
    }
}
