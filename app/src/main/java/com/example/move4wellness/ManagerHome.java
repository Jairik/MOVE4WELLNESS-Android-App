package com.example.move4wellness;

/* Manager home page class
 * Description: Displays stats and redirects to different manager pages
 * Redirects to: Global Message or See All Users */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class ManagerHome extends AppCompatActivity {

    TextView totalUsersText;
    TextView allTimeTotalText;
    TextView allTimeAverageText;
    TextView allTimeActivitiesText;
    TextView allTimeAverageActivitiesText;
    ProgressBar progressBar;
    FirebaseFirestore db;
    CollectionReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_home);
        //Getting relevant text fields that need updated
        totalUsersText = findViewById(R.id.total_Users);
        allTimeTotalText = findViewById(R.id.all_Time_Total);
        allTimeAverageText = findViewById(R.id.all_Time_Average);
        allTimeActivitiesText = findViewById(R.id.total_activities);
        allTimeAverageActivitiesText = findViewById(R.id.average_activities);
        //Getting the loading bar
        progressBar = findViewById(R.id.progress_bar);
        //Setting up database variable
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");
        //Pulling data from the database to edit the statistical text fields
        //setTextFields();
        String debug = "-99999";
        totalUsersText.setText(debug); //Even this isn't showing up
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
        String totalDurationString, numUsersString, averageDurationString,
                totalExercisesString, averageTotalExercisesString;

        progressBar.setVisibility(View.VISIBLE); //Starting the loading bar

        //Query all documents in the database, adding to the total
        usersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    long[] currentExercisesL = {0}, currentTotalDurationL = {0};
                    int[] numUsers = {0}, numExercises = {0}, totalDuration = {0};
                    //For all documents in the users collection
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        //Get the statistics, and cast them to ints
                        currentExercisesL[0] = doc.getLong("num_exercises");
                        currentTotalDurationL[0] = doc.getLong("total_minutes");

                        numUsers[0] += 1; //Incrementing by one
                        numExercises[0] += (int) currentExercisesL[0]; //Incrementing total exercises
                        totalDuration[0] += (int) currentTotalDurationL[0]; //Incrementing total Duration of exercises
                    }
                    //Setting the strings to replace in the text-fields
                    String numUsersString = String.valueOf(numUsers[0]);
                    String totalDurationString = String.valueOf(totalDuration[0]) + " minutes";
                    String averageDurationString = String.valueOf((totalDuration[0]) / numUsers[0]) + " minutes per user";
                    String totalExercisesString = String.valueOf(numExercises[0]);
                    String averageTotalExercisesString = String.valueOf((numExercises[0]) / numUsers[0]) + " per user";

                    //Replacing edit-text fields
                    //PROBLEM: THIS ISNT SHOWING UP AT ALL
                    totalUsersText.setText("Testing");
                    allTimeTotalText.setText(totalDurationString);
                    allTimeAverageText.setText(averageDurationString);
                    allTimeActivitiesText.setText(totalExercisesString);
                    allTimeAverageActivitiesText.setText(averageTotalExercisesString);
                }
                progressBar.setVisibility(View.INVISIBLE); //Hiding the progressbar when operation is complete
            }
        });
    }

}
