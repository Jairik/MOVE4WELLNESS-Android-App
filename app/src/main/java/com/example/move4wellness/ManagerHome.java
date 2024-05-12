package com.example.move4wellness;

/* Manager home page class
 * Description: Displays stats and redirects to different manager pages
 * Redirects to: Global Message or See All Users */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ManagerHome extends AppCompatActivity {

    TextView totalUsersText;
    TextView allTimeTotalText;
    TextView allTimeAverageText;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_home);
        //Getting relevant text fields that need updated
        totalUsersText = findViewById(R.id.week_Total);
        allTimeTotalText = findViewById(R.id.all_Time_Total);
        allTimeAverageText = findViewById(R.id.ALL_TIME_AVERAGE);
        //Setting up database variable
        db = FirebaseFirestore.getInstance();
        //Setting all the textfields
        setTextFields();
    }

    //On-click method to seeAllUsers button, redirects to Manager See Users page
    public void onClickSeeAllUsers(View view) {
        Intent intent = new Intent(ManagerHome.this, ManagerSeeUsers.class);
        startActivity(intent);
    }

    //Redirects to ManagerGlobalMessage page
    public void onClickSendGlobalMessage(View view) {
        Intent intent = new Intent(ManagerHome.this, ManagerGlobalMessage.class);
        startActivity(intent);
    }

    //Query through all users to get their totals and number of users
    private void setTextFields() {
        int numUsers = 0, allTimeTotal = 0;
        String numUsersString, allTimeTotalString;
    }


}
