package com.malfaang.e_culture_tool_a.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.malfaang.e_culture_tool_a.R;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private TextView textView;
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first);

        imageView = findViewById(R.id.logo_id);
        textView = findViewById(R.id.nome_app);
        loginButton = findViewById(R.id.button_login);
        registerButton = findViewById(R.id.button_register);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);


        /*binding = ActivityFirstBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());*/

        /*
        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_first);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_login:
                signIn();
                break;
            case R.id.button_register:
                startActivity(new Intent(getApplicationContext(), RegistrazioneActivity.class));
        }
    }

    private void signIn() {
    }

    /*@Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_first);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
}