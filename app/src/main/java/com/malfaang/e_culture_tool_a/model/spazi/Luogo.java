package com.malfaang.e_culture_tool_a.model.spazi;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.Objects;

/*
 *  TODO commentare la classe
 *
 *  @author Donatellol
 */

public class Luogo {

    protected static final int MAX_CAPIENZA = 100;
    private String nome = null;
    private int capienza = 0;
    private String indirizzo = null;
    private LocalDate orarioApertura = null;
    private LocalDate orarioChiusura = null;

    public Luogo() {

    }

    public Luogo(String nome2, int capienza2, String indirizzo2, LocalDate orarioApertura2, LocalDate orarioChiusura2) {
        this.nome = nome2;
        this.capienza = capienza2;
        this.indirizzo = indirizzo2;
        this.orarioApertura = orarioApertura2;
        this.orarioChiusura = orarioChiusura2;
    }

    @NonNull
    @Override
    public String toString() {
        return "Luogo{" +
                "nome='" + nome + '\'' +
                ", capienza=" + capienza +
                ", indirizzo='" + indirizzo + '\'' +
                ", orarioApertura=" + orarioApertura +
                ", orarioChiusura=" + orarioChiusura +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Luogo luogo = (Luogo) o;
        return nome.equals(luogo.getNome()) && indirizzo.equals(luogo.getIndirizzo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, indirizzo);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome2) {
        this.nome = nome2;
    }

    public int getCapienza() {
        return capienza;
    }

    public void setCapienza(int capienza2) {
        this.capienza = capienza2;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo2) {
        this.indirizzo = indirizzo2;
    }

    public LocalDate getOrarioApertura() {
        return orarioApertura;
    }

    public void setOrarioApertura(LocalDate orarioApertura2) {
        this.orarioApertura = orarioApertura2;
    }

    public LocalDate getOrarioChiusura() {
        return orarioChiusura;
    }

    public void setOrarioChiusura(LocalDate orarioChiusura2) {
        this.orarioChiusura = orarioChiusura2;
    }

}
