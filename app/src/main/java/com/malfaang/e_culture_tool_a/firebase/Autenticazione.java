package com.malfaang.e_culture_tool_a.firebase;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//Funzione per registrare l'utente su  Authentication firebase
public class Autenticazione {
    public void  RegistrazioneFire(String email, String pass, FirebaseAuth mAuth) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                        }
                        else{
                        }
                    }
                });
    }
}
