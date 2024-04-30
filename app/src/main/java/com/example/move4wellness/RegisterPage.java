package com.example.move4wellness;

/* Register page class
 * Description:
 * Redirects to: ChooseEvent Screen */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterPage extends AppCompatActivity {

    //Load the XML file
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
    }

    public void onClickChooseEvent(View view) {
        Intent intent = new Intent(RegisterPage.this, ChooseEvent.class);
        startActivity(intent);
    }
}
