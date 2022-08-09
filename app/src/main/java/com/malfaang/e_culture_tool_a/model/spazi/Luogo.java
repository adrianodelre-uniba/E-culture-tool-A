package com.malfaang.e_culture_tool_a.model.spazi;

import java.time.LocalDate;

public class Luogo {

    protected static final int MAX_CAPIENZA = 100;
    private String nome;
    private int capienza;
    private String indirizzo;
    private LocalDate orarioApertura;
    private LocalDate orarioChiusura;

    public Luogo() {

    }

    public Luogo(String nome, int capienza, String indirizzo, LocalDate orarioApertura, LocalDate orarioChiusura) {
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

    public LocalDate getOrarioApertura() {
        return orarioApertura;
    }

    public void setOrarioApertura(LocalDate orarioApertura) {
        this.orarioApertura = orarioApertura;
    }

    public LocalDate getOrarioChiusura() {
        return orarioChiusura;
    }

    public void setOrarioChiusura(LocalDate orarioChiusura) {
        this.orarioChiusura = orarioChiusura;
    }
}
