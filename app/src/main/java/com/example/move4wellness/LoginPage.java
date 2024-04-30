package com.example.move4wellness;

/* Login page class
* Description:
* Redirects to: */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LoginPage extends AppCompatActivity {
    //Load the XML file
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
    }

    public void onClickHomePage(View view) {
        Intent intent = new Intent(LoginPage.this, ChooseEvent.class);
        startActivity(intent);
    }


}
