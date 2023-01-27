package com.malfaang.e_culture_tool_a.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.malfaang.e_culture_tool_a.databaseFirebase.Utente;

public class DbFirebase  {
    private DatabaseReference mDatabase;

    //Funzione per scrivere i dati dell'utente su real_time_database firebase
    public String scritturadbfi(String nome, String cognome, String email,String password,String tipologia
            , String telefono){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        mDatabase = db.getReference().child("Utente");
        DatabaseReference ref = mDatabase.push();
        String idUtente= ref.getKey();

        Utente utente = new Utente(nome,cognome,email,password,telefono,tipologia);
        ref.setValue(utente);
        return idUtente;
    }
}
