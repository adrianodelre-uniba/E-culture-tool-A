package com.malfaang.e_culture_tool_a.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.database.DatabaseHelper;

public class InsertOperaActivity extends AppCompatActivity {
    private EditText titolo;
    private EditText descrizione;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_opera);
        titolo = findViewById(R.id.titolo);
        descrizione = findViewById(R.id.descrizione);
        Button salva = findViewById(R.id.salva);
        salva.setOnClickListener(view -> salvaOpera());

    }

    private void salvaOpera() {
        DatabaseHelper db = new DatabaseHelper(this);
        db.insertOpera(titolo.getText().toString(), descrizione.getText().toString());
    }

    private void salvaOpera(int id) {
        DatabaseHelper db = new DatabaseHelper(this);
        db.updateOpera(titolo.getText().toString(), descrizione.getText().toString(), id);
    }

}