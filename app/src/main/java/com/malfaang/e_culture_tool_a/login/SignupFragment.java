package com.malfaang.e_culture_tool_a.login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.malfaang.e_culture_tool_a.Database;
import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.firebase.Autenticazione;
import com.malfaang.e_culture_tool_a.firebase.DbFirebase;


public class SignupFragment extends Fragment {
    private EditText email1;
    private EditText password1;
    private EditText nome;
    private EditText confermaPassword;
    private EditText cognome;
    private EditText telefono;
    private RadioGroup tipologia;
    private Button signup;
    private String tipologia2;
    private FirebaseAuth mAuth;
    private String errore;
    private String camp;
    private String coincidenza;
    private String corta;
    private String nomValid;
    private String teleValid;
    private DbFirebase dbFirebase;
    private Autenticazione identificazione;
    private Database local;
    private logout fuori;

    //Creazione del layout con collegamento agli oggetti
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment,container,false);
        tipologia = (RadioGroup)root.findViewById(R.id.Tipologia_Area);
        local = new Database(getContext());
        email1 = root.findViewById(R.id.email1);
        password1 = root.findViewById(R.id.password1);
        confermaPassword = root.findViewById(R.id.conferma_pass);
        nome = root.findViewById(R.id.nome);
        cognome = root.findViewById(R.id.cognome);
        telefono = root.findViewById(R.id.Telefono);
        signup = root.findViewById(R.id.signup);
        fuori = new logout();
        errore = getResources().getString(R.string.Errore_fb);
        camp = getResources().getString(R.string.campi);
        corta = getResources().getString(R.string.password_corta);
        coincidenza = getResources().getString(R.string.password_non_coincide);
        nomValid = getResources().getString(R.string.nome_non_valido);
        teleValid = getResources().getString(R.string.telefono_non_valido);
        identificazione = new Autenticazione();
        dbFirebase = new DbFirebase();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!= null) {
            startActivity(new Intent(getContext(), logout.class));
        }
        // Scelta della tipologia tramite radiogroup
        tipologia.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.radio_curatore) {
                tipologia2 = "Curatore";
            } else if (i == R.id.radio_guida) {
                tipologia2 = "Guida";
            } else if (i == R.id.radio_visitatore) {
                tipologia2 = "Visitatore";
            }
        });
        signup.setOnClickListener(view -> registrazione());
        return root;
    }

    //Funzione che prende tutti i campi e richiama le funzioni di firebase
    private void registrazione() {
        String email = email1.getText().toString();
        String pass = password1.getText().toString();
        String prov = confermaPassword.getText().toString();
        String nom = nome.getText().toString();
        String cogn = cognome.getText().toString();
        String tipologia2 = this.tipologia2;
        String telefono = this.telefono.getText().toString();
        if(isNetworkAvailable()) {
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(prov) || TextUtils.isEmpty(nom)
                    || TextUtils.isEmpty(cogn) || TextUtils.isEmpty(tipologia2)
                    || TextUtils.isEmpty(telefono)) {
                Toast.makeText(getContext(), camp, Toast.LENGTH_LONG).show();
            } else {
                if (emailNonValida(email)) {
                    if (pass.equals(prov)) {
                        if (nomeNonValido(nom)) {
                            if (nomeNonValido(cogn)) {
                                if (telNonValido(telefono)) {
                                    if (passNonValida(pass)) {
                                        identificazione.RegistrazioneFire(email, pass, mAuth);
                                        String idUte = dbFirebase.scritturadbfi(nom, cogn, email, pass, tipologia2, telefono);
                                        Intent login = new Intent(getContext(), LoginActivity.class);
                                        startActivity(login);
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), coincidenza, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }else{
            Toast.makeText(getContext(),errore,Toast.LENGTH_SHORT).show();
        }
    }


    //Funzioni di controllo per la registrazione
    private Boolean emailNonValida(String email){
        return email.contains("@");
    }
    private Boolean passNonValida(String pass) {
        Boolean isValid = false;
        int i, num;
        if (pass.length() >= 8) {
            if (pass.contains("@") || pass.contains("!") || pass.contains("?") || pass.contains("<")
                    || pass.contains(">") || pass.contains(";") || pass.contains(".") || pass.contains("[0-9]+")) {
                return true;
            } else {
                Toast.makeText(getContext(), corta, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return false;
    }

    private Boolean nomeNonValido(String nome){
        boolean isValid= true;
        for(int i=0;i<nome.length();i++){
            char c= nome.charAt(i);
            if(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
                continue;
            }
            isValid=false;
        }
        if(isValid){
            return true;
        }
        else{
            Toast.makeText(getContext(), nomValid,Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private Boolean telNonValido(String telefono){
        Boolean isValid=true;
        if(telefono.length()!=10){
            Toast.makeText(getContext(), teleValid,Toast.LENGTH_LONG).show();
            return false;
        }

        for(int i=0;i<telefono.length();i++){
            char c= telefono.charAt(i);
            if(c >= '0' && c <= '9') {
                continue;
            }
            isValid=false;
        }

        if(isValid){
            return true;
        }
        else{
            Toast.makeText(getContext(), teleValid,Toast.LENGTH_LONG).show();
            return false;
        }

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
