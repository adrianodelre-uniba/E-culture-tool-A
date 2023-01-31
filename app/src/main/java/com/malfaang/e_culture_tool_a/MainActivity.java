package com.malfaang.e_culture_tool_a;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;
import static androidx.core.content.FileProvider.getUriForFile;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.gson.Gson;
import com.malfaang.e_culture_tool_a.databinding.ActivityMainBinding;
import com.malfaang.e_culture_tool_a.newRoute.Area_Order;
import com.malfaang.e_culture_tool_a.newRoute.Route;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public Database db;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ArrayList<Route> percorsi;
    public static final String PREFS_NAME = "MyPrefsFile";
    private ArrayList<Area_Order> ordine;
    private String idSito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();

        db = new Database(getApplicationContext());
        percorsi = new ArrayList<>();
        ordine = new ArrayList<>();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        SharedPreferences preference = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        idSito = preference.getString("Id_Sito", "");

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer_user_logged);

            View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main_user_logged, navigationView, false);
            navigationView.addHeaderView(header);
            navigationView.removeHeaderView(navigationView.getHeaderView(0));

            TextView username = header.findViewById(R.id.user_name);
            TextView userEmail = header.findViewById(R.id.user_email);
            TextView userTypology = header.findViewById(R.id.user_typology);
            String email = user.getEmail();

            preference = getApplicationContext().getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
            String nome = preference.getString("Nome_Utente", "");
            String cognome = preference.getString("Cognome_Utente", "");
            String tipologia = preference.getString("Tipologia_Utente", "");

            userEmail.setText(email);
            username.setText(nome + " " + cognome);
            userTypology.setText(tipologia);
        }
    }

    // Crea tasti condivisione percorso e tutorial su homepage
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.share_route:
                shareRoute();
                return true;
            case R.id.tutorial:
                showTutorial();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //Apertura layout per il tutorial
    private void showTutorial() {
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_tutorial,null);
        ImageView imgTutorial = view.findViewById(R.id.tutorial_img);
        TextView tvTutorial = view.findViewById(R.id.tutorial_desc);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();

        imgTutorial.setImageDrawable(getResources().getDrawable(R.drawable.buttonRoute));
        tvTutorial.setText(getResources().getString(R.string.route_tutorial));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setOnCancelListener(dialogInterface -> {
            imgTutorial.setImageDrawable(getResources().getDrawable(R.drawable.filters));
            tvTutorial.setText(getResources().getString(R.string.filters_tutorial));
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            dialog.setOnCancelListener(dialogInterface1 -> {
                imgTutorial.setImageDrawable(getResources().getDrawable(R.drawable.button_resources));
                tvTutorial.setText(getResources().getString(R.string.resources_tutorial));
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                dialog.setOnCancelListener(dialogInterface11 -> {
                    imgTutorial.setImageDrawable(getResources().getDrawable(R.drawable.button_site));
                    tvTutorial.setText(getResources().getString(R.string.site_tutorial));
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    dialog.setOnCancelListener(dialogInterface111 -> {
                        imgTutorial.setImageDrawable(getResources().getDrawable(R.drawable.fab));
                        tvTutorial.setText(getResources().getString(R.string.fab_tutorial));
                        dialog.getWindow().setGravity(Gravity.CENTER);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                        dialog.setOnCancelListener(dialogInterface1111 -> {
                            imgTutorial.setImageDrawable(getResources().getDrawable(R.drawable.share_route));
                            tvTutorial.setText(getResources().getString(R.string.share_route_tutorial));
                            dialog.getWindow().setGravity(Gravity.CENTER);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();
                            dialog.setOnCancelListener(dialogInterface11111 -> {
                                imgTutorial.setImageDrawable(getResources().getDrawable(R.drawable.share_route));
                                tvTutorial.setText("Per modificare Zone o Item puoi trascinare verso destra");
                                dialog.getWindow().setGravity(Gravity.CENTER);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.show();
                                dialog.setOnCancelListener(dialogInterface111111 -> {
                                    imgTutorial.setImageDrawable(getResources().getDrawable(R.drawable.share_route));
                                    tvTutorial.setText("Per cancellare Zone o Item puoi trascinare verso sinistra");
                                    dialog.getWindow().setGravity(Gravity.CENTER);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.show();
                                    dialog.setOnCancelListener(dialogInterface1111111 -> {
                                        imgTutorial.setImageDrawable(getResources().getDrawable(R.drawable.share_route));
                                        tvTutorial.setText("Per mostrare graficamente il percorso selezionato cliccare sul simbolo della map nella pagina del percorso");
                                        dialog.getWindow().setGravity(Gravity.CENTER);
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        dialog.show();
                                        dialog.dismiss();
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    //Funzione per condividere il percorso
    private void shareRoute() {
        creazione(idSito);
        Intent intentCondivisione = new Intent();
        intentCondivisione.setAction(Intent.ACTION_SEND);
        Gson gson= new Gson();
        Gson gson2= new Gson();
        String order = gson2.toJson(ordine);
        ordine.clear();
        String routes = gson.toJson(percorsi);
        StringBuilder filename = new StringBuilder();
        filename.append("Percorsi.json");
        File dir = new File(getApplicationContext().getFilesDir(),"Percorsi");
        if(!dir.exists()){
            dir.mkdir();
        }
        File file = new File(dir, filename.toString());
        if (file.exists()){
            file.delete();
            FileWriter writer = null;
            try {
                writer = new FileWriter(file);
                writer.write(order);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(order);
                writer.flush();
                writer.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        Uri contentUri = getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID+".provider",file);
        intentCondivisione.setType("text/plain");
        intentCondivisione.putExtra(Intent.EXTRA_STREAM,contentUri);
        startActivity(Intent.createChooser(intentCondivisione, " Share file... "));
    }

    public void creazione(String id) {
        Database localDB;
        localDB = new Database(getApplicationContext());
        boolean goBack;
        Cursor cursor = localDB.readAllRoute(id);
        if (cursor.moveToFirst()) {
            do {
                String idd = cursor.getString(0);
                String titolo = cursor.getString(1);
                String descrizione = cursor.getString(2);
                String tipologia = cursor.getString(3);
                String keyRoute = cursor.getString(4);
                String idSito = cursor.getString(5);
                String data = cursor.getString(6);
                String boole = cursor.getString(7);
                if(boole.equals("true")){
                    goBack=true;
                }else{
                    goBack=false;
                }
                percorsi.add(new Route(titolo, descrizione, tipologia, keyRoute, idSito, data,goBack));
                cursor.moveToNext();
                Cursor cursor1 = localDB.readSecondAreasOrder(idd);
                if (cursor1.moveToFirst()) {
                    do {
                        String area1 = cursor1.getString(0);
                        String area2 = cursor1.getString(1);
                        ordine.add(new Area_Order(area1, area2));
                        cursor1.moveToNext();
                    } while (!cursor1.isAfterLast());
                }
            } while (!cursor.isAfterLast());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void loadLocale() {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        setLocale(language);
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = new Locale(lang.toLowerCase());
        res.updateConfiguration(conf,dm);
    }
}
