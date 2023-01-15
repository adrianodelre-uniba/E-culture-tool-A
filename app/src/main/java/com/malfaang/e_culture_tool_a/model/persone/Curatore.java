package com.malfaang.e_culture_tool_a.model.persone;

import androidx.annotation.NonNull;

import com.malfaang.e_culture_tool_a.model.spazi.Luogo;

import java.time.LocalDate;
import java.util.Objects;

public class Curatore extends User{
    private Luogo luogoGestito = null; //posso gestire più luoghi

    public Curatore(String nome, String cognome, LocalDate dateNascita,
                    String email, String password) {
        super(nome, cognome, dateNascita, email, password);
    }

    public Luogo getLuogoGestito() {
        return this.luogoGestito;
    }

    public void setLuogoGestito(Luogo luogoGestito2) {
        this.luogoGestito = luogoGestito2;
    }

    @Override
    public final boolean equals(Object o) {
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
        return Objects.equals(luogoGestito, curatore.getLuogoGestito());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), luogoGestito);
    }

    @NonNull
    @Override
    public String toString() {
        return "Curatore{" +
                "luogoGestito=" + luogoGestito +
                '}';
    }
}
