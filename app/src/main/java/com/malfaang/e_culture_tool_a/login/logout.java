package com.malfaang.e_culture_tool_a.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.malfaang.e_culture_tool_a.MainActivity;
import com.malfaang.e_culture_tool_a.R;
import com.google.firebase.auth.FirebaseAuth;

public class logout extends AppCompatActivity {
    String errore,uscito;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

                 errore = getResources().getString(R.string.Errore_fb);
                 uscito = getResources().getString(R.string.logout_eseguito);
                showDialog();



    }
    //Layout aggiuntivo come notifica per confermare il logout o no all'utente
    void showDialog(){
        LayoutInflater inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.alert_dialog_logout,null);

        Button acceptButton = view.findViewById(R.id.ad_button_yes);
        Button cancelButton = view.findViewById(R.id.ad_button_no);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),uscito, Toast.LENGTH_LONG).show();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    //Funzione per eseguire il logout e svuotamento delle sharedPreferences
    public void logout(){
        if(isNetworkAvailable()) {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
            preferences.edit().clear().commit();
        }else{
            Toast.makeText(getApplicationContext(),errore,Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNetworkAvailable() {
        Context context = getApplicationContext();
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