package com.malfaang.e_culture_tool_a.databaseFirebase;

//Classe per l'ArrayList degli utenti
public class Utente {
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String telefono;
    private String tipologia;

    public Utente(String nome, String cognome, String email, String password,
                  String telefono, String tipologia) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.tipologia = tipologia;
    }

    public Utente() {}

    public String getNome() {
        return this.nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public String getTipologia() {
        return this.tipologia;
    }
}
