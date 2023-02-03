package com.malfaang.e_culture_tool_a.databaseFirebase;

//Classe per l'ArrayList zona
public class Zone {
    private String Descrizione;
    private String Tipologia;

    public Zone(String descrizione, String tipologia) {
        this.Descrizione = descrizione;
        this.Tipologia = tipologia;
    }

    public String getDescrizione() {
        return this.Descrizione;
    }

    public String getTipologia() {
        return this.Tipologia;
    }

    public void setDescrizione(String descrizione) {
        this.Descrizione = descrizione;
    }

    public void setTipologia(String tipologia) {
        this.Tipologia = tipologia;
    }
}

