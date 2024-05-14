package com.example.move4wellness;

/* Notification screen class
 * Description: Page displays user notifications and allows them to remove/delete them
 * Redirects to: MainHomepage */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationScreen extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;
    String UID;
    ArrayList<String> mList;
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_screen);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            Intent intentMainAct = new Intent(NotificationScreen.this, MainActivity.class);
        }
        UID = user.getUid();
        mList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mList);
        db = FirebaseFirestore.getInstance();
        listView = findViewById(R.id.listViewNotifications);
        listView.setAdapter(adapter);
        loadList();
    }

    private void loadList() {
        db.collection("users").document(UID).collection("notifications").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        mList.clear();
                        for(QueryDocumentSnapshot doc : task.getResult()) {
                            String curMessage = doc.getString("message");
                            mList.add(curMessage);
                        }
                        adapter.notifyDataSetChanged();
                        updateUnseenStatus();
                    }
                }
            });
    }

    //Update the unseen_notification status on database
    private void updateUnseenStatus() {
        //Adding the data to a hashmap
        Map<String, Object> unseenNotifications = new HashMap<>();
        unseenNotifications.put("unseen_notification", false);
        //Add this to the database
        db.collection("users").document(UID).update(unseenNotifications);
    }

    public void onClickGoBack(View view) {
        Intent intent = new Intent(NotificationScreen.this, MainHomepage.class);
        startActivity(intent);

    }

    /* Long click deletes notifications from the collection */
    /*public void onLongClickRemove(View view) {
        //Set up a long click listener to delete entries after long click
        private void setUpListViewListener() {
            lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    //Remove task from firebase
                    String keyToDelete = itemKeys.get(position);
                    DatabaseReference itemToDelete = tasksReference.child(keyToDelete);
                    itemToDelete.removeValue();
                    itemKeys.remove(position); //Remove key from itemKeys list
                    //Remove from GUI
                    items.remove(position); //Remove item from item list
                    itemsAdapter.notifyDataSetChanged();
                    return true;
                }
            });
    } */

}
