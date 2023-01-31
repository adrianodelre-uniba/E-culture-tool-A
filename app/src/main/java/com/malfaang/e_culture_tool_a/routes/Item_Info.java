package com.malfaang.e_culture_tool_a.routes;


import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.malfaang.e_culture_tool_a.Database;
import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.addElements.RecyclerAreaAdapter;
import com.malfaang.e_culture_tool_a.newRoute.Area;
import com.malfaang.e_culture_tool_a.newRoute.Item;
import com.malfaang.e_culture_tool_a.newRoute.RecyclerViewClickInterface;
import com.malfaang.e_culture_tool_a.newRoute.Route;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;

public class Item_Info extends AppCompatActivity implements RecyclerViewClickInterface {
    Bundle extras;
    String item_name;
    String ID_ITEM_SITE;
    String ID_AREA;
    int ID_ITEM;
    Item item_selected;
    Bitmap item_img_selected;
    private Database LocalDB;

    private ArrayList<Route> routeList;
    public RecyclerRouteAdapter adapterRoute;
    private RecyclerView recyclerView_Route;

    private ArrayList<Area> areaList;
    public RecyclerAreaAdapter adapterArea;
    private RecyclerView recyclerView_Area;
    public final static int WIDTH = 800;
    public final static int HEIGHT = 800;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_info);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        LocalDB= new Database(getApplicationContext());
        extras = getIntent().getExtras();
        routeList = new ArrayList<>();
        areaList = new ArrayList<>();
//Controllo della presenza delle chiavi
        if(extras.containsKey("key_item") && extras.containsKey("item")) {
            item_selected = (Item) getIntent().getSerializableExtra("item");
            item_img_selected = (Bitmap) getIntent().getParcelableExtra("item_img");
            item_name = item_selected.getItemTitle();
            ID_ITEM = Integer.parseInt(extras.getString("key_item"));
            ID_ITEM_SITE = item_selected.getItem_Sito();
            ID_AREA = item_selected.getItem_Zona();

            ab.setTitle(item_name);
            ab.setSubtitle(R.string.Area_Title);
        }
//Collegamento degli oggetti
        TextView tv_Titolo = findViewById(R.id.Titolo_Item);
        TextView tv_Tipologia = findViewById(R.id.Tipologia_Item);
        TextView tv_Descrizione = findViewById(R.id.Descrizione_Item);
        ImageView pic = findViewById(R.id.IMG);

        tv_Titolo.setText(item_selected.getItemTitle());
        tv_Tipologia.setText(item_selected.getItemTypology());
        tv_Descrizione.setText(item_selected.getItemDescr());
        pic.setImageBitmap(item_img_selected);

        Local_Area();
        if(areaList != null){
            adapterArea = new RecyclerAreaAdapter( areaList, this);
            recyclerView_Area = findViewById(R.id.areaListItem);
            recyclerView_Area.setNestedScrollingEnabled(false);
            recyclerView_Area.setAdapter(adapterArea);
        }

        Local_route();
        if(routeList != null){
            adapterRoute = new RecyclerRouteAdapter( routeList, this);
            recyclerView_Route= findViewById(R.id.routeListItem);
            recyclerView_Route.setNestedScrollingEnabled(false);
            recyclerView_Route.setAdapter(adapterRoute);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_show_item, menu);

        menu.findItem(R.id.modifica).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.generate_qr_code:
                showDialog();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        Intent intent = super.getParentActivityIntent();
        return intent;
    }
    //Layout per la condivisione
    void showDialog(){

        LayoutInflater inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.alert_dialog_share_qr_code,null);

        Button doButton = view.findViewById(R.id.ad_button_do);
        ImageView iv1 = view.findViewById(R.id.qr_code_image_view);

        try {
            Bitmap bitmap = encodeAsBitmap(String.valueOf(ID_ITEM));
            iv1.setImageBitmap(bitmap);
        } catch (WriterException ex) {
            ex.printStackTrace();
        }

        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();

        doButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    //Creazione del qrcode
    Bitmap encodeAsBitmap(@NonNull String str) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(str, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);

        int w = bitMatrix.getWidth();
        int h = bitMatrix.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                pixels[y * w + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }
    //Lettura delle aree
    private void Local_Area(){
        String titolo;
        String descrizione;
        String tipologia;
        String id_sito;

        Cursor cursor= LocalDB.readArea(ID_AREA);
        areaList.clear();


        if(cursor.moveToFirst() ) {

            do {
                // id = cursor.getString(0);
                titolo = cursor.getString(1);
                tipologia = cursor.getString(2);
                descrizione = cursor.getString(3);
                id_sito = cursor.getString(4);

                areaList.add(new Area(titolo, descrizione, tipologia,id_sito));


                cursor.moveToNext();


            } while (!cursor.isAfterLast() );
        }
    }

    //Lettura dei percorsi
    private void Local_route(){
        Cursor cursor= LocalDB.readAllRouteArea(Integer.parseInt(ID_AREA));
        routeList.clear();

        String titolo,descrizione,tipologia,id_sito,data_creazione,key_route,back;
        Boolean go_back;
        int j=0;


        if(cursor.moveToFirst()) {

            do {


                titolo = cursor.getString(1);
                descrizione = cursor.getString(2);
                tipologia = cursor.getString(3);
                key_route = cursor.getString(4);
                id_sito = cursor.getString(5);
                data_creazione = cursor.getString(6);
                back=cursor.getString(7);
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

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLongItemClick(int position) {

    }
}