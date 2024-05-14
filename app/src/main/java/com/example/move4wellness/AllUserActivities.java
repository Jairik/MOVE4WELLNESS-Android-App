package com.example.move4wellness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import java.util.Objects;


public class AllUserActivities extends AppCompatActivity {
    TextView userNameInput;
    FirebaseFirestore db;
    DocumentReference userRef;
    ArrayList<String> aList; //Activity name and durations list
    ArrayList<String> dList; //List for dates
    ArrayList<Integer> iList; //List of images
    int[] imageIDs; //Holds the IDs for the three different images
    CustomListAdapter adapter; //Custom adapter to make everything look pretty
    ListView listView;
    String UID; //Holds the unique user ID, which is retrieved from previous activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_user_activities);
        Intent intent = getIntent();
        UID = intent.getStringExtra("user_UID");
        //Getting the username text field
        userNameInput = findViewById(R.id.alluseractivities_username);
        //Getting the list and initializing relevant variables
        listView = findViewById(R.id.listViewAllActivities);
        aList = new ArrayList<>();
        dList = new ArrayList<>();
        iList = new ArrayList<>();
        adapter = new CustomListAdapter(this, aList, dList, iList);
        listView.setAdapter(adapter);
        //Getting the image IDs
        imageIDs = new int[]{R.drawable.jumprope, R.drawable.weightlifting, R.drawable.abexercise};
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
                        String username = doc.getString("username") + "'s";
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
                        String dString = String.format("%.0f", duration);
                        String combinedInfo = exerciseName + ": " + dString + " minutes";
                        //Add to the list
                        aList.add(combinedInfo);
                        dList.add(dateOfExercise);
                        iList.add(getEImage(exerciseName));
                    }
                    //Update the list
                    adapter.notifyDataSetChanged();
                }
            });
        }

        //Returns the respective image ID for the exercise name
        int getEImage(String exerciseName) {
            int ID;
            if(Objects.equals(exerciseName, "Sit-ups")) {
                ID = imageIDs[2];
            }
            else if(Objects.equals(exerciseName, "Weightlifting")) {
                ID = imageIDs[1];
            }
            else { //Jump-rope
                ID = imageIDs[0];
            }
            return ID;
        }


    public void onClickGoBack(View view) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        UID = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(UID);

        //Get the value of the "isManager" field and redirect to appropriate activity
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        boolean isManager = Boolean.TRUE.equals(doc.getBoolean("isManager"));
                        Intent intent;
                        if(isManager) { //User is a manager, redirect to manager home page
                            intent = new Intent(AllUserActivities.this, ManagerHome.class);
                        }
                        else { //User is not a manager, redirect to normal homepage
                            intent = new Intent(AllUserActivities.this, MainHomepage.class);
                        }
                        startActivity(intent);
                    }
                }
                else { //Error - navigate to normal homepage
                    Intent intent = new Intent(AllUserActivities.this, MainHomepage.class);
                    startActivity(intent);
                }
            }
        });
    }
}