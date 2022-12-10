package com.malfaang.e_culture_tool_a.auth;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.utility.NetworkConnectivity;

import java.util.Objects;

public class LoginFireBaseActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static FirebaseAuth mAuth = null;
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private SignInClient oneTapClient = null;
    private BeginSignInRequest signUpRequest = null;
    private TextInputEditText emailEdit = null;
    private TextInputEditText passwordEdit = null;
    private ControlloCredenziali checker = null;

    public LoginFireBaseActivity() {
        /* TODO document why this constructor is empty */
    }

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_login_locale);

        checker = new ControlloCredenziali();
        emailEdit = findViewById(R.id.idUserName);
        passwordEdit = findViewById(R.id.idPassword);
        String email = Objects.requireNonNull(Objects.requireNonNull(emailEdit.getText()).toString());
        String password = Objects.requireNonNull(Objects.requireNonNull(passwordEdit.getText()).toString());
        Button loginBtn = findViewById(R.id.idBtnLogin);
        ImageButton gLoginBtn = findViewById(R.id.idBtnGoogle);
        loginBtn.setOnClickListener(view -> signInEmailPassword(email, password));
        gLoginBtn.setOnClickListener(view -> signInGoogle());
    }


    @Override
    protected final void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ONE_TAP) {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();
                String username = credential.getId();
                String password = credential.getPassword();
                if (idToken != null) {
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

                    Log.d(TAG, "Got ID token.");
                } else if (password != null) {
                    //TODO Got a saved username and password. Use them to authenticate
                    // with your backend.
                    Log.d(TAG, "Got password.");
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

    public static FirebaseUser checkCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public final void signInEmailPassword(String email, String password) {
        if(checker.checkEmail(emailEdit, this) && checker.checkPassword(passwordEdit, this)) {
            if (!NetworkConnectivity.check(getApplicationContext())) {
                Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            //TODO update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(this, R.string.logged_in, Toast.LENGTH_SHORT).show();
                            addNewSessionUid(getApplicationContext(), FirebaseAuth.getInstance().getUid());
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            //*startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finishAffinity();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    });
        }
    }

    public final void signInGoogle(){
        oneTapClient = Identity.getSignInClient(this);
        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .setAutoSelectEnabled(true)
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
                    //TODO No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    Log.d(TAG, e.getLocalizedMessage());
                });

    }

    public void reload() {
        // TODO document why this method is empty
    }

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
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public String getUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            return name + " " + email + " " + photoUrl + " " + "email verificata: " + emailVerified;
        }
        return "";
    }

    /**
     * Aggiunge al file sharedPreferences l'uid cloud dell'utente loggato.
     * @param context contesto app android.
     * @param uid user id da inserire.
     */
    public static void addNewSessionUid(@NonNull Context context, String uid) {
        SharedPreferences.Editor editor;
        editor = context.getSharedPreferences("MyPreferences", MODE_PRIVATE).edit();
        editor.putString(context.getString(R.string.uid_preferences), uid);
        editor.apply();
    }

    public final void updateProfile(String nome, String url) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nome)
                .setPhotoUri(Uri.parse(url))
                .build();

        assert user != null;
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User profile updated.");
                    }
                });
    }

    public final void updateEmail(String email) {
        // [START update_email]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        user.updateEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User email address updated.");
                    }
                });
    }

    public final void updatePassword(String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        user.updatePassword(password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User password updated.");
                    }
                });
    }

    public final void deleteUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        user.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User account deleted.");
                    }
                });
    }

    public void reauthenticate(String email, String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);

        // Prompt the user to re-provide their sign-in credentials
        assert user != null;
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> Log.d(TAG, "User re-authenticated."));
    }

    public void linkAndMerge(AuthCredential credential) {
        FirebaseAuth mAuth2 = FirebaseAuth.getInstance();

        // [START auth_link_and_merge]
        FirebaseUser prevUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth2.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    FirebaseUser currentUser = task.getResult().getUser();
                    // Merge prevUser and currentUser accounts and data
                    // ...
                });
    }

    public void unlink(String providerId) {
        mAuth = FirebaseAuth.getInstance();

        Objects.requireNonNull(mAuth.getCurrentUser()).unlink(providerId)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Auth provider unlinked from account
                        // ...
                    }
                });
    }

    public final AuthCredential getGoogleCredentials() {
        String googleIdToken = "";
        return GoogleAuthProvider.getCredential(googleIdToken, null);
    }

    public final void getEmailCredentials(String password) {
        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        assert email != null;
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

    }

    public final void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void updateUI(@Nullable FirebaseUser user) {
        // TODO document why this method is empty
    }
}
