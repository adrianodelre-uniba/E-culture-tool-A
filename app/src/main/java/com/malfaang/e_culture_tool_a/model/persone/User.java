package com.malfaang.e_culture_tool_a.model.persone;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.Objects;

public class User {
    private static final int INIZIALIZZAZIONE = 0;
    private int id;
    private String nome;
    private String cognome;
    private LocalDate dateBirth;
    private String cap;
    private String cellulare;
    private String email;
    private String password;
    private int counter = INIZIALIZZAZIONE;

    public User(String nome, String cognome, LocalDate dateBirth, String cap,
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
    public User(String nome, String cognome, LocalDate dateBirth, String cap, String email, String password){
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
    public LocalDate getDateBirth() {
        return dateBirth;
    }
    public void setDateBirth(LocalDate dateBirth) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        User user = (User) o;
        return id == user.id && nome.equals(user.nome) && cognome.equals(user.cognome)
                && Objects.equals(dateBirth, user.dateBirth) && Objects.equals(cellulare, user.cellulare)
                && email.equals(user.email) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, cognome, dateBirth, cellulare, email, password);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", dateBirth=" + dateBirth +
                ", cap='" + cap + '\'' +
                ", cellulare='" + cellulare + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", counter=" + counter +
                '}';
    }
}
