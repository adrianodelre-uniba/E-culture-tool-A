package com.malfaang.e_culture_tool_a.routes;

import android.graphics.Bitmap;

import java.io.Serializable;
//Classe per l'ArrayList di item
public class ItemAndImage implements Serializable {
    private String itemTitle;
    private String itemDescr;
    private String itemTypology;
    private String item_Zona;
    private String item_Sito;
    private Bitmap item_img;
    private String item_id;


    public ItemAndImage(String itemTitle, String itemDescr, String itemTypology,String item_Zona,String item_Sito,Bitmap item_img,String item_id) {
        this.itemTitle = itemTitle;
        this.itemDescr = itemDescr;
        this.itemTypology = itemTypology;
        this.item_Zona = item_Zona;
        this.item_Sito = item_Sito;
        this.item_img= item_img;
        this.item_id= item_id;
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

    public Bitmap getItem_img() {
        return item_img;
    }

    public String getItem_id() {
        return item_id;
    }

}
