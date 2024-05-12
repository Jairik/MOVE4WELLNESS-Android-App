package com.example.move4wellness;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityTracker extends AppCompatActivity {
    BarChart chart;
    private List<String> values = Arrays.asList("Jump Rope", "Weightlifting", "Sit Ups");
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
    DocumentReference userRef;
    String uniqueUserID;
    float durationFloat = 0.0f;
    float jumprope = 0.0f;
    float weightlifting = 0.0f;
    float situps = 0.0f;
    int countJumprope = 0;
    int countWeightlifting = 0;
    int countSitups = 0;
    String exerciseName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

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

        //accessing user activity data
        if (userRef != null) {
            userRef.collection("activities")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                //Access each document under the "activities" collection
                                exerciseName = documentSnapshot.getString("activity_name");
                                Double duration = documentSnapshot.getDouble("duration");
                                // Convert the duration to float
                                durationFloat = duration != null ? duration.floatValue() : 0.0f;
                                System.out.println("duration: " + durationFloat);
                                System.out.println("Exercise Name: " + exerciseName);

                                if(exerciseName.equals("Jump Rope")){
                                    //Increment the jumprope duration
                                    jumprope += durationFloat;
                                    countJumprope++;
                                }
                                else if(exerciseName.equals("Weightlifting")){
                                    //Increment the weightlifting duration
                                    weightlifting += durationFloat;
                                    countWeightlifting++;
                                }
                                else{
                                    //Increment the situps duration
                                    situps += durationFloat;
                                    countSitups++;
                                }

                            }
                            //method call to create bar chart with user data
                            createBarChart(jumprope, weightlifting, situps);
                            //sets average text on ActivityTracker XML
                            setAverage("Jump Rope", jumprope, countJumprope);
                            setAverage("Weightlifting", weightlifting, countWeightlifting);
                            setAverage("Sit-ups", situps, countSitups);
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

    //If there is somehow no user signed in, will redirect to login/register page
    private void noUserDetected() {
        Intent intent = new Intent(ActivityTracker.this, MainActivity.class);
    }

    //Creates bar chart with user data passed in
    public void createBarChart(float activity1, float activity2, float activity3){
        //find bar chart reference
        chart = findViewById(R.id.barChart);
        //create array list
        ArrayList<BarEntry> entries = new ArrayList<>();
        //populate array list
        entries.add(new BarEntry(0, activity1));
        entries.add(new BarEntry(1, activity2));
        entries.add(new BarEntry(2, activity3));
        //initialize dataset
        BarDataSet dataSet = new BarDataSet(entries, "Exercises");
        //set color template
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        dataSet.setDrawValues(false);
        //set chart data
        chart.setData(new BarData(dataSet));
        //animate chart
        chart.animateY(2000);
        //set description
        chart.getDescription().setText("Exercises Chart");
        chart.getDescription().setTextColor(Color.WHITE);

        //sets x-axis labels
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(values));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setGranularityEnabled(true);
    }

    public void setAverage(String name, float exercise, int count){
        TextView jumpropeAverage = findViewById(R.id.avgJumpRope);
        TextView weightliftingAverage = findViewById(R.id.avgWeightLifting);
        TextView situpsAverage = findViewById(R.id.avgSitUps);
        float avg = exercise/count;
        String avgString = Float.toString(avg) + " minutes";
        if(name.equals("Jump Rope")){
            //set jumprope avg duration text
            jumpropeAverage.setText(avgString);
        }
        else if(name.equals("Weightlifting")){
            //set weightlifting avg duration text
            weightliftingAverage.setText(avgString);
        }
        else{
            //set situps avg duration text
            situpsAverage.setText(avgString);
        }
    }


}