package com.example.move4wellness;

/* Manager see users page class
 * Description:
 * Redirects to: The user that the manager clicks */

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
    userListAdapter adapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_see_users);
        //Getting the username list view
        listView = findViewById(R.id.listView);
        userNames = new ArrayList<>();
        userNameUIDs = new ArrayList<>();
        adapter = new userListAdapter(this, android.R.layout.simple_list_item_1, userNames);
        listView.setAdapter(adapter);
        //Setting up the search view
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setEnabled(true);
        searchView.setFocusable(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
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

    private void filterList(String text) {
        List<String> filteredList = new ArrayList<>();
        for(String item : userNames) {
            if(item.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.setFilteredList(filteredList);
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
