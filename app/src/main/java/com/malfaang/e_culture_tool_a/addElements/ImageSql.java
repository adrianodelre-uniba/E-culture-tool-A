package com.malfaang.e_culture_tool_a.addElements;

import android.graphics.Bitmap;
//Classe per l'ArrayList delle foto
public class ImageSql {
    private String item_id;
    private Bitmap img;


    public ImageSql(Bitmap img, String itemId) {
        this.img = img;
        this.item_id = itemId;
    }


    public String getItem_id() {
        return item_id;
    }

    public Bitmap getItem_imgSql() {
        return img;
    }


}

