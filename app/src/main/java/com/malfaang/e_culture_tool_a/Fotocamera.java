package com.malfaang.e_culture_tool_a;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Fotocamera extends AppCompatActivity {


    private Bitmap mImageBitmap;
    Button Galler, Camera;
    ImageView foto;
    private final int GALLERY_REQ_CODE = 1000;
    private final int CAMERA_REQ_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotocame);
        foto = (ImageView) findViewById(R.id.Immagin);
        Galler = (Button) findViewById(R.id.button);
        Camera = (Button) findViewById(R.id.button2);

        //Ti riporta alla galleria
        Galler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Foto_Gallery = new Intent(Intent.ACTION_PICK);
                Foto_Gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Foto_Gallery, GALLERY_REQ_CODE);
            }
        });
        // ti riporta alla fotocamera
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Foto_Camera = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(Foto_Camera,CAMERA_REQ_CODE);

            }
        });
    }


    //funzione per ottenere l'immagine dalla galleria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            if (requestCode == GALLERY_REQ_CODE) {

                foto.setImageURI(data.getData());
            }
            else if(requestCode == CAMERA_REQ_CODE){

                //Dalla fotocamera

                Bitmap img = (Bitmap)(data.getExtras().get("data"));
                foto.setImageBitmap(img);
            }

        }

    }

    public void route_foto(){
        Intent Foto_Camera = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(Foto_Camera,CAMERA_REQ_CODE);
    }
}











  