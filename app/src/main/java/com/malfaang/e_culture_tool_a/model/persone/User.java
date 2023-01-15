package com.malfaang.e_culture_tool_a.model.persone;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.Objects;

public abstract class User {
    private int id;
    private String nome = null;
    private String cognome = null;
    private String email = null;
    private String password = null;

    public User(String nome2, String cognome2, LocalDate dataNascita2, String email2, String password2){
        this.nome = nome2;
        this.cognome = cognome2;
        this.email = email2;
        this.password = password2;
    }

    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome2) {
        this.nome = nome2;
    }
    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome2) {
        this.cognome = cognome2;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email2) {
        this.email = email2;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password2) {
        this.password = password2;
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
        return id == user.getId() && nome.equals(user.getNome()) && cognome.equals(user.getCognome())
                && email.equals(user.getEmail()) && password.equals(user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(Integer.valueOf(id), nome, cognome, email, password);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void setId(int id2) {
        this.id = id2;
    }
}
