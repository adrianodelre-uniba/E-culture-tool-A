package com.malfaang.e_culture_tool_a.registrazioneAndLogin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.database.DatabaseHelper;

import java.util.Objects;


public class LoginLocaleActivity extends AppCompatActivity {
    private EditText emailEdit;
    private EditText passwordEdit;

    public LoginLocaleActivity() {
    }

    @SuppressLint("WrongViewCast")
    @Override
    public final void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_locale);

        ControlloCredenziali checker = new ControlloCredenziali();
        emailEdit = findViewById(R.id.Email);
        emailEdit.setText("mail@test.com");
        passwordEdit = findViewById(R.id.Password);
        passwordEdit.setText("12345678");
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
        String ciao = "";
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.controlloCredenzialiLogin(email,password);
        if (cursor.moveToFirst()) {

            do {
                Toast.makeText(this, "entratio", Toast.LENGTH_SHORT).show();
                ciao = cursor.getString(0);


                cursor.moveToNext();


            } while (!cursor.isAfterLast());
        }
        // TODO Rimuovere alla fine di tutto
        Toast.makeText(this, "ccc" + ciao, Toast.LENGTH_SHORT).show();
       /* List<String> res;
        try (DatabaseHelper db = new DatabaseHelper(this)) {
            res = db.selectUtente(email);
        }
        System.out.println(res);
                if(ciao.isEmpty()){
                    System.out.println("Utente non trovato");
                    //TODO inserire una label con messaggio di errore "Utente non trovato"
                    //TODO Far diventare le texfield rosse per notificare l'errore
                } else {
                    String email2 = res.get(0);
                    String password2 = res.get(1);
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                }
            }*/
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}


