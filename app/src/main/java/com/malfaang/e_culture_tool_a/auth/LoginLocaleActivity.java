package com.malfaang.e_culture_tool_a.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.database.DatabaseHelper;

import java.util.List;
import java.util.Objects;


public class LoginLocaleActivity extends AppCompatActivity {
    private TextInputEditText emailEdit = null;
    private TextInputEditText passwordEdit = null;

    public LoginLocaleActivity() {
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_locale);

        ControlloCredenziali checker = new ControlloCredenziali();
        emailEdit = findViewById(R.id.idUserName);
        passwordEdit = findViewById(R.id.idPassword);
        Button loginBtn = findViewById(R.id.idBtnLogin);
        loginBtn.setOnClickListener(view -> signInEmailPassword());
        Button registerBtn = findViewById(R.id.Register);
        registerBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistrazioneLocaleActivity.class);
            startActivity(intent);
        });
    }

    public final void signInEmailPassword() {
        String email = Objects.requireNonNull(emailEdit.getText()).toString();
        String password = Objects.requireNonNull(passwordEdit.getText()).toString();

       /* if (checker.checkEmail(emailEdit, LoginLocaleActivity.this)
                && checker.checkPassword(passwordEdit, LoginLocaleActivity.this)) {
            if (!NetworkConnectivity.check(getApplicationContext())) {
                Toast.makeText(LoginLocaleActivity.this, R.string.no_connection, Toast.LENGTH_SHORT).show();
                return;
            } else {*/
        List<String> res;
        try (DatabaseHelper db = new DatabaseHelper(this)) {
            res = db.selectUtente(email);
        }
        System.out.println(res);
                if(res.isEmpty()){
                    System.out.println("Utente non trovato");
                    //TODO inserire una label con messaggio di errore "Utente non trovato"
                    //TODO Far diventare le texfield rosse per notificare l'errore
                } else {
                    String email2 = res.get(0);
                    String password2 = res.get(1);
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                }
            }
}


