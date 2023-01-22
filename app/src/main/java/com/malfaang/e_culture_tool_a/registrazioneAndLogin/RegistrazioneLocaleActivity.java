package com.malfaang.e_culture_tool_a.registrazioneAndLogin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class RegistrazioneLocaleActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration = null;

    private String nome = null;
    private String cognome = null;
    private String dataNascita = null;
    private String email = null;
    private String password = null;

    final Calendar myCalendar = Calendar.getInstance();
    private EditText eNome , eCognome, eCompleanno, eEmail, ePassword;

    private static final String REGEX_NOME = "[a-zA-Z]+";
    private static final String REGEX_COGNOME = "[A-Z]+[:space:]+([ '-][a-zA-Z]+)*";
    private static final String REGEX_EMAIL = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
    private static final String REGEX_DATA_NASCITA = "^\\d{2}/\\d{2}/\\d{4}$";

    public RegistrazioneLocaleActivity() {
    }


    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_locale);

        eNome = findViewById(R.id.idNome);
        eCognome = findViewById(R.id.idCognome);
        eCompleanno = findViewById(R.id.idCompleanno);
        eEmail = findViewById(R.id.idEmail);
        ePassword = findViewById(R.id.idPassword);
        Button bIdBtnRegistrazione = findViewById(R.id.idBtnRegistrazione);

        //da eliminare
        eNome.setText("Donato");
        eCognome.setText("D'Inzeo");
        eCompleanno.setText("07/26/1994");
        eEmail.setText("mail@test.com");
        ePassword.setText("12345678");

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        };
        email = eEmail.getText().toString();
        eCompleanno.setOnClickListener(view -> new DatePickerDialog(this, date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

      /*  bIdBtnRegistrazione.setOnClickListener(view -> {
            if(checkInputString()){
                DatabaseHelper db = new DatabaseHelper(this);
                db.insertUtente(nome, cognome, dataNascita, email, password);
            }

            //TODO questo metodo ti deve permettere di caricare la pagina successiva ovvero la homePage dopo il controllo sull'input
            // TODO ndò cazz sta l'homepage?
        });*/
        bIdBtnRegistrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInputString()){
                    DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                    Cursor cursor = db.controllo(email);
                    String ciao="";
                    if (cursor.moveToFirst()) {

                        do {
                            Toast.makeText(getApplicationContext(), "entratio", Toast.LENGTH_SHORT).show();
                            ciao = cursor.getString(0);


                            cursor.moveToNext();


                        } while (!cursor.isAfterLast());
                    }
                    if(ciao.isEmpty()){
                        db.insertUtente(nome, cognome, dataNascita, email, password);
                        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(intent);
                }
                else{
                        Toast.makeText(getApplicationContext(), "cambia email", Toast.LENGTH_SHORT).show();
                        Log.d("risposta","sei un coglione");
                }
                }
            }
        });

    }

    // Convalida dei campi di registrazione
    private boolean checkInputString() {

        boolean check = true;

        nome = Objects.requireNonNull(eNome.getText()).toString();
        cognome = Objects.requireNonNull(eCognome.getText()).toString();
        dataNascita = Objects.requireNonNull(eCompleanno.getText()).toString();
        email = Objects.requireNonNull(eEmail.getText()).toString();
        password = Objects.requireNonNull(ePassword.getText()).toString();

        System.out.println(dataNascita.getClass());     //RETURN STRING - Mi è servito capire in quale formato fosse la data che ritorna il calendario

        // TODO I messaggi di errore che vengono stampati su console, devono essere sostituiti con dei messaggi da inserire sotto le rispettive caselle di input
        if(nome.isEmpty()){
            System.out.println("Il campo nome è obbligatorio!");
            check = false;
        } else if(!nome.matches(REGEX_NOME)){
            System.out.println("Nel campo nome non si possono usare caratteri speciali.");
            check = false;
        }
        if(cognome.isEmpty()) {
            System.out.println("Il campo cognome è obbligatorio!");
            check = false;
        } else if(!cognome.matches(REGEX_COGNOME)){
            System.out.println("Nel campo cognome non si possono usare caratteri speciali, solo ' è consetito.");
            check = false; // da controllare
        }
        if(dataNascita.isEmpty()){
            System.out.println("Il campo Data di nascita è obbligatorio!");
            check = false;
        }
        if (email.isEmpty()) {
            System.out.println("Il campo e-mail è obbligatorio!");
            check = false;
        } else if(email.matches(REGEX_EMAIL)){//(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            System.out.println("Indirizzo e-mail non valido.");
            check = false;
        }
        if (password.isEmpty()) {
            System.out.println("Il campo password è obbligatorio!");
            check = false;
        } else if(password.matches(REGEX_EMAIL)){
            System.out.println("Password non valida.");
            check = false;
        }else{
            return true;
        }

        //Questo System è da togliere, lo utilizzo solo per vedere cosa acquisisce l'app da tastiera
        System.out.println(nome + " " + cognome + " " + dataNascita + " " + email + " " + password);


        return check;
    }

    public final void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.ITALY);
        eCompleanno.setText(dateFormat.format(myCalendar.getTime()));
    }

    public final boolean check(String nome2, String cognome2, LocalDate dataNascita2, String username2, String password2) {

        return false;
    }
}
