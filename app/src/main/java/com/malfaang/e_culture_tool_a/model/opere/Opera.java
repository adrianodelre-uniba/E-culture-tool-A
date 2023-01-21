package com.malfaang.e_culture_tool_a.model.opere;

/*
 *
 *  @author adrianodelre
 */
public class Opera {
    private  String nome;
    private  String descrizione;
    private  String foto; //forse è da convertire in Base64
    private  String codiceQR; //forse è da convertire in Base64
    private  String attivitaAssociata;
    private  String codiceIot; //forse è da convertire in Base64

    public Opera(String nome, String descrizione, String foto,
                 String codiceQR, String attivitaAssociata, String codiceIot) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.foto = foto;
        this.codiceQR = codiceQR;
        this.attivitaAssociata = attivitaAssociata;
        this.codiceIot = codiceIot;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCodiceQR() {
        return codiceQR;
    }

    public void setCodiceQR(String codiceQR) {
        this.codiceQR = codiceQR;
    }

    public String getAttivitaAssociata() {
        return attivitaAssociata;
    }

    public void setAttivitaAssociata(String attivitaAssociata) {
        this.attivitaAssociata = attivitaAssociata;
    }

    public String getCodiceIot() {
        return codiceIot;
    }

    public void setCodiceIot(String codiceIot) {
        this.codiceIot = codiceIot;
    }
}
