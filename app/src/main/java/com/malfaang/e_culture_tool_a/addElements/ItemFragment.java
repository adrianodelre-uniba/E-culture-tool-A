package com.malfaang.e_culture_tool_a.addElements;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
//TODO Importa Routes.Add_Area_To_Route.getImage;

import static com.malfaang.e_culture_tool_a.routes.Add_Area_To_Route.getImage;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.malfaang.e_culture_tool_a.Database;
import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.login.FireCallback;
import com.malfaang.e_culture_tool_a.newRoute.Image;
import com.malfaang.e_culture_tool_a.newRoute.Item;
import com.malfaang.e_culture_tool_a.newRoute.RecyclerViewClickInterface;
import com.malfaang.e_culture_tool_a.routes.Item_Info;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ItemFragment extends Fragment implements RecyclerViewClickInterface {
    private Database LocalDB;
    private ArrayList<Item> itemList;
    private ArrayList<String> areaList;
    private ArrayList<String> areaKeyList;
    private ArrayList<String> areaFireKeyList;
    private ArrayList<ImageSql> imageList;
    private ArrayList<String> FireKeyList_Item;
    private RecyclerItemAdapter adapter;
    private String id_sito; //Chiave di Sito (Loggato)
    private String id_zona = "";
    private RecyclerView recyclerView_Item;
    private ActionBar ab;
    private DatabaseReference mDatabase,mDatabase2;
    private Button fot;
    private ImageView pick;
    private StorageReference mStorageFoto;
    private String[] id_Item = new String[MAX_ROUTE];
    private String[] tito_temp = new String[MAX_ROUTE];
    private final int GALLERY_REQ_CODE = 1000;
    private final int CAMERA_REQ_CODE = 100;
    private final int CAMERA_PERMISSION_CODE = 112;
    private final int STORAGE_PERMISSION_CODE = 113;
    private static byte[] byt;
    private byte[] foto_it;
    private boolean camera_perm;
    private boolean gallery_perm;
    private Bitmap img = null;
    private Uri uri1;
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final int MAX_ROUTE = 90;
    private String errore;
    private String aggiu;
    private String conce;
    private String conce_camer;
    private String conce_camer_non;
    private String conce_galle;
    private String conce_galle_non;
    private String camp;
    private String presen;
    private int indice=1;
    private int OldPosition = 0;

    //Creazione del layout, collegamento con gli oggetti, apertura dell'ArrayList e di firebase e sql
    //Prendere dati dalle SharedPreferences
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.item_tab_fragment, container, false);
        LocalDB = new Database(getContext());
        itemList = new ArrayList<>();
        areaList = new ArrayList<>();
        areaKeyList = new ArrayList<>();
        areaFireKeyList = new ArrayList<>();
        imageList = new ArrayList<>();
        FireKeyList_Item = new ArrayList<>();
        camp = getResources().getString(R.string.campi);
        presen = getResources().getString(R.string.Presenza_non);
        errore = getResources().getString(R.string.Errore_fb);
        aggiu =getResources().getString(R.string.aggiunto_fb);
        conce = getResources().getString(R.string.Concesso);
        conce_camer = getResources().getString(R.string.Concesso_camera);
        conce_camer_non = getResources().getString(R.string.Concesso_camera_non);
        conce_galle = getResources().getString(R.string.Concesso_galleria);
        conce_galle_non = getResources().getString(R.string.Concesso_galleria_non);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Oggetti");
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Foto_Oggetti");
        SharedPreferences preference = getActivity().getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
        String email = preference.getString("Email", "");
        id_sito = preference.getString("Id_Sito", "");
        mStorageFoto = FirebaseStorage.getInstance().getReference().child("Item");
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt("Indice_Item",1);
        editor.putInt("OldPosition_Item",0);
        editor.commit();
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_new_item, menu);
            }

            //Bottone per aggiungere un nuovo percorso
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.add_new_item:
                        showDialog();
                        return true;
                }
                return false;
            }
        });
        //lettura dei dati da sql
        Local_route_Item(id_sito);
        recyclerView_Item = root.findViewById(R.id.itemList);
        //Controllo presenza internet
        if(isNetworkAvailable()) {
            //lettura dei dati da firebase
            loadFireKeyItem(value -> {
                adapter = new RecyclerItemAdapter(itemList, imageList, ItemFragment.this);
                recyclerView_Item.setAdapter(adapter);
            });
        }else{
            Toast.makeText(getContext(),errore,Toast.LENGTH_SHORT).show();
        }
        //Divisione degli item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView_Item.addItemDecoration(dividerItemDecoration);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView_Item);
        return root;

    }
    //Aggiunta delle chiavi delle zone nell'arraylist da firebase
    private void loadFireKeyItem(final FireCallback myCallback) {
        FireKeyList_Item.clear();
        mDatabase.orderByChild("item_Sito").equalTo(id_sito).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot2: snapshot.getChildren()){
                    FireKeyList_Item.add(snapshot2.getKey());
                }
                myCallback.onCallback("Ok");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }
    private Item deletedItem=null;
    private ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback( ItemTouchHelper.START | ItemTouchHelper.END,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {

        //Spostamento degli item
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
                case ItemTouchHelper.LEFT:
                    deletedItem = itemList.get(position);
                    itemList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Snackbar snackbar = Snackbar.make(recyclerView_Item, deletedItem.getItemTitle() + " "+ presen, Snackbar.LENGTH_LONG)
                            .setAction(R.string.undo, view -> {
                                itemList.add(position, deletedItem);
                                adapter.notifyItemInserted(position);
                                //DeleteFirebase e aggiornamento ordini zone
                            }).addCallback(new Snackbar.Callback(){
                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    super.onDismissed(transientBottomBar, event);
                                    SharedPreferences preference = getActivity().getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
                                    if (event == DISMISS_EVENT_TIMEOUT) {
                                        OldPosition = preference.getInt("OldPosition_Item",0);
                                        indice = preference.getInt("Indice_Item",0);
                                        if(indice==0){
                                            mDatabase.child(""+ FireKeyList_Item.get(position).toString()).removeValue();
                                            LocalDB.delete_Item(position+1);
                                            LocalDB.delete_Item_Image(imageList.get(position).getItem_id().toString());
                                            indice++;
                                            OldPosition =position;
                                        }else{
                                            if(position >= OldPosition){
                                                mDatabase.child(""+ FireKeyList_Item.get(position).toString()).removeValue();
                                                LocalDB.delete_Item(position+indice);
                                                LocalDB.delete_Item_Image(imageList.get(position).getItem_id().toString());
                                                indice++;
                                                OldPosition = position;
                                            }else{
                                                if(position < OldPosition){
                                                    mDatabase.child(""+ FireKeyList_Item.get(position).toString()).removeValue();
                                                    LocalDB.delete_Item(position+1);
                                                    LocalDB.delete_Item_Image(imageList.get(position).getItem_id().toString());
                                                    OldPosition = position;
                                                }
                                            }
                                        }
                                        imageList.remove(position);
                                        Find_Fire_Item_Pic_Key(position, value -> {
                                            mDatabase2.child(""+ value).removeValue();
                                            StorageReference foto_item = mStorageFoto.child(value);
                                            foto_item.delete().addOnSuccessListener(unused -> {
                                                SharedPreferences.Editor editor = preference.edit();
                                                editor.putInt("Indice_Item",indice);
                                                editor.putInt("OldPosition_Item", OldPosition);
                                                editor.commit();
                                            });
                                        });
                                        FireKeyList_Item.remove(position);
                                    }
                                }
                            });
                    snackbar.show();
                    break;

                case ItemTouchHelper.RIGHT:
                    final Item areaToUpdate = itemList.get(position);
                    showDialogEditItem(position);
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
                    p.setColor(ContextCompat.getColor(getContext(), R.color.red_delete));
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), p);
                    icon.draw(c);
                }
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    //Apertura del layout  e cambiamento dei dati con gli item, dopo che essi vengono letti da sql
    private void showDialogEditItem(int position) {
        final String[] item_title = new String[1];
        final String[] item_descr = new String[1];
        final String[] item_typology = new String[1];
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog_add_new_item,null);
        Button acceptButton = view.findViewById(R.id.ad_button_do);
        EditText et1 = view.findViewById(R.id.itemName_alert_dialog);
        EditText et2 = view.findViewById(R.id.itemDescr_alert_dialog);
        EditText et3 = view.findViewById(R.id.itemTypology_alert_dialog);
        TextView tv1 = view.findViewById(R.id.textview_new_edit_item);
        Spinner dropdown = view.findViewById(R.id.itemArea_alert_dialog);
        localArea(id_sito);
        ArrayAdapter<String> adapterAreaSpinner = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, areaList);
        dropdown.setAdapter(adapterAreaSpinner);
        fot = view.findViewById(R.id.button_add_img);
        pick = view.findViewById(R.id.route_pic);
        tv1.setText(R.string.alert_dialog_edit_item);
        et1.setText(itemList.get(position).getItemTitle());
        et2.setText(itemList.get(position).getItemDescr());
        et3.setText(itemList.get(position).getItemTypology());
        pick.setImageBitmap(imageList.get(position).getItem_imgSql());
        dropdown.setSelection(Integer.parseInt(itemList.get(position).getItem_Zona())-1);
        //bottone per inserire le immagini
        fot.setOnClickListener(v -> showDialog_img());
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(view).create();
        acceptButton.setOnClickListener(view1 -> {
            int iFoto=0;
            item_title[0] = et1.getText().toString();
            item_descr[0] = et2.getText().toString();
            item_typology[0] = et3.getText().toString();
            SharedPreferences preference = getActivity().getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
            OldPosition = preference.getInt("OldPosition_Area",0);
            indice = preference.getInt("Indice_Area",0);
            if(indice==0){
                LocalDB.update_item(position+1,item_title[0], item_descr[0],item_typology[0],areaKeyList.get(dropdown.getSelectedItemPosition()).toString());
                iFoto=position+1;
            }else{
                if(position >= OldPosition){
                    LocalDB.update_item(position+indice,item_title[0], item_descr[0],item_typology[0],areaKeyList.get(dropdown.getSelectedItemPosition()).toString());
                    iFoto=position+indice;
                }else{
                    if(position < OldPosition){
                        LocalDB.update_item(position+1,item_title[0], item_descr[0],item_typology[0],areaKeyList.get(dropdown.getSelectedItemPosition()).toString());
                        iFoto=position+1;
                    }
                }
            }
            if(foto_it != null){
                LocalDB.update_Foto_Items(iFoto, foto_it);
                Find_Fire_Item_Pic_Key(position, value -> {
                    StorageReference foto_item = mStorageFoto.child(value);
                    StorageReference foto_item2 = mStorageFoto.child(value);
                    foto_item.delete().addOnSuccessListener(unused -> foto_item2.putBytes(foto_it).addOnSuccessListener(taskSnapshot -> Toast.makeText(getContext(),getResources().getString(R.string.aggiunto_fb),Toast.LENGTH_LONG).show()));
                });
            }
            if(foto_it !=null){
                itemList.set(position, new Item(item_title[0],item_descr[0],item_typology[0],itemList.get(position).getItem_Zona(),itemList.get(position).getItem_Sito()));
                imageList.set(position, new ImageSql(getImage(foto_it), imageList.get(position).getItem_id()));
            }else{
                itemList.set(position, new Item(item_title[0],item_descr[0],item_typology[0],itemList.get(position).getItem_Zona(),itemList.get(position).getItem_Sito()));
            }
            mDatabase.child(""+ FireKeyList_Item.get(position).toString()).child("item_title").setValue(item_title[0]);
            mDatabase.child(""+ FireKeyList_Item.get(position).toString()).child("item_descr").setValue(item_descr[0]);
            mDatabase.child(""+ FireKeyList_Item.get(position).toString()).child("item_Zona").setValue(areaFireKeyList.get(dropdown.getSelectedItemPosition()).toString());
            mDatabase.child(""+ FireKeyList_Item.get(position).toString()).child("item_typology").setValue(item_typology[0]).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    alertDialog.cancel();
                    itemList.clear();
                    imageList.clear();
                    Local_route_Item(id_sito);
                    adapter = new RecyclerItemAdapter( itemList,imageList, ItemFragment.this);
                    ItemFragment.this.recyclerView_Item.setAdapter(adapter);
                    recyclerView_Item.setAdapter(adapter);
                }
            });
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }


    //Ottenimento della chiave dell'item da firebase
    private void Find_Fire_Item_Pic_Key(int position, final FireCallback myCallBack) {
        mDatabase2.orderByChild("item_id").equalTo(FireKeyList_Item.get(position).toString()).addValueEventListener(new ValueEventListener() {
            private String key;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    key = snapshot1.getKey();
                }
                myCallBack.onCallback(key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    //Apertura layout per inserire gli item e inserimento dei dati nel database
    void showDialog(){
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog_add_new_item,null);
        Button acceptButton = view.findViewById(R.id.ad_button_do);
        EditText et1 = view.findViewById(R.id.itemName_alert_dialog);
        EditText et2 = view.findViewById(R.id.itemDescr_alert_dialog);
        EditText et3 = view.findViewById(R.id.itemTypology_alert_dialog);
        Spinner dropdown = view.findViewById(R.id.itemArea_alert_dialog);
        localArea(id_sito);
        ArrayAdapter<String> adapterAreaSpinner = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, areaList);
        dropdown.setAdapter(adapterAreaSpinner);
        fot = view.findViewById(R.id.button_add_img);
        pick = view.findViewById(R.id.route_pic);
        fot.setOnClickListener(v -> showDialog_img());
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(view).create();
        acceptButton.setOnClickListener(view1 -> {
            String item_name = et1.getText().toString();
            String item_descr = et2.getText().toString();
            String item_typology = et3.getText().toString();
            if (TextUtils.isEmpty(item_name) || TextUtils.isEmpty(item_descr) || TextUtils.isEmpty(item_typology) ) {
                Toast.makeText(getContext(), camp, Toast.LENGTH_LONG).show();
            }else{
                Item item = new Item(item_name,item_descr,item_typology,areaFireKeyList.get(dropdown.getSelectedItemPosition()).toString(), id_sito);
                DatabaseReference ref = mDatabase.push();
                DatabaseReference ref2= mDatabase2.push();
                String idOggetti = ref.getKey();

                ref.setValue(item);
                ref2.setValue(new Image(ref2.getKey(), idOggetti));
                String result= LocalDB.addRecord_Item(item_name,item_descr,item_typology,areaKeyList.get(dropdown.getSelectedItemPosition()).toString(), id_sito);
                Cursor cursor = LocalDB.getLastItem();
                cursor.moveToNext();
                //controllo se c'è una foto scattata o scelta dalla galleria, inserimento di essa nel db
                if(foto_it != null){
                    Bitmap bmp = BitmapFactory.decodeByteArray(foto_it, 0, foto_it.length);
                    bmp = Bitmap.createScaledBitmap(bmp, 300, 300, true);
                    byte[] foto = getBitmapAsByteArray(bmp);
                    LocalDB.addFoto_Items(foto,cursor.getString(0));
                    StorageReference foto_item = mStorageFoto.child(ref2.getKey());
                    foto_item.putBytes(foto_it).addOnSuccessListener(taskSnapshot -> Toast.makeText(getContext(),aggiu,Toast.LENGTH_LONG).show());
                }//foto non inserita e quindi viene inserita la foto di default nel sistema
                else{
                    byte[] fotoBase = getBitmapAsByteArray(BitmapFactory.decodeResource(getResources(),R.drawable.logo_app_ok));
                    LocalDB.addFoto_Items(getBitmapAsByteArray(BitmapFactory.decodeResource(getResources(),R.drawable.logo_app_ok)) ,cursor.getString(0));
                    StorageReference foto_item = mStorageFoto.child(ref2.getKey());
                    foto_item.putBytes(fotoBase).addOnSuccessListener(taskSnapshot -> Toast.makeText(getContext(),aggiu,Toast.LENGTH_LONG).show());
                }
                if(result.equals("Success")){
                    alertDialog.cancel();
                    itemList.clear();
                    imageList.clear();
                    Local_route_Item(id_sito);
                    adapter = new RecyclerItemAdapter( itemList,imageList, ItemFragment.this);
                    ItemFragment.this.recyclerView_Item.setAdapter(adapter);
                    recyclerView_Item.setAdapter(adapter);
                }
            }});
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    //Lettura dati da locale
    private void localArea(String id) {
        areaList.clear();
        Cursor cursor= LocalDB.readAreas(id);
        String titolo,key,Firekey;
        if(cursor.moveToFirst()){
            do {
                key = cursor.getString(0);
                titolo = cursor.getString(1);
                Firekey = cursor.getString(5);
                areaList.add(titolo);
                areaKeyList.add(key);
                areaFireKeyList.add(Firekey);
                cursor.moveToNext();
            }while(!cursor.isAfterLast());
        }
    }
    //Aggiunta della foto attraverso un intent
    void showDialog_img(){
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog_add_new_img,null);
        ImageButton camera = view.findViewById(R.id.IB_camera);
        ImageButton galley = view.findViewById(R.id.IB_gallery);
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(view).create();
        //Bottone della fotocamera
        camera.setOnClickListener(view12 -> {
            //Richiesta dei permessi per la fotocamera
            checkPerm(Manifest.permission.CAMERA,CAMERA_PERMISSION_CODE);
            if (camera_perm) {
                Intent fotoCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(fotoCamera, CAMERA_REQ_CODE);
                alertDialog.cancel();
            }
        });
        //Bottone della galleria
        galley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Richiesta dei permessi per la galleria
                checkPerm(Manifest.permission.READ_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE);
                if (gallery_perm) {
                    Intent Foto_Gallery = new Intent(Intent.ACTION_PICK);
                    Foto_Gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Foto_Gallery, GALLERY_REQ_CODE);
                    alertDialog.cancel();

                }
            }
        });

        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    //Controllo dei permessi
    public void checkPerm(String permissioon,int requestcode){
        if(ContextCompat.checkSelfPermission(getContext(),permissioon)== PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{permissioon},requestcode);
        }else{
            Toast.makeText(getContext(), conce, Toast.LENGTH_SHORT).show();
            if (requestcode==CAMERA_PERMISSION_CODE){
                camera_perm =true;
            }
            else{
                gallery_perm =true;
            }
        }
    }

    //Risultato dei permessi ottenuti e risposta in caso di successo o di fallimento
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CAMERA_PERMISSION_CODE){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                camera_perm =true;
                Toast.makeText(getContext(), conce_camer, Toast.LENGTH_SHORT).show();
            }else{
                camera_perm =false;
                Toast.makeText(getContext(), conce_camer_non, Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode==STORAGE_PERMISSION_CODE){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                gallery_perm =true;
                Toast.makeText(getContext(), conce_galle, Toast.LENGTH_SHORT).show();
            }else{
                gallery_perm =false;
                Toast.makeText(getContext(), conce_galle_non, Toast.LENGTH_SHORT).show();
            }
        }
    }
    //Lettura dati da locale e aggiunto nell'ArrayList di item e di immagine
    private void Local_route_Item(String ID){
        String titolo;
        String descrizione;
        String tipologia;
        String id_sito;
        String id_zona;
        String id;
        Cursor cursor= LocalDB.readItem(ID);
        byte[] foto;
        int j=0;
        if(cursor.moveToFirst() ) {
            do {
                id = cursor.getString(0);
                titolo = cursor.getString(1);
                tipologia = cursor.getString(2);
                descrizione = cursor.getString(3);
                id_sito = cursor.getString(5);
                id_zona = cursor.getString(6);
                id_Item[j] = id;
                tito_temp[j] = titolo;
                j++;
                foto=cursor.getBlob(4);
                Bitmap itemFoto= getImage(foto);
                itemList.add(new Item(titolo, descrizione, tipologia,id_zona,id_sito));
                imageList.add(new ImageSql(itemFoto,id));
                cursor.moveToNext();
            } while (!cursor.isAfterLast() );
        }
    }

    //Funzione per ottenere il risultato dell'immagine scattata o presa dalla galleria
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE) {
                pick.setImageURI(data.getData());
                uri1 = data.getData();
                try {
                    img = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pick.setImageBitmap(img);
                foto_it =getBitmapAsByteArray(img);
            }
            else if(requestCode == CAMERA_REQ_CODE){
                //Dalla fotocamera
                img = (Bitmap)(data.getExtras().get("data"));
                pick.setImageBitmap(img);
                foto_it =  getBitmapAsByteArray(img);
            }
        }
    }
    //Funzione di conversione da bitmap a byte
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, outputStream);
        byt =outputStream.toByteArray();
        return byt;
    }


    @Override
    public void onItemClick(int position) {
        Intent intentItem = new Intent(getContext(), Item_Info.class);
        intentItem.putExtra("key_item", id_Item[position]);
        intentItem.putExtra("item", itemList.get(position));
        intentItem.putExtra("item_img", imageList.get(position).getItem_imgSql());
        startActivity(intentItem);
    }

    @Override
    public void onLongItemClick(int position) {
    }

    //Aggiungere Controllo se non c'è la connesione ad internet
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

