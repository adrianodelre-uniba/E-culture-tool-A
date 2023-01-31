package com.malfaang.e_culture_tool_a.routes;


import static com.malfaang.e_culture_tool_a.routes.Add_Area_To_Route.getImage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.malfaang.e_culture_tool_a.Database;
import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.addElements.ImageSql;
import com.malfaang.e_culture_tool_a.newRoute.Area;
import com.malfaang.e_culture_tool_a.newRoute.Item;
import com.malfaang.e_culture_tool_a.newRoute.Route;
import com.malfaang.e_culture_tool_a.newRoute.RouteAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Add_Route extends AppCompatActivity {

    private ListView listView_Route;
    private ArrayList<Route> routeList;
    private ArrayList<Area> areaList;
    private ArrayList<Item> itemList;
    private ArrayList<ImageSql> imageList;
    public RouteAdapter adapter;
    public SearchAdapter adapter_search;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    private String[] ID_ROUTE= new String[MAX_ROUTE];
    private String[] ID_AREA= new String[MAX_ROUTE];
    private String[] ID_ITEM= new String[MAX_ROUTE];
    private static final int MAX_ROUTE = 90;
    private Database LocalDB;
    String id_sito;
    String id_utente;
    String nome_sito;
    String tipologia_utente;
    String tipologia ;
    ActionBar ab;
    String Percorsi;
    String Zone;
    String Oggetti;
    String fall;
    String presen;
    boolean check_routes=false;

    public static final String PREFS_NAME = "MyPrefsFile";
    //Creazione del layout con collegamento agli oggetti, apertura dei db, inizializzazione degli ArrayList
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_route);

        Percorsi = getResources().getString(R.string.Route_Title);
        Zone =getResources().getString(R.string.Area_Title);
        Oggetti = getResources().getString(R.string.Item_Title);
        fall = getResources().getString(R.string.fallito);
        presen = getResources().getString(R.string.Presenza_non);

        SharedPreferences preference = getApplicationContext().getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("Tipologia_Ricerca", getResources().getString(R.string.Route_Title));
        editor.commit();

        nome_sito = preference.getString("Nome_Sito", "");
        id_sito = preference.getString("Id_Sito", "");
        tipologia_utente = preference.getString("Tipologia_Utente", "");
        id_utente = preference.getString("Id_Utente", "");

        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(nome_sito);
        ab.setSubtitle(R.string.Route_Title);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Percorsi");
        routeList = new ArrayList<>();
        areaList = new ArrayList<>();
        itemList = new ArrayList<>();
        imageList = new ArrayList<>();
        LocalDB= new Database(getApplicationContext());
        //Lettura da locale
        Local_route();
        Local_area();
        Local_item();
        listView_Route = findViewById(R.id.routeList);


        if(!routeList.isEmpty()){
            check_routes=true;
            adapter = new RouteAdapter(getApplicationContext(),
                    R.layout.list_route, routeList);

            listView_Route.setAdapter(adapter);

            listView_Route.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    SharedPreferences preference = getApplicationContext().getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
                    String Tipologia_Ricerca = preference.getString("Tipologia_Ricerca", "");
                    String id="";
                    if(Tipologia_Ricerca.equals(Percorsi)){
                        Intent intent_route = new Intent(getApplicationContext(),   Add_Area_To_Route.class);
                        intent_route.putExtra("key_route_name",routeList.get(i).getRouteTitle().toString());
                        intent_route.putExtra("key_route",ID_ROUTE[i]);
                        intent_route.putExtra("route",routeList.get(i));
                        intent_route.putExtra("key_route_fire",routeList.get(i).getKey_route().toString());
                        Cursor cursor=LocalDB.getidRoute(adapter_search.getRoute(i).getKey_route());


                        if (cursor.moveToFirst()){

                             id= cursor.getString(0);
                        }


                        startActivity(intent_route);
                    }else{
                        if(Tipologia_Ricerca.equals(Zone)){
                            Intent intent_Area = new Intent(getApplicationContext(),   AreaInfo.class);
                            intent_Area.putExtra("key_area",ID_AREA[i]);
                            intent_Area.putExtra("area",areaList.get(i));
                            intent_Area.putExtra("goto","Add_Route");
                            startActivity(intent_Area);
                        }else{
                            if(Tipologia_Ricerca.equals(Oggetti)){
                                Intent intent_Item = new Intent(getApplicationContext(),   Item_Info.class);
                                intent_Item.putExtra("key_item",ID_ITEM[i]);
                                intent_Item.putExtra("item", itemList.get(i));
                                intent_Item.putExtra("item_img", imageList.get(i).getItemImgSql());
                                startActivity(intent_Item);
                            }
                        }
                    }

                }
            });

            listView_Route.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    showDialog_update_or_delete(i);
                    return true;
                }
            });

        }else{


            setContentView(R.layout.activity_no_route);
            TextView tv_no_route = findViewById(R.id.None_route);
            tv_no_route.setText(getResources().getString(R.string.None_route_in_the_list));
            Button bt_no_route = findViewById(R.id.add_route);

            bt_no_route.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog_Add_Route();
                }
            });


        }
    }

    private void showDialog_update_or_delete(int position) {
        LayoutInflater inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.alert_dialog_update_or_delete,null);

        TextView tv_edit_or_delete = view.findViewById(R.id.ad_title);
        Button editButton = view.findViewById(R.id.ad_button_edit);
        Button deleteButton = view.findViewById(R.id.ad_button_delete);

        tv_edit_or_delete.setText(routeList.get(position).getRouteTitle());

        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogEditRoute(position);
                alertDialog.cancel();
                }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Route deletedRoute = routeList.get(position);

                routeList.remove(position);
                listView_Route.setAdapter(adapter);

                Snackbar snackbar = Snackbar.make(listView_Route, deletedRoute.getRouteTitle() + presen, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                routeList.add(position, deletedRoute);
                                listView_Route.setAdapter(adapter);
                                //adapter.notifyItemInserted(position);
                                //DeleteFirebase e aggiornamento ordini zone


                            }
                        }).addCallback(new Snackbar.Callback(){

                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);

                                if (event == DISMISS_EVENT_TIMEOUT) {

                                    if (event == DISMISS_EVENT_TIMEOUT) {

                                        LocalDB.delete_Route_ordine_zon(ID_ROUTE[position]);
                                        LocalDB.delete_Route(id_sito,ID_ROUTE[position]);


                                        mDatabase2=FirebaseDatabase.getInstance().getReference().child("Ordine_Zona");

                                        mDatabase2.orderByChild("idPercorso").equalTo(deletedRoute.getKey_route()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String key_ord = null;
                                                for(DataSnapshot snapshot1: snapshot.getChildren()){
                                                    key_ord = snapshot1.getKey();
                                                    mDatabase2.child(""+key_ord).removeValue();
                                                }

                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        mDatabase.child(""+deletedRoute.getKey_route().toString()).removeValue();
                                        Local_route();
                                        Local_area();
                                        Local_item();

                                    }
                                }
                            }
                        });
                snackbar.show();
                alertDialog.cancel();
            }
        });

        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }


    private void showDialogEditRoute(int position) {
        //Apertura del layout  e cambiamento dei dati con gli item, dopo che essi vengono letti da sql
        LayoutInflater inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.alert_dialog_add_new_route,null);

        Button acceptButton = view.findViewById(R.id.ad_button_do);
        EditText et1 = view.findViewById(R.id.routeName_alert_dialog);
        EditText et2 = view.findViewById(R.id.routeDescr_alert_dialog);
        EditText et3 = view.findViewById(R.id.routeTypology_alert_dialog);
        Switch goback = view.findViewById(R.id.switch_back);

        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();

        et1.setText(routeList.get(position).getRouteTitle());
        et2.setText(routeList.get(position).getRouteDescr());
        et3.setText(routeList.get(position).getRouteTypology());
        goback.setChecked(routeList.get(position).getgo_back());

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String route_name = et1.getText().toString();
                String route_descr = et2.getText().toString();
                String route_tipology = et3.getText().toString();
                Boolean go_back=goback.isChecked();

                Calendar c = Calendar.getInstance();

                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());

                if(tipologia_utente.equals(getResources().getString(R.string.curatore))){

                    mDatabase.orderByKey().equalTo(routeList.get(position).getKey_route().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            mDatabase.child(""+routeList.get(position).getKey_route().toString()).child("routeDescr").setValue(route_descr);
                            mDatabase.child(""+routeList.get(position).getKey_route().toString()).child("routeTitle").setValue(route_name);
                            mDatabase.child(""+routeList.get(position).getKey_route().toString()).child("routeTypology").setValue(route_tipology);
                            mDatabase.child(""+routeList.get(position).getKey_route().toString()).child("data_creazione").setValue(formattedDate);
                            mDatabase.child(""+routeList.get(position).getKey_route().toString()).child("go_back").setValue(go_back);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                String result= LocalDB.update_Route(Integer.parseInt(ID_ROUTE[position]),route_name,route_descr,route_tipology,go_back,formattedDate);


                if(!result.equals("Success")){
                    Toast.makeText(getApplicationContext(), fall, Toast.LENGTH_SHORT).show();
                }else{
                    alertDialog.cancel();

                    Local_route();

                    if(check_routes == false){
                        startActivity(new Intent(getApplicationContext(),Add_Route.class));
                    }
                    adapter = new RouteAdapter(getApplicationContext(), R.layout.list_route, routeList);

                    listView_Route.setAdapter(adapter);

                }
            }
        });


        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        Local_route();
        Local_area();
        Local_item();

    }





    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.find);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                menu.findItem(R.id.add_new_route).setVisible(false);
            }
        });


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_route, menu);

        MenuItem menuItem = menu.findItem(R.id.find);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getString(R.string.find_dot));


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                menu.findItem(R.id.add_new_route).setEnabled(true).setVisible(true);
                listView_Route.setAdapter(adapter);
                supportInvalidateOptionsMenu();
                return false;
            }

        });

        SharedPreferences preference = getApplicationContext().getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        String Tipologia_Ricerca = preference.getString("Tipologia_Ricerca", "");
        adapter_search = new SearchAdapter(getApplicationContext(),
                R.layout.list_route, routeList, areaList, itemList,imageList,Tipologia_Ricerca);
        if(!routeList.isEmpty()){
            listView_Route.setAdapter(adapter_search);
        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                adapter_search.getFilter().filter(s);

                listView_Route.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        SharedPreferences preference = getApplicationContext().getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
                        String Tipologia_Ricerca = preference.getString("Tipologia_Ricerca", "");
                        String id="1";
                        if(Tipologia_Ricerca.equals(Percorsi)){
                            Intent intent_route = new Intent(getApplicationContext(),   Add_Area_To_Route.class);
                            intent_route.putExtra("key_route_name",adapter_search.getRoute(i).getRouteTitle());
                            Cursor cursor=LocalDB.getidRoute(adapter_search.getRoute(i).getKey_route());


                            if (cursor.moveToFirst()){

                                id= cursor.getString(0);
                            }

                            intent_route.putExtra("key_route",id);
                            intent_route.putExtra("route",adapter_search.getRoute(i));
                            intent_route.putExtra("key_route_fire",adapter_search.getRoute(i).getKey_route());
                            startActivity(intent_route);
                        }else{
                            if(Tipologia_Ricerca.equals(Zone)){
                                Local_area();
                                Intent intent_Area = new Intent(getApplicationContext(),   AreaInfo.class);

                                intent_Area.putExtra("key_area",""+adapter_search.getAreaid(i));
                                intent_Area.putExtra("area",adapter_search.getArea(i));
                                intent_Area.putExtra("goto","Add_Route");
                                startActivity(intent_Area);
                            }else{
                                if(Tipologia_Ricerca.equals(Oggetti)){
                                    Local_item();
                                    Intent intent_Item = new Intent(getApplicationContext(),   Item_Info.class);
                                    intent_Item.putExtra("key_item",adapter_search.getItemimg(i).getItem_id());
                                    Item itemselected= new Item(adapter_search.getItemimg(i).getItemTitle(),adapter_search.getItemimg(i).getItemDescr(),adapter_search.getItemimg(i).getItemTypology(),adapter_search.getItemimg(i).getItem_Zona(),adapter_search.getItemimg(i).getItem_Sito());
                                    intent_Item.putExtra("item", itemselected);
                                    Bitmap img= adapter_search.getItemimg(i).getItem_img();
                                    intent_Item.putExtra("item_img", img);
                                    startActivity(intent_Item);
                                }
                            }
                        }

                    }
                });

                listView_Route.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        showDialog_update_or_delete(i);
                        return true;
                    }
                });

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.add_new_route:
                showDialog_Add_Route();
                return true;

            case R.id.filters:
                showDialog_Filter_Search();
                return  true;



        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog_Filter_Search() {
        LayoutInflater inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.alert_dialog_add_filters,null);

        SharedPreferences preference = getApplicationContext().getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);

        Button acceptButton = view.findViewById(R.id.sd_button_do);
        RadioGroup Tipologia = view.findViewById(R.id.Tipologia_Ricerca);
        TextView closeTV = view.findViewById(R.id.close_show_dialog);
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();

        RadioButton radiobuttonRoute,radiobuttonArea,radiobuttonItem;
        radiobuttonRoute = view.findViewById(R.id.radio_percorso);
        radiobuttonArea = view.findViewById(R.id.radio_zona);
        radiobuttonItem = view.findViewById(R.id.radio_oggetto);

        SharedPreferences.Editor editor = preference.edit();
        String Tipologia_Ricerca = preference.getString("Tipologia_Ricerca", "");


        if(Tipologia_Ricerca.equals(Percorsi)){

            radiobuttonRoute.setChecked(true);
        }else{
            if(Tipologia_Ricerca.equals(Zone)){

                radiobuttonArea.setChecked(true);
            }else{
                if(Tipologia_Ricerca.equals(Oggetti)){
                    radiobuttonItem.setChecked(true);
                }else{
                    tipologia = Percorsi;
                    editor.putString("Tipologia_Ricerca",Percorsi);
                }

            }
        }

        Tipologia.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i){
                    case R.id.radio_percorso:
                        tipologia = Percorsi;

                        editor.putString("Tipologia_Ricerca",Percorsi);
                        editor.commit();
                        break;

                    case R.id.radio_zona:
                        tipologia = "Zone";
                        editor.putString("Tipologia_Ricerca",Zone);
                        editor.commit();
                        break;
                    case R.id.radio_oggetto:
                        tipologia = Oggetti;
                        editor.putString("Tipologia_Ricerca",Oggetti);
                        editor.commit();
                        break;
                }


            }
        });


        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Tipologia_Ricerca = preference.getString("Tipologia_Ricerca", "");

                adapter_search = new SearchAdapter(getApplicationContext(),
                        R.layout.list_route, routeList, areaList, itemList,imageList,Tipologia_Ricerca);
                listView_Route.setAdapter(adapter_search);
                ab.setSubtitle(Tipologia_Ricerca);
                alertDialog.cancel();
            }
        });

        closeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    //Aggiunta dei percorsi tramite l'apertura di un layout
    void showDialog_Add_Route(){


        LayoutInflater inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.alert_dialog_add_new_route,null);

        Button acceptButton = view.findViewById(R.id.ad_button_do);
        EditText et1 = view.findViewById(R.id.routeName_alert_dialog);
        EditText et2 = view.findViewById(R.id.routeDescr_alert_dialog);
        EditText et3 = view.findViewById(R.id.routeTypology_alert_dialog);
        Switch goback = view.findViewById(R.id.switch_back);

        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String route_name = et1.getText().toString();
                String route_descr = et2.getText().toString();
                String route_tipology = et3.getText().toString();
                String key_route = null;
                Boolean go_back=goback.isChecked();

                Calendar c = Calendar.getInstance();

                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());

                if(tipologia_utente.equals(getResources().getString(R.string.curatore))){

                    DatabaseReference ref = mDatabase.push();
                    key_route=ref.getKey();

                    ref.setValue(new Route(route_name,route_descr,route_tipology,key_route,id_sito,formattedDate,go_back));
                }else{
                    key_route="1";
                }

                String result= LocalDB.insertRoute(route_descr,route_tipology,route_name,key_route,id_sito,formattedDate,go_back);


                if(!result.equals("Success")){
                    Toast.makeText(getApplicationContext(), fall, Toast.LENGTH_SHORT).show();
                }else{
                    alertDialog.cancel();

                    Local_route();

                    if(check_routes == false){
                        startActivity(new Intent(getApplicationContext(),Add_Route.class));
                    }
                    adapter = new RouteAdapter(getApplicationContext(),
                            R.layout.list_route, routeList);

                    listView_Route.setAdapter(adapter);

                }
            }
        });


        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        Local_route();
        Local_area();
        Local_item();
    }



    //lettura dati dei percorsi
    private void Local_route(){
        Cursor cursor= LocalDB.readAllRoute(id_sito);
        routeList.clear();

        String titolo,descrizione,tipologia,id_sito,data_creazione,key_route;
        String back;
        Boolean go_back;
        int j=0;


        if(cursor.moveToFirst()) {

            do {

                ID_ROUTE[j] = cursor.getString(0);
                titolo = cursor.getString(1);
                descrizione = cursor.getString(2);
                tipologia = cursor.getString(3);
                key_route = cursor.getString(4);
                id_sito = cursor.getString(5);
                data_creazione = cursor.getString(6);
                back = cursor.getString(7);

                if (back.equals("true")){
                    go_back=true;
                }else{
                    go_back=false;
                }

                routeList.add(new Route(titolo, descrizione, tipologia,key_route,id_sito,data_creazione,go_back));
                cursor.moveToNext();
                j++;
            } while (!cursor.isAfterLast());
        }


    }
    //lettura dati delle aree
    private void Local_area(){
        Cursor cursor= LocalDB.readAllArea(id_sito);
        areaList.clear();

        String titolo,descrizione,tipologia,id_sito;
        int j=0;


        if(cursor.moveToFirst()) {

            do {

                ID_AREA[j] = cursor.getString(0);
                titolo = cursor.getString(1);
                descrizione = cursor.getString(2);
                tipologia = cursor.getString(3);
                id_sito = cursor.getString(4);


                areaList.add(new Area(titolo, descrizione, tipologia,id_sito));
                cursor.moveToNext();
                j++;
            } while (!cursor.isAfterLast());
        }


    }
    //lettura dati degli item
    private void Local_item(){
        Cursor cursor= LocalDB.readAllItem(id_sito);
        itemList.clear();

        String titolo,descrizione,tipologia,id_sito,id_zona;
        int j=0;
        byte[] foto;

        if(cursor.moveToFirst()) {

            do {

                ID_ITEM[j] = cursor.getString(0);
                titolo = cursor.getString(1);
                descrizione = cursor.getString(2);
                tipologia = cursor.getString(3);
                id_zona = cursor.getString(4);
                id_sito = cursor.getString(5);

                foto=cursor.getBlob(6);
                Bitmap item_foto= getImage(foto);

                itemList.add(new Item(titolo, descrizione, tipologia,id_zona,id_sito));

                imageList.add(new ImageSql(item_foto,ID_ITEM[j]));

                cursor.moveToNext();
                j++;
            } while (!cursor.isAfterLast());
        }


    }
}

