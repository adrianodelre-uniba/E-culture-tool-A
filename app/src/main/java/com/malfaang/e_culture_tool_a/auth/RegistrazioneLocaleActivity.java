package com.malfaang.e_culture_tool_a.auth;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.textfield.TextInputEditText;
import com.malfaang.e_culture_tool_a.R;
import com.malfaang.e_culture_tool_a.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegistrazioneLocaleActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    private String nome;
    private String cognome;
    private String dataNascita;
    private String email;
    private String password;

    final Calendar myCalendar = Calendar.getInstance();
    private TextInputEditText eNome, eCognome, eCompleanno, eEmail, ePassword;
    private Button bIdBtnRegistrazione;

    private static final String REGEX_NOME = "[a-zA-Z]+";
    private static final String REGEX_COGNOME = "[A-Z]+([ '-][a-zA-Z]+)*";
    private static final String REGEX_EMAIL = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
    private static final String REGEX_DATA_NASCITA = "^\\d{2}/\\d{2}/\\d{4}$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_locale);



        eNome = findViewById(R.id.idNome);
        eCognome = findViewById(R.id.idCognome);
        eCompleanno = findViewById(R.id.idCompleanno);
        eEmail = findViewById(R.id.idEmail);
        ePassword = findViewById(R.id.idPassword);
        bIdBtnRegistrazione = findViewById(R.id.idBtnRegistrazione);

        //da eliminare
        eNome.setText("Donato");
        eCognome.setText("D'Inzeo");
        eCompleanno.setText("07/26/1994");
        eEmail.setText("mail@test.com");
        ePassword.setText("12345678");

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        eCompleanno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RegistrazioneLocaleActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        bIdBtnRegistrazione.setOnClickListener(view -> {

            if(checkInputString()){
                DatabaseHelper db = new DatabaseHelper(this);
                db.insertUtente(nome, cognome, dataNascita, email, password);
            }
            //TODO questo metodo ti deve permettere di caricare la pagina successiva ovvero la homePage dopo il controllo sull'input
            // TODO ndò cazz sta l'homepage?
        });


        /*
        binding = ActivityRegistrazioneLocaleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_registrazione_locale);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_registrazione_locale);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
        return false;
    }
    */
    }

    // Convalida dei campi di registrazione
    private boolean checkInputString() {

        boolean check = true;

        nome = eNome.getText().toString();
        cognome = eCognome.getText().toString();
        dataNascita = eCompleanno.getText().toString();
        email = eEmail.getText().toString();
        password = ePassword.getText().toString();

        System.out.println(dataNascita.getClass());     //RETURN STRING - Mi è servito capire in quale formato fosse la data che ritorna il calendario

        // TODO I messaggi di errore che vengono stampati su console, devono essere sostituiti con dei messaggi da inserire sotto le rispettive caselle di input
        if(nome.isEmpty()){
            System.out.println("Il campo nome è obbligatorio!");
            check = false;
        } else if(!nome.matches(REGEX_NOME)){
            System.out.println("Nel campo nome non si possono usare caratteri speciali.");
            check = false;
        }
        if(cognome.isEmpty()){
            System.out.println("Il campo cognome è obbligatorio!");
            check = false;
        } else if(!cognome.matches(REGEX_COGNOME)){
            System.out.println("Nel campo cognome non si possono usare caratteri speciali, solo ' è consetito.");
            check = false;
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
        }

        //Questo System è da togliere, lo utilizzo solo per vedere cosa acquisisce l'app da tastiera
        System.out.println(nome + " " + cognome + " " + dataNascita + " " + email + " " + password);


        return check;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.ITALY);
        eCompleanno.setText(dateFormat.format(myCalendar.getTime()));
    }

    public boolean check(String nome, String cognome, LocalDate dataNascita, String username, String password) {

        return false;
    }
}
