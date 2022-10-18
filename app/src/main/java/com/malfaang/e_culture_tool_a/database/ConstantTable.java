package com.malfaang.e_culture_tool_a.database;

public class ConstantTable {

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

    //Tabella Visita
    public static final String TABELLA_VISITA = "chiaveVisita";
    public static final String COL_ID_VISITA = "ID_VISITA";
    public static final String COL_FK_ID_UTENTE = "ID_UTENTE";
    public static final String COL_FK_ID_LUOGO = "ID_LUOGO";
    public static final String COL_FK_ID_ZONA = "ID_ZONA";
}
