package com.malfaang.e_culture_tool_a.databaseFirebase;

//Classe per l'ArrayList zona
public class Zone {
    private String descrizione;
    private String tipologia;

    public Zone(String descrizione, String tipologia) {
        this.descrizione = descrizione;
        this.tipologia = tipologia;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public String getTipologia() {
        return this.tipologia;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }
}

