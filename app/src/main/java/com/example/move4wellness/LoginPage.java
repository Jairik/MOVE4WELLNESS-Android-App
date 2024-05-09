package com.example.move4wellness;

/* Login page class
* Description:
* Redirects to: */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginPage extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    EditText emailInput;
    EditText passwordInput;
    //ProgressBar progressBar;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; //Regular expression for email verification

    //Load the XML file
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        //Getting the editText fields
        emailInput = findViewById(R.id.login_name);
        passwordInput = findViewById(R.id.Login_Password);
        //progressBar = findViewById(R.id.progressBar1); Can implement later
        //Initializing user and auth objects
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    public void onClickHomepage(View view) {

        //Getting the text from the text-fields
        String emailText = emailInput.getText().toString().trim();
        String passwordText = passwordInput.getText().toString().trim();

        //Validating input before attempting to sign in
        if(!emailText.matches(emailPattern)) {
            emailInput.setError("Invalid Email");
        }
        else if(passwordText.isEmpty() || passwordText.length() < 6) {
            passwordInput.setError("Invalid Password");
        }
        else {
            auth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        //User is successfully signed in, redirect to appropriate task
                        Intent intent;
                        if(isManager()) {
                            intent = new Intent(LoginPage.this, ManagerHome.class);
                        }
                        else {
                            intent = new Intent(LoginPage.this, MainHomepage.class);
                        }
                        startActivity(intent);
                    }
                    else {
                        emailInput.setError("Could not sign in");
                    }
                    return false;
                }
            });
        }
    }

    private void setManagerCallback(ManagerStatusCallback callback) {
        //If the user is null (it shouldn't be), return false
        if(user == null) {
            return;
        }
        //Getting details of the current user
        user = auth.getCurrentUser();
        assert user != null;
        String UID = user.getUid();
        //Getting reference to user through their unique ID
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(UID);
        boolean isManagerValue;
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        boolean isManager = Boolean.TRUE.equals(doc.getBoolean("isManager"));
                    }
                }
            }
        });
        //I want to return isManager here
        return false; //If unsuccessful, just return false
    }

    //Returns if the user is a manager
    public boolean isManager() {

    }

}




