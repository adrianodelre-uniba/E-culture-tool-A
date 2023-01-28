package com.malfaang.e_culture_tool_a.sito;

//Classe per l'Arraylist di sito
public class Sito {
    private String nomeSito;
    private String cittaSito;
    private String telefonoSito;
    private String emailSito;
    private String orarioApertura;
    private String orarioChiusura;
    private String idCuratore;
    private String dataUltimaModifica;


    public Sito(String nomeSito, String cittaSito, String telefonoSito, String emailSito,
                String orarioApertura, String orarioChiusura,String dataUltimaModifica, String idCuratore ) {
        this.nomeSito = nomeSito;
        this.cittaSito = cittaSito;
        this.telefonoSito = telefonoSito;
        this.emailSito = emailSito;
        this.orarioApertura = orarioApertura;
        this.orarioChiusura = orarioChiusura;
        this.idCuratore = idCuratore;
        this.dataUltimaModifica = dataUltimaModifica;

    }

    public String getNomeSito() {
        return nomeSito;
    }

    public void setNomeSito(String nomeSito) {
        this.nomeSito = nomeSito;
    }

    public String getCittaSito() {
        return cittaSito;
    }

    public void setCittaSito(String cittaSito) {
        this.cittaSito = cittaSito;
    }

    public String getTelefonoSito() {
        return telefonoSito;
    }

    public void setTelefonoSito(String telefonoSito) {
        this.telefonoSito = telefonoSito;
    }

    public String getEmailSito() {
        return emailSito;
    }

    public void setEmailSito(String emailSito) {
        this.emailSito = emailSito;
    }

    public String getOrarioApertura() {
        return orarioApertura;
    }

    public void setOrarioApertura(String orarioApertura) {
        this.orarioApertura = orarioApertura;
    }

    public String getOrarioChiusura() {
        return orarioChiusura;
    }

    public void setOrarioChiusura(String orarioChiusura) {
        this.orarioChiusura = orarioChiusura;
    }

    public String getIdCuratore() {
        return idCuratore;
    }

    public void setIdCuratore(String idCuratore) {
        this.idCuratore = idCuratore;
    }

    public String getDataUltimaModifica() {
        return dataUltimaModifica;
    }

    public void setDataUltimaModifica(String dataUltimaModifica) {
        this.dataUltimaModifica = dataUltimaModifica;
    }
}
