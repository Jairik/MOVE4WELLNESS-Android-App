package com.example.move4wellness;

/* Main Homepage Class
 * Description:
 * Redirects to: notification, activity tracker, activity updater, user profile*/

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainHomepage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_homepage);
    }

    //redirects to noti screen
    public void onClickNotification(View view) {
        Intent intent = new Intent(MainHomepage.this, NotificationScreen.class);
        startActivity(intent);
    }

    //redirects to profile
    public void onClickProfile(View view) {
        Intent intent = new Intent(MainHomepage.this, UserProfile.class);
        startActivity(intent);
    }

    //redirects to activity updater - (not sure how many activy updater screens we should have)
    public void onClickActivityUpdate(View view) {
        Intent intent = new Intent(MainHomepage.this, UpdateActivity.class);
        startActivity(intent);
    }

    //redirects to activity tracker screen
    public void onClickActivityTracker(View view) {
        Intent intent = new Intent(MainHomepage.this, ActivityTracker.class);
        startActivity(intent);
    }


}
