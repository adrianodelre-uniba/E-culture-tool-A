package com.malfaang.e_culture_tool_a.newRoute;

import java.io.Serializable;
//Classe per l'ArrayList delle zone
public class Area implements Serializable {
    private String areaTitle;
    private String areaDescr;
    private String areaTypology;
    private String area_id_sito;

    public Area(String areaTitle, String areaDescr, String areaTypology,String area_id_sito) {
        this.areaTitle = areaTitle;
        this.areaDescr = areaDescr;
        this.areaTypology = areaTypology;
        this.area_id_sito = area_id_sito;
    }
    public String getAreaTitle() { return areaTitle;}

    public String getAreaDescr() {
        return areaDescr;
    }

    public String getAreaTypology() {
        return areaTypology;
    }

    public String getArea_id_sito() {
        return area_id_sito;
    }
}
