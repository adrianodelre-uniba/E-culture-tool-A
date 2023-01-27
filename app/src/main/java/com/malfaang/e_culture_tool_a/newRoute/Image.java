package com.malfaang.e_culture_tool_a.newRoute;

//Classe per l'Arraylist dell'immagine
public class Image {
    private String item_id;
    private String img;


    public Image(String img, String item_id) {
        this.img = img;
        this.item_id = item_id;
    }


    public String getItem_id() {
        return item_id;
    }

    public String getItem_img() {
        return img;
    }
}
