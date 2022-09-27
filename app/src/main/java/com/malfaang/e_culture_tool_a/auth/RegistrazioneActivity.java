package com.malfaang.e_culture_tool_a.auth;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.listeners.FailureListener;
import com.malfaang.e_culture_tool_a.listeners.SuccessListener;
import com.malfaang.e_culture_tool_a.model.persone.User;

import java.time.LocalDate;

public class RegistrazioneActivity extends AppCompatActivity {
    private static final String TAG = "EmailPassword";
    private static final String URI = "https://e-culture-tool-a-6f940-default-rtdb.europe-west1.firebasedatabase.app/";
    private static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(URI);
    private FirebaseAuth mAuth;
    private User user;
    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;
    private ControlloCredenziali checker;
    private static final int REQ_ONE_TAP = 2;
    private boolean showOneTapUI = true;
    private static DatabaseReference reference;
    private static final String TABLE_NAME = "Users";

    public RegistrazioneActivity(){ /* TODO document why this constructor is empty */ }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        mAuth = FirebaseAuth.getInstance();
        checker = new ControlloCredenziali();

        //TODO Utente tappa su registrazione --> registraUtenteSuFirebaseFinale
        //TODO Utente tappa su simbolo Google --> creaAccountGoogle
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    if (idToken !=  null) {
                        Log.d(TAG, "Got ID token.");
                        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                        mAuth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this, task -> {
                                    if (task.isSuccessful()) {
                                        //TODO update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithCredential:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                                        updateUI(null);
                                    }
                                });
                    }
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case CommonStatusCodes.CANCELED:
                            Log.d(TAG, "One-tap dialog was closed.");
                            showOneTapUI = false;
                            break;
                        case CommonStatusCodes.NETWORK_ERROR:
                            Log.d(TAG, "One-tap encountered a network error.");
                            break;
                        default:
                            Log.d(TAG, "Couldn't get credential from result."
                                    + e.getLocalizedMessage());
                            break;
                    }
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void reload() {
        // TODO document why this method is empty
    }

    private void registraUtenteSuFirebaseFinale(String nome, String cognome,
                                                LocalDate dataDiNascita, String email, String password){
        registraUtenteSuFirebaseParziale(nome, cognome, dataDiNascita, email, password,
                () ->{
                    Toast.makeText(this, R.string.success_registration, Toast.LENGTH_LONG).show();
                    mAuth.signOut();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                },
                (String errorMsg) ->{
                    if(errorMsg.contains("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException")) {
                        Toast.makeText(this, R.string.credentials_not_accepted, Toast.LENGTH_LONG).show();
                    } else if(errorMsg.contains("com.google.firebase.auth.FirebaseAuthUserCollisionException")) {
                        Toast.makeText(this, R.string.email_already_use, Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(this, R.string.failed_registration, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void registraUtenteSuFirebaseParziale(String nome, String cognome,
                                                  LocalDate dataDiNascita, String email, String password,
                                                  SuccessListener success, FailureListener failure) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        assert firebaseUser != null;
                        String userID = firebaseUser.getUid();
                        DatabaseReference actualReference = reference.child(userID);
                        actualReference.setValue(new User(nome, cognome, dataDiNascita, email, password))
                                .addOnCompleteListener(taskUserValueSet -> {
                            if (taskUserValueSet.isSuccessful()) {
                                success.onSuccess();
                            } else {
                                mAuth.getCurrentUser().delete();
                                failure.onFail(taskUserValueSet.getException() != null
                                        ? taskUserValueSet.getException().toString() : "");
                            }
                        });
                    } else {
                        failure.onFail(task.getException() != null ? task.getException().toString() : "");
                    }
                });
    }

    private void creaAccountGoogle() {
        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        oneTapClient.beginSignIn(signUpRequest)
                .addOnSuccessListener(this, result -> {
                    try {
                        startIntentSenderForResult(
                                result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                null, 0, 0, 0);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                    }
                })
                .addOnFailureListener(this, e -> {
                    Log.d(TAG, e.getLocalizedMessage());
                });
    }

    private void sendEmailVerification() {
        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    // Email sent
                });
    }


    private void updateUI(FirebaseUser user) {
        // TODO document why this method is empty
    }

    // Gestisce il pulsante "back" nella action bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
