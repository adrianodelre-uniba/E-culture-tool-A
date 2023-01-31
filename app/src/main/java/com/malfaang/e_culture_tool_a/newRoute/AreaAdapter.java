package com.malfaang.e_culture_tool_a.newRoute;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.malfaang.e_culture_tool_a.R;

import java.util.ArrayList;

public class AreaAdapter extends ArrayAdapter<Area> {
    private ArrayList<Area> arealist;

    public AreaAdapter(@NonNull Context context, int resource, ArrayList<Area> arealist) {
        super(context, resource,arealist);
        this.arealist = arealist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int phraseIndex = position;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,
                    parent, false);
        }
//Collegamento agli oggetti
        ImageView pic = convertView.findViewById(R.id.route_pic);
        TextView titleTextView = convertView.findViewById(R.id.route_title);
        TextView descriptionTextView = convertView.findViewById(R.id.route_description);
        TextView tipologyTextView = convertView.findViewById(R.id.route_description);
//Settaggio delle informazioni negli oggetti
        titleTextView.setText(arealist.get(position).getAreaTitle());
        descriptionTextView.setText(arealist.get(position).getAreaDescr());
        tipologyTextView.setText(arealist.get(position).getAreaTypology());
        return convertView;
    }
}
