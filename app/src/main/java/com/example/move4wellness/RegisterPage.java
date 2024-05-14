package com.example.move4wellness;

/* Register page class
 * Description: Creates a new used from the information from the editTextFields, if valid
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
    EditText emailInput;
    EditText passwordInput;
    EditText userNameInput;
    ProgressBar progressBar;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";//Regular expression for email verification
    String userName;
    boolean userAdded;


    //Load the XML file & get text-fields
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        //Getting the editText fields
        emailInput = findViewById(R.id.Register_Email_Address);
        passwordInput = findViewById(R.id.Register_Password);
        userNameInput = findViewById(R.id.Register_Full_Name);
        //progressBar = findViewById(R.id.progressBar1); //Can implement later
        //Initializing user and auth objects
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    //On the button click, create the user if valid information is provided
    public void onClickChooseEvent(View view) {
        //Getting the text from the text-fields
        String emailText = emailInput.getText().toString().trim();
        String passwordText = passwordInput.getText().toString().trim();
        userName = userNameInput.getText().toString().trim();

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

    public void onClickBack(View view){
        Intent intent = new Intent(RegisterPage.this, MainActivity.class);
        startActivity(intent);
    }
    //Add the current user to the Fire store database after they have registered
    private void addUserToDatabase() {
        user = auth.getCurrentUser(); //Ensuring that the current user is now received
        if(user != null) {
            String userUID = user.getUid();
            //Validating username
            if(userName == null) {
                userName = "User";
            }
            db = FirebaseFirestore.getInstance();
            Map<String, Object> userData = new HashMap<>();
            userData.put("uid", userUID);  // Storing UID
            userData.put("username", userName); //Storing user ID
            userData.put("isManager", false); //Setting user to not be a manager
            userData.put("total_minutes", 0); //Initializing total_minutes
            userData.put("num_exercises", 0); //Initializing num_exercises
            userData.put("unseen_notifications", true);
            db.collection("users").document(userUID).set(userData)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    pullNotificationsFromDatabase(userUID);
                    userAdded = true;
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    userAdded = false;
                }
            });
        }
    }

    /*Pulls all notifications from the global "notifications" collection, adding them to the user's
    notification collection*/
    private void pullNotificationsFromDatabase(String userUID) {
        //Query through all notifications
        db.collection("notifications").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                //Store the current message in a hashmap
                                Map<String, Object> notificationList = new HashMap<>();
                                String curMessage = doc.getString("message");
                                notificationList.put("message", curMessage);
                                //Add this message to the user's collection
                                db.collection("users").document(userUID)
                                        .collection("notifications").add(notificationList);
                            }
                        }
                    }
                });
    }
}
