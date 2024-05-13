package com.example.move4wellness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfile extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    TextView usernameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        usernameText = findViewById(R.id.username_profile);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        setUsernameText();
    }

    private void setUsernameText() {
        user = auth.getCurrentUser();
        //If the user is null (it shouldn't be), return false
        if(user == null) {
            return;
        }
        //Getting details of the current user & the database
        String UID = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(UID);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        String userName = doc.getString("username");
                        usernameText.setText(userName);
                    }
                }
            }
        });
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