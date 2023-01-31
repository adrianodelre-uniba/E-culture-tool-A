package com.malfaang.e_culture_tool_a.addElements;

import android.graphics.Bitmap;
//Classe per l'ArrayList delle foto
public class ImageSql {
    private String itemId;
    private Bitmap img;


    public ImageSql(Bitmap img, String itemId) {
        this.img = img;
        this.itemId = itemId;
    }


    public String getItemId() {
        return itemId;
    }

    public Bitmap getItemImgSql() {
        return img;
    }


}

