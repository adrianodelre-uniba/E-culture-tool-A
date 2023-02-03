package com.malfaang.e_culture_tool_a.routes;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.addElements.ImageSql;
import com.malfaang.e_culture_tool_a.newRoute.Area;
import com.malfaang.e_culture_tool_a.newRoute.Item;
import com.malfaang.e_culture_tool_a.newRoute.RecyclerViewClickInterface;
import com.malfaang.e_culture_tool_a.newRoute.Route;

import java.util.ArrayList;
//Classe per il recyclerview per la ricerca
public class RecyclerSearchAdapter extends RecyclerView.Adapter<RecyclerSearchAdapter.ViewHolder>  implements Filterable {

    private static final String TAG = "RecyclerAdapter";
    private ArrayList<Route> routelist;
    private RecyclerViewClickInterface ClickInterface;
    private ArrayList<Route> routeList;
    private ArrayList<Area> areaList;
    private ArrayList<Item> itemList;
    private ArrayList<ImageSql> imageList;
    private String tipologia;
    private ArrayList<Route> filteredData_Route;
    private ArrayList<Area> filteredData_Area;
    private ArrayList<Item> filteredData_Item;
    private ArrayList<ImageSql> filteredData_Item_img;

    private ItemFilter mFilter = new ItemFilter();

    String Percorsi, Zone, Oggetti;

    public RecyclerSearchAdapter(ArrayList<Route> routeList, ArrayList<Area> areaList, ArrayList<Item> itemList, ArrayList<ImageSql> imageList, String tipologia, RecyclerViewClickInterface ClickInterface) {

        this.ClickInterface=ClickInterface;
        this.routeList = routeList;
        this.areaList = areaList;
        this.itemList = itemList;
        this.tipologia = tipologia;
        this.imageList = imageList;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_route, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);


        Percorsi = viewHolder.itemView.getResources().getString(R.string.Route_Title);
        Zone = viewHolder.itemView.getResources().getString(R.string.Area_Title);
        Oggetti = viewHolder.itemView.getResources().getString(R.string.Item_Title);


        if(tipologia.equals(Percorsi)){
            this.filteredData_Route = routeList;
        }else{
            if(tipologia.equals(Zone)){
                this.filteredData_Area = areaList;
            }else{
                if(tipologia.equals(Oggetti)){
                    this.filteredData_Item = itemList;
                    this.filteredData_Item_img = imageList;
                }

            }
        }

        return viewHolder;
    }
    //assegnazione del valore nelle variabili
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        if(tipologia.equals(Percorsi) && filteredData_Route.size() > 0){
            //Titolo Percorso
            holder.titleTextView.setText(filteredData_Route.get(position).getRouteTitle());
            //Descrizione Percorso
            holder.descriptionTextView.setText(filteredData_Route.get(position).getRouteDescr());

            //Data Percorso
            String[] date = filteredData_Route.get(position).getData_creazione().split(" ");
            holder.creationdateTextView.setText(date[0]);
            //Immagine Percorso ::> Da aggiungere
        }

        if(tipologia.equals(Zone) && filteredData_Area.size() > 0 &&filteredData_Area !=  null){
            //Titolo Area
            holder.titleTextView.setText(filteredData_Area.get(position).getAreaTitle());
            //Descrizione Area
            holder.descriptionTextView.setText(filteredData_Area.get(position).getAreaDescr());

            //Tipologia Area
            holder.creationdateTextView.setText(filteredData_Area.get(position).getAreaTypology());
            //Immagine Percorso ::> Da aggiungere
            holder.creationyTextView.setVisibility(View.INVISIBLE);
        }

        if(tipologia.equals(Oggetti) && filteredData_Item.size() > 0){
            //Titolo Item
            holder.titleTextView.setText(filteredData_Item.get(position).getItemTitle());
            //Descrizione Item
            holder.descriptionTextView.setText(filteredData_Item.get(position).getItemDescr());

            //Tipologia Item
            holder.creationdateTextView.setText(filteredData_Item.get(position).getItemTypology());
            //Immagine Percorso ::> Da aggiungere
            holder.creationyTextView.setVisibility(View.INVISIBLE);

            holder.pic.setImageBitmap(imageList.get(position).getItem_imgSql());
        }
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView pic;
        TextView titleTextView;
        TextView descriptionTextView;
        TextView creationdateTextView;
        TextView creationyTextView;
        //Interfaccia dove è stato cliccato l'oggetto
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pic = itemView.findViewById(R.id.route_pic);
            titleTextView = itemView.findViewById(R.id.route_title);
            descriptionTextView = itemView.findViewById(R.id.route_description);
            creationdateTextView = itemView.findViewById(R.id.Creation_date);
            creationyTextView = itemView.findViewById(R.id.Creation);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!ClickInterface.toString().contains("com.example.prvaloocale.ui.Routes.Add_Area_To_Route")){
                        ClickInterface.onItemClick(getAdapterPosition());
                    }
                }
            });


        }

    }
    public Filter getFilter(){
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            if(tipologia.equals(Percorsi)){
                final ArrayList<Route> nlistRoute =  search_route(filterString);
                results.values = nlistRoute;
                results.count = nlistRoute.size();
            }else{
                if(tipologia.equals(Zone)){
                    final ArrayList<Area> nlistArea =  search_area(filterString);
                    results.values = nlistArea;
                    results.count = nlistArea.size();
                }else{
                    if(tipologia.equals(Oggetti)){
                        final ArrayList<Item> nlistItem =  search_item(filterString);
                        results.values = nlistItem;
                        results.count = nlistItem.size();
                    }
                }
            }



            return results;
        }

        private ArrayList<Item> search_item(String filterString) {
            final ArrayList<Item> list_item = itemList;

            int count = list_item.size();

            //Lista in cui aggiungo i percorsi che corrispondono a quello cercato
            final ArrayList<Item> nlist = new ArrayList<Item>(count);
            nlist.clear();
            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list_item.get(i).getItemTitle().toString();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list_item.get(i));
                }
            }
            return nlist;
        }

        private ArrayList<Area> search_area(String filterString) {
            final ArrayList<Area> list_area = areaList;

            int count = list_area.size();

            //Lista in cui aggiungo i percorsi che corrispondono a quello cercato
            final ArrayList<Area> nlist = new ArrayList<Area>(count);
            nlist.clear();
            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list_area.get(i).getAreaTitle().toString();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list_area.get(i));
                }
            }
            return nlist;
        }

        private ArrayList<Route> search_route(String filterString) {

            final ArrayList<Route> list_route = routeList;

            int count = list_route.size();

            //Lista in cui aggiungo i percorsi che corrispondono a quello cercato
            final ArrayList<Route> nlist = new ArrayList<Route>(count);
            nlist.clear();
            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list_route.get(i).getRouteTitle().toString();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list_route.get(i));
                }
            }
            return nlist;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if(results.count == 0){
                filteredData_Route.clear();
                filteredData_Area.clear();
                filteredData_Item.clear();
                //notifyDataSetInvalidated();
            }else{

                if(tipologia.equals(Percorsi)){
                    filteredData_Route = (ArrayList<Route>) results.values;
                }else{
                    if(tipologia.equals(Zone)){
                        filteredData_Area = (ArrayList<Area>) results.values;

                    }else{
                        if(tipologia.equals(Oggetti)){
                            filteredData_Item = (ArrayList<Item>) results.values;
                        }
                    }
                }
                notifyDataSetChanged();
            }



        }

    }
}
