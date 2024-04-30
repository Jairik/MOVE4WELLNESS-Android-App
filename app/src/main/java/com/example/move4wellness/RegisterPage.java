package com.example.move4wellness;

/* Register page class
 * Description:
 * Redirects to: ChooseEvent Screen */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterPage extends AppCompatActivity {

    Model model;

    //Load the XML file
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        //Getting the model
        Intent intent = getIntent();
        model = (Model) intent.getSerializableExtra("modelKey");
        //Getting the editText fields

    }




}
