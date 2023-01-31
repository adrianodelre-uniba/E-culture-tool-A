package com.malfaang.e_culture_tool_a.routes;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.malfaang.e_culture_tool_a.Database;
import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.addElements.ImageSql;
import com.malfaang.e_culture_tool_a.addElements.RecyclerAreaAdapter;
import com.malfaang.e_culture_tool_a.addElements.RecyclerItemAdapter;
import com.malfaang.e_culture_tool_a.newRoute.Area;
import com.malfaang.e_culture_tool_a.newRoute.Item;
import com.malfaang.e_culture_tool_a.newRoute.Ordine_Zone;
import com.malfaang.e_culture_tool_a.newRoute.RecyclerViewClickInterface;
import com.malfaang.e_culture_tool_a.newRoute.Route;
import com.malfaang.e_culture_tool_a.login.FireCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.malfaang.e_culture_tool_a.newRoute.RecyclerViewClickInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Add_Area_To_Route extends AppCompatActivity implements RecyclerViewClickInterface {

    ArrayList<Ordine_Zone> ordine;
    ActionBar ab;
    Bundle extras;
    String errore;
    private Database LocalDB;
    private ArrayList<Area> areaList_Route;
    private ArrayList<Area> areaList_Site;
    private ArrayList<Area> areaList_OrderRoute;
    private ArrayList<Area> areaList_OrderZone;
    String ID_PERCORSO;
    String[] ID2 =new String[MAX_ROUTE];
    String[] ID_AREA_LOCALE =new String[MAX_ROUTE];
    String[] ID_AREA_FIRE =new String[MAX_ROUTE];
    String[] ID_AREA_LOCALE_Order =new String[MAX_ROUTE];
    String[] ID_AREA_LOCALE_Order1 =new String[MAX_ROUTE];
    String[] ID_AREA_LOCALE_Order2 =new String[MAX_ROUTE];
    private RecyclerAreaAdapter adapter;
    private RecyclerView recyclerView_Area;

    private static final int MAX_ROUTE = 90;
    String id_sito;
    String id_utente, id_percorso_fire;
    String nome_sito;
    String tipologia_utente;
    boolean[] checkedItems;
    boolean[] checkedItems_tmp;
    boolean[][] checkedOrderItems;
    DatabaseReference mDatabase,mDatabase2,mDatabase3;

    private ArrayList<Item> itemList;
    public RecyclerItemAdapter adapterItem;
    private RecyclerView recyclerView_Item;
    private ArrayList<ImageSql> imageList;

    Route route_selected;
    //Inizializzazioni delle variabili, collegamento ai dati su firebase
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        LocalDB= new Database(getApplicationContext());
        errore = getResources().getString(R.string.Errore_fb);
        mDatabase2 =FirebaseDatabase.getInstance().getReference().child("Zone");
        extras = getIntent().getExtras();
        areaList_Route = new ArrayList<>();
        areaList_Site = new ArrayList<>();
        areaList_OrderRoute= new ArrayList<>();
        areaList_OrderZone= new ArrayList<>();
        itemList = new ArrayList<>();
        imageList = new ArrayList<>();
        ordine= new ArrayList<>();


        mDatabase =FirebaseDatabase.getInstance().getReference().child("Ordine_Zona");
        mDatabase3 =FirebaseDatabase.getInstance().getReference();
        SharedPreferences preference = getApplicationContext().getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
        nome_sito = preference.getString("Nome_Sito", "");
        id_sito = preference.getString("Id_Sito", "");
        tipologia_utente = preference.getString("Tipologia_Utente", "");
        id_utente = preference.getString("Id_Utente", "");

        setContentView(R.layout.activity_route_config_areas);

        if(extras.containsKey("key_route_name")) {
            ab.setTitle(extras.getString("key_route_name"));
            ab.setSubtitle(R.string.Area_Title);
            route_selected = (Route) getIntent().getSerializableExtra("route");
            ID_PERCORSO=extras.getString("key_route");
            id_percorso_fire = extras.getString("key_route_fire");

        }

        TextView tv_Titolo = findViewById(R.id.Titolo_Route);
        TextView tv_Tipologia = findViewById(R.id.Tipologia_Route);
        TextView tv_Descrizione = findViewById(R.id.Descrizione_Route);


        tv_Titolo.setText(route_selected.getRouteTitle());
        tv_Tipologia.setText(route_selected.getRouteTypology());
        tv_Descrizione.setText(route_selected.getRouteDescr());

//Lettura da locale
        Local_route(id_sito);
        Local_orderRoute();
        checkedItems_tmp = new boolean[areaList_OrderRoute.size()];

        for (int i=0; i<areaList_Site.size();i++){
            for (int j=0; j<areaList_Site.size(); j++){
                if (ID_AREA_LOCALE[i].equals(ID_AREA_LOCALE_Order[j])){
                    checkedItems[i]=true;
                }
            }
        }

        adapter = new RecyclerAreaAdapter( areaList_OrderRoute,this);


        recyclerView_Area = findViewById(R.id.areaList);
        recyclerView_Area.setNestedScrollingEnabled(false);
        recyclerView_Area.setAdapter(adapter);


        itemList.clear();
        imageList.clear();
        for(int i=0; i<ID_AREA_LOCALE_Order.length; i++){
            Local_route_Item(ID_AREA_LOCALE_Order[i]);
        }

        if(itemList != null){
            adapterItem = new RecyclerItemAdapter( itemList,imageList, this);
            recyclerView_Item = findViewById(R.id.itemList);
            recyclerView_Item.setNestedScrollingEnabled(false);
            recyclerView_Item.setAdapter(adapterItem);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_to_route, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.add_new_route:
                if (areaList_Site.size()>0){
                    showDialog_Add_Area_To_Route();
                }else{
                    Toast.makeText(this, getResources().getString(R.string.fallito), Toast.LENGTH_SHORT).show();
                }

                return true;

            case R.id.show_route:
                Intent intent = new Intent(getApplicationContext(),   Show_Route.class);

                if (areaList_OrderRoute.size()>1){
                    intent.putExtra("key_route",ID_PERCORSO); //Percorso
                    intent.putExtra("areas_add_to_route", areaList_OrderRoute); //aree aggiunte al percorso
                    intent.putExtra("id_zona1",(Serializable) ID_AREA_LOCALE_Order1); //id_zona1
                    intent.putExtra("id_zona2",(Serializable) ID_AREA_LOCALE_Order2); //id_zona2
                    intent.putExtra("id_zone",(Serializable) ID_AREA_LOCALE_Order);

                    intent.putExtra("key_route_name",extras.getString("key_route_name"));
                    intent.putExtra("route",route_selected);
                    intent.putExtra("key_route_fire",id_percorso_fire);

                    startActivity(intent);
                }else{
                    Toast.makeText(this, getResources().getString(R.string.fallito), Toast.LENGTH_SHORT).show();
                }

                return true;

        }
        return super.onOptionsItemSelected(item);
    }
//Funzione per aggiungere il sito
    private void showDialog_Add_Area_To_Route() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Add_Area_To_Route.this, R.style.AlertDialogStyle);
        builder.setTitle("Aggiungi al Percorso");


        String[] Area = new String[areaList_Site.size()];
        for(int i = 0; i< areaList_Site.size(); i++){
            Area[i] = areaList_Site.get(i).getAreaTitle();
        }


        checkedItems_tmp = checkedItems.clone();


        builder.setMultiChoiceItems(Area, checkedItems_tmp, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                checkedItems_tmp[which] = isChecked;
            }
        });


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                areaList_Route.clear();
                areaList_OrderRoute.clear();


                LocalDB.delete_OrderZona(ID_PERCORSO);
                deleteFire(id_percorso_fire, new FireCallback() {
                    @Override
                    public void onCallback(String value) {
                        String result="";
                        int j=0;
                        for(int i = 0; i< areaList_Site.size(); i++){
                            if(checkedItems_tmp[i] == true){
                                j++;

                                mDatabase.push().setValue(new Ordine_Zone(id_percorso_fire,ID_AREA_FIRE[i],"",""));
                                LocalDB.insertRouteZone(ID_AREA_LOCALE[i],ID_PERCORSO,null,"");
                                areaList_Route.add(areaList_Site.get(i));
                                areaList_OrderRoute.add(areaList_Site.get(i));

                            }
                        }
                        Local_orderRoute();
                        for (int i=0; i<areaList_Site.size();i++){
                            for (int k=0; k<areaList_Site.size(); k++){
                                if (ID_AREA_LOCALE[i].equals(ID_AREA_LOCALE_Order[k])){
                                    checkedItems[i]=true;
                                }
                            }
                        }

                        adapter = new RecyclerAreaAdapter(areaList_OrderRoute,Add_Area_To_Route.this);
                        recyclerView_Area.setAdapter(adapter);
                        checkedItems= checkedItems_tmp.clone();

                        itemList.clear();
                        imageList.clear();

                        for(int i=0; i<ID_AREA_LOCALE_Order1.length; i++){

                            Local_route_Item(ID_AREA_LOCALE_Order1[i]);
                        }

                        adapterItem = new RecyclerItemAdapter( itemList,imageList, Add_Area_To_Route.this);
                        recyclerView_Item = findViewById(R.id.itemList);
                        recyclerView_Item.setAdapter(adapterItem);



                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.red_delete));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.color_Primary_500));
    }
//Lettura dei dati
    private void Local_route_Item(String ID_Area){
        String titolo;
        String descrizione;
        String tipologia;
        String id_sito;
        String id_zona;
        String id;

        Cursor cursor= LocalDB.readItemByArea(ID_Area);

        byte[] foto;
        int j=0;

        if(cursor.moveToFirst() ) {

            do {
                id = cursor.getString(0);
                titolo = cursor.getString(1);
                tipologia = cursor.getString(2);
                descrizione = cursor.getString(3);
                id_sito = cursor.getString(5);
                id_zona = cursor.getString(6);

                j++;
                foto=cursor.getBlob(4);
                Bitmap item_foto= getImage(foto);

                itemList.add(new Item(titolo, descrizione, tipologia,id_zona,id_sito));
                imageList.add(new ImageSql(item_foto,id));

                //imageList.add(new Image(item_foto,id));
                cursor.moveToNext();


            } while (!cursor.isAfterLast() );
        }
    }
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
//Lettura dei dati
    private void Local_route(String id){

        areaList_Site.clear();
        Cursor cursor= LocalDB.readAreas(id);

        String titolo,descrizione,tipologia,id_sito;
        int j=0;


        if(cursor.moveToFirst()){

            do {

                ID_AREA_LOCALE[j] = cursor.getString(0);
                titolo = cursor.getString(1);
                descrizione = cursor.getString(2);
                tipologia = cursor.getString(3);
                id_sito = cursor.getString(4);
                j++;



                areaList_Site.add(new Area(titolo, descrizione, tipologia, id_sito));
                cursor.moveToNext();

            }while(!cursor.isAfterLast());

            checkedItems = new boolean[areaList_Site.size()];
            Arrays.fill(checkedItems,false);

            mDatabase2.orderByChild("area_id_sito").equalTo(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int i = 0;
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        ID_AREA_FIRE[i]= snapshot1.getKey();

                        i++;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
//Lettura dei dati
    private void Local_orderRoute(){
        areaList_OrderRoute.clear();
        Arrays.fill(ID_AREA_LOCALE_Order,null);
        Arrays.fill(ID_AREA_LOCALE_Order1,null);
        Arrays.fill(ID_AREA_LOCALE_Order2,null);
        Cursor cursor= LocalDB.readAreasOrder(ID_PERCORSO);

        String titolo,descrizione,tipologia,id_sito;
        int j=0;


        if(cursor.moveToFirst()){

            do {

                ID_AREA_LOCALE_Order[j] = cursor.getString(0);
                titolo = cursor.getString(1);
                descrizione = cursor.getString(2);
                tipologia = cursor.getString(3);
                id_sito = cursor.getString(4);

                j++;

                areaList_OrderRoute.add(new Area(titolo, descrizione, tipologia, id_sito));
                cursor.moveToNext();

            }while(!cursor.isAfterLast());

            Cursor cursor2= LocalDB.readSecondAreasOrder(ID_PERCORSO);
            j=0;

            if(cursor2.moveToFirst()) {
                do {
                    ID_AREA_LOCALE_Order1[j] = cursor2.getString(0);
                    ID_AREA_LOCALE_Order2[j] = cursor2.getString(1);
                    j++;
                    cursor2.moveToNext();
                } while (!cursor2.isAfterLast());
            }



            checkedOrderItems = new boolean[areaList_OrderRoute.size()][areaList_OrderRoute.size()];

            for(int i=0; i<areaList_OrderRoute.size(); i++){
                for(int k=0; k<areaList_OrderRoute.size(); k++){
                    checkedOrderItems[i][k]=false;
                }
            }


            for(int i=0; i<=areaList_OrderRoute.size(); i++){
                if(ID_AREA_LOCALE_Order2[i] != null){
                    checkedOrderItems[Integer.parseInt(ID_AREA_LOCALE_Order1[i])-1][Integer.parseInt(ID_AREA_LOCALE_Order2[i])-1]=true;
                }
            }
        }
    }
//Cancellazione dei dati su firebase
    private void  deleteFire(String id_perc, final FireCallback myCallback){
        DatabaseReference ref  = mDatabase3;
        Query delete_query = ref.child("Ordine_Zona").orderByChild("idPercorso").equalTo(id_perc);

        delete_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren())
                {
                    ds.getRef().removeValue();
                }
                myCallback.onCallback("OK");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Add_Area_To_Route.this, errore, Toast.LENGTH_SHORT).show();
            }
        });


    }
    //Cancellazione dei dati su firebase
    private void  deleteFireOrder(String id_perc,String id_zona1, final FireCallback myCallback){
        DatabaseReference ref  = mDatabase3;
        Query delete_query = ref.child("Ordine_Zona").orderByChild("idPercorso").equalTo(id_perc);

        delete_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren())
                {
                    if(ds.child("idZona1").getValue().toString().equals(id_zona1)){
                        ds.getRef().removeValue();
                    }

                }
                myCallback.onCallback("OK");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Add_Area_To_Route.this, errore, Toast.LENGTH_SHORT).show();
            }
        });


    }



    @Override
    public void onItemClick(int position) {


        AlertDialog.Builder builder = new AlertDialog.Builder(Add_Area_To_Route.this, R.style.AlertDialogStyle);
        builder.setTitle(R.string.branches);
        areaList_OrderZone = (ArrayList<Area>) areaList_OrderRoute.clone();

        String[] Area = new String[areaList_OrderZone.size()];
        for(int i = 0; i< areaList_OrderZone.size(); i++){
            Area[i] = areaList_OrderZone.get(i).getAreaTitle();
        }

        for(int i=0; i<areaList_OrderZone.size(); i++){
            checkedItems_tmp[i] = checkedOrderItems [position][i];
        }

        builder.setMultiChoiceItems(Area, checkedItems_tmp, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                ((AlertDialog) dialog).getListView().getChildAt(position).setEnabled(false);
                checkedItems_tmp[which] = isChecked;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                LocalDB.delete_OrderZona2(ID_PERCORSO,ID_AREA_LOCALE[position]); //id percorso e id zona1
                deleteFireOrder(id_percorso_fire,ID_AREA_FIRE[position], new FireCallback() {
                    @Override
                    public void onCallback(String value) {
                        String result="";
                        int j=0;
                        for(int i = 0; i< areaList_OrderRoute.size(); i++){
                            if(checkedItems_tmp[i] == true){
                                j++;
                                mDatabase.push().setValue(new Ordine_Zone(id_percorso_fire,ID_AREA_FIRE[position],ID_AREA_FIRE[i],""));
                                result=LocalDB.insertRouteZone(ID_AREA_LOCALE[position],ID_PERCORSO,ID_AREA_LOCALE[i],"");

                                Toast.makeText(Add_Area_To_Route.this, ""+result+"" +ID_AREA_LOCALE[i], Toast.LENGTH_SHORT).show();



                            }
                        }

                        for(int i=0; i<areaList_OrderRoute.size(); i++){
                            checkedOrderItems[position][i]= checkedItems_tmp[i];
                        }


                        Local_orderRoute();
                        for (int i=0; i<areaList_Site.size();i++){
                            for (int k=0; k<areaList_Site.size(); k++){
                                if (ID_AREA_LOCALE[i].equals(ID_AREA_LOCALE_Order[k])){
                                    checkedItems[i]=true;
                                }
                            }
                        }

                        adapter = new RecyclerAreaAdapter(areaList_OrderRoute,Add_Area_To_Route.this);
                        recyclerView_Area.setAdapter(adapter);
                        checkedItems= checkedItems_tmp.clone();

                        itemList.clear();
                        imageList.clear();

                        for(int i=0; i<ID_AREA_LOCALE_Order1.length; i++){
                            //Leggo gli item presenti nelle aree aggiunte al percorso
                            Local_route_Item(ID_AREA_LOCALE_Order1[i]);
                        }

                        adapterItem = new RecyclerItemAdapter( itemList,imageList, Add_Area_To_Route.this);
                        recyclerView_Item = findViewById(R.id.itemList);
                        recyclerView_Item.setAdapter(adapterItem);
                    }
                });


            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();


        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.red_delete));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.color_Primary_500));
    }



    @Override
    public void onLongItemClick(int position) {

    }
}
