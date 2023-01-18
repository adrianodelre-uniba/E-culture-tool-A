package com.malfaang.e_culture_tool_a.database;

public class ConstantTable {

    // TODO aggiungere le chiavi esterne alle relative tabelle

    //Tabella Utente
    public static final String TABELLA_UTENTE = "utente";
    public static final String COL_ID_UTENTE = "ID_UTENTE";
    public static final String COL_NOME_UTENTE = "NOME_UTENTE";
    public static final String COL_COGNOME_UTENTE = "COGNOME_UTENTE";
    public static final String COL_DATA_NASCITA_UTENTE = "DATA_NASCITA_UTENTE";
    public static final String COL_EMAIL_UTENTE = "EMAIL_UTENTE";
    public static final String COL_PASSWORD_UTENTE = "PASSWORD_UTENTE";
    public static final String COL_CODICE_CATEGORIA_UTENTE = "CODICE_CATEGORIA_UTENTE";

    // Tabella Curatore
    public static final String TABELLA_CURATORE = "curatore";
    public static final String COL_ID_CURATORE = "ID_CURATORE";
    public static final String COL_FK_ID_UTENTE = "ID_UTENTE";
    //Se il curatore gestisce un solo luogo => allora diventa PUBLIC STATIC FINAL STRING LUOGO
    //Altrimenti è una lista di luoghi e diventa PUBLIC STATIC FINAL LIST<STRING> LUOGHI, che sul db diventa
    // un VARCHAR che contiene un JSON che a sua volta conterrà dei luoghi. ALIAS per il visitatore.

    //Tabella Luogo
    public static final String TABELLA_LUOGO= "TABELLA_LUOGO";
    public static final String COL_ID_LUOGO = "ID_LUOGO";
    public static final String COL_NOME_LUOGO= "NOME_LUOGO";
    public static final String COL_INDIRIZZO_LUOGO = "INDIRIZZO_LUOGO";
    public static final String COL_TIPOLOGIA_LUOGO = "TIPOLOGIA_LUOGO";

    //Tabella Zona
    public static final String TABELLA_ZONA = "zona";
    public static final String COL_ID_ZONA = "ID_ZONA";
    public static final String COL_NOME_ZONA= "NOME_ZONA";
    public static final String COL_CAPIENZA_OGGETTI_ZONA = "CAPIENZA_OGGETTI_ZONA";
    public static final String COL_NUMERO_OGGETTI_ZONA= "NUMERO_OGGETTI_ZONA";

    //Tabella Oggetto TODO Questa la consideriamo come opera?
    // Il codice QR non serve, perchè esso conterrà l'ID dell'opera
    public static final String TABELLA_OGGETTO = "oggetto";
    public static final String COL_ID_OGGETTO = "ID_OGGETTO";
    public static final String COL_NOME_OGGETTO = "NOME_OGGETTO";
    public static final String COL_DESCRIZIONE_OGGETTO = "DESCRIZIONE_OGGETTO";
    public static final String COL_FOTO_OGGETTO = "FOTO_OGGETTO";
    public static final String COL_ATTIVITA_ASSOCCIATA = "ATTIVITA_ASSOCIATA";
    public static final String COL_CODICE_IOT= "CODICE_IOT";

    //Tabella Visita
    public static final String TABELLA_VISITA = "chiaveVisita";
    public static final String COL_ID_VISITA = "ID_VISITA";
    //public static final String COL_FK_ID_UTENTE = "ID_UTENTE";
    public static final String COL_FK_ID_LUOGO = "ID_LUOGO";
    public static final String COL_FK_ID_ZONA = "ID_ZONA";

    //Tabella del Valore Distanza (Distance Value)
    public static String TABELLA_DISTANZA_VALORE = "distanceValue";
    public static String COL_ID_OPERA1 = "COL_ID_OPERA1";
    public static String COL_ID_OPERA2= "COL_ID_OPERA2";;
    public static String COL_DISTANZA= "COL_DISTANZA";;


    public ConstantTable() {
    }
}
