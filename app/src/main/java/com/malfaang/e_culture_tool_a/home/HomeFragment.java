package com.malfaang.e_culture_tool_a.home;

import static android.content.Context.MODE_PRIVATE;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.os.Environment;
import android.text.Html;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.malfaang.e_culture_tool_a.Database;
import com.malfaang.e_culture_tool_a.MainActivity;
//import com.example.prvaloocale.R;
//import com.example.prvaloocale.Splash_Screen;
import com.malfaang.e_culture_tool_a.databinding.FragmentHomeBinding;
import com.malfaang.e_culture_tool_a.addElements.AddElements;
import com.malfaang.e_culture_tool_a.newRoute.Area_Order;
import com.malfaang.e_culture_tool_a.newRoute.Route;
import com.malfaang.e_culture_tool_a.QRcode.CaptureAct;

import com.malfaang.e_culture_tool_a.routes.Add_Route;
import com.malfaang.e_culture_tool_a.sito.NewSite;
import com.malfaang.e_culture_tool_a.sito.SitoUtente;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.File;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private FragmentHomeBinding binding;
    public static final String PREFS_NAME = "MyPrefsFile";
    private Database LocalDB;
    private boolean pass=false;
    private FloatingActionButton associazione;
    private DatabaseReference   mDatabase3;

    private ArrayList<Route> percorsi;
    private ArrayList<Area_Order> ordine;
    private String TAG = "InternalFileWriteReadActivity";
    String path = Environment.getExternalStorageDirectory() + File.separator + "com.example.prvaloocale/app_cache" + File.separator;
    String errore,associa,inquadra_qr,download;

    //Creazione del layout con collegamento agli oggetti
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        inquadra_qr = getResources().getString(R.string.inquadra);
        download = getResources().getString(R.string.download_file);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        LocalDB = new Database(getContext());
        mDatabase3 = FirebaseDatabase.getInstance().getReference().child("Sito_Utente");
        SharedPreferences preference;
        preference = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String id_sito = preference.getString("Id_Sito", "");
        String tipologia = preference.getString("Tipologia_Utente", "");
        controllo_tipologia_utente(tipologia);
        controllo_Presenza_sito(id_sito);
        associa = getResources().getString(R.string.Scansione);
        File dir = new File(String.valueOf(path));
        percorsi = new ArrayList<>();
        ordine = new ArrayList<>();



        //creazione dei bottoni
        binding.newRouteButtton.setText(Html.fromHtml("<title>" + getResources().getString(R.string.miei_per) + "</title>" + "<br><br />" +
                "<small>" + getResources().getString(R.string.cre_mod) + "</small>"));

        binding.addElements.setText(Html.fromHtml("<title>" + getResources().getString(R.string.miei_ris) + "</title>" + "<br><br />" +
                "<small>" + getResources().getString(R.string.aggiu_zo) + "</small>"));

        binding.newSiteButtton.setText(Html.fromHtml("<title>" + getResources().getString(R.string.sito_m) + "</title>" + "<br><br />" +
                "<small>" + getResources().getString(R.string.mod_sit) + "</small>"));

        binding.newRouteButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Add_Route.class);
                startActivity(i);
            }
        });
        //Bottone per essere reinderizzati nelle tue risorse
        binding.addElements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddElements.class);
                startActivity(i);
            }
        });
        //Bottone per essere reinderizzati nel sito
        binding.newSiteButtton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), NewSite.class);
                startActivity(i);
            }
        });

        //Bottone per scannerizzare (se si è un curatore), bottone per creare un qrcode ( se sei un curatore)
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Guida = getResources().getString(R.string.guida);
                String Visitatore = getResources().getString(R.string.visitatore);

                if (tipologia.equals(Guida)|| tipologia.equals(Visitatore)) {

                    scanCode();




                } else {

                    view = inflater.inflate(R.layout.alert_dialog_share_qr_code, null);

                    Button doButton = view.findViewById(R.id.ad_button_do);
                    ImageView iv1 = view.findViewById(R.id.qr_code_image_view);
                    TextView tv_qr = view.findViewById(R.id.tv_shareQR);

                    try {
                        tv_qr.setText(associa);
                        Bitmap bitmap = encodeAsBitmap(id_sito);
                        iv1.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }


                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(view).create();

                    doButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.cancel();
                        }
                    });

                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();
                }

            }
        });

        return root;
    }




    //Funzione per controllare che tipologia di utente sei, a seconda di questa escono i vari bottoni
    private void controllo_tipologia_utente(String tipologia) {

        if(tipologia.equals(getResources().getString(R.string.curatore))){
            binding.addElements.setVisibility(View.VISIBLE);
            binding.newSiteButtton.setVisibility(View.VISIBLE);
        }

        if(tipologia.equals(getResources().getString(R.string.guida))){
            binding.addElements.setVisibility(View.INVISIBLE);
            binding.newSiteButtton.setVisibility(View.INVISIBLE);
        }

        if(tipologia.equals(getResources().getString(R.string.visitatore))){
            binding.addElements.setVisibility(View.INVISIBLE);
            binding.newSiteButtton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    //Creazione del qrcode
    Bitmap encodeAsBitmap(@NonNull String str) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(str, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);

        int w = bitMatrix.getWidth();
        int h = bitMatrix.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                pixels[y * w + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }

    //funzione che legge i dati del sito e ritorna se il sito è presente o no tramite l'id del curatore
    private Boolean controllo_Presenza_sito(String id_sito){
        Boolean risposta=false;
        Cursor cursor;
        cursor=LocalDB.getSitoByKey(id_sito);
        if (cursor.getCount()<1){
            risposta=true;
        }
        return risposta;
    }
    //Funzione per scannerizzare il qrcode
    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt(inquadra_qr);
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() !=null) {
            final Intent opera = new Intent(getActivity(), Splash_Screen.class);
            String str = result.getContents();
            SharedPreferences preference = getActivity().getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
            SharedPreferences.Editor editor = preference.edit();
            editor.putString("Id_Sito",str);
            editor.commit();
            String id_utente = preference.getString("Id_Utente", "");
            LocalDB.addRecordSito_Utente(str,id_utente);
            mDatabase3.push().setValue(new Sito_Utente(str,id_utente));


            startActivity(opera);
        }else{
            final Intent i = new Intent( getContext(), MainActivity.class);
            startActivity(i);
        }
    });
}



