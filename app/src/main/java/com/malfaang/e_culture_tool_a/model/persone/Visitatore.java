package com.malfaang.e_culture_tool_a.model.persone;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.Objects;

@SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
public class Visitatore extends User {
    private Genere genere = null;
    private LocalDate tempoDisponibile = null;

    public Visitatore(String nome, String cognome, LocalDate dateNascita,
                      String email, String password) {
        super(nome, cognome, dateNascita, email, password);
    }

    public Genere getGenere() {
        return genere;
    }

    public void setGenere(Genere genere2) {
        this.genere = genere2;
    }

    public LocalDate getTempoDisponibile() {
        return tempoDisponibile;
    }

    public void setTempoDisponibile(LocalDate tempoDisponibile2) {
        this.tempoDisponibile = tempoDisponibile2;
    }

    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        if (!super.equals(o)){
            return false;
        }
        Visitatore visitatore = (Visitatore) o;
        //noinspection AccessingNonPublicFieldOfAnotherObject
        return genere == visitatore.genere && Objects.equals(tempoDisponibile, visitatore.tempoDisponibile);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), genere, tempoDisponibile);
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

