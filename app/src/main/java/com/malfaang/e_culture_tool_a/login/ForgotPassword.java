package com.malfaang.e_culture_tool_a.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.malfaang.e_culture_tool_a.MainActivity;
import com.malfaang.e_culture_tool_a.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    Button vai;
    EditText ee;
    private FirebaseAuth mAuth;
    //Creazione del layout e abbinamento con i componenti
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.ema), Toast.LENGTH_LONG).show();
        setContentView(R.layout.passdimenticata);
        ee=(EditText)findViewById(R.id.email1);
        vai=(Button)findViewById(R.id.dim);
        mAuth = FirebaseAuth.getInstance();
        vai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ricordami();
            }
        });

    }
    //Funzione di firebase per recuperare la password
    private void ricordami() {
        String em = ee.getText().toString();
        if (TextUtils.isEmpty(em)) {
            Toast.makeText(getApplicationContext(), ""+getResources().getString(R.string.ema_non), Toast.LENGTH_LONG).show();
        } else {

            mAuth.setLanguageCode("es");
            mAuth.sendPasswordResetEmail(em).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {


                    if(task.isSuccessful()){

                        Toast.makeText(getApplicationContext(), ""+getResources().getString(R.string.ema), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);

                    }
                    else{
                        Toast.makeText(getApplicationContext(),""+ getResources().getString(R.string.ema_non_invi), Toast.LENGTH_LONG).show();
                    }
                }
            });


        }

    }
}