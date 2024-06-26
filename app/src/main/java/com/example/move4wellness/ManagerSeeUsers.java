package com.example.move4wellness;

/* Manager see users page class
 * Description: Holds all user's usernames in a list view
 * Redirects to: See all activities page of the user they selected */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ManagerSeeUsers extends AppCompatActivity {
    FirebaseFirestore db;
    ListView listView;
    ArrayList<String> userNames;
    ArrayList<String> userNameUIDs; //Holds UIDs for all users in userNames
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_see_users);
        //Getting the username list view
        listView = findViewById(R.id.listView);
        userNames = new ArrayList<>();
        userNameUIDs = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userNames);
        listView.setAdapter(adapter);
        //Setting up the search view
        /*searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setEnabled(true);
        searchView.setFocusable(true); */

        //Initializing user and auth objects
        db = FirebaseFirestore.getInstance();
        setList();
        //Adding an onClickListener to the listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get the corresponding UID and redirect to that page
                String UID = userNameUIDs.get(position);
                if(UID == null) {
                    Log.e("ManagerSeeUsers", "UID is null");
                }
                else {
                    Intent intent = new Intent(ManagerSeeUsers.this, AllUserActivities.class);
                    intent.putExtra("user_UID", UID);
                    startActivity(intent);
                }
            }
        });
    }

    //Set all of the items in listView with items from the database
    private void setList() {

        //Getting reference to users collection
        CollectionReference usersRef = db.collection("users");
        //accessing user activity data
        usersRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            //Access each document under the "activities" collection
                            String username = documentSnapshot.getString("username");
                            String UID = documentSnapshot.getString("uid");
                            //Add to the lists
                            userNames.add(username);
                            userNameUIDs.add(UID);
                        }
                        //Update the list
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    /* On click for the back button, redirects to manager home page */
    public void onClickGoBackManager(View view) {
        Intent intent = new Intent(ManagerSeeUsers.this, ManagerHome.class);
        startActivity(intent);
    }

}
