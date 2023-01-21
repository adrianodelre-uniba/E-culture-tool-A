package com.malfaang.e_culture_tool_a.grafo;

/*
 *
 *  @author adrianodelre
 */
public class NodoOpera<T> {
    private T operaNodo;
    private T operaPrecedenteLaterale;
    private T operaPrecedenteLaterale2; // Per gestire nodo relativo all'uscita; su altri nodi e' null
    private T operaPrecedenteFrontale;
    private T operaSuccessivaLaterale;
    private T operaSuccessivaLaterale2; // Per gestire nodo relativo all'entrata; su altri nodi e' null
    private T operaSuccessivaFrontale;

    public NodoOpera(T operaNodo, T operaPrecedenteLaterale, T operaPrecedenteFrontale,
                     T operaSuccessivaLaterale, T operaSuccessivaFrontale){
        this.operaNodo = operaNodo;
        this.operaPrecedenteLaterale = operaPrecedenteLaterale;
        this.operaPrecedenteFrontale = operaPrecedenteFrontale;
        this.operaSuccessivaLaterale = operaSuccessivaLaterale;
        this.operaSuccessivaFrontale = operaSuccessivaFrontale;
    }

    public T getOperaNodo() {
        return operaNodo;
    }

    public void setOperaNodo(T operaNodo) {
        this.operaNodo = operaNodo;
    }

    public T getOperaPrecedenteLaterale() {
        return operaPrecedenteLaterale;
    }

    public void setOperaPrecedenteLaterale(T operaPrecedenteLaterale) {
        this.operaPrecedenteLaterale = operaPrecedenteLaterale;
    }

    public T getOperaPrecedenteLaterale2() {
        return operaPrecedenteLaterale2;
    }

    public void setOperaPrecedenteLaterale2(T operaPrecedenteLaterale2) {
        this.operaPrecedenteLaterale2 = operaPrecedenteLaterale2;
    }

    public T getOperaPrecedenteFrontale() {
        return operaPrecedenteFrontale;
    }

    public void setOperaPrecedenteFrontale(T operaPrecedenteFrontale) {
        this.operaPrecedenteFrontale = operaPrecedenteFrontale;
    }

    public T getOperaSuccessivaLaterale() {
        return operaSuccessivaLaterale;
    }

    public void setOperaSuccessivaLaterale(T operaSuccessivaLaterale) {
        this.operaSuccessivaLaterale = operaSuccessivaLaterale;
    }

    public T getOperaSuccessivaLaterale2() {
        return operaSuccessivaLaterale2;
    }

    public void setOperaSuccessivaLaterale2(T operaSuccessivaLaterale2) {
        this.operaSuccessivaLaterale2 = operaSuccessivaLaterale2;
    }

    public T getOperaSuccessivaFrontale() {
        return operaSuccessivaFrontale;
    }

    public void setOperaSuccessivaFrontale(T operaSuccessivaFrontale) {
        this.operaSuccessivaFrontale = operaSuccessivaFrontale;
    }

}
