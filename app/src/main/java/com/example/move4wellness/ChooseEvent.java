package com.example.move4wellness;

/* Choose event page class
 * Description:
 * Redirects to: */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChooseEvent extends AppCompatActivity {
    Spinner spinner; //Holds the different years
    String year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_event);
        spinner = findViewById(R.id.spinner);
        updateSpinner();
    }

    public void onClickHomepage(View view) {
        String spinnerYear = spinner.getSelectedItem().toString();
        if(!spinnerYear.equals("Select a year")) {
            addToDatabase(spinnerYear);
            Intent intent = new Intent(ChooseEvent.this, MainHomepage.class);
            startActivity(intent);
        }
        else {
            Log.w("Choose Event Spinner", "Invalid Year");
        }
    }

    //Updates the spinner with a hard-coded years
    private void updateSpinner() {
        ArrayList<String> years = new ArrayList<>();
        years.add("Select a year"); //Title item
        years.addAll(Arrays.asList("2024", "2023", "2022", "2021", "2020", "2019", "2018"));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    //Adds the given event year to the database
    private void addToDatabase(String year) {
        //Getting user ID and Database
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        String UID = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Adding the data to a hashmap
        Map<String, Object> eventYear = new HashMap<>();
        eventYear.put("event", year);
        //Add this to the database
        db.collection("users").document(UID).update(eventYear);
    }

}
