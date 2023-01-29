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
import java.util.List;
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
        sitoList = new ArrayList<Sito>();
        errore = getResources().getString(R.string.Errore_fb);
        update = getResources().getString(R.string.update);
        FirebaseUser user = mAuth.getCurrentUser();
        preference = getApplicationContext().getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        idSito = preference.getString("Id_Sito", "");
        idUtente = preference.getString("Id_Utente", "");

        // viene fatto un confronto nei due database e se quello locale  risultasse con una data più recente,  il database online verrebbe aggiornato con i dati di quello locale
        if(isNetworkAvailable()) {
            Fire(new SiteListCallback() {

                @Override
                public void onCallback(List<Sito> value) {

                    if (value != null) {
                        nomeSito.setText(value.get(0).getNomeSito());

                        cittaSito.setText(value.get(0).getCittaSito());
                        telefonoSito.setText(value.get(0).getTelefonoSito());
                        emailSito.setText(value.get(0).getEmailSito());
                        orarioApertura.setText(value.get(0).getOrarioApertura());
                        orarioChiusura.setText(value.get(0).getOrarioChiusura());
                        dat = Lettura_local();
                        if (dat != null) {
                            Calendar c = Calendar.getInstance();

                            try {
                                Date data_locale = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                        .parse(dat);

                                Date data_fire = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                        .parse(preference.getString("Last_Data", ""));

                                if (data_locale.compareTo(data_fire) > 0) {
                                    //se data locale è più recente di data fire
                                    idSito = preference.getString("Id_Sito", "");

                                    Update_Fire_Site(idSito);

                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
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
    private void Update_Fire_Site(String id_sito) {

        String Nome_sito = null,città_sito = null,telefono_sito = null,email_sito= null,orario_apertura = null,orario_chiusura = null,Id_Curatore;
        String data = null;
        key();
        preference = getApplicationContext().getSharedPreferences("MyPrefsFile",MODE_PRIVATE);



        Cursor cursor = localdb.readSito(idUtente);

        if(cursor.moveToFirst() ) {

            do {
                Toast.makeText(this,getResources().getString(R.string.Aggiornato), Toast.LENGTH_SHORT).show();
                Nome_sito = cursor.getString(1);
                città_sito = cursor.getString(2);
                email_sito = cursor.getString(3);
                telefono_sito = cursor.getString(4);
                orario_apertura = cursor.getString(5);
                orario_chiusura = cursor.getString(6);
                data = cursor.getString(8);

                cursor.moveToNext();


            } while (!cursor.isAfterLast() );
        }

        String finalCittà_sito = città_sito;
        String finalEmail_sito = email_sito;
        String finalNome_sito = Nome_sito;
        String finalOrario_apertura = orario_apertura;
        String finalOrario_chiusura = orario_chiusura;
        String finalTelefono_sito = telefono_sito;
        String finalData = data;

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Sito");
        mDatabase.orderByKey().equalTo(id_sito).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mDatabase.child(""+id_sito).child("città_sito").setValue(finalCittà_sito);
                mDatabase.child(""+id_sito).child("email_sito").setValue(finalEmail_sito);
                mDatabase.child(""+id_sito).child("nome_sito").setValue(finalNome_sito);
                mDatabase.child(""+id_sito).child("orario_apertura").setValue(finalOrario_apertura);
                mDatabase.child(""+id_sito).child("orario_chiusura").setValue(finalOrario_chiusura);
                mDatabase.child(""+id_sito).child("telefono_sito").setValue(finalTelefono_sito);
                mDatabase.child(""+id_sito).child("data_Ultima_Modifica").setValue(finalData);
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

                if(Lettura_local() == null){
                    Aggiunta_sito();
                }else{
                    preference = getApplicationContext().getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
                    key();
                    Update_sql();



                    dat=Lettura_local();
                    if(dat != null){
                        Calendar c = Calendar.getInstance();

                        try {
                            Date data_locale = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                    .parse(dat);

                            Date data_fire = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                    .parse(preference.getString("Last_Data",""));

                            if(data_locale.compareTo(data_fire) > 0){
                                //se data locale è più recente di data fire
                                idSito = preference.getString("Id_Sito", "");

                                Update_Fire_Site(idSito);

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
    private void Aggiunta_sito() {


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
                String id_sito_fire = ref.getKey();
                ref.setValue(sito);
                mDatabase3.push().setValue(new SitoUtente(id_sito_fire, idUtente));
                SharedPreferences.Editor editor = preference.edit();
                editor.putString("Id_Sito", ref.getKey());
                editor.commit();

                String idSito = preference.getString("Id_Sito", "");
                localdb.addRecordSito(idSito, nome, city, telefono, email, apertura, chiusura, formattedDate, idUtente);
                localdb.addRecordSitoUtente(idSito, idUtente);
                Toast.makeText(this, getResources().getString(R.string.aggiunto_fb), Toast.LENGTH_SHORT).show();
            }}

    }




    //lettura dati dal database online
    private void Fire(final SiteListCallback myCallback){

        key();
        preference = getApplicationContext().getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
        sitoList.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Sito");
        mDatabase.orderByChild("id_Curatore").equalTo(idUtente).addValueEventListener(new ValueEventListener() {

            SharedPreferences.Editor editor = preference.edit();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    String nome = snapshot1.child("nome_sito").getValue().toString();
                    String city = snapshot1.child("città_sito").getValue().toString();
                    String telefono = snapshot1.child("telefono_sito").getValue().toString();;
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
                SharedPreferences preference = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preference.edit();

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String id_ute = snapshot1.getKey();
                        editor.putString("Id_Utente",id_ute);
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
    private String Lettura_local(){
        String Nome_sito,città_sito,telefono_sito,email_sito,orario_apertura,orario_chiusura,Id_Curatore;
        String data = null;
        key();
        preference = getApplicationContext().getSharedPreferences("MyPrefsFile",MODE_PRIVATE);


        Cursor cursor = localdb.readSito(idUtente);

        if(cursor.moveToFirst() ) {

            do {

                Nome_sito = cursor.getString(1);
                città_sito = cursor.getString(2);
                email_sito = cursor.getString(3);
                telefono_sito = cursor.getString(4);
                orario_apertura = cursor.getString(5);
                orario_chiusura = cursor.getString(6);
                data = cursor.getString(8);

                cursor.moveToNext();


            } while (!cursor.isAfterLast() );
        }
        return data;
    }
    //aggiornamento dati dal database locale dopo che il curatore li inserisce
    private void Update_sql(){


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
    private Boolean telNonValido(String Telefono){
        Boolean isValid=true;
        if(Telefono.length()!=10){
            Toast.makeText(getApplicationContext(), teleValid,Toast.LENGTH_LONG).show();
            return false;
        }

        for(int i=0;i<Telefono.length();i++){
            char c= Telefono.charAt(i);
            if(c >= '0' && c <= '9') {
                continue;
            }
            isValid=false;
        }

        if(isValid){
            return true;
        }
        else{
            Toast.makeText(getApplicationContext(), teleValid,Toast.LENGTH_LONG).show();
            return false;
        }

    }
}
