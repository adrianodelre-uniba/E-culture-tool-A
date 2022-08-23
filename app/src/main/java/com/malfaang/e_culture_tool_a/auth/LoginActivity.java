package com.malfaang.e_culture_tool_a.auth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import com.facebook.AccessToken;

import com.malfaang.e_culture_tool_a.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;

    public LoginActivity(){ /* TODO document why this constructor is empty */ }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public boolean checkCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    public void reload() {
        // TODO document why this method is empty
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

    public void updateProfile(String nome, String url) {
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

    public void updateEmail(String email) {
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

    public void updatePassword(String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        user.updatePassword(password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User password updated.");
                    }
                });
    }

    public void deleteUser() {
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // [START auth_link_and_merge]
        FirebaseUser prevUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth.signInWithCredential(credential)
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

    public AuthCredential getGoogleCredentials() {
        String googleIdToken = "";
        return GoogleAuthProvider.getCredential(googleIdToken, null);
    }

    public AuthCredential getFbCredentials() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        assert token != null;
        return FacebookAuthProvider.getCredential(token.getToken());
    }


    public void getEmailCredentials(String password) {
        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        assert email != null;
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void updateUI(@Nullable FirebaseUser user) {
        // TODO document why this method is empty
    }
}
