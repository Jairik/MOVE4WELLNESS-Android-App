package com.example.move4wellness;

/* Login page class
* Description: Takes in the input from the edit text fields and,
* if valid, signs the user in
* Redirects to:  Main homepage or manager home page, dependent on
* user authority (stored in database) */

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
                        checkManagerStatusandNavigate();
                    }
                    else {
                        emailInput.setError("Could not sign in");
                    }
                }
            });
        }
    }

    private void checkManagerStatusandNavigate() {
        user = auth.getCurrentUser();
        //If the user is null (it shouldn't be), return false
        if(user == null) {
            return;
        }
        //Getting details of the current user & the database
        String UID = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(UID);

        //Get the value of the "isManager" field and redirect to appropriate activity
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        boolean isManager = Boolean.TRUE.equals(doc.getBoolean("isManager"));
                        Intent intent;
                        if(isManager) { //User is a manager, redirect to manager home page
                            intent = new Intent(LoginPage.this, ManagerHome.class);
                        }
                        else { //User is not a manager, redirect to normal homepage
                            intent = new Intent(LoginPage.this, MainHomepage.class);
                        }
                        startActivity(intent);
                    }
                }
                else { //Error - navigate to normal homepage
                    Intent intent = new Intent(LoginPage.this, MainHomepage.class);
                    startActivity(intent);
                }
            }
        });
    }
}




