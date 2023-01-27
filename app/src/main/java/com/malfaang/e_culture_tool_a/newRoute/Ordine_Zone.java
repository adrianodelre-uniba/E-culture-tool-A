package com.malfaang.e_culture_tool_a.newRoute;

//Classe per l'ArrayList dell'ordinamento delle zone
public class Ordine_Zone {
    private String idPercorso;
    private String idZona1;
    private String idZona2;
    private String edgevalue;

    public Ordine_Zone(String idPercorso, String idZona1, String idZona2, String edgevalue) {
        this.idPercorso = idPercorso;
        this.idZona1 = idZona1;
        this.idZona2 = idZona2;
        this.edgevalue = edgevalue;
    }

    public String getIdPercorso() {
        return idPercorso;
    }

    public String getIdZona1() {
        return idZona1;
    }

    public String getIdZona2() {
        return idZona2;
    }

    public String getEdgevalue() {
        return edgevalue;
    }
}
