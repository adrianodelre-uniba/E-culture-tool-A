package com.malfaang.e_culture_tool_a.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.database.DatabaseHelper;
import com.malfaang.e_culture_tool_a.utility.NetworkConnectivity;

import java.util.List;
import java.util.Objects;


public class LoginLocaleActivity extends AppCompatActivity {
    private TextInputEditText emailEdit;
    private TextInputEditText passwordEdit;
    private ControlloCredenziali checker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_locale);

        checker = new ControlloCredenziali();
        emailEdit = findViewById(R.id.idUserName);
        passwordEdit = findViewById(R.id.idPassword);
        Button loginBtn = findViewById(R.id.idBtnLogin);
        loginBtn.setOnClickListener(view -> signInEmailPassword());
        Button registerBtn = findViewById(R.id.Register);
        registerBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LoginLocaleActivity.this, RegistrazioneLocaleActivity.class);
            startActivity(intent);
        });
    }

    public void signInEmailPassword() {
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();

       /* if (checker.checkEmail(emailEdit, LoginLocaleActivity.this)
                && checker.checkPassword(passwordEdit, LoginLocaleActivity.this)) {
            if (!NetworkConnectivity.check(getApplicationContext())) {
                Toast.makeText(LoginLocaleActivity.this, R.string.no_connection, Toast.LENGTH_SHORT).show();
                return;
            } else {*/
                DatabaseHelper db = new DatabaseHelper(this);
                List<String> res = db.selectUtente(email);
                System.out.println(res.toString());
                if(res.size() == 0){
                    System.out.println("Utente non trovato");
                    //TODO inserire una label con messaggio di errore "Utente non trovato"
                    //TODO Far diventare le texfield rosse per notificare l'errore
                } else {
                    Intent intent = new Intent(LoginLocaleActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
            //TODO passare dati a DatabaseHelper
}


