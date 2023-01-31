package com.malfaang.e_culture_tool_a.login;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malfaang.e_culture_tool_a.Database;
import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.Splash_Screen;
import com.malfaang.e_culture_tool_a.sito.NewSite;

public class  LoginFragment extends Fragment {
    private EditText email;
    private EditText password;
    private Database db;
    private Button login;
    private Button di;
    private TextView forgotPassword;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    private String errore;
    private String camp;
    private String entra;
    private String fallimento;
    private FirebaseAuth mAuth;
    public static final String PREFS_NAME = "MyPrefsFile";
    private boolean check = false;
    //Creazione del layout e abbinamento con i componenti
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Sito");
        forgotPassword = (TextView) root.findViewById(R.id.passdim);
        email = (EditText) root.findViewById(R.id.email);
        password = (EditText) root.findViewById(R.id.password);
        login = (Button) root.findViewById(R.id.login);
        db = new Database(getContext());
        mAuth = FirebaseAuth.getInstance();
        camp = getResources().getString(R.string.campi);
        errore = getResources().getString(R.string.Errore_fb);
        entra = getResources().getString(R.string.Entrata);
        fallimento = getResources().getString(R.string.auth_falli);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
        login.setOnClickListener(this::onClick);
        forgotPassword.setOnClickListener(this::onClick);
        return root;
    }
    //Funzione per fare il login dell'utente
    private void log() {
        String em = email.getText().toString();
        String pass = password.getText().toString();
        if(isNetworkAvailable()) {
            if (TextUtils.isEmpty(em) || TextUtils.isEmpty(pass)) {
                Toast.makeText(getContext(), camp, Toast.LENGTH_LONG).show();
            } else {
                mAuth.signInWithEmailAndPassword(em, pass)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Utente");
                                FirebaseUser user = mAuth.getCurrentUser();
                                key(mAuth, value -> {
                                    Toast.makeText(getContext(), entra, Toast.LENGTH_LONG).show();
                                    SharedPreferences preference = getActivity().getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
                                    String tipologia = preference.getString("Tipologia_Utente", "");
                                    String idUtente = preference.getString("Id_Utente", "");
                                    checkFireInfoSite(idUtente, value1 -> {
                                        final Intent i;
                                        String siteExists = preference.getString("Site_Exists", "");
                                        if (tipologia.equals("Curatore") && siteExists.equals("false")) {
                                            i = new Intent(getContext(), NewSite.class);
                                        } else {
                                            i = new Intent(getContext(), Splash_Screen.class);
                                        }
                                        startActivity(i);
                                    });
                                });
                            } else {
                                // Se l'autenticazione fallisce, esce questo messaggio
                                Toast.makeText(getContext(), fallimento,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }else{
            Toast.makeText(getContext(),errore,Toast.LENGTH_SHORT).show();
        }
    }

    //funzione per controllare che tipologia di utente logga
    private void checkFireInfoSite(String idUtente, final FireCallback myCallback) {
        mDatabase2= FirebaseDatabase.getInstance().getReference().child("Sito");
        mDatabase2.orderByChild("id_Curatore").equalTo(idUtente).addListenerForSingleValueEvent(new ValueEventListener() {
            SharedPreferences preference = getActivity().getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preference.edit();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 0){
                    editor.putString("Site_Exists","false");
                }else{
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        editor.putString("Site_Exists","true");
                        editor.putString("Id_Sito",snapshot1.getKey());
                        editor.putString("Nome_Sito",snapshot1.child("nome_sito").getValue().toString());
                    }
                }
                editor.commit();
                myCallback.onCallback("OK");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                editor.putString("Site_Exists","false");
            }
        });
    }


    //Funzione per recuperare la chiave dell'utente
    public void key(FirebaseAuth mAuth,final FireCallback myCallBack){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(mAuth.getCurrentUser()!=null){
            String userEmail = user.getEmail();
            mDatabase.orderByChild("email").equalTo(userEmail).addValueEventListener(new ValueEventListener() {
                SharedPreferences preference = getActivity().getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preference.edit();

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        String idUte = snapshot1.getKey();
                        String tipologiaUtente = snapshot1.child("tipologia").getValue().toString();
                        String nome = snapshot1.child("nome").getValue().toString();
                        String cognome = snapshot1.child("cognome").getValue().toString();
                        String telefono = snapshot1.child("telefono").getValue().toString();
                        editor.putString("Id_Utente",idUte);
                        editor.putString("Tipologia_Utente",tipologiaUtente);
                        editor.putString("Nome_Utente",nome);
                        editor.putString("Cognome_Utente",cognome);
                        editor.putString("Telefono_Utente",telefono);
                    }
                    editor.putString("Email",userEmail);
                    editor.commit();
                    myCallBack.onCallback("OK");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            SharedPreferences preference = getActivity().getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
            String idUte= preference.getString("Id_Utente","");
        }
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.passdim:
                startActivity(new Intent(getContext(), ForgotPassword.class));
                break;
            case R.id.login:
                log();
                break;
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
