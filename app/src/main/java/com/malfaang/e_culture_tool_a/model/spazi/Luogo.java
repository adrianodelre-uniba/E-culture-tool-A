package com.malfaang.e_culture_tool_a.model.spazi;

public class Luogo {

    //private static final int maxCapienza = ??;
    private String nome;
    private int capienza;
    private String indirizzo;
    private String orarioApertura;
    private String orarioChiusura;

    public Luogo() {

    }

    public Luogo(String nome, int capienza, String indirizzo, String orarioApertura, String orarioChiusura) {
        this.nome = nome;
        this.capienza = capienza;
        this.indirizzo = indirizzo;
        this.orarioApertura = orarioApertura;
        this.orarioChiusura = orarioChiusura;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCapienza() {
        return capienza;
    }

    public void setCapienza(int capienza) {
        this.capienza = capienza;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getOrarioApertura() {
        return orarioApertura;
    }

    public void setOrarioApertura(String orarioApertura) {
        this.orarioApertura = orarioApertura;
    }

    public String getOrarioChiusura() {
        return orarioChiusura;
    }

    public void setOrarioChiusura(String orarioChiusura) {
        this.orarioChiusura = orarioChiusura;
    }
}
