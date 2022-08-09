package com.malfaang.e_culture_tool_a.model.persone;

import androidx.annotation.NonNull;

import com.malfaang.e_culture_tool_a.model.spazi.Luogo;

import java.time.LocalDate;
import java.util.Objects;

public class Curatore extends User{
    private Luogo luogoGestito;

    public Curatore(String nome, String cognome, LocalDate dateBirth,
                    String cap, String cellulare, String email, String password) {
        super(nome, cognome, dateBirth, cap, cellulare, email, password);
    }

    public Luogo getLuogoGestito() {
        return luogoGestito;
    }

    public void setLuogoGestito(Luogo luogoGestito) {
        this.luogoGestito = luogoGestito;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        if (!super.equals(o)){
            return false;
        }
        Curatore curatore = (Curatore) o;
        return Objects.equals(luogoGestito, curatore.luogoGestito);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), luogoGestito);
    }

    @NonNull
    @Override
    public String toString() {
        return "Curatore{" +
                "luogoGestito=" + luogoGestito +
                '}';
    }
}
