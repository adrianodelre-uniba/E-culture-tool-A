package com.malfaang.e_culture_tool_a.newRoute;

import java.io.Serializable;
//Classe per l'Arraylist del percorso
public class Route implements Serializable {
    private String routeTitle;
    private String routeDescr;
    private String routeTypology;
    private String id_site;
    private String data_creazione;
    private String key_route;
    private boolean go_back;

    public Route(String routeTitle, String routeDescr, String routeTypology,String key_route, String id_site, String data_creazione,boolean go_back) {
        this.routeTitle = routeTitle;
        this.routeDescr = routeDescr;
        this.routeTypology = routeTypology;
        this.id_site = id_site;
        this.data_creazione = data_creazione;
        this.key_route = key_route;
        this.go_back= go_back;
    }

    public String getRouteTitle() {
        return routeTitle;
    }

    public String getRouteDescr() {
        return routeDescr;
    }

    public String getRouteTypology() {
        return routeTypology;
    }

    public String getidSite() {
        return id_site;
    }

    public String getData_creazione() {
        return data_creazione;
    }

    public String getKey_route() {
        return key_route;
    }

    public boolean getgo_back(){ return  go_back ;}

    public void setRouteDescr(String routeDescr) {
        this.routeDescr = routeDescr;
    }

    public void setRouteTypology(String routeTypology) {
        this.routeTypology = routeTypology;
    }
}
