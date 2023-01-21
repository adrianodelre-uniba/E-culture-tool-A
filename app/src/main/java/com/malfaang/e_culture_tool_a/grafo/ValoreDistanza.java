package com.malfaang.e_culture_tool_a.grafo;

import com.malfaang.e_culture_tool_a.model.opere.Opera;

public class ValoreDistanza {
    private NodoOpera<Opera> operaA;
    private NodoOpera<Opera> operaB;
    private int valore;

    public ValoreDistanza(NodoOpera<Opera> operaA, NodoOpera<Opera> operaB, int valore){
        this.operaA = operaA;
        this.operaB = operaB;
        this.valore = valore;
    }

    public NodoOpera<Opera> getOperaA() {
        return operaA;
    }

    public void setOperaA(NodoOpera<Opera> operaA) {
        this.operaA = operaA;
    }

    public NodoOpera<Opera> getOperaB() {
        return operaB;
    }

    public void setOperaB(NodoOpera<Opera> operaB) {
        this.operaB = operaB;
    }

    public int getValore() {
        return valore;
    }

    public void setValore(int valore) {
        this.valore = valore;
    }
}
