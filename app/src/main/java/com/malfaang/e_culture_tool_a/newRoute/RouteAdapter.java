package com.malfaang.e_culture_tool_a.newRoute;

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

import java.util.ArrayList;

public class RouteAdapter extends ArrayAdapter<Route> implements Filterable {
    private ArrayList<Route> routelist;
    private ArrayList<Route> filteredData;
    private ItemFilter mFilter = new ItemFilter();

    public RouteAdapter(@NonNull Context context, int resource, ArrayList<Route> routelist) {
        super(context, resource,routelist);
        this.routelist = routelist;
        this.filteredData = routelist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int phraseIndex = position;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_route,
                    parent, false);
        }
        ImageView pic = convertView.findViewById(R.id.route_pic);
        TextView titleTextView = convertView.findViewById(R.id.route_title);
        TextView descriptionTextView = convertView.findViewById(R.id.route_description);
        TextView typologyTextView = convertView.findViewById(R.id.Creation_date);
        if(filteredData.size() > 0){
            //Titolo Percorso
            titleTextView.setText(filteredData.get(position).getRouteTitle());
            //Descrizione Percorso
            descriptionTextView.setText(filteredData.get(position).getRouteDescr());
            //Data Percorso
            String[] date = filteredData.get(position).getData_creazione().split(" ");
            typologyTextView.setText(date[0]);
            //Immagine Percorso ::> Da aggiungere
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Route getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter(){
        return mFilter;
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final ArrayList<Route> list = routelist;
            int count = list.size();
            //Lista in cui aggiungo i percorsi che corrispondono a quello cercato
            final ArrayList<Route> nlist = new ArrayList<Route>(count);
            nlist.clear();
            String filterableString;
            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getRouteTitle().toString();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.count == 0){
                filteredData.clear();
                notifyDataSetInvalidated();
            }else{
                filteredData = (ArrayList<Route>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
