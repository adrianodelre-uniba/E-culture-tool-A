package com.malfaang.e_culture_tool_a;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    private Context context;
    SQLiteDatabase db;

    private static final String DATABASE_NAME = "Tool.db";
    private static final int DATABASE_VERSION = 1 ;
    private static final String TABLE_NAME = "Utente" ;
    private static final String ZONA = "Zona" ;
    private static final String TABLE_NAME5 = "Sito" ;

    private static final String Ordine_Zon = "Ordine_Zon" ;
    private static final String ITEMS = "Item";
    private static final String FOTO_ITEMS = "Foto_item";
    private static final String PERCORSO = "Percorsi";
    private static final String COLUM_ID = "_id" ;
    private static final String COLUM_NAME = "name" ;
    private static final String COLUM_SURNAME = "cognome" ;
    private static final String COLUM_EDGE_VALUE = "edge_value" ;
    private static final String COLUM_PASSWORD = "password" ;
    private static final String COLUM_EMAIL = "email" ;
    private static final String COLUM_TELEFONO = "telefono" ;
    private static final String COLUM_TIPOLOGIA = "tipologia" ;
    private static final String COLUM_CITTA = "citta" ;
    private static final String COLUM_TITOLO = "titolo" ;
    private static final String COLUM_CODICE = "Codice_iot" ;
    private static final String COLUM_DESC = "descrizione";
    private static final String COLUM_IMG = "immagine";
    private static final String COLUM_ID_FOTO = "id_foto";
    private static final String COLUM_ID_OGGETTO = "id_oggetto";
    private static final String COLUM_ID_OGGETTO1 = "id_oggetto1";
    private static final String COLUM_BACK= "Back";
    private static final String COLUM_ID_SITO = "id_sito";
    private static final String COLUM_ID_ZONA = "id_zona";
    private static final String COLUM_ID_ZONA1 = "id_zona1";
    private static final String COLUM_ID_ZONA2 = "id_zona2";
    private static final String COLUM_ID_PERCORSO = "id_percorso";
    private static final String COLUM_ORARIO_APERTURA ="orario_apertura";
    private static final String COLUM_ORARIO_CHIUSURA = "orario_chiusura";
    private static final String COLUM_DATA_MODIFICA = " data_ultima_modifica ";
    private static final String COLUM_ID_UTENTE = "id_utente_sito";
    private static final String COLUM_ID_FIRE = "Id_zona_Fire";
    private static final String SITO_UTENTE = "Sito_Utente";





    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context ;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query =
                " CREATE TABLE " + TABLE_NAME +
                        " ( " +  COLUM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUM_NAME + " TEXT, "
                        + COLUM_SURNAME + " TEXT, "
                        + COLUM_PASSWORD + " TEXT, "
                        + COLUM_EMAIL + " TEXT, "
                        + COLUM_TELEFONO + " INTEGER,"
                        + COLUM_TIPOLOGIA + " TEXT); ";


        String query2 =
                " CREATE TABLE " + PERCORSO +
                        " ( " +  COLUM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUM_DESC + " TEXT, "
                        + COLUM_TIPOLOGIA + " TEXT, "
                        + COLUM_TITOLO + " TEXT, "
                        + COLUM_DATA_MODIFICA + " TEXT, "
                        + COLUM_ID_PERCORSO + " TEXT, "
                        + COLUM_ID_SITO + " TEXT,"
                        + COLUM_BACK +" BOOLEAN,"
                        + "FOREIGN KEY ( " + COLUM_ID_SITO + " ) REFERENCES "+ TABLE_NAME5 + "("+COLUM_ID+")"+ "ON UPDATE CASCADE ON DELETE CASCADE ); " ;

        String query4 =
                " CREATE TABLE " + ZONA +
                        " ( " +  COLUM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUM_DESC + " TEXT ,"
                        + COLUM_TITOLO + " TEXT ,"
                        + COLUM_TIPOLOGIA + " TEXT, "
                        + COLUM_ID_SITO + " TEXT,"
                        + COLUM_ID_FIRE + " TEXT,"
                        + "FOREIGN KEY ( " + COLUM_ID_SITO + " ) REFERENCES "+ TABLE_NAME5 + "("+COLUM_ID+")"+ "ON UPDATE CASCADE ON DELETE CASCADE ); " ;
        String query5 =
                " CREATE TABLE " + TABLE_NAME5 +
                        " ( " +  COLUM_ID + " TEXT, "
                        + COLUM_TITOLO + " TEXT, "
                        + COLUM_CITTA + " TEXT, "
                        + COLUM_EMAIL + " TEXT, "
                        + COLUM_TELEFONO + " INTEGER, "
                        + COLUM_ORARIO_APERTURA + " TEXT, "
                        + COLUM_ORARIO_CHIUSURA + " TEXT, "
                        + COLUM_DATA_MODIFICA + " TEXT , "
                        + COLUM_ID_UTENTE + " INTEGER, "
                        + "PRIMARY KEY ('"+COLUM_ID+ "'),"
                        +  " FOREIGN KEY (" + COLUM_ID_UTENTE  + ") REFERENCES " + TABLE_NAME + "("+ COLUM_ID +")" + " ON UPDATE CASCADE ON DELETE CASCADE   );" ;



        String query8 =
                " CREATE TABLE " + ITEMS +
                        " ( " +  COLUM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUM_TITOLO + " TEXT, "
                        + COLUM_TIPOLOGIA + " TEXT, "
                        + COLUM_DESC + " TEXT ,"
                        + COLUM_ID_ZONA + " TEXT,"
                        + COLUM_ID_SITO + " TEXT,"
                        +  " FOREIGN KEY (" + COLUM_ID_SITO  + ") REFERENCES " + TABLE_NAME5 + "("+ COLUM_ID +")" + " ON UPDATE CASCADE ON DELETE CASCADE  ,"
                        +  " FOREIGN KEY (" + COLUM_ID_ZONA  + ") REFERENCES " + ZONA + "("+ COLUM_ID +")" + " ON UPDATE CASCADE ON DELETE CASCADE   );" ;
        String query9 =
                " CREATE TABLE " + FOTO_ITEMS+
                        " ( " +  COLUM_ID_FOTO + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUM_IMG + " BLOB,"
                        + COLUM_ID_OGGETTO + " TEXT, "
                        + " FOREIGN KEY (" + COLUM_ID_OGGETTO  + ") REFERENCES " + ITEMS + "("+ COLUM_ID +")" + " ON UPDATE CASCADE ON DELETE CASCADE );";
        String query10 =
                " CREATE TABLE " + Ordine_Zon +
                        " ("  + COLUM_ID_PERCORSO + " TEXT, "
                        + COLUM_ID_ZONA1 + " TEXT, "
                        + COLUM_ID_ZONA2 + " TEXT, "
                        + COLUM_EDGE_VALUE + " TEXT ,"
                        + " PRIMARY KEY ( " + COLUM_ID_PERCORSO + " , " + COLUM_ID_ZONA1 + " , " + COLUM_ID_ZONA2 + "),"
                        +  " FOREIGN KEY (" + COLUM_ID_ZONA1  + ") REFERENCES " + ZONA + "("+ COLUM_ID +")" + " ON UPDATE CASCADE ON DELETE CASCADE   ,"
                        +  " FOREIGN KEY (" + COLUM_ID_ZONA2  + ") REFERENCES " + ZONA + "("+ COLUM_ID +")" + " ON UPDATE CASCADE ON DELETE CASCADE   ,"
                        + "FOREIGN KEY ( " + COLUM_ID_PERCORSO + " ) REFERENCES "+ PERCORSO + "("+COLUM_ID+")"+ "ON UPDATE CASCADE ON DELETE CASCADE ); " ;
        String query11 =
                " CREATE TABLE " + SITO_UTENTE +
                        " ( " + COLUM_ID_SITO + " TEXT , "
                + COLUM_ID_UTENTE + " TEXT ,"
                + " PRIMARY KEY  ( '" + COLUM_ID_SITO + "','" + COLUM_ID_UTENTE + "'), "
                +  " FOREIGN KEY (" + COLUM_ID_SITO  + ") REFERENCES " + TABLE_NAME5 + "("+ COLUM_ID +")" + " ON UPDATE CASCADE ON DELETE CASCADE  ,"
                        +  " FOREIGN KEY (" + COLUM_ID_UTENTE  + ") REFERENCES " + TABLE_NAME5 + "("+ COLUM_ID_UTENTE +")" + " ON UPDATE CASCADE ON DELETE CASCADE   );" ;




        db.execSQL(query);
        db.execSQL(query2);
        db.execSQL(query4);
        db.execSQL(query5);
        db.execSQL(query11);
        db.execSQL(query8);
        db.execSQL(query9);
        db.execSQL(query10);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);

        db.execSQL(" DROP TABLE IF EXISTS " + ZONA);

        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME5);

        db.execSQL(" DROP TABLE IF EXISTS " + PERCORSO);

        db.execSQL(" DROP TABLE IF EXISTS " + SITO_UTENTE);

        db.execSQL(" DROP TABLE IF EXISTS " + ITEMS);

        db.execSQL(" DROP TABLE IF EXISTS " + FOTO_ITEMS);

        db.execSQL(" DROP TABLE IF EXISTS " + Ordine_Zon);



        onCreate(db);

    }
    
    public void close() {
        this.db.close();
    }


    public Cursor readAllRoute(String id_sito){

        String query =" SELECT "  + COLUM_ID+","+ COLUM_TITOLO + "," + COLUM_DESC + "," + COLUM_TIPOLOGIA
                + "," + COLUM_ID_PERCORSO + "," + COLUM_ID_SITO + "," + COLUM_DATA_MODIFICA +","+COLUM_BACK +" FROM "+ PERCORSO + " WHERE " + COLUM_ID_SITO + " = '" + id_sito + "'";
        Cursor cursor = null ;
        db= this.getReadableDatabase();
        if( db != null) {
            cursor = db.rawQuery(query, null) ;
        }
        return cursor ;
    }

    //Legge tutti i percorsi che comprendono l'area selezionata
    public Cursor readAllRouteArea(int id_area){

        String query =" SELECT "  + COLUM_ID+","+ COLUM_TITOLO + "," + COLUM_DESC + "," + COLUM_TIPOLOGIA
                + ", P." + COLUM_ID_PERCORSO + "," + COLUM_ID_SITO + "," + COLUM_DATA_MODIFICA+","+COLUM_BACK
                +" FROM "+ PERCORSO + " P "
                + " JOIN " +  Ordine_Zon + " OZ" + " ON P." + COLUM_ID + " = OZ." + COLUM_ID_PERCORSO
                + " WHERE " + COLUM_ID_ZONA1 + " = '" + id_area + "'";

        Cursor cursor = null ;
        db= this.getReadableDatabase();
        if( db != null) {
            cursor = db.rawQuery(query, null) ;
        }
        return cursor ;
    }

    public Cursor readAllArea(String id_sito){

        String query =" SELECT "  + COLUM_ID+","+ COLUM_TITOLO + "," + COLUM_DESC + "," + COLUM_TIPOLOGIA + ","
             + COLUM_ID_SITO +" FROM "+ ZONA + " WHERE " + COLUM_ID_SITO + " = '" + id_sito + "'";
        Cursor cursor = null ;
        db= this.getReadableDatabase();
        if( db != null) {
            cursor = db.rawQuery(query, null) ;
        }
        return cursor ;
    }

    public Cursor readAllItem(String id_sito){

        String query =" SELECT "  + COLUM_ID+","+ COLUM_TITOLO + "," + COLUM_DESC + "," + COLUM_TIPOLOGIA
                + "," + COLUM_ID_ZONA + "," + COLUM_ID_SITO + " , " +COLUM_IMG +
                " FROM "+ ITEMS + " , "+ FOTO_ITEMS + " F "+
                " WHERE " + COLUM_ID_SITO + " = '" + id_sito + "' AND " + COLUM_ID + " = " + "  F." + COLUM_ID_OGGETTO;
        Cursor cursor = null ;
        db= this.getReadableDatabase();
        if( db != null) {
            cursor = db.rawQuery(query, null) ;
        }
        return cursor ;
    }

    public Cursor readItemByAre2a(String id_area){
        String query="SELECT I. "+ COLUM_ID +", I. "+ COLUM_TITOLO+ " , I."+ COLUM_TIPOLOGIA + " , I. " + COLUM_DESC + ", F." + COLUM_IMG + " , I. " + COLUM_ID_SITO + " , I. " + COLUM_ID_ZONA +
                "  FROM " + ITEMS + " I," + FOTO_ITEMS + " F "+
                " WHERE  " + "  F." + COLUM_ID_OGGETTO + " = I. " + COLUM_ID + " AND I." + COLUM_ID_ZONA + " = '" + id_area +"'";
        Cursor cursor=null;
        db=this.getReadableDatabase();
        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }



    public String insertRoute(String desc, String tipo, String titolo, String key_route, String id_sito, String formattedDate,Boolean go_back){
        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues cv = new ContentValues() ;
        cv.put(COLUM_TITOLO, titolo);
        cv.put(COLUM_DESC, desc);
        cv.put(COLUM_TIPOLOGIA, tipo);
        cv.put(COLUM_ID_SITO,id_sito);
        cv.put(COLUM_DATA_MODIFICA,formattedDate);
        cv.put(COLUM_ID_PERCORSO,key_route);
        cv.put(COLUM_BACK,go_back);

        long res = db.insert(PERCORSO, null , cv) ;
        if( res == -1 )
            return "Failed" ;
        else
            return "Success" ;
    }

    public String insertRouteZone(String id_zona,String id_percorso,String id_zona2,String edgeValue){
        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(COLUM_ID_PERCORSO,id_percorso);
        cv.put(COLUM_ID_ZONA1,id_zona);
        cv.put(COLUM_ID_ZONA2,id_zona2);
        cv.put(COLUM_EDGE_VALUE,edgeValue);

        long res = db.insert(Ordine_Zon, null , cv) ;
        if( res == -1 )
            return "Failed" ;
        else
            return "Success" ;
    }

    public Cursor readAreas(String id){
        String query=" SELECT " + COLUM_ID +  " , " + COLUM_TITOLO + " , " + COLUM_DESC + ", "+ COLUM_TIPOLOGIA + ", " + COLUM_ID_SITO + ", " + COLUM_ID_FIRE
                +  " FROM "+ ZONA
                + " WHERE " + COLUM_ID_SITO + " = '" + id +"'";
        Cursor cursor=null;
        db=this.getReadableDatabase();

        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    //Restituisce l'id dell'area con id_fire
    public Cursor readAreasID(String id_fire){
        String query=" SELECT " + COLUM_ID
                +  " FROM "+ ZONA
                + " WHERE " + COLUM_ID_FIRE + " = '" + id_fire +"'" ;
        Cursor cursor=null;
        db=this.getReadableDatabase();

        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }


    public Cursor readAreasOrder(String id_percorso){
        String query="SELECT DISTINCT "+ COLUM_ID +  " , " + COLUM_TITOLO + " , " + COLUM_DESC + ", "+ COLUM_TIPOLOGIA + ", " + COLUM_ID_SITO
                +" FROM "+ZONA+","+Ordine_Zon
                +" WHERE "+ COLUM_ID +" = "+COLUM_ID_ZONA1+" AND "+ COLUM_ID_PERCORSO+" = "+ id_percorso;

        Cursor cursor=null;
        db=this.getReadableDatabase();

        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;

    }

    public Cursor readSecondAreasOrder(String id_percorso){
        String query="SELECT "+ COLUM_ID_ZONA1 + " , " + COLUM_ID_ZONA2
                +" FROM "+ Ordine_Zon
                +" WHERE "+ COLUM_ID_PERCORSO + " = " + id_percorso;

        Cursor cursor=null;
        db=this.getReadableDatabase();

        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;

    }

    //restituisce l'area partendo dall'id passato come attributo
    public Cursor readArea(String ID){
        String query="SELECT "+ COLUM_ID +", "+ COLUM_TITOLO+ " , "+ COLUM_TIPOLOGIA + " , " + COLUM_DESC + ", " + COLUM_ID_SITO +
                "  FROM " + ZONA +
                " WHERE  " + COLUM_ID + " = " + ID ;
        Cursor cursor=null;
        db=this.getReadableDatabase();
        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor readItem(String ID){
        String query="SELECT I. "+ COLUM_ID +", I. "+ COLUM_TITOLO+ " , I."+ COLUM_TIPOLOGIA + " , I. " + COLUM_DESC + ", F." + COLUM_IMG + " , I. " + COLUM_ID_SITO + " , I. " + COLUM_ID_ZONA +
                "  FROM " + ITEMS + " I," + FOTO_ITEMS + " F "+
                " WHERE  " + "  F." + COLUM_ID_OGGETTO + " = I. " + COLUM_ID + " AND I." + COLUM_ID_SITO + " = '" + ID +"'";
        Cursor cursor=null;
        db=this.getReadableDatabase();
        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor readItemByArea(String id_area){
        String query="SELECT I. "+ COLUM_ID +", I. "+ COLUM_TITOLO+ " , I."+ COLUM_TIPOLOGIA + " , I. " + COLUM_DESC + ", F." + COLUM_IMG + " , I. " + COLUM_ID_SITO + " , I. " + COLUM_ID_ZONA +
                "  FROM " + ITEMS + " I," + FOTO_ITEMS + " F "+
                " WHERE  " + "  F." + COLUM_ID_OGGETTO + " = I. " + COLUM_ID + " AND I." + COLUM_ID_ZONA + " = '" + id_area +"'";
        Cursor cursor=null;
        db=this.getReadableDatabase();
        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor readItem_show(String ID , String tit){
        String query="SELECT I. "+ COLUM_ID +", I. "+ COLUM_TITOLO+ " , I."+ COLUM_TIPOLOGIA + " , I. " + COLUM_DESC + ", F." + COLUM_IMG + " , I. " + COLUM_ID_SITO + " , I. " + COLUM_ID_ZONA +
                "  FROM " + ITEMS + " I," + FOTO_ITEMS + " F "+
                " WHERE  " + "  F. " + COLUM_ID_OGGETTO + " = I. " + COLUM_ID + " AND I."
                + COLUM_ID_SITO + " = '" + ID +"' AND I. " + COLUM_TITOLO + "  =  '" + tit + "'";
        Cursor cursor=null;
        db=this.getReadableDatabase();
        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }
//Funzione che sta dentro il file qrcodeActivity per leggere i dati dopo la scansione del qrcode dell'opera
    public Cursor readItem_show2(String ID , String item){
        String query="SELECT I. "+ COLUM_ID +", I. "+ COLUM_TITOLO+ " , I."+ COLUM_TIPOLOGIA +
                " , I. " + COLUM_DESC + ", F." + COLUM_IMG + " , I. " + COLUM_ID_SITO + " , I. " + COLUM_ID_ZONA +
                "  FROM " + ITEMS + " I," + FOTO_ITEMS + " F "+
                " WHERE  " + "  F. " + COLUM_ID_OGGETTO + " = I. " + COLUM_ID + " AND I."
                + COLUM_ID_SITO + " = '" + ID +"' AND I." + COLUM_ID + " = '" + item + "'";
        Cursor cursor=null;
        db=this.getReadableDatabase();
        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }




    public Cursor readSito(String ID){
        String query="SELECT S. "+ COLUM_ID +", S. "+ COLUM_TITOLO+ " , S."+ COLUM_CITTA + " , S. " + COLUM_EMAIL + ", S." + COLUM_TELEFONO + " , S. " + COLUM_ORARIO_APERTURA + " , S. " + COLUM_ORARIO_CHIUSURA +
                ", S."+ COLUM_ID_UTENTE + " ,S. " + COLUM_DATA_MODIFICA +"  FROM " + TABLE_NAME5+ " S"+
                " WHERE  " + "  S." + COLUM_ID_UTENTE + " ='"+ID+"'" ;
        Cursor cursor=null;
        db=this.getReadableDatabase();
        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor getSitoByKey(String id_sito){
        String query=" SELECT " +COLUM_ID+ " FROM " +TABLE_NAME5+" WHERE "+COLUM_ID+" = '"+id_sito+"'";
        Cursor cursor=null;
        db=this.getReadableDatabase();
        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public String addRecord_Utente(String nam, String cogn, String pass,String emai, String tele, String tipo,String key_fire){

        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues cv = new ContentValues() ;
        cv.put(COLUM_NAME, nam);
        cv.put(COLUM_SURNAME, cogn);
        cv.put(COLUM_PASSWORD, pass);
        cv.put(COLUM_EMAIL, emai);
        cv.put(COLUM_TELEFONO, tele);
        cv.put(COLUM_TIPOLOGIA, tipo);
        cv.put(COLUM_ID_FIRE, key_fire);



        long res = db.insert(TABLE_NAME, null , cv) ;

        if( res == -1  )
            return "Failed" ;
        else
            return "Success" ;

    }


    public String addRecord_Area(String titolo, String desc, String tipo,String id,String key){

        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues cv = new ContentValues() ;
        ContentValues pv = new ContentValues() ;
        cv.put(COLUM_TITOLO, titolo);
        cv.put(COLUM_DESC, desc);
        cv.put(COLUM_TIPOLOGIA, tipo);
        cv.put(COLUM_ID_SITO, id);
        cv.put(COLUM_ID_FIRE,key);



        long res = db.insert(ZONA, null , cv) ;

        if( res == -1  )
            return "Failed" ;
        else
            return "Success" ;

    }

    public String addRecordSito(String id_sito, String titolo, String city, String email, String telefono, String orario_apertura,
                                String orario_chiusura, String data_ultima_modifica, String id_ute){

        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues cv = new ContentValues() ;
        cv.put(COLUM_ID,id_sito);
        cv.put(COLUM_TITOLO,titolo);
        cv.put(COLUM_CITTA, city);
        cv.put(COLUM_EMAIL, email);
        cv.put(COLUM_TELEFONO, telefono);
        cv.put(COLUM_ORARIO_APERTURA, orario_apertura);
        cv.put(COLUM_ORARIO_CHIUSURA, orario_chiusura);
        cv.put(COLUM_DATA_MODIFICA, data_ultima_modifica);
        cv.put(COLUM_ID_UTENTE, id_ute);



        long res = db.insert(TABLE_NAME5, null , cv) ;

        if( res == -1  )
            return "Failed" ;
        else
            return "Success" ;

    }
    public String addRecordSitoUtente(String id_sito, String id_utente){
        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(COLUM_ID_SITO,id_sito);
        cv.put(COLUM_ID_UTENTE,id_utente);


        long res = db.insert(SITO_UTENTE, null , cv) ;
        if( res == -1 )
            return "Failed" ;
        else
            return "Success" ;
    }

    public Cursor getLastArea(){

        String query = " SELECT " + COLUM_ID +" FROM " + ZONA + " ORDER BY " + COLUM_ID +  " DESC LIMIT 1" ;
        Cursor cursor=null;
        db=this.getReadableDatabase();
        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;

    }

    public String add_Ordine_zona(String Id_area, String Id_percorso, String Id_area2,String edge_value){
        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues cv = new ContentValues() ;
        cv.put(COLUM_ID_PERCORSO, Id_percorso);
        cv.put(COLUM_ID_ZONA1, Id_area);
        cv.put(COLUM_ID_ZONA2, Id_area2);
        cv.put(COLUM_EDGE_VALUE, edge_value);





        long res = db.insert(Ordine_Zon, null , cv) ;
        if( res == -1 )
            return "Failed" ;
        else
            return "Success" ;
    }

    public String addRecord_Item(String titolo, String desc, String tipo ,String id_zona, String id_sito){
        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues cv = new ContentValues() ;
        cv.put(COLUM_TITOLO, titolo);
        cv.put(COLUM_DESC, desc);
        cv.put(COLUM_TIPOLOGIA, tipo);
        cv.put(COLUM_ID_ZONA,id_zona);
        cv.put(COLUM_ID_SITO,id_sito);




        long res = db.insert(ITEMS, null , cv) ;
        if( res == -1 )
            return "Failed" ;
        else
            return "Success" ;
    }


    public Cursor getLastItem(){
        String query = " SELECT " + COLUM_ID +" FROM " + ITEMS + " ORDER BY " + COLUM_ID +  " DESC LIMIT 1" ;
        Cursor cursor=null;
        db=this.getReadableDatabase();
        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }




    public String addFoto_Items(byte[] image,String id_item){
        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues cv = new ContentValues() ;
        cv.put(COLUM_IMG, image);
        cv.put(COLUM_ID_OGGETTO,id_item);
        long res = db.insert(FOTO_ITEMS, null , cv) ;

        if( res == -1 )
            return "Failed" ;
        else
            return "Success" ;
    }



    public String updateSql(String titolo, String city, String email, String telefono, String orario_apertura,
                            String orario_chiusura, String data_ultima_modifica, String id_ute){


        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues cv = new ContentValues() ;
        cv.put(COLUM_TITOLO,titolo);
        cv.put(COLUM_CITTA, city);
        cv.put(COLUM_EMAIL, email);
        cv.put(COLUM_TELEFONO, telefono);
        cv.put(COLUM_ORARIO_APERTURA, orario_apertura);
        cv.put(COLUM_ORARIO_CHIUSURA, orario_chiusura);
        cv.put(COLUM_DATA_MODIFICA, data_ultima_modifica);
        cv.put(COLUM_ID_UTENTE, id_ute);


        long res = db.update(TABLE_NAME5, cv , COLUM_ID_UTENTE + " = '" +  id_ute +"'" , null ) ;

        if( res == -1 )
            return "Failed" ;
        else
            return "Success" ;


    }

    public boolean delete_Zona(int i) {

       return db.delete(ZONA,COLUM_ID + " = " + i,null) > 0;
    }

    public boolean delete_OrderZona(String i) {

        return db.delete(Ordine_Zon,COLUM_ID_PERCORSO + " = '" + i+"'",null) > 0;
    }

    public boolean delete_OrderZona2(String id_percorso, String id_zona1) {

        return db.delete(Ordine_Zon,COLUM_ID_PERCORSO + " = '" + id_percorso +"' AND " + COLUM_ID_ZONA1 + " = '" + id_zona1 + "'" ,null) > 0;
        //Cancello la riga relativa alla zona che ho eliminato
    }


    public boolean delete_Item(int i) {
        return db.delete(ITEMS,COLUM_ID + " = " + i,null) > 0;
    }

    public boolean delete_Item_Image(String item_id) {
        return db.delete(FOTO_ITEMS,COLUM_ID_OGGETTO + " = " + item_id,null) > 0;
    }

    public boolean update_zona(int i, String Title, String Desc, String Typology) {

        SQLiteDatabase db = this.getWritableDatabase() ;
        ContentValues cv = new ContentValues() ;

        cv.put(COLUM_TITOLO,Title);
        cv.put(COLUM_DESC,Desc);
        cv.put(COLUM_TIPOLOGIA,Typology);

        return db.update(ZONA,cv,COLUM_ID + " = " +  i , null) != -1;
    }

    public boolean update_item(int i, String Title, String Desc, String Typology,  String id_zona) {

        SQLiteDatabase db = this.getWritableDatabase() ;
        ContentValues cv = new ContentValues() ;

        cv.put(COLUM_TITOLO,Title);
        cv.put(COLUM_DESC,Desc);
        cv.put(COLUM_TIPOLOGIA,Typology);
        cv.put(COLUM_ID_ZONA,id_zona);

        return db.update(ITEMS,cv,COLUM_ID + " = " +  i , null) != -1;
    }

    public boolean update_Foto_Items(int i, byte[] foto_it) {
        SQLiteDatabase db = this.getWritableDatabase() ;
        ContentValues cv = new ContentValues() ;

        cv.put(COLUM_IMG,foto_it);

        return db.update(FOTO_ITEMS,cv,COLUM_ID_FOTO+ " = " +  i , null) != -1;
    }

    public Cursor getidZona(String id_zonaFire) {


        String query = " SELECT " + COLUM_ID +" FROM " + ZONA + " WHERE " + COLUM_ID_FIRE + " ='"+id_zonaFire+"'";
        Cursor cursor=null;
        db=this.getReadableDatabase();
        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor getidRoute(String id_Fire) {


        String query = " SELECT " + COLUM_ID +" FROM " + PERCORSO + " WHERE " + COLUM_ID_PERCORSO + " ='"+id_Fire+"'";
        Cursor cursor=null;
        db=this.getReadableDatabase();
        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }


    public Cursor getLastRoute() {
        String query = " SELECT " + COLUM_ID_PERCORSO +" FROM " + PERCORSO + " ORDER BY " + COLUM_ID_PERCORSO +  " DESC LIMIT 1" ;
        Cursor cursor=null;
        db=this.getReadableDatabase();
        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor getLastRouteLocale() {
        String query = " SELECT " + COLUM_ID +" FROM " + PERCORSO + " ORDER BY " + COLUM_ID +  " DESC LIMIT 1" ;
        Cursor cursor=null;
        db=this.getReadableDatabase();
        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }


    public boolean update_releaseEdge(String id_percorso, String id_zona2) {

        SQLiteDatabase db = this.getWritableDatabase() ;
        ContentValues cv = new ContentValues() ;

        cv.putNull(COLUM_ID_ZONA2);

        return db.update(Ordine_Zon,cv,COLUM_ID_PERCORSO+ " = " +  id_percorso + " AND " + COLUM_ID_ZONA2 + " = " + id_zona2 , null) != -1;
        //metto a null la colonna id_zona2 in tutte le righe che hanno il percorso selezionato e la relativa zona2 associata e che verrà eliminata
    }


    public Cursor searchIdAreaLocal(String id_zona) {

        String query=" SELECT " + COLUM_ID
                +  " FROM "+ ZONA
                + " WHERE " + COLUM_ID_FIRE + " = '" + id_zona +"'";
        Cursor cursor=null;
        db=this.getReadableDatabase();

        if (db != null ){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public boolean delete_Route(String id_sito, String id_percorso) {
        return db.delete(PERCORSO,COLUM_ID_SITO + " = '" + id_sito + "' AND " + COLUM_ID + " = " + id_percorso,null) > 0;
    }

    public boolean delete_Route_ordine_zon( String id_percorso) {
        return db.delete(Ordine_Zon,COLUM_ID_PERCORSO + " = " + id_percorso ,null) > 0;
    }

    public String update_Route(int i, String Title, String Desc, String Typology,boolean back,String data) {

        SQLiteDatabase db = this.getWritableDatabase() ;
        ContentValues cv = new ContentValues() ;

        cv.put(COLUM_TITOLO,Title);
        cv.put(COLUM_DESC,Desc);
        cv.put(COLUM_TIPOLOGIA,Typology);
        cv.put(COLUM_DATA_MODIFICA,data);
        cv.put(COLUM_BACK, back);

        long res = db.update(PERCORSO,cv,COLUM_ID + " = " +  i , null);

        if( res == -1 )
            return "Failed" ;
        else
            return "Success" ;
    }

}
