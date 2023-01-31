package com.malfaang.e_culture_tool_a.routes;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.malfaang.e_culture_tool_a.Database;
import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.newRoute.Area;
import com.malfaang.e_culture_tool_a.newRoute.Route;

import java.io.Serializable;
import java.util.ArrayList;

public class Show_Route extends AppCompatActivity {
    ActionBar ab;
    Bundle extras;
    private Database LocalDB;
    private ArrayList<Area> areaList_Site;
    String ID_PERCORSO;
    String[] ID_AREA_LOCALE_Order1 =new String[MAX_ROUTE];
    String[] ID_AREA_LOCALE_Order2 =new String[MAX_ROUTE];
    String[] ID_AREA_LOCALE =new String[MAX_ROUTE]; //lista degli id delle zone del percorso
    ArrayList<AreaCanvas> AreaListCanvas = new ArrayList<>();
    private static final int MAX_ROUTE = 90;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();

        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(extras.getString("key_route_name"));

        LocalDB= new Database(getApplicationContext());


        areaList_Site = new ArrayList<>();
        AreaListCanvas = new ArrayList<>();

        Intent intent = getIntent();

        ID_PERCORSO=extras.getString("key_route");
        areaList_Site = (ArrayList<Area>) intent.getSerializableExtra("areas_add_to_route");
        ID_AREA_LOCALE_Order1 = (String[]) intent.getSerializableExtra("id_zona1");
        ID_AREA_LOCALE_Order2 = (String[]) intent.getSerializableExtra("id_zona2");
        ID_AREA_LOCALE= (String[]) intent.getSerializableExtra("id_zone");


        setContentView(new MyView(this));
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        Intent intent = super.getParentActivityIntent();
        if (intent != null)
            enhanceParentActivityIntent(intent);
        return intent;
    }

    private void enhanceParentActivityIntent(@NonNull Intent intent) {

        intent.putExtra("key_route_name", extras.getString("key_route_name"));
        intent.putExtra("key_route",ID_PERCORSO);
        intent.putExtra("route",  (Route) getIntent().getSerializableExtra("route"));
        intent.putExtra("key_route_fire",  extras.getString("key_route_fire"));

    }


    public class MyView extends View
    {
        Paint paint_area = null;
        Paint paint_text = null;
        Paint paint_text2 = null;
        public MyView(Context context)
        {
            super(context);
            startNestedScroll(getScrollY());
            paint_area = new Paint();
            paint_text = new Paint();
            paint_text2 = new Paint();
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            int x = getWidth();
            int y = getHeight();
            int dir=0;
            int raw = 0;
            int space = 0;
            int radius;
            radius = 60;
            paint_area.setStyle(Paint.Style.FILL);
            paint_area.setColor(Color.WHITE);
            canvas.drawPaint(paint_area);
            paint_area.setColor(getResources().getColor(R.color.color_Accent_500));
            paint_area.setStrokeWidth(9);

            paint_text.setColor(getResources().getColor(R.color.color_Primary_500));
            paint_text.setStyle(Paint.Style.FILL);
            paint_text.setTextSize(80);
            paint_text.setColor(Color.GREEN);


            paint_text2.setStyle(Paint.Style.FILL);
            paint_text2.setTextSize(80);
            paint_text2.setColor(Color.RED);

            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {

                int height = y / 10;
                int width = x / 2;
                for(int i=1; i<areaList_Site.size(); i++){
                    if(i==1){
                        canvas.drawCircle(width, height, radius, paint_area);
                        AreaListCanvas.add(new AreaCanvas(width,height));

                        canvas.drawText("START",width-115,height-80,paint_text);

                        //Controllo che ci siano diramazioni
                        dir = dir_count(i);
                        if(dir != 0){
                            //Ci sono diramazioni
                            raw = x / dir; //Riga dove mettere le zone
                            space = raw / 2; //Porzione dove andare a mettere la zona nel canvas
                            height= height+300; //Modifico l'altezza (Asse Y) della nuova diramazione
                        }

                    }else{
                        //Dalla seconda zona in poi

                        //Controllo le diramazioni
                        dir = dir_count(i);
                        if(dir != 0){
                            raw = x / dir;

                            space = raw / 2; //width

                            height= height+300;

                        }

                    }

                    for(int j=0; j<dir_count(i); j++){

                        canvas.drawCircle(space  , height, radius, paint_area);

                        AreaListCanvas.add(new AreaCanvas(space,height));

                        space=space + raw;

                    }

                }

                canvas.drawText(getResources().getString(R.string.End),AreaListCanvas.get(areaList_Site.size()-1).getWidth()-90, AreaListCanvas.get(areaList_Site.size()-1).getHeight()+140,paint_text2);

                setEdge(canvas);

            } else {


                int height = x / 10;
                int width = y / 2;
                for(int i=1; i<areaList_Site.size(); i++){
                    if(i==1){
                        canvas.drawCircle(height, width, radius, paint_area);
                        AreaListCanvas.add(new AreaCanvas(height,width));

                        canvas.drawText(getResources().getString(R.string.start),height-150,width-80,paint_text);

                        //Controllo che ci siano diramazioni
                        dir = dir_count(i);
                        if(dir != 0){
                            //Ci sono diramazioni
                            raw = y / dir;      //Riga dove mettere le zone
                            space = raw / 2;    //Porzione dove andare a mettere la zona nel canvas
                            height= height+300; //Modifico l'altezza (Asse Y) della nuova diramazione
                        }

                    }else{
                        //Dalla seconda zona in poi

                        //Controllo le diramazioni
                        dir = dir_count(i);
                        if(dir != 0){
                            raw = y / dir;

                            space = raw / 2; //width

                            height= height+300;

                        }

                    }

                    for(int j=0; j<dir_count(i); j++){
                        canvas.drawCircle(height  ,space , radius, paint_area);

                        AreaListCanvas.add(new AreaCanvas(height,space));

                        canvas.drawLine(AreaListCanvas.get(i-1).getWidth(),AreaListCanvas.get(i-1).getHeight(),height,space,paint_area);
                        space=space + raw;

                    }

                }
                canvas.drawText(getResources().getString(R.string.End),AreaListCanvas.get(areaList_Site.size()-1).getWidth()-80, AreaListCanvas.get(areaList_Site.size()-1).getHeight()+140,paint_text2);

                setEdge(canvas);

            }

            for(int i=0; i<areaList_Site.size();i++ ){
                canvas.drawText(""+ (i+1),AreaListCanvas.get(i).getWidth()-25, AreaListCanvas.get(i).getHeight()+25,paint_text2);
            }

        }

        private void setEdge(Canvas canvas) {

                for(int i=0; i<ID_AREA_LOCALE_Order1.length; i++){
                    if(ID_AREA_LOCALE_Order1[i] == null){
                        i=ID_AREA_LOCALE_Order1.length;
                    }else{

                        if(ID_AREA_LOCALE_Order2[i] != null){

                            canvas.drawLine(AreaListCanvas.get(Integer.parseInt(ID_AREA_LOCALE_Order1[i]) - 1).getWidth(),
                                    AreaListCanvas.get(Integer.parseInt(ID_AREA_LOCALE_Order1[i]) -1 ).getHeight(),
                                    AreaListCanvas.get(Integer.parseInt(ID_AREA_LOCALE_Order2[i]) - 1).getWidth(),
                                    AreaListCanvas.get(Integer.parseInt(ID_AREA_LOCALE_Order2[i]) -1 ).getHeight(),
                                    paint_area);

                        }
                    }

                }

        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                for(int i=0; i<areaList_Site.size(); i++){
                    if((x > AreaListCanvas.get(i).getWidth() - 60  && x < AreaListCanvas.get(i).getWidth() +60)
                            && (y > AreaListCanvas.get(i).getHeight() + 160 && y < AreaListCanvas.get(i).getHeight() + 260)){
                        Intent intent_Area = new Intent(getApplicationContext(),   AreaInfo.class);
                        intent_Area.putExtra("key_area",ID_AREA_LOCALE[i]);
                        intent_Area.putExtra("area",areaList_Site.get(i));
                        intent_Area.putExtra("goto","Show_Route");

                        intent_Area.putExtra("key_route",ID_PERCORSO); //Percorso
                        intent_Area.putExtra("areas_add_to_route", areaList_Site); //aree aggiunte al percorso
                        intent_Area.putExtra("id_zona1",(Serializable) ID_AREA_LOCALE_Order1); //id_zona1
                        intent_Area.putExtra("id_zona2",(Serializable) ID_AREA_LOCALE_Order2); //id_zona2
                        intent_Area.putExtra("id_zone",(Serializable) ID_AREA_LOCALE);

                        intent_Area.putExtra("key_route_name", extras.getString("key_route_name"));
                        intent_Area.putExtra("key_route",ID_PERCORSO);
                        intent_Area.putExtra("route",  (Route) getIntent().getSerializableExtra("route"));
                        intent_Area.putExtra("key_route_fire",  extras.getString("key_route_fire"));
                        startActivity(intent_Area);
                    }
                }
                return true;
        }

        return  false;
    }

    private int dir_count(int id_zona) {
        int count=0;
        boolean a=false;

        for(int i=0; i<ID_AREA_LOCALE_Order1.length; i++){
            if(ID_AREA_LOCALE_Order1[i] == null){
                i=ID_AREA_LOCALE_Order1.length;
            }else{

                if( ID_AREA_LOCALE_Order1[i].equals(String.valueOf(id_zona)) && ID_AREA_LOCALE_Order2[i] != null){
                    for(int k=0; k<i; k++){
                        if(ID_AREA_LOCALE_Order2[i].equals(ID_AREA_LOCALE_Order2[k]) ){
                            a=true;
                        }
                    }
                    if(!a){
                        count++;
                    }


                }
            }

        }
        return count;
    }
}
