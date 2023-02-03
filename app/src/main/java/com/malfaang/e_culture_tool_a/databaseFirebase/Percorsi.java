package com.malfaang.e_culture_tool_a.databaseFirebase;

//Classe per l'ArrayList dei percorsi
public class Percorsi {
    private String Titolo;

    public Percorsi() {
    }

    public Percorsi(String titolo) {
        this.Titolo = titolo;
    }

    public void setTitolo(String titolo) {
        this.Titolo = titolo;
    }

    public String getTitolo() {
        return this.Titolo;
    }
}
