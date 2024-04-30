package com.example.move4wellness;

/*Authors: JJ McCauley & Will Lamuth
* Creation Date: 4/2/24
* Last Update: 4/28/24
* Notes: This page acts as the initial login sign-up page
* Redirects to: Register/login pages */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//import com.google.firebase.auth.*; //Can refine later


public class MainActivity extends AppCompatActivity {
    Model model; //Will need to passed to different activities
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        model = new Model(); //Creating the model
        //CAUSING ERROR -> model.setAuth(FirebaseAuth.getInstance());
    }

    //Switch to register screen
    public void onClickRegister(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterPage.class);
        startActivity(intent);
    }

    //Switch to login screen
    public void onClickLogin(View view) {
        Intent intent = new Intent(MainActivity.this, LoginPage.class);
        startActivity(intent);
    }
}