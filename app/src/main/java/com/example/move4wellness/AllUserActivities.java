package com.example.move4wellness;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class AllUserActivities extends AppCompatActivity {
    TextView userNameInput;
    FirebaseFirestore db;
    DocumentReference userRef;
    ArrayList<String> aList; //Main and sub items
    ArrayAdapter<String> adapter;
    ListView listView;

    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_user_activities);
        Intent intent = getIntent();
        UID = intent.getStringExtra("user_UID");
        //Getting the username text field and list
        userNameInput = findViewById(R.id.alluseractivities_username);
        listView = findViewById(R.id.listViewAllActivities);
        aList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, aList);
        listView.setAdapter(adapter);
        //progressBar = findViewById(R.id.progressBar1); Can implement later
        //Initializing user and auth objects
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users").document(UID);
        displayUsername();
        setList();
    }

    //Setting the text for the username
    private void displayUsername() {
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        String username = doc.getString("username");
                        userNameInput.setText(username);
                    }
                }
            }
        });
    }

    //Set all of the items in listView with items from the database
    private void setList() {

        //Getting reference to user
        userRef = db.collection("users").document(UID);
        //accessing user activity data
        userRef.collection("activities")
            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        //Access each document under the "activities" collection
                        String exerciseName = documentSnapshot.getString("activity_name");
                        Double duration = documentSnapshot.getDouble("duration");
                        String dateOfExercise = documentSnapshot.getString("date");
                        //Combine the information into one string
                        String combinedInfo = getCombinedInfo(exerciseName, duration, dateOfExercise);
                        //Add to the list
                        aList.add(combinedInfo);
                    }
                    //Update the list
                    adapter.notifyDataSetChanged();
                }
            });
        }

    /* Formatting the String so it appears correctly in the textview */
    private String getCombinedInfo(String exerciseName, Double duration, String dateOfExercise) {
        String combinedInfo;
        String spaces = "                     "; //Maxes out spaces so it appears on the far right
        if(dateOfExercise != null) {
            combinedInfo = exerciseName + ": " + duration + " minutes" + spaces + dateOfExercise;
        }
        else {
            combinedInfo = exerciseName + ": " + duration + " minutes";
        }
        return combinedInfo;
    }

}