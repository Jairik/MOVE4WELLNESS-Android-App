package com.example.move4wellness;

/* Notification screen class
 * Description: Page displays user notifications and allows them to remove/delete them
 * Redirects to: MainHomepage */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_screen);
    }

    public void onClickGoBack(View view) {
        Intent intent = new Intent(NotificationScreen.this, MainHomepage.class);
        startActivity(intent);
    }

    //skeleton method for removing notifications from listview with long click
    public void onLongClickRemove(View view){

    }

}
