package com.example.move4wellness;

/* Register page class
 * Description:
 * Redirects to: ChooseEvent Screen */

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterPage extends AppCompatActivity {

    Model model;
    FirebaseAuth auth;
    FirebaseUser user;
    EditText email;
    EditText password;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; //Regular expression for email verification
    char status;


    //Load the XML file
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        //Getting the editText fields
        email = findViewById(R.id.Register_Email_Address);
        password = findViewById(R.id.Register_Password);
    }

    public void onClickRegister() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        //Start loading box thing
        char status1 = createUser(emailText, passwordText);
        if(status1 == 'S') { //Successful
            //Continue to next activity
            Intent intent = new Intent(RegisterPage.this, ChooseEvent.class);
            startActivity(intent);
        }
        else if(status1 == 'E') { //Email Error
            //Display an error message on the email text field
        }
        else if(status1 == 'P') { //Password Error
            //Display an error message on password text field

        }
        else { //Unknown error
            //Display a general error message
        }
    }

    public char createUser(String email, String password) {
        status = 'U'; //Signal value for unsuccessful/unknown error
        if(email.matches(emailPattern)) {
            status = 'E'; //Signal value that email is incorrect
        }
        else if(password.isEmpty() || password.length() < 6) {
            status = 'P'; //Signal value that password is invalid
        }
        else {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        status = 'S'; //Successful
                    }
                    else {
                        status = 'U'; //Unsuccessful
                    }
                }
            });
        }
        return status;
    }

    public void onClickChooseEvent(View view) {
        Intent intent = new Intent(RegisterPage.this, ChooseEvent.class);
        startActivity(intent);
    }
}
