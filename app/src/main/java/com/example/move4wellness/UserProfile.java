package com.example.move4wellness;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
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

/* User Profile Class
Description: Shows the user profile and relevant options/statistics
 */

public class UserProfile extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    TextView usernameText;
    TextView eventyear;
    String eventYearText;
    PieChart piechart;
    FirebaseFirestore db;
    DocumentReference userRef;
    String uniqueUserID;
    float duration = 0.0f;
    float total = 0.0f;
    TextView minutesTotal;
    String exerciseName;
    float jumprope = 0.0f;
    float weightlifting = 0.0f;
    float situps = 0.0f;
    int countJumprope = 0;
    int countWeightlifting = 0;
    int countSitups = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        usernameText = findViewById(R.id.username_profile);

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
        setUsernameText();
        getEventYear();
        getTotalDuration();
    }

    //If there is somehow no user signed in, will redirect to login/register page
    private void noUserDetected() {
        Intent intent = new Intent(UserProfile.this, MainActivity.class);
        startActivity(intent);
    }

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

    //method sends back to first login/register screen
    public void onClickLogOut(View view){
        Intent intent = new Intent(UserProfile.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickBackHomepage(View view){
        Intent intent = new Intent(UserProfile.this, MainHomepage.class);
        startActivity(intent);
    }

    //method grabs which event year the user is participating
    public void getEventYear(){
        user = auth.getCurrentUser();
        //If the user is null (it shouldn't be), return false
        if(user == null) {
            return;
        }
        //Getting details of the current user & the database
        String UID = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(UID);
        eventyear = findViewById(R.id.eventyear);
        eventYearText= eventyear.getText().toString();
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        String year = doc.getString("event");
                        eventYearText += year;
                        eventyear.setText(eventYearText);
                    }
                }
            }
        });
    }


    public void createPieChart(float act1, float act2, float act3){
        piechart = findViewById(R.id.piechart);

        // Create array list
        ArrayList<PieEntry> entries = new ArrayList<>();
        // Populate array list
        entries.add(new PieEntry(act1, "Jump Rope"));
        entries.add(new PieEntry(act2, "Weightlifting"));
        entries.add(new PieEntry(act3, "Sit-ups"));

        // Initialize dataset
        PieDataSet dataSet = new PieDataSet(entries, "");
        // Set color template
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        dataSet.setDrawValues(false);
        // Set text color for entries


        // Set chart data
        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(false);
        pieData.setValueTextColor(Color.BLACK); // Change the color as neede
        piechart.setData(pieData);

        // Animate chart
        piechart.animateY(2000);

        // Set description
        piechart.getDescription().setEnabled(false);

        // Display text at the center
        piechart.setDrawCenterText(true);
        piechart.setCenterText("Exercises");
        piechart.setCenterTextSize(20f);
        piechart.setCenterTextColor(Color.WHITE);
    }




    public void getTotalDuration(){

        //accessing user activity data
        if (userRef != null) {
            userRef.collection("activities")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                //Access each document under the "activities" collection
                                duration = documentSnapshot.getDouble("duration").floatValue();
                                exerciseName = documentSnapshot.getString("activity_name");
                                total += duration;

                                if(exerciseName.equals("Jump Rope")){
                                    //Increment the jumprope duration
                                    jumprope += duration;
                                    countJumprope++;
                                }
                                else if(exerciseName.equals("Weightlifting")){
                                    //Increment the weightlifting duration
                                    weightlifting += duration;
                                    countWeightlifting++;
                                }
                                else{
                                    //Increment the situps duration
                                    situps += duration;
                                    countSitups++;
                                }
                            }
                            setDuration(total);
                            createPieChart(jumprope/2, weightlifting/2, situps/2);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("failed to access document");
                        }
                    });
        } else {
            System.out.println("userRef = NULL");
        }
    }

    //sets text for total minutes
    public void setDuration(float totalduration){
        minutesTotal = findViewById(R.id.totalminutes);
        String totalString = minutesTotal.getText().toString();
        String floatString = String.valueOf(totalduration);
        totalString += floatString;
        minutesTotal.setText(totalString);
    }


}