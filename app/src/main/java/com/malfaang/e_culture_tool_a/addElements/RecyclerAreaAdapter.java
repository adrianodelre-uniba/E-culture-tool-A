package com.malfaang.e_culture_tool_a.addElements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.newRoute.Area;
import com.malfaang.e_culture_tool_a.newRoute.RecyclerViewClickInterface;

import java.util.ArrayList;


//Classe per il recyclerview dell'area
public class RecyclerAreaAdapter extends RecyclerView.Adapter<RecyclerAreaAdapter.ViewHolder>  {
    private static final String TAG = "RecyclerAdapter";
    private ArrayList<Area> arealist;
    private RecyclerViewClickInterface clickInterface;

    public RecyclerAreaAdapter(ArrayList<Area> arealist,RecyclerViewClickInterface clickInterface) {
        this.arealist = arealist;
        this.clickInterface =clickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    //assegnazione del valore nelle variabili
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTextView.setText(arealist.get(position).getAreaTitle());
        holder.descriptionTextView.setText(arealist.get(position).getAreaDescr());
        holder.tipologyTextView.setText(arealist.get(position).getAreaTypology());
    }

    @Override
    public int getItemCount() {
        return arealist.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView pic;
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView tipologyTextView;
        private Bundle extras;

        //Collegamento con oggetti grafici
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.route_pic);
            titleTextView = itemView.findViewById(R.id.route_title);
            descriptionTextView = itemView.findViewById(R.id.route_description);
            tipologyTextView = itemView.findViewById(R.id.typology);

            //Interfaccia dove è stato cliccato l'oggetto
            itemView.setOnClickListener(view -> clickInterface.onItemClick(getAdapterPosition()));
        }
    }
}