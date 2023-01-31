package com.malfaang.e_culture_tool_a.newRoute;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.addElements.ImageSql;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<Item> {
    private ArrayList<Item> itemlist;

    private ArrayList<ImageSql> imageSqlList;
    private StorageReference storage;

    public ItemAdapter(@NonNull Context context, int resource, ArrayList<Item> itemlist,ArrayList<ImageSql> imageSqlList) {
        super(context, resource,itemlist);
        this.itemlist = itemlist;

        this.imageSqlList = imageSqlList;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int phraseIndex = position;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,
                    parent, false);
        }
//Collegamento agli oggetti
        ImageView pic = convertView.findViewById(R.id.route_pic);
        TextView titleTextView = convertView.findViewById(R.id.route_title);
        TextView descriptionTextView = convertView.findViewById(R.id.route_description);
        TextView typologyTextView = convertView.findViewById(R.id.typology);
//Settaggio delle informazioni negli oggetti
        titleTextView.setText(itemlist.get(position).getItemTitle());
        descriptionTextView.setText(itemlist.get(position).getItemDescr());
        typologyTextView.setText(itemlist.get(position).getItemTypology());



        return convertView;
    }



}
