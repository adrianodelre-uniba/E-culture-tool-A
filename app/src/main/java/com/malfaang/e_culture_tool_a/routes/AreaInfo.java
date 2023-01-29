package com.malfaang.e_culture_tool_a.routes;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.malfaang.e_culture_tool_a.Database;
import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.addElements.ImageSql;
import com.malfaang.e_culture_tool_a.addElements.RecyclerItemAdapter;
import com.malfaang.e_culture_tool_a.newRoute.Area;
import com.malfaang.e_culture_tool_a.newRoute.Item;
import com.malfaang.e_culture_tool_a.newRoute.RecyclerViewClickInterface;
import com.malfaang.e_culture_tool_a.newRoute.Route;

import java.util.ArrayList;

public class AreaInfo extends AppCompatActivity implements RecyclerViewClickInterface {
    private Bundle extras;
    private String areaName;
    private String idAreaSite;
    private String idArea;
    private Area areaSelected;
    private Database localDB;
    private ArrayList<Route> routeList;
    private RecyclerRouteAdapter adapterRoute;
    private RecyclerView recyclerViewRoute;
    private ArrayList<Item> itemList;
    private RecyclerItemAdapter adapterItem;
    private RecyclerView recyclerViewItem;
    private ArrayList<ImageSql> imageList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_info);
        localDB = new Database(getApplicationContext());
        routeList = new ArrayList<>();
        itemList = new ArrayList<>();
        imageList = new ArrayList<ImageSql>();
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        extras = getIntent().getExtras();
        //Controllo della presenza delle chiavi
        if(extras.containsKey("area") && extras.containsKey("key_area")) {
            areaSelected = (Area) getIntent().getSerializableExtra("area");
            areaName = areaSelected.getAreaTitle();
            idArea = extras.getString("key_area");
            idAreaSite = areaSelected.getArea_id_sito();
            ab.setTitle(areaName);
            ab.setSubtitle(R.string.Area_Title);
        }
        //Collegamento degli oggetti
        TextView tvTitolo = findViewById(R.id.Titolo_Area);
        TextView tvTipologia = findViewById(R.id.Tipologia_Area);
        TextView tvDescrizione = findViewById(R.id.Descrizione_Area);
        tvTitolo.setText(areaSelected.getAreaTitle());
        tvTipologia.setText(areaSelected.getAreaTypology());
        tvDescrizione.setText(areaSelected.getAreaDescr());
        localRouteItem(idArea);
        ImageView img = findViewById(R.id.IMG_Area);
        img.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.logo_app));
        if(itemList != null){
            adapterItem = new RecyclerItemAdapter( itemList,imageList, this);
            recyclerViewItem = findViewById(R.id.itemListArea);
            recyclerViewItem.setNestedScrollingEnabled(false);
            recyclerViewItem.setAdapter(adapterItem);
        }
        localRoute();
        if(routeList != null){
            adapterRoute = new RecyclerRouteAdapter(routeList,this);
            recyclerViewRoute = findViewById(R.id.routeListArea);
            recyclerViewRoute.setNestedScrollingEnabled(false);
            recyclerViewRoute.setAdapter(adapterRoute);
        }
    }

    //Lettura dei percorsi
    private void localRoute(){
        Cursor cursor= localDB.readAllRouteArea(Integer.parseInt(idArea));
        routeList.clear();
        String titolo;
        String descrizione;
        String tipologia;
        String idSito;
        String dataCreazione;
        String keyRoute;
        String back;
        boolean goBack;
        int j = 0;
        if(cursor.moveToFirst()) {
            do {
                titolo = cursor.getString(1);
                descrizione = cursor.getString(2);
                tipologia = cursor.getString(3);
                keyRoute = cursor.getString(4);
                idSito = cursor.getString(5);
                dataCreazione = cursor.getString(6);
                back = cursor.getString(7);
                if (back.equals("true")){
                    goBack=true;
                }else{
                    goBack=false;
                }
                routeList.add(new Route(titolo, descrizione, tipologia,keyRoute,idSito,dataCreazione,goBack));
                cursor.moveToNext();
                j++;
            } while (!cursor.isAfterLast());
        }


    }
    //Lettura degli item
    private void localRouteItem(String idArea){
        String titolo;
        String descrizione;
        String tipologia;
        String idSito;
        String idZona;
        String id;
        Cursor cursor= localDB.readItemByArea(idArea);
        byte[] foto;
        if(cursor.moveToFirst() ) {
            do {
                id = cursor.getString(0);
                titolo = cursor.getString(1);
                tipologia = cursor.getString(2);
                descrizione = cursor.getString(3);
                idSito = cursor.getString(5);
                idZona = cursor.getString(6);
                foto=cursor.getBlob(4);
                Bitmap itemFoto= getImage(foto);
                itemList.add(new Item(titolo, descrizione, tipologia,idZona,idSito));
                imageList.add(new ImageSql(itemFoto,id));
                cursor.moveToNext();
            } while (!cursor.isAfterLast() );
        }
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        return getParentActivityIntentImplement();
    }

    private Intent getParentActivityIntentImplement() {
        Bundle extras = getIntent().getExtras();
        Intent intent2 = getIntent();
        Intent intent = null;
        String goToIntent = extras.getString("goto");
        if(goToIntent.equals("Show_Route")){
            intent = new Intent(this, Show_Route.class);
            intent.putExtra("key_route",extras.getString("key_route")); //Percorso
            intent.putExtra("areas_add_to_route",  (ArrayList<Area>) intent2.getSerializableExtra("areas_add_to_route")); //aree aggiunte al percorso
            intent.putExtra("id_zona1",(String[]) intent2.getSerializableExtra("id_zona1")); //id_zona1
            intent.putExtra("id_zona2",(String[]) intent2.getSerializableExtra("id_zona2")); //id_zona2
            intent.putExtra("id_zone",(String[]) intent2.getSerializableExtra("id_zone"));
            intent.putExtra("key_route_name", extras.getString("key_route_name"));
            intent.putExtra("key_route",extras.getString("key_route"));
            intent.putExtra("route",  (Route) getIntent().getSerializableExtra("route"));
            intent.putExtra("key_route_fire",  extras.getString("key_route_fire"));
        }else{
            intent = new Intent(this, Add_Route.class);
        }
        return intent;
    }


    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        return getParentActivityIntentImplement();
    }

    @Override
    public void onItemClick(int position) {
    }

    @Override
    public void onLongItemClick(int position) {
    }
}
