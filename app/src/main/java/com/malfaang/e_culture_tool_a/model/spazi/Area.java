package com.malfaang.e_culture_tool_a.model.spazi;

public class Area extends Luogo{

    private String nomeArea;
    private int capienzaArea;

    public Area(String nome, int capienza, String indirizzo, String orarioApertura, String orarioChiusura, String nomeArea, int capienzaArea) {
        super(nome, capienza, indirizzo, orarioApertura, orarioChiusura);
        this.nomeArea = nomeArea;
        this.capienzaArea = capienzaArea;
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
