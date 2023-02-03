package com.malfaang.e_culture_tool_a.routes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.addElements.ImageSql;
import com.malfaang.e_culture_tool_a.newRoute.Area;
import com.malfaang.e_culture_tool_a.newRoute.Item;
import com.malfaang.e_culture_tool_a.newRoute.Route;

import java.util.ArrayList;

public class SearchAdapter extends ArrayAdapter<Route> implements Filterable {
    private ArrayList<Route> routeList;
    private ArrayList<Area> areaList;
    private ArrayList<Item> itemList;
    private ArrayList<ImageSql> imageList;
    private String tipologia;
    private ArrayList<Route> filteredData_Route;
    private ArrayList<Area> filteredData_Area;
    private ArrayList<Item> filteredData_Item;
    private ArrayList<ImageSql> filteredData_Item_img;
    private ArrayList<ItemAndImage> item_with_img;
    private ArrayList<Integer> ID_AREA_list;


    private ItemFilter mFilter = new ItemFilter();

    String Percorsi, Zone, Oggetti;

    public SearchAdapter(@NonNull Context context, int resource, ArrayList<Route> routeList, ArrayList<Area> areaList, ArrayList<Item> itemList, ArrayList<ImageSql> imageList, String tipologia) {
        super(context, resource,routeList);
        this.routeList = routeList;
        this.areaList = areaList;
        this.itemList = itemList;
        this.tipologia = tipologia;
        this.imageList = imageList;

         Percorsi = getContext().getResources().getString(R.string.Route_Title);
         Zone = getContext().getResources().getString(R.string.Area_Title);
         Oggetti = getContext().getResources().getString(R.string.Item_Title);

         item_with_img= new ArrayList<ItemAndImage>();
        ID_AREA_list= new ArrayList<Integer>();

        if(tipologia.equals(Percorsi)){
            this.filteredData_Route = routeList;
        }else{
            if(tipologia.equals(Zone)){
                this.filteredData_Area = areaList;
            }else{
                if(tipologia.equals(Oggetti)){
                    this.filteredData_Item = itemList;
                    this.filteredData_Item_img = imageList;
                    for (int i=0 ; i<itemList.size();i++){
                        item_with_img.add(new ItemAndImage(itemList.get(i).getItemTitle(),itemList.get(i).getItemDescr(),itemList.get(i).getItemTypology(),itemList.get(i).getItem_Zona(),itemList.get(i).getItem_Sito(),
                                filteredData_Item_img.get(i).getItem_imgSql(),filteredData_Item_img.get(i).getItem_id()));
                    }
                }

            }
        }


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int phraseIndex = position;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_route,
                    parent, false);
        }
//Collegamento con gli oggetti
        ImageView pic = convertView.findViewById(R.id.route_pic);
        TextView titleTextView = convertView.findViewById(R.id.route_title);
        TextView descriptionTextView = convertView.findViewById(R.id.route_description);
        TextView creationdateTextView = convertView.findViewById(R.id.Creation_date);

        TextView creationyTextView = convertView.findViewById(R.id.Creation);
        if(tipologia.equals(Percorsi) && filteredData_Route.size() > 0){
            //Titolo Percorso
            titleTextView.setText(filteredData_Route.get(position).getRouteTitle());
            //Descrizione Percorso
            descriptionTextView.setText(filteredData_Route.get(position).getRouteDescr());

            //Data Percorso
            String[] date = filteredData_Route.get(position).getData_creazione().split(" ");
            creationdateTextView.setText(date[0]);

        }

        if(tipologia.equals(Zone) && filteredData_Area.size() > 0){
            //Titolo Area
            titleTextView.setText(filteredData_Area.get(position).getAreaTitle());
            //Descrizione Area
            descriptionTextView.setText(filteredData_Area.get(position).getAreaDescr());

            //Tipologia Area
            creationdateTextView.setText(filteredData_Area.get(position).getAreaTypology());

            creationyTextView.setVisibility(View.INVISIBLE);
        }

        if(tipologia.equals(Oggetti) && item_with_img.size() > 0){
            //Titolo Item
            titleTextView.setText(item_with_img.get(position).getItemTitle());
            //Descrizione Item
            descriptionTextView.setText(item_with_img.get(position).getItemDescr());

            //Tipologia Item
            creationdateTextView.setText(item_with_img.get(position).getItemTypology());

            creationyTextView.setVisibility(View.INVISIBLE);

            pic.setImageBitmap(item_with_img.get(position).getItem_img());


        }

        return convertView;
    }

    public int getCount() {

        if(tipologia.equals(Percorsi)){
            return filteredData_Route.size();
        }else{
            if(tipologia.equals(Zone)){
                return filteredData_Area.size();
            }else{
                if(tipologia.equals(Oggetti)){
                    return item_with_img.size();
                }else{
                    return  0;
                }
            }
        }

    }

    public Route getRoute(int position) {
        return filteredData_Route.get(position);
    }

    public ItemAndImage getItemimg(int position) {
        return item_with_img.get(position);
    }


    public Area getArea(int position) {
        return filteredData_Area.get(position);
    }

    public int getAreaid(int position){return  ID_AREA_list.get(position);}


    public long getItemId(int position) {
        return position;
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
                    ID_AREA_list.clear();
                    final ArrayList<Area> nlistArea =  search_area(filterString);
                    results.values = nlistArea;
                    results.count = nlistArea.size();
                }else{
                    if(tipologia.equals(Oggetti)){
                        final ArrayList<ItemAndImage> nlistItem =  search_item(filterString);


                        results.values = nlistItem;
                        results.count = nlistItem.size();


                    }
                }
            }



            return results;
        }
//Ricerca degli item
        private ArrayList<ItemAndImage> search_item(String filterString) {
            final ArrayList<Item> list_item = itemList;

            int count = list_item.size();

            //Lista in cui aggiungo i percorsi che corrispondono a quello cercato
            final ArrayList<ItemAndImage> nlist = new ArrayList<ItemAndImage>(count);

            nlist.clear();
            String filterableString;


            for (int i = 0; i < count; i++) {
                filterableString = list_item.get(i).getItemTitle().toString();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(new ItemAndImage(list_item.get(i).getItemTitle(),list_item.get(i).getItemDescr(),list_item.get(i).getItemTypology(),list_item.get(i).getItem_Zona(),list_item.get(i).getItem_Sito(),
                            filteredData_Item_img.get(i).getItem_imgSql(),filteredData_Item_img.get(i).getItem_id()));

                }
            }
            return nlist;
        }
//Ricerca dell'area
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
                    ID_AREA_list.add(i);
                }
            }
            return nlist;
        }
//Ricerca dei percorsi
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

        private ArrayList<ImageSql> search_item_img(String filterString) {
            final ArrayList<Item> list_item = itemList;

            int count = list_item.size();

            //Lista in cui aggiungo i percorsi che corrispondono a quello cercato
            final ArrayList<ImageSql> nlist = new ArrayList<ImageSql>(count);
            nlist.clear();
            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list_item.get(i).getItemTitle().toString();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filteredData_Item_img.get(i));
                }
            }
            return nlist;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {


            if(results.count == 0){
                if(tipologia.equals(Percorsi)){
                    filteredData_Route.clear();
                }else{
                    if(tipologia.equals(Zone)){
                        filteredData_Area.clear();

                    }else{
                        if(tipologia.equals(Oggetti)){
                            filteredData_Item.clear();
                        }
                    }
                }

                notifyDataSetInvalidated();
            }else{

                if(tipologia.equals(Percorsi)){
                    filteredData_Route = (ArrayList<Route>) results.values;
                }else{
                    if(tipologia.equals(Zone)){
                        filteredData_Area = (ArrayList<Area>) results.values;

                    }else{
                        if(tipologia.equals(Oggetti)){
                            item_with_img = (ArrayList<ItemAndImage>) results.values;
                        }
                    }
                }
                notifyDataSetChanged();
            }



        }

    }

}
