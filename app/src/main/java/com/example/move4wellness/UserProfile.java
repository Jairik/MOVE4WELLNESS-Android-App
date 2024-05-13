package com.example.move4wellness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
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


}