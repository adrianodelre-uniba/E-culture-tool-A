package com.malfaang.e_culture_tool_a.databaseFirebase;

//Classe per l'ArrayList dei percorsi
public class Percorsi {
    private String titolo;

    public Percorsi() {
    }

    public Percorsi(String titolo) {
        this.titolo = titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getTitolo() {
        return this.titolo;
    }
}
