package com.malfaang.e_culture_tool_a.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.utility.NetworkConnectivity;

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
        emailEdit = findViewById(R.id.idEdtUserName);
        passwordEdit = findViewById(R.id.idEdtPassword);
        String email = Objects.requireNonNull(emailEdit.getText().toString());
        String password = Objects.requireNonNull(passwordEdit.getText().toString());
        Button loginBtn = findViewById(R.id.idBtnLogin);
        loginBtn.setOnClickListener(view -> signInEmailPassword(email, password));
        Button registerBtn = findViewById(R.id.Register);
        registerBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LoginLocaleActivity.this, RegistrazioneLocaleActivity.class);
            startActivity(intent);
        });
    }

    public void signInEmailPassword(String email, String password) {
        if (checker.checkEmail(emailEdit, LoginLocaleActivity.this)
                && checker.checkPassword(passwordEdit, LoginLocaleActivity.this)) {
            if (!NetworkConnectivity.check(getApplicationContext())) {
                Toast.makeText(LoginLocaleActivity.this, R.string.no_connection, Toast.LENGTH_SHORT).show();
                return;
            }
            //TODO passare dati a DatabaseHelper
        }
    }
}

