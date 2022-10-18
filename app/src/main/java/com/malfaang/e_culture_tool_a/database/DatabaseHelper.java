package com.malfaang.e_culture_tool_a.database;

import static com.malfaang.e_culture_tool_a.database.ConstantTable.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "EcultureDb";
    static final int DB_VERSION = 1;

    // --------------------------- INIZIO QUERY ----------------------------------
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
            COL_FK_ID_LUOGO + "INTEGER NOT NULL REFERENCES TABELLA_LUOGO (COL_ID_LUOGO)," +
            COL_NOME_ZONA + "TEXT NOT NULL, " +
            COL_CAPIENZA_OGGETTI_ZONA + "INTEGER NOT NULL, " +
            COL_NUMERO_OGGETTI_ZONA + "INTEGER NOT NULL)";
    String queryCreazioneOggetto = "CREATE TABLE " + TABELLA_OGGETTO + " (" +
            COL_ID_OGGETTO + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_FK_ID_ZONA + "INTEGER NOT NULL REFERENCES TABELLA_ZONA (COL_ID_ZONA)," +
            COL_NOME_OGGETTO + "TEXT NOT NULL," +
            COL_DESCRIZIONE_OGGETTO + "TEXT NOT NULL," +
            COL_FOTO_OGGETTO + "BYTE[]," + //La foto va convertita in una stringa di numeri per poter essere memorizzata
            COL_CODICE_QR + "TEXT," + //Non bisogna salvare propriamente lui, ma il testo a cui si riferisce il QR CODE
            COL_CODICE_IOT + "TEXT)";
    String queryCreazioneVisita = "CREATE TABLE " + TABELLA_VISITA + " (" +
            COL_ID_VISITA + "INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_FK_ID_LUOGO + "INTEGER NOT NULL REFERENCES LUOGO (COL_ID_LUOGO), " +
            COL_FK_ID_UTENTE + "INTEGER NOT NULL REFERENCES UTENTE (COL_ID_UTENTE))";
//          COL_FK_ID_LUOGO + "INTEGER NOT NULL, FOREIGN KEY (" + COL_ID_LUOGO + ") REFERENCES " + TABELLA_LUOGO + " (" + COL_ID_LUOGO + ") ON UPDATE CASCADE ON DELETE CASCADE, " +
//          COL_FK_ID_UTENTE + "INTEGER NOT NULL, FOREIGN KEY (" + COL_ID_UTENTE + ") REFERENCES "+ TABELLA_UTENTE+" ("+COL_ID_UTENTE+") ON UPDATE CASCADE ON DELETE CASCADE)";
    // --------------------------- FINE QUERY ----------------------------------


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(queryCreazioneUtente);
        sqLiteDatabase.execSQL(queryCreazioneLuogo);
        sqLiteDatabase.execSQL(queryCreazioneZona);
        sqLiteDatabase.execSQL(queryCreazioneOggetto);
        sqLiteDatabase.execSQL(queryCreazioneVisita);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
