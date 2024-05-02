package com.example.move4wellness;

/* Register page class
 * Description:
 * Redirects to: ChooseEvent Screen */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterPage extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    EditText emailInput;
    EditText passwordInput;
    ProgressBar progressBar;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; //Regular expression for email verification


    //Load the XML file & get text-fields
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        //Getting the editText fields
        emailInput = findViewById(R.id.Register_Email_Address);
        passwordInput = findViewById(R.id.Register_Password);
        //progressBar = findViewById(R.id.progressBar1); Can implement later
        //Initializing user and auth objects
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    //On the button click, create the user if valid information is provided
    public void onClickChooseEvent(View view) {
        //Getting the text from the text-fields
        String emailText = emailInput.getText().toString().trim();
        String passwordText = passwordInput.getText().toString().trim();

        //Validating email and password
        if(!emailText.matches(emailPattern)) {
            //Display an error message on the email text field
            emailInput.setError("Invalid Email");
        }
        else if(passwordText.isEmpty() || passwordText.length() < 6) {
            //Display an error message on the password text field
            passwordInput.setError("Invalid Password");
        }
        else {
            auth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        //Success! Add uniqueID to database and go to next activity
                        addUserToDatabase();
                        Intent intent = new Intent(RegisterPage.this, ChooseEvent.class);
                        startActivity(intent);
                    }
                    else {
                        emailInput.setError("Unknown Error - please try again");
                    }
                }
            });
        }
    }

    private void addUserToDatabase() {
        user = auth.getCurrentUser(); //Ensuring that the current user is now received
        if(user != null) {
            String userUID = user.getUid();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            //Creating a reference to the user's child where we can place the UID
            DatabaseReference usersRef = database.child("users").child(userUID);
            //Adding the UniqueID to the database
            usersRef.setValue(userUID);
        }
        else {
            //Break the program? It shouldn't ever get here
        }
    }


}
