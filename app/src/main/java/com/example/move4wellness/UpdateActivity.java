package com.example.move4wellness;

/* Update Activity page class
 * Description: Page allows users to manually enter exercise data and update it
 * Redirects to: MainHomepage*/

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UpdateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity_screen);
    }

    //Back button redirect
    public void onClickGoBack(View view) {
        Intent intent = new Intent(UpdateActivity.this, MainHomepage.class);
        startActivity(intent);
    }

    //method skeleton for updating activity button
    public void onClickUpdateData(View view){

    }

}
