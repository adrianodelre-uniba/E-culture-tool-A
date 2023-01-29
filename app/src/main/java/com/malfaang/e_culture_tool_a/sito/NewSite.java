package com.malfaang.e_culture_tool_a.sito;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malfaang.e_culture_tool_a.Database;
import com.malfaang.e_culture_tool_a.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewSite extends AppCompatActivity {
    private ListView listView;
    private EditText nomeSito;
    private EditText cittaSito;
    private EditText telefonoSito;
    private EditText emailSito;
    private EditText orarioApertura;
    private EditText orarioChiusura;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    private DatabaseReference mDatabase3;
    private String dat = null;
    private FirebaseAuth mAuth;
    public static final String PREFS_NAME = "MyPrefsFile";
    private Database localdb;
    private SharedPreferences preference;
    private String idSito;
    private String idUtente;
    private ArrayList<Sito> sitoList;
    private String errore;
    private String update;
    private String camp;
    private String teleValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_site);
        mAuth = FirebaseAuth.getInstance();
        localdb = new Database(getApplicationContext());
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Sito");
        teleValid = getResources().getString(R.string.telefono_non_valido);
        camp = getResources().getString(R.string.campi);
        nomeSito = findViewById(R.id.editText);
        cittaSito = findViewById(R.id.editText2);
        telefonoSito = findViewById(R.id.editText3);
        emailSito = findViewById(R.id.editText4);
        orarioApertura = findViewById(R.id.editTextTime1);
        orarioChiusura = findViewById(R.id.editTextTime2);
        sitoList = new ArrayList<>();
        errore = getResources().getString(R.string.Errore_fb);
        update = getResources().getString(R.string.update);
        FirebaseUser user = mAuth.getCurrentUser();
        preference = getApplicationContext().getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        idSito = preference.getString("Id_Sito", "");
        idUtente = preference.getString("Id_Utente", "");

        // viene fatto un confronto nei due database e se quello locale  risultasse con una data più recente,  il database online verrebbe aggiornato con i dati di quello locale
        if(isNetworkAvailable()) {
            fire(value -> {
                if (value != null) {
                    nomeSito.setText(value.get(0).getNomeSito());
                    cittaSito.setText(value.get(0).getCittaSito());
                    telefonoSito.setText(value.get(0).getTelefonoSito());
                    emailSito.setText(value.get(0).getEmailSito());
                    orarioApertura.setText(value.get(0).getOrarioApertura());
                    orarioChiusura.setText(value.get(0).getOrarioChiusura());
                    dat = letturaLocal();
                    if (dat != null) {
                        Calendar c = Calendar.getInstance();
                        try {
                            Date dataLocale = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                    .parse(dat);
                            Date dataFire = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                    .parse(preference.getString("Last_Data", ""));
                            if (dataLocale.compareTo(dataFire) > 0) {
                                //se data locale è più recente di data fire
                                idSito = preference.getString("Id_Sito", "");
                                updateFireSite(idSito);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

            });
        }else{
            Toast.makeText(getApplicationContext(),errore,Toast.LENGTH_SHORT).show();
        }
    }

    // aggiornamento dei dati sul database online, leggendo prima quello locale e passando i dati a delle variabili temporali
    //per poi essere settate sul database online
    private void updateFireSite(String idSito) {
        String nomeSito2 = null;
        String cittaSito2 = null;
        String telefonoSito2 = null;
        String emailSito2= null;
        String orarioApertura2 = null;
        String orarioChiusura2 = null;
        String idCuratore;
        String data = null;
        key();
        preference = getApplicationContext().getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
        Cursor cursor = localdb.readSito(idUtente);
        if(cursor.moveToFirst() ) {
            do {
                Toast.makeText(this,getResources().getString(R.string.Aggiornato), Toast.LENGTH_SHORT).show();
                nomeSito2 = cursor.getString(1);
                cittaSito2 = cursor.getString(2);
                emailSito2 = cursor.getString(3);
                telefonoSito2 = cursor.getString(4);
                orarioApertura2 = cursor.getString(5);
                orarioChiusura2 = cursor.getString(6);
                data = cursor.getString(8);
                cursor.moveToNext();
            } while (!cursor.isAfterLast() );
        }

        String finalCittaSito = cittaSito2;
        String finalEmailSito = emailSito2;
        String finalNomeSito = nomeSito2;
        String finalOrarioApertura = orarioApertura2;
        String finalOrarioChiusura = orarioChiusura2;
        String finalTelefonoSito = telefonoSito2;
        String finalData = data;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Sito");
        mDatabase.orderByKey().equalTo(idSito).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDatabase.child(""+idSito).child("città_sito").setValue(finalCittaSito);
                mDatabase.child(""+idSito).child("emailSito2").setValue(finalEmailSito);
                mDatabase.child(""+idSito).child("nome_sito").setValue(finalNomeSito);
                mDatabase.child(""+idSito).child("orarioApertura2").setValue(finalOrarioApertura);
                mDatabase.child(""+idSito).child("orarioChiusura2").setValue(finalOrarioChiusura);
                mDatabase.child(""+idSito).child("telefono_sito").setValue(finalTelefonoSito);
                mDatabase.child(""+idSito).child("data_Ultima_Modifica").setValue(finalData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_site, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //quando si clicca sul bottone c'è un controllo per vedere se c'è o no già un sito con la chiave del curatore
    //se il sito non si trovasse viene richiamata la funzione per l'inserimento del nuovo sito in locale e online
    //nel caso contrario viene fatto un confronto nei due database e se risultasse vero il database online verrebbe aggiornato con i dati di quello locale
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_info:
                if(letturaLocal() == null){
                    aggiuntaSito();
                }else{
                    preference = getApplicationContext().getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
                    key();
                    updateSql();
                    dat= letturaLocal();
                    if(dat != null){
                        Calendar c = Calendar.getInstance();
                        try {
                            Date dataLocale = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                    .parse(dat);
                            Date dataFire = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                    .parse(preference.getString("Last_Data",""));
                            if(dataLocale.compareTo(dataFire) > 0){
                                //se data locale è più recente di data fire
                                idSito = preference.getString("Id_Sito", "");
                                updateFireSite(idSito);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //inserimento del sito sul database locale e online
    private void aggiuntaSito() {
        String nome = nomeSito.getText().toString();
        String city = cittaSito.getText().toString();
        String telefono = telefonoSito.getText().toString();
        String email = emailSito.getText().toString();
        String apertura = orarioApertura.getText().toString();
        String chiusura = orarioChiusura.getText().toString();
        key();
        preference = getApplicationContext().getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
        idUtente = preference.getString("Id_Utente", "");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Sito");
        mDatabase3 = FirebaseDatabase.getInstance().getReference().child("Sito_Utente");
        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(city) || TextUtils.isEmpty(telefono) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(apertura) || TextUtils.isEmpty(chiusura) ) {
            Toast.makeText(getApplicationContext(), camp, Toast.LENGTH_LONG).show();
        }else {
            if (telNonValido(telefono)) {
                Sito sito = new Sito(nome, city, telefono, email, apertura, chiusura, formattedDate, idUtente);
                DatabaseReference ref = mDatabase.push();
                String idSitoFire = ref.getKey();
                ref.setValue(sito);
                mDatabase3.push().setValue(new SitoUtente(idSitoFire, idUtente));
                SharedPreferences.Editor editor = preference.edit();
                editor.putString("Id_Sito", ref.getKey());
                editor.commit();
                String idSito2 = preference.getString("Id_Sito", "");
                localdb.addRecordSito(idSito2, nome, city, telefono, email, apertura, chiusura, formattedDate, idUtente);
                localdb.addRecordSitoUtente(idSito2, idUtente);
                Toast.makeText(this, getResources().getString(R.string.aggiunto_fb), Toast.LENGTH_SHORT).show();
            }}

    }

    //lettura dati dal database online
    private void fire(final SiteListCallback myCallback){
        key();
        preference = getApplicationContext().getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
        sitoList.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Sito");
        mDatabase.orderByChild("id_Curatore").equalTo(idUtente).addValueEventListener(new ValueEventListener() {
            private SharedPreferences.Editor editor = preference.edit();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    String nome = snapshot1.child("nome_sito").getValue().toString();
                    String city = snapshot1.child("città_sito").getValue().toString();
                    String telefono = snapshot1.child("telefono_sito").getValue().toString();
                    String email = snapshot1.child("email_sito").getValue().toString();
                    String apertura = snapshot1.child("orario_apertura").getValue().toString();
                    String chiusura = snapshot1.child("orario_chiusura").getValue().toString();
                    String utente = snapshot1.child("id_Curatore").getValue().toString();
                    String data = snapshot1.child("data_Ultima_Modifica").getValue().toString();
                    String id = snapshot1.getKey();
                    sitoList.add(new Sito(nome,city,telefono,email,apertura,chiusura,data, idUtente));
                    editor.putString("Id_Sito",id);
                    editor.putString("Last_Data", data);
                    editor.commit();
                    myCallback.onCallback(sitoList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    //funzione per ottenere la chiave dell'utente usando firebase
    //l'utente dopo che si logga avviene un controllo sulla tabella utente e si legge la chiave per poi passarla con la preference
    public void key() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Utente");
        if (user != null) {
            String userEmail = user.getEmail();
            mDatabase2.orderByChild("email").equalTo(userEmail).addValueEventListener(new ValueEventListener() {
                private SharedPreferences preference = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                private SharedPreferences.Editor editor = preference.edit();

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String idUte = snapshot1.getKey();
                        editor.putString("Id_Utente",idUte);
                        editor.commit();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    //lettura dei dati dal database locale
    private String letturaLocal(){
        String nomeSito3;
        String cittaSito3;
        String telefonoSito3;
        String emailSito3;
        String orarioApertura3;
        String orarioChiusura3;
        String idCuratore;
        String data = null;
        key();
        preference = getApplicationContext().getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
        Cursor cursor = localdb.readSito(idUtente);
        if(cursor.moveToFirst() ) {
            do {
                nomeSito3 = cursor.getString(1);
                cittaSito3 = cursor.getString(2);
                emailSito3 = cursor.getString(3);
                telefonoSito3 = cursor.getString(4);
                orarioApertura3 = cursor.getString(5);
                orarioChiusura3 = cursor.getString(6);
                data = cursor.getString(8);
                cursor.moveToNext();
            } while (!cursor.isAfterLast() );
        }
        return data;
    }

    //aggiornamento dati dal database locale dopo che il curatore li inserisce
    private void updateSql(){
        key();
        preference = getApplicationContext().getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
        String city = cittaSito.getText().toString();
        String ema= emailSito.getText().toString();
        String nome = nomeSito.getText().toString();
        String apertura = orarioApertura.getText().toString();
        String chiusura = orarioChiusura.getText().toString();
        String telefono = telefonoSito.getText().toString();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        localdb.updateSql(nome,city,ema,telefono,apertura,chiusura,formattedDate, idUtente);
    }

    public boolean isNetworkAvailable() {
        Context context = getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Controllo sul telefono
    private Boolean telNonValido(String telefono){
        boolean isValid=true;
        if(telefono.length()!=10){
            Toast.makeText(getApplicationContext(), teleValid,Toast.LENGTH_LONG).show();
            return false;
        }

        for(int i=0;i<telefono.length();i++){
            char c= telefono.charAt(i);
            if(c >= '0' && c <= '9') {
                continue;
            }
            isValid=false;
        }

        if(isValid){
            return true;
        } else{
            Toast.makeText(getApplicationContext(), teleValid,Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
