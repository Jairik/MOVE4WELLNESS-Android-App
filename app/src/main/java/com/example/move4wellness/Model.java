package com.example.move4wellness;

import java.util.ArrayList;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;


/*Authors: JJ McCauley & Will Lamuth
 * Creation Date: 4/2/24
 * Last Update: 4/27/24
 * Notes: N/A
 * Description: This class serves as the Model to the app, handling all of
 * the backend data management and stuff. */

/* -- Current Database Tree Structure --
* users:
*   ->username (maybe)
*   ->phone_number(maybe)
*   ->email
*   ->password
*   ->activities {
*       ->minutes
*       ->activity_type
*   }
* managers:
*   ->username (maybe)
*   ->phone_number (maybe)
*   ->email
*   ->password
* statistics:
*   ->total_minutes
*   ->total_users
* ---------------------------------------
* Current Issues: Will need to refactor if the manager wants to see all activities. Else, it will be
* very inefficient. However, totals can be easily achievable with the statistics collection. */

public class Model {
    //Data Members Here
    ArrayList<String> firebaseKeys; //Holds keys for activities
    String userKey; //Holds key for user
    String statisticsKey; //Key for statistics node, will be modified when new user/activity is added

    /*Make a new user and add to the database
    If the user is already in the database, will return false. Else, if successful, returns true */
    public boolean createUser(String email, String phoneNumber, String password) {
        return false; //for now
    }

    /*Find the email/phone number and assign userKey and activityKeys. If successful, returns true
    * Parameters: x represents either the email or password */
    public boolean findUser(String x, String password) {
        return false; //for now
    }

    /* Given a userKey, will load the activity keys onto the arrayList */
    private void loadActivities(/*Reference To User*/) {
        //load the activities
    }

    /*Fetches all activities and details from the current user
    * Returns: An ArrayList<String>, with the even entries holding the activity names and
    * the odd entries holding the corresponding minutes */
    public ArrayList<String> fetchActivities() {
        ArrayList<String> names_minutes = new ArrayList<>();
        //for snapshot: whatever idk -> load them all


        return names_minutes;
    }

}
