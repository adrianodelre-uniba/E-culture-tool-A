package com.malfaang.e_culture_tool_a.model.persone;

import java.util.Date;

public class User {
    private static final int INIZIALIZZAZIONE = 0;
    private int id;
    private String nome;
    private String cognome;
    private Date dateBirth;
    private String cap;
    private String cellulare;
    private String email;
    private String password;
    private int counter = INIZIALIZZAZIONE;

    public User(String nome, String cognome, Date dateBirth, String cap,
                String cellulare, String email, String password){
        this.id = counter;
        this.nome = nome;
        this.cognome = cognome;
        this.dateBirth = dateBirth;
        this.cap = cap;
        this.cellulare = cellulare;
        this.email = email;
        this.password = password;
        counter++;
    }
    public User(String nome, String cognome, Date dateBirth, String cap, String email, String password){
        this.id = counter;
        this.nome = nome;
        this.cognome = cognome;
        this.dateBirth = dateBirth;
        this.cap = cap;
        this.email = email;
        this.password = password;
        counter++;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    public Date getDateBirth() {
        return dateBirth;
    }
    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }
    public String getCap() {
        return cap;
    }
    public void setCap(String cap) {
        this.cap = cap;
    }
    public String getCellulare() {
        return cellulare;
    }
    public void setCellulare(String cellulare) {
        this.cellulare = cellulare;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
