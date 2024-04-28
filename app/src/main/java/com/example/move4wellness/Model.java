package com.example.move4wellness;

import java.util.ArrayList;

/*Authors: JJ McCauley & Will Lamuth
 * Creation Date: 4/2/24
 * Last Update: 4/9/24
 * Notes: N/A
 * Description: This class serves as the Model to the app, handling all of
 * the backend data management and stuff. */

public class Model {
    //Data Members Here
    ArrayList<String> firebaseKeys;


    //Looks through the database to check if the user exists
    public boolean authenticateUser(String username, String password) {
        //Check if the user exists in the database
        return true; //Temporary
    }

    //Given a String email, username, and password, attempts to create the user and returns
    //true if it is successful
    public boolean createUser(String email, String username, String password) {
        //Check if it exists. Could use authenticateUser function
        //Add user to the database, returning true if it is valid
        return true; //Temporary
    }

    //Deletes a given thing from the firebase


    //-Rest of the functions-

}
