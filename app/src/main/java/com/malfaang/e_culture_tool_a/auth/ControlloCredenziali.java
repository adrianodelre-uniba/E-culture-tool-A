package com.malfaang.e_culture_tool_a.auth;

import android.app.Activity;
import android.util.Patterns;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.malfaang.e_culture_tool_a.R;

import java.util.Objects;

public class ControlloCredenziali {
    public static final int MAX_PASSWORD = 30;
    public static final int MIN_PASSWORD = 8;
    public static final int SPACE = 32;
    public static final int MAX_EMAIL = 50;

    public ControlloCredenziali() {
    }


    /**
     * Funzione che verifica la correttezza della password inserita dell'utente
     * sia in termini di lunghezza, eventuale presenza di spazi e
     * soprattutto controlla che le due password inserite combacino
     * @param firstPassword, TextInputEditText contenente la prima password inserita dall'utente
     * @param secondPassword, TextInputEditText contenente la seconda password inserita dall'utente
     * @param activity, referimento all'activity genitore
     * @return true se la password inserita è corretta, altrimenti ritorna false
     */
    public final boolean checkPassword(@NonNull TextInputEditText firstPassword,
                                       @NonNull TextInputEditText secondPassword, Activity activity) {
        String txtPassword = Objects.requireNonNull(firstPassword.getText()).toString();
        String txtSecondPassword = Objects.requireNonNull(secondPassword.getText()).toString();
        if (checkGenericStringGeneral("password", firstPassword, MAX_PASSWORD, txtPassword, activity)) {
            return false;
        }
        int len = txtPassword.length();
        if (len < MIN_PASSWORD) {
            firstPassword.setError(activity.getString(R.string.psw_too_short, Integer.valueOf(MIN_PASSWORD)));
            firstPassword.requestFocus();
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (txtPassword.charAt(i) == SPACE) {
                firstPassword.setError(activity.getString(R.string.msg_psw_without_space));
                firstPassword.requestFocus();
                return false;
            }
        }
        // others checks for first password
        if (!txtPassword.equals(txtSecondPassword)) {
            firstPassword.requestFocus();
            secondPassword.requestFocus();
            Toast toast = Toast.makeText(activity, R.string.msg_different_psw, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 200); // y340 bottom
            toast.show();
            return false;
        }
        return true;
    }

    /**
     * Funzione che verifica la correttezza della password inserita dell'utente
     * sia in termini di lunghezza, eventuale presenza di spazi e
     * soprattutto controlla che le due password inserite combacino
     * @param firstPassword, prima password inserita dall'utente
     * @param activity, referimento all'activity genitore
     * @return true se la password inserita è corretta, altrimenti ritorna false
     */
    public final boolean checkPassword(@NonNull TextInputEditText firstPassword, Activity activity) {
        String txtPassword = Objects.requireNonNull(firstPassword.getText()).toString();
        if (checkGenericStringGeneral("password", firstPassword, MAX_PASSWORD, txtPassword, activity)) {
            return false;
        }
        int len = txtPassword.length();
        if (len < MIN_PASSWORD) {
            firstPassword.setError(activity.getString(R.string.psw_too_short, Integer.valueOf(MIN_PASSWORD)));
            firstPassword.requestFocus();
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (txtPassword.charAt(i) == SPACE) {
                firstPassword.setError(activity.getString(R.string.msg_psw_without_space));
                firstPassword.requestFocus();
                return false;
            }
        }
        return true;
    }

    /**
     * Funzione che verifica la correttezza di una stringa generica inserita dell'utente
     * sia in termini di lunghezza ma anche se è eventualmente vuota
     * @param tag, tag della stringa
     * @param stringa, TextInputEditText in cui è stata inserita la stringa dell'utente
     * @param MAX, lunghezza massima che la striga può avere
     * @param txtString, stringa inserita dall'utente
     * @param activity, referimento all'activity genitore
     * @return true se la stringa generica inserita è corretta, altrimenti ritorna false
     */
    public final boolean checkGenericStringGeneral(String tag, TextInputEditText stringa, int MAX,
                                                   String txtString, Activity activity) {
        if (txtString == null) {
            txtString = Objects.requireNonNull(stringa.getText()).toString().trim();
        }
        if (txtString.isEmpty()) {
            stringa.setError(activity.getString(R.string.field_empty, tag));
            stringa.requestFocus();
            return true;
        }
        int len = txtString.length();
        if (len > MAX) {
            stringa.setError(activity.getString(R.string.field_too_long, tag, Integer.valueOf(MAX)));
            stringa.requestFocus();
            return true;
        }
        return false;
    }

    /**
     * Funzione che verifica la correttezza di una email
     * sia in termini di lunghezza ma soprattutto di correttezza formale
     * @param email, TextInputEditText che contiene l'email inserita dall'utente
     * @param activity, referimento all'activity genitore
     * @return true se l'email inserita è corretta, altrimenti ritorna false
     */
    public final boolean checkEmail(@NonNull TextInputEditText email, Activity activity) {
        String txtEmail = Objects.requireNonNull(email.getText()).toString().trim();
        if (checkGenericStringGeneral("email", email, MAX_EMAIL, txtEmail, activity)) {
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
            email.setError(activity.getString(R.string.valid_email));
            email.requestFocus();
            return false;
        }
        return true;
    }
}
