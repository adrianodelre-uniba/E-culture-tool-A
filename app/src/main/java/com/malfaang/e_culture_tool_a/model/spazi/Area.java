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

    private String nomeArea = null;
    private int capienzaArea = 0;

    public Area(String nome, int capienza, String indirizzo,
                LocalDate orarioApertura, LocalDate orarioChiusura, String nomeArea2, int capienzaArea2) {
        super(nome, capienza, indirizzo, orarioApertura, orarioChiusura);
        this.nomeArea = nomeArea2;
        this.capienzaArea = capienzaArea2;
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
    public final boolean equals(Object o) {
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
        return capienzaArea == area.getCapienzaArea() && nomeArea.equals(area.getNomeArea());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), nomeArea, Integer.valueOf(capienzaArea));
    }

    public String getNomeArea() {
        return this.nomeArea;
    }

    public void setNomeArea(String nomeArea2) {
        this.nomeArea = nomeArea2;
    }

    public int getCapienzaArea() {
        return this.capienzaArea;
    }

    public void setCapienzaArea(int capienzaArea2) {
        this.capienzaArea = capienzaArea2;
    }
}
