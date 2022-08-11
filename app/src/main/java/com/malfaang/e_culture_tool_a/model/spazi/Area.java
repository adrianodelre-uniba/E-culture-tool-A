package com.malfaang.e_culture_tool_a.model.spazi;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.Objects;

/*
 *  TODO commentare la classe
 *
 *  @author Donatellol
 */


public class Area extends Luogo{

    private String nomeArea;
    private int capienzaArea;

    public Area(String nome, int capienza, String indirizzo,
                LocalDate orarioApertura, LocalDate orarioChiusura, String nomeArea, int capienzaArea) {
        super(nome, capienza, indirizzo, orarioApertura, orarioChiusura);
        this.nomeArea = nomeArea;
        this.capienzaArea = capienzaArea;
    }

    @NonNull
    @Override
    public String toString() {
        return "Area{" +
                "nomeArea='" + nomeArea + '\'' +
                ", capienzaArea=" + capienzaArea +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)){
            return false;
        }
        Area area = (Area) o;
        return capienzaArea == area.capienzaArea && nomeArea.equals(area.nomeArea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nomeArea, capienzaArea);
    }

    public String getNomeArea() {
        return nomeArea;
    }

    public void setNomeArea(String nomeArea) {
        this.nomeArea = nomeArea;
    }

    public int getCapienzaArea() {
        return capienzaArea;
    }

    public void setCapienzaArea(int capienzaArea) {
        this.capienzaArea = capienzaArea;
    }
}
