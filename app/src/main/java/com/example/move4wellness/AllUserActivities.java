package com.example.move4wellness;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class AllUserActivities extends AppCompatActivity {
    TextView userNameInput;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
    DocumentReference userRef;

    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_user_activities);
        //Getting the username text field
        userNameInput = findViewById(R.id.alluseractivities_username);
        //progressBar = findViewById(R.id.progressBar1); Can implement later
        //Initializing user and auth objects
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        assert user != null;
        UID = user.getUid();
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users").document(UID);
        displayUsername();
        setList();
    }

    //Setting the text for the username
    private void displayUsername() {
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        String username = doc.getString("username");
                        userNameInput.setText(username);
                    }
                }
            }
        });
    }

    //Set all of the items in listView with items from the database
    private void setList() {

    }

}
