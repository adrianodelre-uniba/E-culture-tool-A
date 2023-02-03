package com.malfaang.e_culture_tool_a;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.malfaang.e_culture_tool_a.login.FireCallback;
import com.malfaang.e_culture_tool_a.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Splash_Screen extends AppCompatActivity {
    private Database LocalDB;
    private ArrayList<String> areaKeyList;
    public static final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences preference;
    String pat_file,foto_non,errore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalDB = new Database(getApplicationContext());
        areaKeyList = new ArrayList<>();

        //Configuriamo la finestra in full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_splash);
        preference = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String id_utente = preference.getString("Id_Utente", "");
        String id_sito = preference.getString("Id_Sito", "");
        pat_file = getResources().getString(R.string.Percorso_file);
        foto_non = getResources().getString(R.string.foto_no);
        errore = getResources().getString(R.string.Errore_fb);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(!id_utente.isEmpty() && !id_sito.isEmpty()){

                    sync_Data_from_Fire(new FireCallback() {
                        @Override
                        public void onCallback(String value) {
                            Intent intent = new Intent(getApplicationContext(),   MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
                else {
                    Intent intent = new Intent(getApplicationContext(),   LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 1000);



    }
    //Funzione per sincronizzare i dati
    private void sync_Data_from_Fire( final FireCallback myCallback) {


        preference = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preference.edit();

        String id_utente = preference.getString("Id_Utente", "");
        String tipologia = preference.getString("Tipologia_Utente", "");

        DatabaseReference mdatabaseSito = FirebaseDatabase.getInstance().getReference().child("Sito");
        DatabaseReference mdatabasePercorsi = FirebaseDatabase.getInstance().getReference().child("Percorsi");
        DatabaseReference mdatabaseZone = FirebaseDatabase.getInstance().getReference().child("Zone");
        DatabaseReference mdatabaseOggetti = FirebaseDatabase.getInstance().getReference().child("Oggetti");
        DatabaseReference mdatabasefoto = FirebaseDatabase.getInstance().getReference().child("Foto_Oggetti");
        DatabaseReference mdatabaseOrdine = FirebaseDatabase.getInstance().getReference().child("Ordine_Zona");
        DatabaseReference mdatabaseSito_Utente = FirebaseDatabase.getInstance().getReference().child("Sito_Utente");
        StorageReference mStorage_foto = FirebaseStorage.getInstance().getReference().child("Item/");

        mdatabaseSito_Utente.orderByChild("id_utente").equalTo(id_utente).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    String idsito=snapshot1.child("id_sito").getValue().toString();
                    editor.putString("Id_Sito",idsito);
                    editor.commit();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String id_sito = preference.getString("Id_Sito", "");
//Controllo se c'è gia un sito
        if(controllo_Presenza_sito(id_sito)) {

            mdatabaseSito.orderByKey().equalTo(id_sito).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String nome = snapshot1.child("nome_sito").getValue().toString();
                        String city = snapshot1.child("città_sito").getValue().toString();
                        String telefono = snapshot1.child("telefono_sito").getValue().toString();
                        String email = snapshot1.child("email_sito").getValue().toString();
                        String apertura = snapshot1.child("orario_apertura").getValue().toString();
                        String chiusura = snapshot1.child("orario_chiusura").getValue().toString();
                        String utente = snapshot1.child("id_Curatore").getValue().toString();
                        String data = snapshot1.child("data_Ultima_Modifica").getValue().toString();


                        String result = LocalDB.addRecordSito(id_sito, nome, city, email, telefono, apertura, chiusura, data, utente);
                        if (!result.equals("Success")) {
                            Toast.makeText(getApplicationContext(), errore, Toast.LENGTH_SHORT).show();
                        }



                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            mdatabaseZone.orderByChild("area_id_sito").equalTo(id_sito).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String Desc = snapshot1.child("areaDescr").getValue().toString();
                        String Titolo = snapshot1.child("areaTitle").getValue().toString();
                        String Tipologia = snapshot1.child("areaTypology").getValue().toString();

                        String key= snapshot1.getKey();
                        LocalDB.addRecord_Area(Titolo, Desc, Tipologia, id_sito,key);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



            mdatabasePercorsi.orderByChild("idSite").equalTo(id_sito).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                        String data = snapshot1.child("data_creazione").getValue().toString();
                        String id_sito =  snapshot1.child("idSite").getValue().toString();
                        String descrizione =  snapshot1.child("routeDescr").getValue().toString();
                        String titolo =  snapshot1.child("routeTitle").getValue().toString();
                        String tipologia =  snapshot1.child("routeTypology").getValue().toString();
                        String id_percorso = snapshot1.child("key_route").getValue().toString();
                        String back= snapshot1.child("go_back").getValue().toString();

                        Boolean go_back;

                        if (back.equals("true")){
                            go_back=true;
                        }else{
                            go_back=false;
                        }


                        String result = LocalDB.insertRoute(descrizione,tipologia,titolo,id_percorso,id_sito,data,go_back);
                        String idp="";
                        String idp_locale="";
                        Cursor cursor = LocalDB.getLastRoute();
                        if (cursor.moveToFirst()){
                            idp=cursor.getString(0);
                        }

                        Cursor cursor3 = LocalDB.getLastRouteLocale();
                        if (cursor3.moveToFirst()){
                            idp_locale=cursor3.getString(0);
                        }

                        String finalIdp_locale = idp_locale;


                        mdatabaseOrdine.orderByChild("idPercorso").equalTo(idp).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snapshot1:snapshot.getChildren()){
                                    String id_zona1 = snapshot1.child("idZona1").getValue().toString();

                                    String id_zona2=snapshot1.child("idZona2").getValue().toString();
                                    String id="",id2="";


                                    Cursor cursor =LocalDB.getidZona(id_zona1);
                                    if (cursor.moveToFirst()){
                                        id= cursor.getString(0);
                                    }

                                    if (!id_zona2.equals("")){



                                        Cursor cursor2= LocalDB.getidZona(id_zona2);
                                        if (cursor2.moveToFirst()){
                                            id2 = cursor2.getString(0);
                                        }


                                        LocalDB.add_Ordine_zona(id, finalIdp_locale, id2,null);

                                    }else{
                                        LocalDB.add_Ordine_zona(id, finalIdp_locale,null,null);
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });







            mdatabaseOggetti.orderByChild("item_Sito").equalTo(id_sito).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                        String titolo = snapshot1.child("itemTitle").getValue().toString();
                        String desc = snapshot1.child("itemDescr").getValue().toString();
                        String tipo = snapshot1.child("itemTypology").getValue().toString();
                        String id_zona = snapshot1.child("item_Zona").getValue().toString();
                        String id_zona_locale = Local_IDArea(id_zona);
                        String id = snapshot1.getKey();

                        LocalDB.addRecord_Item(titolo, desc, tipo, id_zona_locale , id_sito);

                        Cursor cursor = LocalDB.getLastItem();
                        if (cursor.moveToFirst()) {
                            String item = cursor.getString(0);


                            mdatabasefoto.orderByChild("item_id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for (DataSnapshot snapshot2 : snapshot.getChildren()) {


                                        StorageReference photoReference = mStorage_foto.child(snapshot2.getKey());
                                        final long ONE_MEGABYTE = 1024 * 1024;

                                        photoReference.getBytes(ONE_MEGABYTE).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                                            @Override
                                            public void onComplete(@NonNull Task<byte[]> task) {
                                                Bitmap bmp = BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length);
                                                bmp = Bitmap.createScaledBitmap(bmp, 300, 300, true);
                                                byte[] fot = getBitmapAsByteArray(bmp);
                                                LocalDB.addFoto_Items(fot, item);
                                                myCallback.onCallback("OK");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                Toast.makeText(getApplicationContext(), pat_file, Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), foto_non, Toast.LENGTH_SHORT).show();
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else{
            myCallback.onCallback("OK");
        }

    }

    private String Local_IDArea(String id_area) {
        Cursor cursor= LocalDB.readAreasID(id_area);

        String key = null;



        if(cursor.moveToFirst()){
            do {
                key = cursor.getString(0);

                cursor.moveToNext();

            }while(!cursor.isAfterLast());

        }
        return key;
    }
    //Funzione per controllare il sito
    private Boolean controllo_Presenza_sito(String id_sito){
        Boolean risposta=false;
        Cursor cursor;
        cursor=LocalDB.getSitoByKey(id_sito);
        if (cursor.getCount()<1){
            risposta=true;
        }
        return risposta;
    }
    //Funzione per convertire l'immagine
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, outputStream);
        byte[]  byt = outputStream.toByteArray();
        //file_foto = Base64.encodeToString(byt,Base64.DEFAULT);
        return byt;

    }
}
