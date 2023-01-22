package com.malfaang.e_culture_tool_a.database;

import static com.malfaang.e_culture_tool_a.database.ConstantTable.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.malfaang.e_culture_tool_a.io.GestioneFile;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper implements Serializable {

    public static final String DB_NAME = "EcultureDb";
    static final int DB_VERSION = 1;
    private static final long serialVersionUID = 1856014446291459063L;
    private static SQLiteDatabase db;

    // --------------------------- INIZIO QUERY CREAZIONE TABELLE ----------------------------------
    final String queryCreazioneUtente = "CREATE TABLE " + TABELLA_UTENTE + " (" +
            COL_ID_UTENTE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NOME_UTENTE + " TEXT NOT NULL," +
            COL_COGNOME_UTENTE + " TEXT NOT NULL," +
            COL_DATA_NASCITA_UTENTE + " TEXT NOT NULL," +
            COL_EMAIL_UTENTE + " TEXT NOT NULL," +
            COL_PASSWORD_UTENTE + " TEXT NOT NULL," +
            COL_CODICE_CATEGORIA_UTENTE + " TEXT )"; //TODO da aggiungere not null
    final String queryCreazioneLuogo = "CREATE TABLE " + TABELLA_LUOGO + " (" +
            COL_ID_LUOGO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NOME_LUOGO + " TEXT NOT NULL," +
            COL_INDIRIZZO_LUOGO + " TEXT NOT NULL," +
            COL_TIPOLOGIA_LUOGO + " TEXT)";
    final String queryCreazioneZona = "CREATE TABLE " + TABELLA_ZONA + " (" +
            COL_ID_ZONA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_FK_ID_LUOGO + " INTEGER NOT NULL REFERENCES TABELLA_LUOGO (COL_ID_LUOGO)," +
            COL_NOME_ZONA + " TEXT NOT NULL, " +
            COL_CAPIENZA_OGGETTI_ZONA + " INTEGER NOT NULL, " +
            COL_NUMERO_OGGETTI_ZONA + " INTEGER NOT NULL)";
    final String queryCreazioneOggetto = "CREATE TABLE " + TABELLA_OGGETTO + " (" +
            COL_ID_OGGETTO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_FK_ID_ZONA + " INTEGER NOT NULL REFERENCES TABELLA_ZONA (COL_ID_ZONA)," +
            COL_NOME_OGGETTO + " TEXT NOT NULL," +
            COL_DESCRIZIONE_OGGETTO + " TEXT NOT NULL," +
            COL_FOTO_OGGETTO + " BYTE," + //La foto va convertita in una stringa di numeri per poter essere memorizzata
            COL_CODICE_IOT + " TEXT)";
    final String queryCreazioneVisita = "CREATE TABLE " + TABELLA_VISITA + " (" +
            COL_ID_VISITA + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_FK_ID_LUOGO + " INTEGER NOT NULL REFERENCES LUOGO (COL_ID_LUOGO), " +
            COL_FK_ID_UTENTE + " INTEGER NOT NULL REFERENCES UTENTE (COL_ID_UTENTE))";
//          COL_FK_ID_LUOGO + "INTEGER NOT NULL, FOREIGN KEY (" + COL_ID_LUOGO + ") REFERENCES " + TABELLA_LUOGO + " (" + COL_ID_LUOGO + ") ON UPDATE CASCADE ON DELETE CASCADE, " +
//          COL_FK_ID_UTENTE + "INTEGER NOT NULL, FOREIGN KEY (" + COL_ID_UTENTE + ") REFERENCES "+ TABELLA_UTENTE+" ("+COL_ID_UTENTE+") ON UPDATE CASCADE ON DELETE CASCADE)";
    final String queryCreazioneDistanceValue = "CREATE TABLE " + TABELLA_DISTANZA_VALORE + " (" +
        COL_ID_OPERA1 + " INTEGER, " +
        COL_ID_OPERA2 + " INTEGER, " +
        COL_DISTANZA + " INTEGER, " +
        "PRIMARY KEY ("+ COL_ID_OPERA1 + ", " + COL_ID_OPERA2 +"))";
    // --------------------------- FINE QUERY CREAZIONE TABELLE ----------------------------------
//sharedpreferences

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();

    }

    @Override
    public final void onCreate(SQLiteDatabase sqLiteDatabase) {


        sqLiteDatabase.execSQL(this.queryCreazioneUtente);
        sqLiteDatabase.execSQL(this.queryCreazioneLuogo);
        sqLiteDatabase.execSQL(this.queryCreazioneZona);
        sqLiteDatabase.execSQL(this.queryCreazioneOggetto);
        sqLiteDatabase.execSQL(this.queryCreazioneVisita);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
public Cursor controllo(String email){
        String query = "SELECT " + COL_EMAIL_UTENTE + " FROM " + TABELLA_UTENTE + " WHERE " + COL_EMAIL_UTENTE + " = '" + email + "' " ;
    Cursor cursor=null;
    db=this.getReadableDatabase();

    if (db != null ){
        cursor = db.rawQuery(query,null);
    }
    return cursor;
}
    // TODO Provare boolean oppure facciamo un altro cursor e nel codice java verifichiamo se è già presente l'email e comparirà un TOAST che dice di cambiare email.

    public void insertUtente(String nome, String cognome, String dataNascita, String email, String password) {

        ContentValues values = new ContentValues();
        values.put(COL_NOME_UTENTE, nome);
        values.put(COL_COGNOME_UTENTE, cognome);
        values.put(COL_DATA_NASCITA_UTENTE, dataNascita);
        values.put(COL_EMAIL_UTENTE, email);
        values.put(COL_PASSWORD_UTENTE, password);
        values.put(COL_CODICE_CATEGORIA_UTENTE, Integer.valueOf(2)); //TODO da aggiungere nella schermata di registrazione => in tutte le parti del codice

        db.insert(TABELLA_UTENTE, null, values);
    }

    public void insertLuogo(String nomeLuogo, String indirizzoLuogo, String tipologiaLuogo) {

        ContentValues values = new ContentValues();
        values.put(COL_NOME_LUOGO, nomeLuogo);
        values.put(COL_INDIRIZZO_LUOGO, indirizzoLuogo);
        values.put(COL_TIPOLOGIA_LUOGO, tipologiaLuogo);

        db.insert(TABELLA_LUOGO, null, values);
    }

    public void insertVisita(String idUtente, String idLuogo) {

        ContentValues values = new ContentValues();
        values.put(COL_FK_ID_UTENTE, idUtente);
        values.put(COL_FK_ID_LUOGO, idLuogo);

        db.insert(TABELLA_VISITA, null, values);
    }

    public void insertZona(String nomeZona, String capienzaOggettiZona, String numeroOggettiZona) {

        ContentValues values = new ContentValues();
        values.put(COL_NOME_ZONA, nomeZona);
        values.put(COL_CAPIENZA_OGGETTI_ZONA, capienzaOggettiZona);
        values.put(COL_NUMERO_OGGETTI_ZONA, numeroOggettiZona);

        db.insert(TABELLA_ZONA, null, values);
    }

    public void insertOggetto(String nomeOggetto, String descrizioneOggetto, String fotoOggetto, String codiceQR, String attivitaAssociata, String codiceIOT) {

        ContentValues values = new ContentValues();
        values.put(COL_NOME_OGGETTO, nomeOggetto);
        values.put(COL_DESCRIZIONE_OGGETTO, descrizioneOggetto);
        values.put(COL_FOTO_OGGETTO, fotoOggetto);
        values.put(COL_ATTIVITA_ASSOCCIATA, attivitaAssociata);
        values.put(COL_CODICE_IOT, codiceIOT);

        db.insert(TABELLA_OGGETTO, null, values);
    }

    public List<String> selectUtente(String email) {

        String queryString = "SELECT " + COL_EMAIL_UTENTE + ", " + COL_PASSWORD_UTENTE + " FROM " + TABELLA_UTENTE + " WHERE " + COL_EMAIL_UTENTE + " = " + "'"+email+"'";
        System.out.println(queryString);

        List<String> results = new ArrayList<>();
        SQLiteDatabase db2 = getReadableDatabase();
        Cursor cursor = db2.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
                results.add(cursor.getString(0));
            results.add(cursor.getString(1));

        } else {
            cursor.close();
            db2.close();
        }
        return results;
    }

    public void serializzaDB(){
        GestioneFile gf = new GestioneFile();
        gf.serializzazione("DatabaseSerializzato.txt", db);
    }

    public SQLiteDatabase deserializzaDB() throws IOException {
        GestioneFile gf2 = new GestioneFile();
        return (SQLiteDatabase) gf2.deSerializzazione("DatabaseSerializzato.txt");
    }

    public static SQLiteDatabase getDb() {
        return db;
    }

    // TODO Completare implementazione di serializzazione del DB
    // TODO Completare implementazione di deserializzazione del DB
    // TODO Implementare salvataggio e manipolazione dati tramite SharedPreferences

    public void insertDistanceValue(int idOpera1, int idOpera2, int distance){
        ContentValues values = new ContentValues();
        values.put(COL_ID_OPERA1, idOpera1);
        values.put(COL_ID_OPERA2, idOpera2);
        values.put(COL_DISTANZA, distance);

        db.insert(TABELLA_DISTANZA_VALORE, null, values);

    }

    public int selectDistanceValue(int idOpera1, int idOpera2){
        int distanceValue = 0;
        String queryString = "SELECT " + COL_DISTANZA + " FROM " + TABELLA_DISTANZA_VALORE + " WHERE " + COL_ID_OPERA1 + " = " + "'"+idOpera1+"'" +
                " AND " + COL_ID_OPERA2 + " = " + "'"+idOpera2+"'";
        System.out.println(queryString); // Per Debug gg

//        List<String> results = new ArrayList<>();
        SQLiteDatabase db2 = getReadableDatabase();
        Cursor cursor = db2.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            distanceValue = cursor.getInt(0);
        } else {
            cursor.close();
            db2.close();
        }
        return distanceValue;
    }

    public void insertOpera(String titolo, String descrizione) {
        ContentValues values = new ContentValues();
        values.put(COL_NOME_OGGETTO, titolo);
        values.put(COL_DESCRIZIONE_OGGETTO, descrizione);
        // TODO inserire gli altri campi della tabella
        // TODO Cambiare il nome da OGGETTO AD OPERA
        db.insert(TABELLA_OGGETTO, null, values);

    }

    // Update per modificare tramite il QR
    public void updateOpera(String titolo, String descrizione, int id) {
        ContentValues values = new ContentValues();
        values.put(COL_NOME_OGGETTO, titolo);
        values.put(COL_DESCRIZIONE_OGGETTO, descrizione);
        // TODO inserire gli altri campi della tabella
        // TODO Cambiare il nome da OGGETTO AD OPERA
        db.update(TABELLA_OGGETTO, values, COL_ID_OGGETTO + "=?", new String[]{String.valueOf(id)});

    }

    public Cursor controlloCredenzialiLogin(String email, String password) {
        String query = "SELECT " + COL_EMAIL_UTENTE + "," + COL_PASSWORD_UTENTE + " FROM " + TABELLA_UTENTE + " WHERE "
                + COL_EMAIL_UTENTE + " = '" + email + "' AND " + COL_PASSWORD_UTENTE + " = '" + password + "' " ;
        Cursor cursor=null;
        db=this.getReadableDatabase();

        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }
}
