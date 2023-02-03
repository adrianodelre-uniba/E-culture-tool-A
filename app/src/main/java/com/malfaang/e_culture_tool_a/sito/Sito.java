package com.malfaang.e_culture_tool_a.sito;
//Classe per l'Arraylist di sito
public class Sito{
    String Nome_sito,città_sito,telefono_sito,email_sito,orario_apertura,orario_chiusura,Id_Curatore,data_ultima_modifica;


    public Sito(String nome_sito, String città_sito, String telefono_sito, String email_sito, String orario_apertura, String orario_chiusura,String data_ultima_modifica, String id_Curatore ) {
        this.Nome_sito = nome_sito;
        this.città_sito = città_sito;
        this.telefono_sito = telefono_sito;
        this.email_sito = email_sito;
        this.orario_apertura = orario_apertura;
        this.orario_chiusura = orario_chiusura;

        this.Id_Curatore = id_Curatore;
        this.data_ultima_modifica = data_ultima_modifica;

    }


    public String getNome_sito() {
        return Nome_sito;
    }

    public String getCittà_sito() {
        return città_sito;
    }

    public String getTelefono_sito() {
        return telefono_sito;
    }

    public String getEmail_sito() {
        return email_sito;
    }

    public String getOrario_apertura() {
        return orario_apertura;
    }

    public String getOrario_chiusura() {
        return orario_chiusura;
    }

    public String getId_Curatore() {
        return Id_Curatore;
    }

    public String getData_Ultima_Modifica() {
        return data_ultima_modifica;
    }
}
