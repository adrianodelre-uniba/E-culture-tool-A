package com.malfaang.e_culture_tool_a.routes;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.newRoute.RecyclerViewClickInterface;
import com.malfaang.e_culture_tool_a.newRoute.Route;

import java.util.ArrayList;

//Classe per il recyclerview dei percorsi
public class RecyclerRouteAdapter extends RecyclerView.Adapter<RecyclerRouteAdapter.ViewHolder> {

    private static final String TAG = "RecyclerAdapter";
    private ArrayList<Route> routelist;
    private RecyclerViewClickInterface ClickInterface;

    public RecyclerRouteAdapter(ArrayList<Route> routelist, RecyclerViewClickInterface ClickInterface) {
        this.routelist = routelist;
        this.ClickInterface=ClickInterface;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_route, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    //assegnazione del valore nelle variabili
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTextView.setText(routelist.get(position).getRouteTitle());
        holder.descriptionTextView.setText(routelist.get(position).getRouteDescr());

        String[] date = routelist.get(position).getData_creazione().split(" ");
        holder.creationDateTextView.setText(date[0]);
    }

    @Override
    public int getItemCount() {
        return routelist.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView pic;
        TextView titleTextView;
        TextView descriptionTextView;
        TextView creationDateTextView;
        //Collegamento con oggetti grafici
        public ViewHolder(@NonNull View routeView) {
            super(routeView);

            pic = routeView.findViewById(R.id.route_pic);
            titleTextView = routeView.findViewById(R.id.route_title);
            descriptionTextView = routeView.findViewById(R.id.route_description);
            creationDateTextView = routeView.findViewById(R.id.Creation_date);


//Interfaccia dove è stato cliccato l'oggetto
            routeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClickInterface.onItemClick(getAdapterPosition());
                }
            });


        }

    }
}
