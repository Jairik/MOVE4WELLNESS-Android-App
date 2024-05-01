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
                        //User is successfully signed in
                        Intent intent = new Intent(LoginPage.this, ChooseEvent.class);
                        startActivity(intent);
                    }
                    else {
                        emailInput.setError("Could not sign in");
                    }
                }
            });
        }
    }
}
