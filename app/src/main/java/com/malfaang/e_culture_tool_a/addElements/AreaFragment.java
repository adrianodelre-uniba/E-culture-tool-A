package com.malfaang.e_culture_tool_a.addElements;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malfaang.e_culture_tool_a.Database;
import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.login.FireCallback;
import com.malfaang.e_culture_tool_a.newRoute.Area;
import com.malfaang.e_culture_tool_a.newRoute.RecyclerViewClickInterface;
import com.malfaang.e_culture_tool_a.routes.AreaInfo;

import java.util.ArrayList;

public class AreaFragment extends Fragment implements RecyclerViewClickInterface {
    private Database LocalDB;
    private ArrayList<Area> areaList2;
    private ArrayList<String> FireKeyList_Area;
    private RecyclerAreaAdapter adapter;
    private String id_sito; //Chiave di Sito (Loggato)
    private RecyclerView recyclerView_Area;
    private ActionBar ab;
    private DatabaseReference mDatabase;
    private String id_zon; // chiave della zona
    private int indice=1;
    private int OldPosition = 0;
    private String errore;
    private String fall;
    private String camp;
    private String presenza;
    private String[] ID_AREA = new String[MAX_AREA];
    private static final int MAX_AREA = 90;

    //Creazione del layout, collegamento con gli oggetti, apertura dell'ArrayList e di firebase e sql
    //Prendere dati dalle SharedPreferences
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.area_tab_fragment, container, false);
        LocalDB = new Database(getContext());
        areaList2 = new ArrayList<>();
        FireKeyList_Area = new ArrayList<>();
        camp = getResources().getString(R.string.campi);
        presenza = getResources().getString(R.string.Presenza_non);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Zone");
        SharedPreferences preference = getActivity().getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
        String email = preference.getString("Email", "");
        id_sito = preference.getString("Id_Sito", "");
        errore = getResources().getString(R.string.Errore_fb);
        fall = getResources().getString(R.string.fallito);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt("Indice_Area",1);
        editor.putInt("OldPosition_Area",0);
        editor.commit();

        requireActivity().addMenuProvider(new MenuProvider() {
            //Settaggio del menù, con relative funzionalità, per il fragment "area"
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_new_area, menu);
            }
            //Bottone per aggiungere un nuovo percorso
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.add_new_area:
                        showDialog();
                        return true;
                }
                return false;
            }
        });
        //lettura dei dati da sql
        localRoute(id_sito);
        recyclerView_Area = root.findViewById(R.id.areaList1);
        if(isNetworkAvailable()){
            //lettura dei dati da firebase
            Load_Fire_Key(new FireCallback() {
                @Override
                public void onCallback(String value) {
                    adapter = new RecyclerAreaAdapter( areaList2, AreaFragment.this);
                    recyclerView_Area.setAdapter(adapter);
                }
            });}
        else{
            Toast.makeText(getContext(),errore,Toast.LENGTH_SHORT).show();
        }
        //Divisione degli item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView_Area.addItemDecoration(dividerItemDecoration);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView_Area);
        return root;
    }
    //Aggiunta delle chiavi delle zone nell'arraylist da firebase
    private void Load_Fire_Key(final FireCallback myCallback) {
        FireKeyList_Area.clear();
        mDatabase.orderByChild("area_id_sito").equalTo(id_sito).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot2: snapshot.getChildren()){
                    FireKeyList_Area.add(snapshot2.getKey());
                }
                myCallback.onCallback("Ok");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error",errore);
            }
        });
    }

    Area deletedArea=null;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
        //Spostamento delle aree e definizione del nuovo ordine del percorso
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            return false;
        }



        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            switch (direction) {
                //eliminazione dell'area
                case ItemTouchHelper.LEFT:
                    deletedArea = areaList2.get(position);
                    areaList2.remove(position);
                    adapter.notifyItemRemoved(position);
                    Snackbar snackbar = Snackbar.make(recyclerView_Area, deletedArea.getAreaTitle() + " "+presenza, Snackbar.LENGTH_LONG)
                            .setAction(R.string.undo, view -> {
                                areaList2.add(position, deletedArea);
                                adapter.notifyItemInserted(position);
                                //DeleteFirebase e aggiornamento ordini zone
                            }).addCallback(new Snackbar.Callback(){
                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    super.onDismissed(transientBottomBar, event);
                                    SharedPreferences preference = getActivity().getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
                                    if (event == DISMISS_EVENT_TIMEOUT) {
                                        OldPosition = preference.getInt("OldPosition_Area",0);
                                        indice = preference.getInt("Indice_Area",0);
                                        if(indice==1){
                                            mDatabase.child(""+ FireKeyList_Area.get(position).toString()).removeValue();
                                            LocalDB.delete_Zona(position+1);
                                            FireKeyList_Area.remove(position);
                                            indice++;
                                            OldPosition =position;
                                        }else{
                                            if(position >= OldPosition){
                                                mDatabase.child(""+ FireKeyList_Area.get(position).toString()).removeValue();
                                                LocalDB.delete_Zona(position+indice);
                                                FireKeyList_Area.remove(position);
                                                indice++;
                                                OldPosition = position;
                                            }else{
                                                if(position < OldPosition){
                                                    mDatabase.child(""+ FireKeyList_Area.get(position).toString()).removeValue();
                                                    LocalDB.delete_Zona(position+1);
                                                    FireKeyList_Area.remove(position);
                                                    OldPosition = position;
                                                }
                                            }
                                        }
                                        SharedPreferences.Editor editor = preference.edit();
                                        editor.putInt("Indice_Area",indice);
                                        editor.putInt("OldPosition_Area", OldPosition);
                                        editor.commit();
                                    }
                                }
                            });
                    snackbar.show();
                    break;

                //Modifica dell'area
                case ItemTouchHelper.RIGHT:
                    final Area areaToUpdate = areaList2.get(position);
                    showDialogEditArea(position);
                    adapter.notifyItemChanged(position);
                    break;
            }
        }


        @Override
        public float getSwipeEscapeVelocity(float defaultValue) {
            return super.getSwipeEscapeVelocity(0.5f * defaultValue); //Velocità minima con la quale considerare lo swipe
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            Drawable icon;
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // Get RecyclerView item from the ViewHolder
                View itemView = viewHolder.itemView;
                Paint p = new Paint();
                int iconMargin;
                int iconTop;
                int iconBottom;
                if (dX > 0 ) { //Swipe a destra
                    icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_edit_24);
                    iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                    iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                    iconBottom = iconTop + icon.getIntrinsicHeight();
                    int iconLeft = itemView.getLeft() + iconMargin ;
                    int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    /* Settaggio del colore se si va a destra */
                    p.setColor(ContextCompat.getColor(getContext(),R.color.orange_edit));
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), (float) itemView.getLeft() + dX, (float) itemView.getBottom(), p);
                    icon.draw(c);
                } else if(dX < 0){ //swipe a Sinistra
                    icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_delete_24);
                    iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                    iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                    iconBottom = iconTop + icon.getIntrinsicHeight();
                    int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                    int iconRight = itemView.getRight() - iconMargin;
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    /* Settaggio colore se si va a sinistra*/
                    p.setColor(ContextCompat.getColor(getContext(),R.color.red_delete));
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), p);
                    icon.draw(c);
                }
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }


    };
    //Modifica dei dati dell'area
    void showDialogEditArea(int position){
        final String[] areaTitle = new String[1];
        final String[] areaDescr = new String[1];
        final String[] areaTypology = new String[1];
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog_add_new_area,null);
        Button acceptButton = view.findViewById(R.id.ad_button_do);
        EditText et1 = view.findViewById(R.id.areaName_alert_dialog);
        EditText et2 = view.findViewById(R.id.areaDescr_alert_dialog);
        EditText et3 = view.findViewById(R.id.areaTypology_alert_dialog);
        TextView tv1 = view.findViewById(R.id.textview_new_edit_area);

        tv1.setText(R.string.alert_dialog_edit_area);
        et1.setText(areaList2.get(position).getAreaTitle());
        et2.setText(areaList2.get(position).getAreaDescr());
        et3.setText(areaList2.get(position).getAreaTypology());
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(view).create();

        acceptButton.setOnClickListener(view1 -> {
            areaTitle[0] = et1.getText().toString();
            areaDescr[0] = et2.getText().toString();
            areaTypology[0] = et3.getText().toString();
            SharedPreferences preference = getActivity().getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
            OldPosition = preference.getInt("OldPosition_Area",0);
            indice = preference.getInt("Indice_Area",0);
            if(indice==0){
                LocalDB.update_zona(position+1,areaTitle[0], areaDescr[0],areaTypology[0]);
            }else{
                if(position >= OldPosition){
                    LocalDB.update_zona(position+indice,areaTitle[0], areaDescr[0],areaTypology[0]);
                }else{
                    if(position < OldPosition){
                        LocalDB.update_zona(position+1,areaTitle[0], areaDescr[0],areaTypology[0]);
                    }
                }
            }
            areaList2.set(position, new Area(areaTitle[0],areaDescr[0],areaTypology[0],areaList2.get(position).getArea_id_sito()));
            mDatabase.child(""+ FireKeyList_Area.get(position).toString()).child("areaTitle").setValue(areaTitle[0]);
            mDatabase.child(""+ FireKeyList_Area.get(position).toString()).child("areaDescr").setValue(areaDescr[0]);
            mDatabase.child(""+ FireKeyList_Area.get(position).toString()).child("areaTypology").setValue(areaTypology[0]).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    alertDialog.cancel();
                }
            });
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    //Apertura di un layout temporaneo in cui ci prendiamo tutti i dati del nuovo percorso e li inseriamo nei database
    void showDialog(){
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog_add_new_area,null);
        Button acceptButton = view.findViewById(R.id.ad_button_do);
        EditText et1 = view.findViewById(R.id.areaName_alert_dialog);
        EditText et2 = view.findViewById(R.id.areaDescr_alert_dialog);
        EditText et3 = view.findViewById(R.id.areaTypology_alert_dialog);
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(view).create();
        acceptButton.setOnClickListener(view1 -> {
            String areaName = et1.getText().toString();
            String areaDescr = et2.getText().toString();
            String areaTipology = et3.getText().toString();
            if (TextUtils.isEmpty(areaName) || TextUtils.isEmpty(areaDescr) || TextUtils.isEmpty(areaTipology) ) {
                Toast.makeText(getContext(), camp, Toast.LENGTH_LONG).show();
            }else {
                DatabaseReference ref = mDatabase.push();
                String idZon = ref.getKey();
                FireKeyList_Area.add(idZon);
                Area zona = new Area(areaName, areaDescr, areaTipology, id_sito);
                ref.setValue(zona);
                String result = LocalDB.addRecord_Area(areaName, areaDescr, areaTipology, id_sito, idZon);
                areaList2.add(new Area(areaName, areaDescr, areaTipology, id_sito));
                if (!result.equals("Success")) {
                    Toast.makeText(getContext(), fall, Toast.LENGTH_SHORT).show();
                }
                alertDialog.cancel();
            }});
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    //Prelievo della route da locale
    private void localRoute(String id){
        areaList2.clear();
        Cursor cursor= LocalDB.readAreas(id);
        String titolo,descrizione,tipologia;
        int j=0;
        if(cursor.moveToFirst()){
            do {
                ID_AREA[j] = cursor.getString(0);
                titolo = cursor.getString(1);
                descrizione = cursor.getString(2);
                tipologia = cursor.getString(3);
                areaList2.add(new Area(titolo, descrizione, tipologia, id));
                cursor.moveToNext();
                j++;
            }while(!cursor.isAfterLast());
        }
    }


    @Override
    public void onItemClick(int position) {
        Intent intentArea = new Intent(getContext(), AreaInfo.class);
        intentArea.putExtra("key_area", ID_AREA[position]);
        intentArea.putExtra("area",areaList2.get(position));
        intentArea.putExtra("goto","Add_Route");
        startActivity(intentArea);
    }

    @Override
    public void onLongItemClick(int position) {
    }

    public boolean isNetworkAvailable() {
        Context context = getContext();
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
