package com.malfaang.e_culture_tool_a.grafo;

import com.malfaang.e_culture_tool_a.model.opere.Opera;

import java.util.ArrayList;
import java.util.List;

/*
 *
 *  @author adrianodelre
 */
public class GrafoStanza<T> {
    private List<NodoOpera<Opera>> opereStanza = new ArrayList<>();
    private T stanzaNodo;
    private T stanzaSuccessiva;
    private T stanzaPrecedente;
    private NodoOpera<Opera> ingresso;
    private NodoOpera<Opera> uscita;

    public GrafoStanza(){

    }

    public Object getOpera(NodoOpera<Opera> opera){ //TODO Implementare get avente come parametro il nome dell'opera
        if(opereStanza.contains(opera)) {
            int index = opereStanza.indexOf(opera);
            return opereStanza.get(index);
        }
        return null;
    }

    public boolean aggiungiOperaAllaStanza(NodoOpera<Opera> opera){
        return opereStanza.add(opera);
    }

    public boolean rimuoviOperaDallaStanza(NodoOpera<Opera> opera){
        if(opereStanza.contains(opera)){
            opereStanza.remove(opera);
            return true;
        }
        return false;
    }

    public boolean modificaOperaNellaStanza(NodoOpera<Opera> operaIniziale, NodoOpera<Opera> operaDesiderata){
        if(opereStanza.contains(operaIniziale)){
            int index = opereStanza.indexOf(operaIniziale);
            NodoOpera<Opera> tmp = opereStanza.get(index);
            opereStanza.remove(index);
            opereStanza.add(operaDesiderata);
        }
        return false;
    }

    public List<NodoOpera<Opera>> getOpereStanza() {
        return opereStanza;
    }

    public void setOpereStanza(List<NodoOpera<Opera>> opereStanza) {
        this.opereStanza = opereStanza;
    }

    public T getStanzaNodo() {
        return stanzaNodo;
    }

    public void setStanzaNodo(T stanzaNodo) {
        this.stanzaNodo = stanzaNodo;
    }

    public T getStanzaSuccessiva() {
        return stanzaSuccessiva;
    }

    public void setStanzaSuccessiva(T stanzaSuccessiva) {
        this.stanzaSuccessiva = stanzaSuccessiva;
    }

    public T getStanzaPrecedente() {
        return stanzaPrecedente;
    }

    public void setStanzaPrecedente(T stanzaPrecedente) {
        this.stanzaPrecedente = stanzaPrecedente;
    }

    public NodoOpera<Opera> getIngresso() {
        return ingresso;
    }

    public void setIngresso(NodoOpera<Opera> ingresso) {
        this.ingresso = ingresso;
    }

    public NodoOpera<Opera> getUscita() {
        return uscita;
    }

    public void setUscita(NodoOpera<Opera> uscita) {
        this.uscita = uscita;
    }
}
