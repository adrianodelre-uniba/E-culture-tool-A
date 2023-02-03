package com.malfaang.e_culture_tool_a.qrCode;


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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.malfaang.e_culture_tool_a.Database;
import com.malfaang.e_culture_tool_a.MainActivity;
import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.addElements.ImageSql;
import com.malfaang.e_culture_tool_a.newRoute.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

public class qrcodeActivity extends AppCompatActivity{

    public static final String PREFS_NAME = "MyPrefsFile";
    ActionBar ab;

    private Database localDb;
    private String ID_AREA,ID_ITEM,id_sito,tipologia;
    Bundle extras;
    TextView Item_Title;
    TextView Item_Tipology;
    TextView Item_Description;
    final int MAX=50;
    ImageView Item_pic;
    private ArrayList<Item> itemFire;
    private ArrayList<Item> itemList;
    private String[] key_list = new String[MAX];
    int posID;
    String inquadra_qr;
    public final static int WIDTH = 800;
    public final static int HEIGHT = 800;
    String id_zona,titolo;
    DatabaseReference mDatabase;



    ArrayList<ImageSql> imageSQList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preference;
        preference = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        inquadra_qr = getResources().getString(R.string.inquadra);

        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();

//Controllo della presenza della chiave, altrimenti va con la scansione
        if( !extras.containsKey("key_item")){
            scanCode();
        }else{ //se c'è qualcosa mostra il layout con tutte le informazioni dell'oggetto
            setContentView(R.layout.activity_mostra_opera);


            id_sito = preference.getString("Id_Sito", "");
            tipologia = preference.getString("Tipologia_Utente", "");

            ID_ITEM=extras.getString("key_item");


            mDatabase = FirebaseDatabase.getInstance().getReference().child("Oggetti");



            ab = getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);
            localDb = new Database(getApplicationContext());
            itemList = new ArrayList<>();
            itemFire = new ArrayList<>();
            imageSQList = new ArrayList<>();

                    Local_route_Item(id_sito,ID_ITEM);
                    if (itemList.size() == 1) {

                        Item_Title = (TextView) findViewById(R.id.Titolo);

                        Item_Description = (TextView) findViewById(R.id.opera_desc);
                        Item_Tipology = (TextView) findViewById(R.id.Tipologia);
                        Item_pic = findViewById(R.id.IMG);
                        ab.setTitle(itemList.get(0).getItemTitle());
                        Item_Title.setText(itemList.get(0).getItemTitle());
                        Item_Description.setText(itemList.get(0).getItemDescr());
                        Item_Tipology.setText(itemList.get(0).getItemTypology());
                        Item_pic.setImageBitmap(imageSQList.get(0).getItem_imgSql());

                        if (tipologia.equals("Visitatore")|| tipologia.equals("Guida")){
                            Item_Title.setEnabled(false);
                            Item_Description.setEnabled(false);
                            Item_Tipology.setEnabled(false);
                            Item_Title.setEnabled(false);
                        }

                    }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


//Funzione per scansionare
    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt(inquadra_qr);
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() !=null) {
                    final Intent opera = new Intent(getApplicationContext(), qrcodeActivity.class);
                    String str = result.getContents();
                    opera.putExtra("key_item", str);
                    startActivity(opera);
        }else{
            final Intent i = new Intent(qrcodeActivity.this, MainActivity.class);
            startActivity(i);
        }
    });
//Funzione per condividere il qrcode
    void showDialog(){

        LayoutInflater inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.alert_dialog_share_qr_code,null);

        Button doButton = view.findViewById(R.id.ad_button_do);
        ImageView iv1 = view.findViewById(R.id.qr_code_image_view);

        try {
            Bitmap bitmap = encodeAsBitmap(ID_ITEM);
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
//Funzione per generare il qrcode
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





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences preference;
        preference = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        getMenuInflater().inflate(R.menu.menu_show_item, menu);
        tipologia = preference.getString("Tipologia_Utente", "");
        String Visitatore = getResources().getString(R.string.visitatore);
        String Guida = getResources().getString(R.string.guida);

        if (tipologia.equals(Visitatore) || tipologia.equals(Guida)){
            menu.findItem(R.id.modifica).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.generate_qr_code:
                showDialog();
                return true;

            case R.id.modifica:
                Salva_modifica();
                return  true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        Intent intent = super.getParentActivityIntent();
        return intent;
    }
//Funzione per leggere  i dati dell'item
    private void Local_route_Item(String ID, String id_item){

        String descrizione;
        String tipologia;
        String id_sito;

        String id;

        Cursor cursor= localDb.readItem_show2(ID, id_item);

        byte[] foto;


        if(cursor.moveToFirst() ) {

            do {
                id = cursor.getString(0);
                titolo = cursor.getString(1);
                tipologia = cursor.getString(2);
                descrizione = cursor.getString(3);
                id_sito = cursor.getString(5);
                id_zona = cursor.getString(6);



                foto=cursor.getBlob(4);
                Bitmap item_foto= getImage(foto);


                itemList.add(new Item(titolo, descrizione, tipologia,id_zona,id_sito));
                imageSQList.add(new ImageSql(item_foto,id));


                cursor.moveToNext();


            } while (!cursor.isAfterLast() );
        }

        mDatabase.orderByChild("item_Sito").equalTo(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String descrizione;
                String tipologia;
                String id_sito;
                int i=0;
                String id;

                for (DataSnapshot snapshot1:snapshot.getChildren()){

                    id = snapshot1.getKey();
                    titolo = snapshot1.child("itemTitle").getValue().toString();
                    tipologia =  snapshot1.child("itemTypology").getValue().toString();
                    descrizione =  snapshot1.child("itemDescr").getValue().toString();
                    id_sito =  snapshot1.child("item_Sito").getValue().toString();
                    id_zona =  snapshot1.child("item_Zona").getValue().toString();

                    itemFire.add(new Item(titolo, descrizione, tipologia,id_zona,id_sito));
                    key_list[i]=id;
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        for (int i=0;i<itemFire.size();i++){
            if (itemFire.get(i).getItemTitle().equals(titolo)){
                posID=i;
            }
        }

    }

//Funzione per modificare gli oggetti
    private void  Salva_modifica(){
        String titolo2,descrizione,tipologia;
        titolo2 = Item_Title.getText().toString();
        descrizione = Item_Description.getText().toString();
        tipologia = Item_Tipology.getText().toString();
       boolean ciao = localDb.update_item(Integer.parseInt(ID_ITEM),titolo2,descrizione,tipologia,id_zona);

       mDatabase.orderByKey().equalTo(key_list[posID]).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               mDatabase.child(""+key_list[posID]).child("itemTitle").setValue(titolo2);
               mDatabase.child(""+key_list[posID]).child("itemTypology").setValue(tipologia);
               mDatabase.child(""+key_list[posID]).child("itemDescr").setValue(descrizione);

               Toast.makeText(qrcodeActivity.this, ""+getResources().getString(R.string.Conferma_Modifica), Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });






    }
}