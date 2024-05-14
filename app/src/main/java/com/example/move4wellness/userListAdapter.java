package com.example.move4wellness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/* User List Adapter
Description: Not used, however easily expandable to make it work.
Intended to clean up the list view for the users on the manager side */

public class userListAdapter extends ArrayAdapter<String> {

    private List<String> itemList;
    private List<String> fullList;
    private int resource;

    public userListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.resource = resource;
        this.itemList = new ArrayList<>();
        this.fullList = new ArrayList<>();
    }

    public userListAdapter(@NonNull Context context, int resource, List<String> names) {
        super(context, resource, names);
        this.resource = resource;
        this.itemList = new ArrayList<>(names);
        this.fullList = new ArrayList<>(names);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return itemList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        String name = getItem(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(name);

        return convertView;
    }

    public void setFilteredList(List<String> filteredList) {
        this.itemList = filteredList;
        notifyDataSetChanged();
    }

    public void filter(String query) {
        query = query.toLowerCase().trim();
        List<String> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            filteredList.addAll(fullList);
        } else {
            for (String name : fullList) {
                if (name.toLowerCase().contains(query)) {
                    filteredList.add(name);
                }
            }
        }

        setFilteredList(filteredList);
    }
}
