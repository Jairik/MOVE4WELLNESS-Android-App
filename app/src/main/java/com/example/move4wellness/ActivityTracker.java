package com.example.move4wellness;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ActivityTracker extends AppCompatActivity {
    BarChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        //find bar chart reference
        chart = findViewById(R.id.barChart);

        //create array list
        ArrayList<BarEntry> entries = new ArrayList<>();

        for(int i = 1; i<4; i++){
            //convert to float
            float value = (float)(i*10.0);

            //create new entry
            BarEntry newEntry = new BarEntry(i, value);

            //add entry to list
            entries.add(newEntry);
        }

        //initialize dataset
        BarDataSet dataSet = new BarDataSet(entries, "Exercises");

        //set color template
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS);

        dataSet.setDrawValues(false);

        //set chat data
        chart.setData(new BarData(dataSet));

        //animate chart
        chart.animateY(2000);

        //set description
        chart.getDescription().setText("Exercises Chart");
        chart.getDescription().setTextColor(Color.WHITE);


    }


}