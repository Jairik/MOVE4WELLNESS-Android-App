package com.example.move4wellness;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/* Custom List Adapter
Description: Class that adapts the simple list view into a list view
that holds pictures, a title, and subtitle. Used for the AllUserActivities page */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> mainTitle;
    private final List<String> subTitle;
    private final List<Integer> imageArray;

    public CustomListAdapter(Activity context, List<String> mainTitle, List<String> subTitle, List<Integer> imageArray) {
        super(context, R.layout.custom_list, mainTitle);
        this.context = context;
        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
        this.imageArray = imageArray;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_list, null, true);

        TextView titleText = rowView.findViewById(R.id.title);
        TextView subtitleText = rowView.findViewById(R.id.subtitle);
        ImageView imageView = rowView.findViewById(R.id.image);

        titleText.setText(mainTitle.get(position));
        subtitleText.setText(subTitle.get(position));
        imageView.setImageResource(imageArray.get(position));

        return rowView;
    }
}
