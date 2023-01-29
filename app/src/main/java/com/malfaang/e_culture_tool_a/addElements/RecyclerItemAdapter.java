package com.malfaang.e_culture_tool_a.addElements;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.newRoute.Item;
import com.malfaang.e_culture_tool_a.newRoute.RecyclerViewClickInterface;

import java.util.ArrayList;
//Classe per il recyclerview dell'item
public class RecyclerItemAdapter extends RecyclerView.Adapter<RecyclerItemAdapter.ViewHolder>  {

    private static final String TAG = "RecyclerAdapter";
    private ArrayList<Item> itemlist;
    ArrayList<ImageSql> imageSql;
    private RecyclerViewClickInterface clickInterface;

    public RecyclerItemAdapter(ArrayList<Item> itemlist,ArrayList<ImageSql> imageSql, RecyclerViewClickInterface clickInterface) {
        this.itemlist = itemlist;
        this.imageSql = imageSql;
        this.clickInterface = clickInterface;
    }
    //assegnazione del valore nelle variabili
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTextView.setText(itemlist.get(position).getItemTitle());
        holder.descriptionTextView.setText(itemlist.get(position).getItemDescr());
        holder.tipologyTextView.setText(itemlist.get(position).getItemTypology());
        holder.pic.setImageBitmap(imageSql.get(position).getItemImgSql());
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView pic;
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView tipologyTextView;
        //Collegamento con oggetti grafici
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pic = itemView.findViewById(R.id.route_pic);
            titleTextView = itemView.findViewById(R.id.route_title);
            descriptionTextView = itemView.findViewById(R.id.route_description);
            tipologyTextView = itemView.findViewById(R.id.typology);


//Interfaccia dove è stato cliccato l'oggetto
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!clickInterface.toString().contains("com.example.prvaloocale.ui.Routes.Add_Area_To_Route")){
                        clickInterface.onItemClick(getAdapterPosition());
                    }
                }
            });


        }

    }
}
