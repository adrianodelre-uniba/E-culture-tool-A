package com.malfaang.e_culture_tool_a.sito;

//Classe per l'Arraylist di sito_utente
public class SitoUtente {

    private String idSito;
    private String idUtente;

    public SitoUtente(String idSito, String idUtente) {
        this.idSito = idSito;
        this.idUtente = idUtente;
    }

    public String getIdSito() {
        return this.idSito;
    }

    public String getIdUtente() {
        return this.idUtente;
    }
}
