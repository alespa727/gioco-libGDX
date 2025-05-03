package progetto.utils;

import java.util.LinkedHashMap;

/**
 * Questa classe rappresenta una struttura dati personalizzata basata su una {@link LinkedHashMap},
 * utilizzata per memorizzare comandi modificabili associati a chiavi di tipo {@link String}.
 *
 * @param <T> il tipo degli oggetti associati alle chiavi nella mappa.
 * @see LinkedHashMap
 *
 * @author Ferrarese Tommaso
 */
public class CustomMapComandiModificabili <T> {

    /**
     * La mappa che associa una chiave {@link String} a un valore di tipo {@code T}.
     */
    private LinkedHashMap<String, T> hashMap = new LinkedHashMap<>();

    /**
     * Costruttore di default della classe. Inizializza la mappa come vuota.
     */
    public CustomMapComandiModificabili(){}

    /**
     * Restituisce la {@link LinkedHashMap} attualmente memorizzata.
     *
     * @return la mappa contenente i comandi e i loro valori associati.
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
}
