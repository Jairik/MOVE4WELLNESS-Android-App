package com.example.move4wellness;

/* Choose event page class
 * Description:
 * Redirects to: */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChooseEvent extends AppCompatActivity {
    TextView yearTextInput;
    TextView dateTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_event);

    }

    public void onClickHomepage(View view) {
        Intent intent = new Intent(ChooseEvent.this, MainHomepage.class);
        startActivity(intent);
    }

}
