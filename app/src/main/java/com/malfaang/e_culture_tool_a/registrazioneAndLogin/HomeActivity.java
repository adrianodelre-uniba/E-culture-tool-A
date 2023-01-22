package com.malfaang.e_culture_tool_a.registrazioneAndLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.gestioneOpere.InsertOperaActivity;

public class HomeActivity extends AppCompatActivity {

    public HomeActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SQLiteDatabase database = DatabaseHelper.getDb();
        setContentView(R.layout.activity_home);

        Button button = findViewById(R.id.insertOpera);
        button.setOnClickListener(view -> insertOpera());
    }

    private void insertOpera() {
        Intent intent = new Intent(this, InsertOperaActivity.class);
        startActivity(intent);
    }
}
