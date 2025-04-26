package progetto.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Questa classe rappresenta una struttura dati personalizzata basata su una {@link LinkedHashMap}
 * che tiene traccia di un indice corrente per navigare tra i suoi elementi.
 *
 * @param <T> il tipo degli elementi contenuti nella mappa.
 * @see LinkedHashMap
 * @see List
 *
 * @author Ferrarese Tommaso
 */
public class CustomLinkedHashMap <T> {

    /**
     * Mappa che associa una chiave {@link String} a un valore di tipo {@code T}.
     */
    private LinkedHashMap<String, T> hashMap = new LinkedHashMap<>();

    /**
     * Indice corrente nella lista delle chiavi.
     */
    private int indice;

    /**
     * Costruttore della classe.
     *
     * @param indice indice iniziale da impostare.
     */
    public CustomLinkedHashMap(int indice){
        this.indice = indice;
    }

    /**
     * Restituisce la {@link LinkedHashMap} attualmente memorizzata.
     *
     * @return la mappa contenente i dati.
     */
    public LinkedHashMap<String, T> getHashMap() {
        return hashMap;
    }

    /**
     * Imposta una nuova {@link LinkedHashMap} come contenuto di questa struttura.
     *
     * @param nuoviValori la nuova mappa da assegnare.
     */
    public void setHashMap(LinkedHashMap<String, T> nuoviValori) {
        this.hashMap = nuoviValori;
    }

    /**
     * Restituisce l'indice corrente.
     *
     * @return indice corrente.
     */
    public int getIndice() {
        return indice;
    }

    /**
     * Imposta un nuovo valore per l'indice corrente.
     *
     * @param nuovoIndice nuovo indice da assegnare.
     */
    public void setIndice(int nuovoIndice) {
        this.indice = nuovoIndice;
    }

    /**
     * Restituisce il valore corrente della mappa, in base all'indice.
     *
     * @return valore di tipo {@code T} associato alla chiave corrispondente all'indice.
     */
    public T getValoreCorrente() {
        List<String> chiavi = new ArrayList<>(this.hashMap.keySet());
        return this.hashMap.get(chiavi.get(this.indice));
    }

    /**
     * Avanza l'indice al prossimo elemento, ritornando all'inizio se si supera la fine della lista.
     */
    public void avanti() {
        this.indice = (this.indice + 1 < this.hashMap.size())? (this.indice + 1) : this.hashMap.size();
    }

    /**
     * Torna indietro di un elemento, ritornando alla fine della lista se si va prima del primo elemento.
     */
    public void indietro() {
        this.indice = (this.indice - 1 < this.hashMap.size())? (this.indice - 1) : this.hashMap.size();
    }
}
