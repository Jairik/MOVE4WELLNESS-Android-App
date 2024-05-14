package com.example.move4wellness;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityTracker extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    BarChart chart;
    private List<String> values = Arrays.asList("Jump Rope", "Weightlifting", "Sit Ups");
    private List<String> week = Arrays.asList("Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat");
    private List<String> month = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
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
    Spinner spinner;
    String selectedString;
    String formattedStartDate;

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
        spinner = findViewById(R.id.tracktime);
        // Define spinner options
        String[] options = {"Total", "Week", "Month"};
        // Create ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set adapter to spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        if(user != null) {
            uniqueUserID = user.getUid();
            userRef = db.collection("users").document(uniqueUserID);
        }
        else { //No user is signed in, redirect to main activity
            noUserDetected(); //Go back to first welcome screen
        }
        //initial graph will be year to date
        getTotal();
    }

    //If there is somehow no user signed in, will redirect to login/register page
    private void noUserDetected() {
        Intent intent = new Intent(ActivityTracker.this, MainActivity.class);
        startActivity(intent);
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
        chart.getDescription().setText("");

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

    public void onClickGoUserHistory(View view){
        Intent intent = new Intent(ActivityTracker.this, AllUserActivities.class);
        startActivity(intent);
    }

    public void onClickGoHomePage(View view){
        Intent intent = new Intent(ActivityTracker.this, MainHomepage.class);
        startActivity(intent);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object selectedItem = parent.getItemAtPosition(position);

        // Cast the selected item to the appropriate type (e.g., String)
        if (selectedItem != null) {
            selectedString = selectedItem.toString();
            Log.d("Spinner", "Selected item: " + selectedString);
        }

        System.out.println("item selected");
        if(selectedString.equals("Week")){
            Log.d("Spinner", "Week option selected. Creating bar chart for week.");
            //getPastWeek();
            getWeeklyActivities();
        }
        else if(selectedString.equals("Total")){
            getTotal();
        }
        else if(selectedString.equals("Month")){
            getMonthlyActivities();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Implement the logic to handle when no item is selected
        // This method will be called when no item is selected in the spinner
    }

    public void getTotal(){
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

    public void createBarChartWeek(float[] weeklyActivityDuration){

        System.out.println("inside createBarChartWeek");
        chart = findViewById(R.id.barChart);
        ArrayList<BarEntry> entriesWeek = new ArrayList<>();
        //populate array list
        // Populate array list with data from the weeklyActivityDuration array
        for (int i = 0; i < weeklyActivityDuration.length; i++) {
            entriesWeek.add(new BarEntry(i, weeklyActivityDuration[i]));
        }
        //initialize dataset
        BarDataSet dataSet = new BarDataSet(entriesWeek, "Week");
        //set color template
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        dataSet.setDrawValues(false);
        //set chart data
        chart.setData(new BarData(dataSet));
        //animate chart
        chart.animateY(2000);
        //set description
        chart.getDescription().setText("");

        //sets x-axis labels
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(week));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setGranularityEnabled(true);
    }

    public void getWeeklyActivities() {
        // Array to store total duration of activities for each day of the week
        float[] weeklyActivityDuration = new float[7]; // 7 days in a week (Sunday = 0, Monday = 1, ..., Saturday = 6)
        System.out.println("inside getWeeklyActivities()");
        // Accessing user activity data
        if (userRef != null) {
            userRef.collection("activities")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            // Get the start date of the current week (Sunday)
                            Calendar startOfWeek = Calendar.getInstance();
                            startOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                            System.out.println("Start of the week: " + startOfWeek.getTime());

                            // Filter documents for the current week
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                // Access each document under the "activities" collection
                                String dateString = documentSnapshot.getString("date");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                                Date date = null;
                                try {
                                    date = dateFormat.parse(dateString);
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }

                                // Create a Calendar instance and set the date
                                Calendar activityDate = Calendar.getInstance();
                                activityDate.setTime(date);

                                // Debug logging for activity date
                                System.out.println("Activity date: " + activityDate.getTime());

                                // Check if the activity date is within or on the current week
                                if (!activityDate.before(startOfWeek) || isSameDay(activityDate, startOfWeek)) {
                                    // Calculate the day index (0 for Sunday, 1 for Monday, ..., 6 for Saturday)
                                    int dayIndex = activityDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;

                                    // Get the duration of the activity
                                    float duration = documentSnapshot.getDouble("duration").floatValue();

                                    // Add the duration to the total duration for the corresponding day of the week
                                    weeklyActivityDuration[dayIndex] += duration;
                                }
                            }
                            // Process or display the weekly activity duration as needed
                            for (int i = 0; i < 7; i++) {
                                // Print or use the total duration for each day of the week
                                System.out.println("Day " + (i + 1) + ": " + weeklyActivityDuration[i] + " minutes");
                            }
                            System.out.println("call createBarChartWeek(weeklyActivityDuration);");
                            createBarChartWeek(weeklyActivityDuration);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Failed to access document: " + e.getMessage());
                        }
                    });
        } else {
            System.out.println("userRef = NULL");
        }
    }

    // Helper method to check if two Calendar instances represent the same day
    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    public void getMonthlyActivities() {
        // Get the current year
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        // Array to store total duration of activities for each month
        float[] monthlyActivityDuration = new float[12]; // 12 months in a year (January = 0, February = 1, ..., December = 11)

        // Accessing user activity data
        if (userRef != null) {
            userRef.collection("activities")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                // Access each document under the "activities" collection
                                String dateString = documentSnapshot.getString("date");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                                Date date = null;
                                try {
                                    date = dateFormat.parse(dateString);
                                } catch (ParseException e) {
                                    // Handle parse exception
                                    e.printStackTrace();
                                    continue; // Skip to the next document
                                }

                                // Create a Calendar instance and set the date
                                Calendar activityDate = Calendar.getInstance();
                                activityDate.setTime(date);

                                // Check if the activity date is in the current year
                                if (activityDate.get(Calendar.YEAR) != currentYear) {
                                    continue; // Skip to the next document if the activity is not in the current year
                                }

                                // Get the month index (0 for January, 1 for February, ..., 11 for December)
                                int monthIndex = activityDate.get(Calendar.MONTH);

                                // Get the duration of the activity
                                float duration = documentSnapshot.getDouble("duration").floatValue();

                                // Add the duration to the total duration for the corresponding month
                                monthlyActivityDuration[monthIndex] += duration;
                            }

                            // Process or display the monthly activity duration as needed
                            for (int i = 0; i < 12; i++) {
                                // Print or use the total duration for each month
                                System.out.println("Month " + (i + 1) + ": " + monthlyActivityDuration[i] + " minutes");
                            }
                            System.out.println("call createBarChartMonth(monthlyActivityDuration);");
                            createBarChartMonthly(monthlyActivityDuration);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                            System.out.println("Failed to access document: " + e.getMessage());
                        }
                    });
        } else {
            System.out.println("userRef = NULL");
        }
    }

    public void createBarChartMonthly(float[]monthlyActivityDuration){
        System.out.println("inside createBarChartWeek");
        chart = findViewById(R.id.barChart);
        ArrayList<BarEntry> entriesWeek = new ArrayList<>();
        //populate array list
        // Populate array list with data from the weeklyActivityDuration array

        for (int i = 0; i < monthlyActivityDuration.length; i++) {
            entriesWeek.add(new BarEntry(i, monthlyActivityDuration[i]));
        }

        //initialize dataset
        BarDataSet dataSet = new BarDataSet(entriesWeek, "Months");
        //set color template
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        dataSet.setDrawValues(false);
        //set chart data
        chart.setData(new BarData(dataSet));
        //animate chart
        chart.animateY(2000);
        //set description
        chart.getDescription().setText("");

        //sets x-axis labels
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(month));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setGranularityEnabled(true);
    }
}


