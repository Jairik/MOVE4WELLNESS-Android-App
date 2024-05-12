package com.example.move4wellness;

/* Manager global message page class
 * Description:
 * Redirects to: */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ManagerGlobalMessage extends AppCompatActivity {
    EditText messageInput;
    CheckBox checkBox;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_global_message);
        db = FirebaseFirestore.getInstance();
        messageInput = findViewById(R.id.editTextText);
        checkBox = findViewById(R.id.checkBox);
    }

    /* Passes the input from the messageInput to the sendNotification method. If the checkbox
    is not checked, sets an error */
    public void onClickSendMessage(View view) {
        if(checkBox.isChecked()) {
            String message = messageInput.getText().toString();
            sendNotification(message);
            Intent intent = new Intent(ManagerGlobalMessage.this, ManagerHome.class);
            startActivity(intent);
        }
        else {
            checkBox.setError("Please Confirm");
        }
    }

    /* Sends the message to the database (can later expand this to make it a push notification) */
    private void sendNotification(String message) {
        Map<String, Object> notiData = new HashMap<>();
        notiData.put("message", message);
        //Add the new document to the notifications collection
        db.collection("notifications").add(notiData);
    }
}
