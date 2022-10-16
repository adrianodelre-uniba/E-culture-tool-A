package com.malfaang.e_culture_tool_a.model.persone;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.Objects;

public class Visitatore extends User {
    private Genere genere;
    private LocalDate tempoDisponibile;

    public Visitatore(String nome, String cognome, LocalDate dateNascita,
                      String email, String password) {
        super(nome, cognome, dateNascita, email, password);
    }

    public Genere getGenere() {
        return genere;
    }

    public void setGenere(Genere genere) {
        this.genere = genere;
    }

    public LocalDate getTempoDisponibile() {
        return tempoDisponibile;
    }

    public void setTempoDisponibile(LocalDate tempoDisponibile) {
        this.tempoDisponibile = tempoDisponibile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        if (!super.equals(o)){
            return false;
        }
        Visitatore that = (Visitatore) o;
        return genere == that.genere && Objects.equals(tempoDisponibile, that.tempoDisponibile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), genere, tempoDisponibile);
    }

    @NonNull
    @Override
    public String toString() {
        return "Visitatore{" +
                "genere=" + genere +
                ", tempoDisponibile=" + tempoDisponibile +
                '}';
    }
}

