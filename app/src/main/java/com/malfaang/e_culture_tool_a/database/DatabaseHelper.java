package com.malfaang.e_culture_tool_a.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Tabella Utente
    public static final String TABELLA_UTENTE = "utente";
    public static final String COL_ID_UTENTE = "ID_UTENTE";
    public static final String COL_NOME_UTENTE = "NOME_UTENTE";
    public static final String COL_COGNOME_UTENTE = "COGNOME_UTENTE";
    public static final String COL_UTENTE_MAIL = "EMAIL_UTENTE";
    public static final String COL_PASSWORD_UTENTE = "PASSWORD_UTENTE";
    public static final String COL_CODICE_CATEGORIA_UTENTE = "CODICE_CATEGORIA_UTENTE";

    //Tabella Luogo
    public static final String TABELLA_LUOGO= "TABELLA_LUOGO";
    public static final String COL_ID_LUOGO = "ID_LUOGO";
    public static final String COL_NOME_LUOGO= "NOME_LUOGO";
    public static final String COL_INDIRIZZO_LUOGO = "INDIRIZZO_LUOGO";
    public static final String COL_TIPOLOGIA_LUOGO = "TIPOLOGIA_LUOGO";

    //Tabella Zona
    public static final String TABELLA_ZONA = "zona";
    public static final String COL_NOME_ZONA= "NOME_ZONA";
    public static final String COL_ID_ZONA = "ID_ZONA";
    public static final String COL_CAPIENZA_OGGETTI_ZONA = "CAPIENZA_OGGETTI_ZONA";
    public static final String COL_NUMERO_OGGETTI_ZONA= "NUMERO_OGGETTI_ZONA";

    //Tabella Oggetto
    public static final String TABELLA_OGGETTO = "oggetto";
    public static final String COL_ID_OGGETTO = "ID_OGGETTO";
    public static final String COL_NOME_OGGETTO = "NOME_OGGETTO";
    public static final String COL_DESCRIZIONE_OGGETTO = "DESCRIZIONE_OGGETTO";
    public static final String COL_FOTO_OGGETTO = "FOTO_OGGETTO";
    public static final String COL_CODICE_QR= "CODICE_QR";
    public static final String COL_ATTIVITA_ASSOCCIATA = "ATTIVITA_ASSOCIATA";
    public static final String COL_CODICE_IOT= "CODICE_IOT";

    //Tabella Chiave Esterna (FK)
    public static final String TABELLA_CHIAVE_ESTERNA = "chiaveEsterna";
    public static final String COL_ID_CHIAVE_ESTERNA = "ID_CHIAVE_ESTERNA";
    public static final String COL_FK_ID_UTENTE = "ID_UTENTE";
    public static final String COL_FK_ID_LUOGO = "ID_LUOGO";
    public static final String COL_FK_ID_ZONA = "ID_ZONA";
    public static final String COL_FK_ID_OGGETTO = "ID_OGGETTO";


    static final String DB_NAME = "EcultureDb";
    static final int DB_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryCreazioneUtente = "CREATE TABLE " + TABELLA_UTENTE + " (" +
                COL_ID_UTENTE + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOME_UTENTE + "TEXT NOT NULL," +
                COL_COGNOME_UTENTE + "TEXT," +
                COL_UTENTE_MAIL + "TEXT NOT NULL," +
                COL_PASSWORD_UTENTE + "TEXT NOT NULL," +
                COL_CODICE_CATEGORIA_UTENTE + "TEXT NOT NULL)";
        String queryCreazioneLuogo = "CREATE TABLE " + TABELLA_LUOGO + " (" +
                COL_ID_LUOGO + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOME_LUOGO + "TEXT NOT NULL," +
                COL_INDIRIZZO_LUOGO + "TEXT NOT NULL," +
                COL_TIPOLOGIA_LUOGO + "TEXT)";
        String queryCreazioneZona = "CREATE TABLE " + TABELLA_ZONA + " (" +
                COL_ID_ZONA + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOME_ZONA + "TEXT NOT NULL, " +
                COL_CAPIENZA_OGGETTI_ZONA + "INTEGER NOT NULL, " +
                COL_NUMERO_OGGETTI_ZONA + "INTEGER NOT NULL)";
        String queryCreazioneOggetto = "CREATE TABLE " + TABELLA_OGGETTO + " (" +
                COL_ID_OGGETTO + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOME_OGGETTO + "TEXT NOT NULL," +
                COL_DESCRIZIONE_OGGETTO + "TEXT NOT NULL," +
                COL_FOTO_OGGETTO + "BYTE[]," + //La foto va convertita in una stringa di numeri per poter essere memorizzata
                COL_CODICE_QR + "TEXT," + //Non bisogna salvare propriamente lui, ma il testo a cui si riferisce il QR CODE
                COL_CODICE_IOT + "TEXT)";

        sqLiteDatabase.execSQL(queryCreazioneUtente);
        sqLiteDatabase.execSQL(queryCreazioneLuogo);
        sqLiteDatabase.execSQL(queryCreazioneZona);
        sqLiteDatabase.execSQL(queryCreazioneOggetto);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
