package com.example.move4wellness;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.*; //Can refine later


/*Authors: JJ McCauley & Will Lamuth
 * Creation Date: 4/2/24
 * Last Update: 4/27/24
 * Notes: Uses Firebase User Authentication and Firebase Realtime Database.
 * Description: This class serves as the Model to the app, handling all of
 * the backend data management and stuff. */

/* -- Current Database Tree Structure (Tentative) --
* users {
*   -userID {  (NOTE: Will be assigned via Firebase User Authentication)
*         -activities collection {
*            -> activity_name
*            -> activity_length
*         }
*         -> isManager (boolean value)
*    }
* }
* statistics {
*   -total_users
*   -total_minutes
* }
* -------------------------------------------------- */


//Model must implement Serializable in order to be passed into different intents
public class Model implements Serializable {

    ArrayList<String> activityKeys; //Holds keys for activities
    String userKey; //Holds key for user
    String statisticsKey; //Key for statistics node, will be modified when new user/activity is added
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; //Regular expression to verify valid email
    FirebaseAuth auth;
    FirebaseUser user;
    String userID; //Stores key used for writing data to user
    private char status; //For login/registration, must be global for scope reasons (may refactor)

    /*Used for registering a new user, given a String email and String password
    * NOTES: an overloaded function could be used for verifying Phone-numbers*/
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

    /* Used for allowing a existing user to login
    * Note: Email+Password validation occurs to cut down on Firebase queries*/
    public char authUser(String email, String password) {
        status = 'U'; //Signal value for unsuccessful/unknown
        if(!email.matches(emailPattern)) {
            status = 'E'; //Signal value that email is incorrect
        }
        else if(password.isEmpty() || password.length() < 6) {
            status = 'P'; //Signal value that password is invalid
        }
        else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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



    /* Given a userKey, will load the activity keys onto the arrayList */
    private void loadActivities(/*Reference To User*/) {
        userID = user.getUid(); //gets the user ID from the current user
        //load the activities
    }

    /*Fetches all activities and details from the current user
    * Returns: An ArrayList<String>, with the even entries holding the activity names and
    * the odd entries holding the corresponding minutes
    * NOTE: There is definitely a better way to do this */
    public ArrayList<String> fetchActivities() {
        ArrayList<String> names_minutes = new ArrayList<>();
        //for snapshot: whatever idk -> load them all


        return names_minutes;
    }

    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
        user = auth.getCurrentUser();
    }


}
