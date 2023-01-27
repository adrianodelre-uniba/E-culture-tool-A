package com.malfaang.e_culture_tool_a.newRoute;

import java.io.Serializable;
//Classe per l'ArrayList di item
public class Item implements Serializable {
    private String itemTitle;
    private String itemDescr;
    private String itemTypology;
    private String item_Zona;
    private String item_Sito;


    public Item(String itemTitle, String itemDescr, String itemTypology, String item_Zona, String item_Sito) {
        this.itemTitle = itemTitle;
        this.itemDescr = itemDescr;
        this.itemTypology = itemTypology;
        this.item_Zona = item_Zona;
        this.item_Sito = item_Sito;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public String getItemDescr() {
        return itemDescr;
    }

    public String getItemTypology() {
        return itemTypology;
    }

    public String getItem_Sito() {
        return item_Sito;
    }

    public String getItem_Zona() {
        return item_Zona;
    }
}
