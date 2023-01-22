package com.malfaang.e_culture_tool_a.fireBase;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.registrazioneAndLogin.ControlloCredenziali;
import com.malfaang.e_culture_tool_a.listeners.FailureListener;
import com.malfaang.e_culture_tool_a.listeners.SuccessListener;
import com.malfaang.e_culture_tool_a.model.persone.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/*
 *
 *  @author adrianodelre
 */
public class RegistrazioneFireBaseActivity extends AppCompatActivity {
    private static final String TAG = "EmailPassword";
    private static final String URI = "https://e-culture-tool-a-6f940-default-rtdb.europe-west1.firebasedatabase.app/";
    private static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(URI);
    private FirebaseAuth mAuth = null;
    private User user = null;
    private SignInClient oneTapClient = null;
    private static final int REQ_ONE_TAP = 2;
    private static DatabaseReference reference = null;
    private static final String TABLE_NAME = "Users";

    public RegistrazioneFireBaseActivity(){ /* TODO document why this constructor is empty */ }

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        onCreate(savedInstanceState);
        // setContentView(R.layout.activity_registrazione);

        mAuth = FirebaseAuth.getInstance();
        ControlloCredenziali checker = new ControlloCredenziali();

        TextInputEditText nomeEdit = findViewById(R.id.idNome);
        TextInputEditText cognomeEdit = findViewById(R.id.idCognome);
        TextInputEditText compleannoEdit = findViewById(R.id.idCompleanno);
        TextInputEditText emailEdit = findViewById(R.id.idEmail);
        TextInputEditText passwordEdit = findViewById(R.id.idPassword);
        String name = Objects.requireNonNull(Objects.requireNonNull(nomeEdit.getText()).toString());
        String surname = Objects.requireNonNull(Objects.requireNonNull(cognomeEdit.getText()).toString());
        String birthday = Objects.requireNonNull(Objects.requireNonNull(compleannoEdit.getText()).toString());
        String email = Objects.requireNonNull(Objects.requireNonNull(emailEdit.getText()).toString());
        String password = Objects.requireNonNull(Objects.requireNonNull(passwordEdit.getText()).toString());
        Button registrationBtn = findViewById(R.id.idBtnRegistrazione);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate birthdayLD = LocalDate.parse(birthday, formatter);

        registrationBtn.setOnClickListener(view -> registraUtenteSuFirebaseFinale(name, surname,
                 birthdayLD, email, password));

        //TODO Utente tappa su simbolo Google --> creaAccountGoogle
    }

    @Override
    protected final void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ONE_TAP) {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();
                if (idToken != null) {
                    Log.d(TAG, "Got ID token.");
                    AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                    mAuth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    //TODO update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithCredential:success");
                                    FirebaseUser user2 = mAuth.getCurrentUser();
                                    updateUI(user2);
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
                        boolean showOneTapUI = false;
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
        }
    }

    @Override
    public final void onStart() {
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
                    startActivity(new Intent(this, LoginFireBaseActivity.class));
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
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
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
        BeginSignInRequest signUpRequest = BeginSignInRequest.builder()
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
                .addOnFailureListener(this, e -> Log.d(TAG, e.getLocalizedMessage()));
    }

    private void sendEmailVerification() {
        // Send verification email
        FirebaseUser user2 = mAuth.getCurrentUser();
        assert user2 != null;
        user2.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    // Email sent
                });
    }


    private void updateUI(FirebaseUser user2) {
        // TODO document why this method is empty
    }

    // Gestisce il pulsante "back" nella action bar
    @Override
    public final boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public final void onBackPressed() {
        super.onBackPressed();
    }

}
