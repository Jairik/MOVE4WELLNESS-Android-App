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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainHomepage extends AppCompatActivity {
    TextView usernameText;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_homepage);
        usernameText = findViewById(R.id.textView3);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        setUsernameText();
    }

    //Sets the username at the top of the screen
    private void setUsernameText() {
        user = auth.getCurrentUser();
        //If the user is null (it shouldn't be), return false
        if(user == null) {
            return;
        }
        //Getting details of the current user & the database
        String UID = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(UID);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        String userName = doc.getString("username");
                        usernameText.setText(userName);
                    }
                }
            }
        });
    }

    //redirects to notification screen
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
