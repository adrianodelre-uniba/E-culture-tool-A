package com.malfaang.e_culture_tool_a.grafo;

/*
 *
 *  @author adrianodelre
 */
import com.malfaang.e_culture_tool_a.model.opere.Opera;

import java.util.ArrayList;
import java.util.List;

public class GrafoLuogo {
    private List<GrafoStanza<NodoOpera<Opera>>> stanzeLuogo = new ArrayList<>();

    public GrafoLuogo(){}

    public Object getStanza(GrafoStanza<NodoOpera<Opera>> stanza){ //TODO Implementare get avente come parametro il nome dell'opera
        if(stanzeLuogo.contains(stanza)) {
            int index = stanzeLuogo.indexOf(stanza);
            return stanzeLuogo.get(index);
        }
        return null;
    }

    public boolean aggiungiStanzaAlLuogo(GrafoStanza<NodoOpera<Opera>> stanza){
        return stanzeLuogo.add(stanza);
    }

    public boolean rimuoviStanzaDaLuogo(GrafoStanza<NodoOpera<Opera>> stanza){
        if(stanzeLuogo.contains(stanza)){
            stanzeLuogo.remove(stanza);
            return true;
        }
        return false;
    }

    public boolean modificaStanzaNelLuogo(GrafoStanza<NodoOpera<Opera>> stanzaIniziale,
                                          GrafoStanza<NodoOpera<Opera>> stanzaDesiderata){
        if(stanzeLuogo.contains(stanzaIniziale)){
            int index = stanzeLuogo.indexOf(stanzaIniziale);
            GrafoStanza<NodoOpera<Opera>> tmp = stanzeLuogo.get(index);
            stanzeLuogo.remove(index);
            stanzeLuogo.add(stanzaDesiderata);
        }
        return false;
    }

    public List<GrafoStanza<NodoOpera<Opera>>> getStanzeLuogo() {
        return stanzeLuogo;
    }

    public void setStanzeLuogo(List<GrafoStanza<NodoOpera<Opera>>> stanzeLuogo) {
        this.stanzeLuogo = stanzeLuogo;
    }
}
