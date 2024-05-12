package com.example.move4wellness;

/* Manager home page class
 * Description: Displays stats and redirects to different manager pages
 * Redirects to: Global Message or See All Users */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class ManagerHome extends AppCompatActivity {

    TextView totalUsersText;
    TextView allTimeTotalText;
    TextView allTimeAverageText;
    TextView allTimeActivitiesText;
    TextView allTimeAverageActivitiesText;
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
        //Setting up database variable
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");
        //Setting all the text-fields
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
        int[] numUsers = {0}, numExercises = {0}, totalDuration = {0};

        //Query all documents in the database, adding to the total
        usersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        Integer currentExercises = doc.getLong("num_exercises").intValue();
                        Integer currentTotalDuration = doc.getLong("total_minutes").intValue();

                        numUsers[0] += 1; //Incrementing by one
                        numExercises[0] += currentExercises; //Incrementing total exercises
                        totalDuration[0] += currentTotalDuration; //Incrementing total Duration of exercises
                    }
                    //Setting the strings to replace in the text-fields
                    String numUsersString = String.valueOf(numUsers[0]);
                    String totalDurationString = String.valueOf(totalDuration[0]) + " minutes";
                    String averageDurationString = String.valueOf((totalDuration[0])/numUsers[0]) + " minutes";
                    String totalExercises = String.valueOf(totalDuration[0]);
                    String averageTotalExercises = String.valueOf((totalDuration[0])/numUsers[0]);

                    //Replacing edit-text fields
                    totalUsersText.setText(numUsersString);
                    allTimeTotalText.setText(totalDurationString);
                    allTimeAverageText.setText(averageDurationString);
                    allTimeActivitiesText.setText(totalExercises);
                    allTimeAverageActivitiesText.setText(averageTotalExercises);
                }
                else {
                    totalUsersText.setText("ERROR - Try Again");
                }
            }
        });

    }


}
