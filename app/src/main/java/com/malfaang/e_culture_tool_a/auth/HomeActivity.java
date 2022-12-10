package com.malfaang.e_culture_tool_a.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.malfaang.e_culture_tool_a.R;

public class HomeActivity extends AppCompatActivity {

    public HomeActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SQLiteDatabase database = DatabaseHelper.getDb();
        setContentView(R.layout.activity_home);
    }
}
