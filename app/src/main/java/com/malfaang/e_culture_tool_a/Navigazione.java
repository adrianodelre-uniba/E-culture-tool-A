package com.malfaang.e_culture_tool_a;

import com.malfaang.e_culture_tool_a.grafo.GrafoLuogo;
import com.malfaang.e_culture_tool_a.grafo.GrafoStanza;
import com.malfaang.e_culture_tool_a.grafo.NodoOpera;
import com.malfaang.e_culture_tool_a.grafo.ValoreDistanza;
import com.malfaang.e_culture_tool_a.model.opere.Opera;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 *
 *  @author adrianodelre
 */
public class Navigazione {

    /*  //TODO Continuare descrizione processo
     *
     * Input: Opere selezionate dall'utente
     * Output: Percorso per visitare le opere selezionate passando attraverso le varie stanze del luogo
     *
     * 1) Utente seleziona opere
     *      Opere vengono passate alla classe Navigazione che poi processa i dati
     *  2) Sistema carica dati
     *  3) Creare matrice di adiacenza binaria nxn in cui le opere precedenti e successive hanno valore pari a 1
     *      e corrispondono ai nodi immediatamente raggiungibili dal grafo di adiacenza, le altre opere assumono
     *      valore pari a 0
     *  4) Inizializzazione:    (farla qui o in altra classe di più alto livello?)
     *      Alpha è l'entrata della stanza
     *      Omega è l'uscita della stanza
     *      Alpha ha 2 opere successive laterali e 1 opera successiva frontale , gli altri noodi (che sono opere
     *          tranne omega) hanno 1 opera precedente laterale, 1 opera precedente frontale, 1 opera successiva
     *          laterale e 1 opera successiva frontale
     *  5) Configurazione navigazione:
     *      Gli utenti selezionano le opere desiderate
     *      Le opere vengono divise in relazione alle stanze
     *      Viene/vengono individuata/e la/e opera/e che ha/hanno null come valore relativo all'opera precedente
     *          in quanto è/sono la/e opera/e iniziale/i la/e più vicina/e all'entrata (alpha)
     *  3) Sistema calcola distanza tra nodi/opere
     *  4) Sistema calcola percorso migliore
     *  5) Sistema restituisce percorso migliore
     *      --> Storage in variabile user defined type
     *          --> "Percorso"
     */
    //TODO rendere privato con implementazione di getter e setter
    private List<NodoOpera<Opera>> opere = new ArrayList<>();
    private List<ValoreDistanza> valori = new ArrayList<>();

    public Navigazione(){}

    public void caricaDati(List<NodoOpera<Opera>> opere2){
        setOpere(opere2);
    }

    public void inizializzazione(){}

    private boolean controlloCorrispondenzaOpere(NodoOpera<Opera> operaA, NodoOpera<Opera> operaB){
        if(operaA.getOperaSuccessivaLaterale().equals(operaB.getOperaNodo())){
            return true;
        }else if(operaA.getOperaSuccessivaFrontale().equals(operaB.getOperaNodo())){
            return true;
        }else if(operaA.getOperaSuccessivaLaterale2().equals(operaB.getOperaNodo())){
            return true;
        }else if(operaA.getOperaPrecedenteLaterale().equals(operaB.getOperaNodo())){
            return true;
        }else if(operaA.getOperaPrecedenteFrontale().equals(operaB.getOperaNodo())){
            return true;
        }else if(operaA.getOperaPrecedenteLaterale2().equals(operaB.getOperaNodo())){
            return true;
        }else{
            return false;
        }
    }

    private int calcolaDistanzaBinariaTraNodi(NodoOpera<Opera> operaA, NodoOpera<Opera> operaB){
        // TODO Controllare se c'e' necessita' di aggiungere casistiche all'if
        if(controlloCorrispondenzaOpere(operaA, operaB)){
            return 1;
        }else{
            return 0;
        }
    }

    private void distanzaTraNodiPerMatriceAdiacenza(NodoOpera<Opera> operaA, NodoOpera<Opera> operaB){
        valori.add(new ValoreDistanza(operaA, operaB, calcolaDistanzaBinariaTraNodi(operaA, operaB)));
    }

    private void creaMatriceAdiacenzaStanza(GrafoStanza<NodoOpera<Opera>> stanza){
        List<NodoOpera<Opera>> opereStanza = stanza.getOpereStanza();
        NodoOpera[] opereArray = new NodoOpera[opereStanza.size()];
        opereArray = opereStanza.toArray(opereArray);
        for(int i = 0; i < opereArray.length; i++){
            for(int j = 0; j < opereArray.length; j++){
                NodoOpera<Opera> opera1 = opereArray[i];
                NodoOpera<Opera> opera2 = opereArray[j];
                distanzaTraNodiPerMatriceAdiacenza(opera1, opera2);
            }
        }
    }

    public void creaMatriceAdiacenzaLuogo(GrafoLuogo luogo){
        List<GrafoStanza<NodoOpera<Opera>>> stanze = luogo.getStanzeLuogo();
        GrafoStanza<NodoOpera<Opera>>[] stanzeArray = new GrafoStanza[stanze.size()];
        stanzeArray = stanze.toArray(stanzeArray);
        for(int i = 0; i < stanzeArray.length; i++){
            GrafoStanza<NodoOpera<Opera>> stanza = stanzeArray[i];
            creaMatriceAdiacenzaStanza(stanza);
        }
    }

    private boolean controlloCorrispondenzaOpere2(NodoOpera<Opera> operaA, NodoOpera<Opera> operaB){
        if(operaA.getOperaSuccessivaLaterale().equals(operaB.getOperaPrecedenteLaterale())){
            return true;
        }else if(operaA.getOperaSuccessivaLaterale().equals(operaB.getOperaPrecedenteFrontale())){
            return true;
        }else if(operaA.getOperaSuccessivaFrontale().equals(operaB.getOperaPrecedenteLaterale())){
            return true;
        }else if(operaA.getOperaSuccessivaFrontale().equals(operaB.getOperaPrecedenteFrontale())){
            return true;
        }else if(operaA.getOperaSuccessivaLaterale().equals(operaB.getOperaPrecedenteLaterale2())){
            return true;
        }else if(operaA.getOperaSuccessivaFrontale().equals(operaB.getOperaPrecedenteLaterale2())){
            return true;
        }else if(operaA.getOperaSuccessivaLaterale2().equals(operaB.getOperaPrecedenteLaterale())){
            return true;
        }else if(operaA.getOperaSuccessivaLaterale2().equals(operaB.getOperaPrecedenteFrontale())){
            return true;
        }else if(operaA.getOperaSuccessivaLaterale2().equals(operaB.getOperaPrecedenteLaterale2())){
            return true;
        }else{
            return false;
        }
    }

    private NodoOpera<Opera> ricercaNodoOpera(Opera opera){
        NodoOpera<Opera> nodoTemp1 = null;
        NodoOpera<Opera>[] opereArray = new NodoOpera[getOpere().size()];
        opereArray = getOpere().toArray(opereArray);
        for(int i = 0; i < opereArray.length; i++){
            if(opera.equals(opereArray[i].getOperaNodo())){
                nodoTemp1 = opereArray[i];
            }
        }
        return nodoTemp1;
    }

    private NodoOpera<Opera> determinaNodoMigliore(NodoOpera<Opera> operaA, NodoOpera<Opera> operaB){
        Opera temp1 = operaA.getOperaSuccessivaLaterale();
        NodoOpera<Opera> nodoTemp1 = ricercaNodoOpera(temp1);
        int distanzaTemp1 = calcolaDistanzaTraNodiDaMatrice(nodoTemp1,operaB);
        Opera temp2 = operaA.getOperaSuccessivaFrontale();
        NodoOpera<Opera> nodoTemp2 = ricercaNodoOpera(temp2);
        int distanzaTemp2 = calcolaDistanzaTraNodiDaMatrice(nodoTemp2,operaB);
        Opera temp3 = operaA.getOperaSuccessivaLaterale2();
        NodoOpera<Opera> nodoTemp3 = ricercaNodoOpera(temp3);
        int distanzaTemp3 = calcolaDistanzaTraNodiDaMatrice(nodoTemp3,operaB);
        Opera temp4 = operaA.getOperaPrecedenteLaterale();
        NodoOpera<Opera> nodoTemp4 = ricercaNodoOpera(temp4);
        int distanzaTemp4 = calcolaDistanzaTraNodiDaMatrice(nodoTemp4,operaB);
        Opera temp5 = operaA.getOperaPrecedenteFrontale();
        NodoOpera<Opera> nodoTemp5 = ricercaNodoOpera(temp5);
        int distanzaTemp5 = calcolaDistanzaTraNodiDaMatrice(nodoTemp5,operaB);
        Opera temp6 = operaA.getOperaPrecedenteLaterale2();
        NodoOpera<Opera> nodoTemp6 = ricercaNodoOpera(temp6);
        int distanzaTemp6 = calcolaDistanzaTraNodiDaMatrice(nodoTemp6,operaB);
        int[] distanze = {distanzaTemp1, distanzaTemp2, distanzaTemp3, distanzaTemp4, distanzaTemp5, distanzaTemp6};
        Arrays.sort(distanze);
        if(distanze[0] != distanze[1]) {
            if(distanzaTemp1 == distanze[0]) {
                return nodoTemp1;
            } else if (distanzaTemp2 == distanze[0]) {
                return nodoTemp2;
            } else if (distanzaTemp3 == distanze[0]) {
                return nodoTemp3;
            } else if (distanzaTemp4 == distanze[0]) {
                return nodoTemp4;
            } else if (distanzaTemp5 == distanze[0]) {
                return nodoTemp5;
            } else if (distanzaTemp6 == distanze[0]) {
                return nodoTemp6;
            } else {
                return null;
            }
        }else{
            if(distanzaTemp1 == distanzaTemp2){
                return nodoTemp1;
            }else if(distanzaTemp1 == distanzaTemp3){
                return nodoTemp1;
            }else if(distanzaTemp1 == distanzaTemp4){
                return nodoTemp1;
            }else if(distanzaTemp1 == distanzaTemp5){
                return nodoTemp1;
            }else if(distanzaTemp1 == distanzaTemp6){
                return nodoTemp1;
            }else if(distanzaTemp2 == distanzaTemp3){
                if(nodoTemp3 != null) {
                    return nodoTemp3;
                }else{
                    return nodoTemp2;
                }
            }else if(distanzaTemp2 == distanzaTemp4){
                return nodoTemp4;
            }else if(distanzaTemp2 == distanzaTemp5){
                return nodoTemp2;
            }else if(distanzaTemp2 == distanzaTemp6){
                if(nodoTemp6 != null) {
                    return nodoTemp6;
                }else{
                    return nodoTemp2;
                }
            }else if(distanzaTemp3 == distanzaTemp4){
                if(nodoTemp3 != null) {
                    return nodoTemp3;
                }else{
                    return nodoTemp4;
                }
            }else if(distanzaTemp3 == distanzaTemp5){
                if(nodoTemp3 != null) {
                    return nodoTemp3;
                }else{
                    return nodoTemp5;
                }
            }else if(distanzaTemp3 == distanzaTemp6){
                if(nodoTemp6 != null) {
                    return nodoTemp6;
                }else if(nodoTemp3 != null){
                    return nodoTemp2;
                }else{
                    return null;
                }
            }else if(distanzaTemp4 == distanzaTemp5){
                return nodoTemp4;
            }else if(distanzaTemp4 == distanzaTemp6){
                if(nodoTemp6 != null) {
                    return nodoTemp6;
                }else{
                    return nodoTemp4;
                }
            }else if(distanzaTemp5 == distanzaTemp6){
                if(nodoTemp6 != null) {
                    return nodoTemp6;
                }else{
                    return nodoTemp5;
                }
            }else {
                return null;
            }
        }
    }

    private int calcolaDistanzaTraNodiDaMatrice(NodoOpera<Opera> operaA, NodoOpera<Opera> operaB){
        if(calcolaDistanzaBinariaTraNodi(operaA,operaB) == 1){
            return  1;
        }else if (calcolaDistanzaBinariaTraNodi(operaA,operaB) == 0){
            if(controlloCorrispondenzaOpere2(operaA, operaB)){
                return 2;
            }else{
                int counter = 1;
                NodoOpera<Opera> nodoMigliore = determinaNodoMigliore(operaA, operaB);
                counter += calcolaDistanzaTraNodiDaMatrice(nodoMigliore, operaB);
                return counter;
            }
        }else{
            return 0;
        }
    }
    // TODO Rivedere perchè è probabile che ci siano molti errori
    private List<NodoOpera<Opera>[]> generaCombinazioni(NodoOpera<Opera>[] opereArray, int k){
        List<NodoOpera<Opera>[]> combinazioni = new ArrayList<>();
        int[] combinazione = new int[k];
        for(int i = 0; i < k; i++){
            combinazione[i] = i;
        }
        while(combinazione[k-1] < opereArray.length){
            NodoOpera<Opera>[] combinazioneSuccessiva = new NodoOpera[k];
            for(int i = 0; i < k; i++){
                combinazioneSuccessiva[i] = opereArray[combinazione[i]];
            }
            combinazioni.add(combinazioneSuccessiva);
            int t = k - 1;
            while(t != 0 && combinazione[t] == opereArray.length - k + t){
                t--;
            }
            combinazione[t]++;
            for(int i = t+1; i < k; i++){
                combinazione[i] = combinazione[i-1] + 1;
            }
        }
        return combinazioni;
    }

    //TODO Il void sarà "Percorso"
    public void calcolaPercorso(List<Opera> opereSelezionate, GrafoLuogo luogo) {
        //TODO Capire come implementare questione che uscita di una stanza è entrata di altra stanza
        //TODO Usare indexed for loop al posto di for each per evitare raw type e unchecked assignment

        // Bisogna suddividere le opere in relazione alle stanze
        List<GrafoStanza<NodoOpera<Opera>>> stanze = luogo.getStanzeLuogo();
        for(GrafoStanza<NodoOpera<Opera>> stanza : stanze){
            List<NodoOpera<Opera>> opere2 = stanza.getOpereStanza();
            List<NodoOpera<Opera>> opereSelezionateInStanza = new ArrayList<>();
            for(NodoOpera<Opera> opera : opere2){
                for(Opera opera1 : opereSelezionate){
                    if(opera.getOperaNodo().equals(opera1)){
                        opereSelezionateInStanza.add(opera);
                    }
                }
            }
            // Per ogni stanza, creare combinazioni di opere appartenenti alla stanza
            NodoOpera<Opera>[] opereSelezionateInStanzaArray = new NodoOpera[opereSelezionateInStanza.size()];
            opereSelezionateInStanzaArray = opereSelezionateInStanza.toArray(opereSelezionateInStanzaArray);
            List<NodoOpera<Opera>[]> combinazioni =
                    generaCombinazioni(opereSelezionateInStanzaArray, opereSelezionateInStanzaArray.length);
            int[] contatoreArray = new int[combinazioni.size()];
            // Calcolare distanza tra nodi da matrice e usare counter per avere int risultato di distanza
            // tra nodo alpha e omega passando per le opere selezionate nella stanza
            for (int j = 0; j < combinazioni.size(); j++) {
                NodoOpera<Opera>[] combinazioneArray = combinazioni.get(j);
                int counter = 0;
                // calcolare distanza tra ingresso e prima opera e aggiungere a counter
                counter += calcolaDistanzaTraNodiDaMatrice(stanza.getIngresso(), combinazioneArray[0]);
                for (int i = 0; i < combinazioneArray.length; i++) {
                    counter += calcolaDistanzaTraNodiDaMatrice(combinazioneArray[i], combinazioneArray[i + 1]);
                }
                // calcolare distanza tra ultima opera e uscita e aggiungere a counter
                counter += calcolaDistanzaTraNodiDaMatrice(combinazioneArray[combinazioneArray.length - 1],
                        stanza.getUscita());
                contatoreArray[j] = counter;
            }
            // Usare counter per capire quale combinazione arriva dall'entrata all'uscita con effort minore
            int[] contatoreArray2 = contatoreArray;
            Arrays.sort(contatoreArray);
            NodoOpera<Opera>[] combinazioneMigliore = null;
            for(int i = 0; i < contatoreArray2.length; i++){
                if(contatoreArray[0] == contatoreArray2[i]){
                    combinazioneMigliore = combinazioni.get(i);
                }
            }

        }
        //TODO To be done
        // Per risultato numericamente minore, creare Percorso per stanza
        // Omega di una stanza diventa alpha di un'altra
    }

    public List<NodoOpera<Opera>> getOpere() {
        return opere;
    }

    public void setOpere(List<NodoOpera<Opera>> opere) {
        this.opere = opere;
    }
}
