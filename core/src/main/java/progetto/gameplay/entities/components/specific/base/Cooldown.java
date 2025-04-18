package progetto.gameplay.entities.components.specific.base;

import progetto.gameplay.entities.components.base.IteratableComponent;

/**
 * Classe che gestisce un cooldown, ovvero un timer che impedisce l'esecuzione
 * di un'azione prima che un determinato intervallo di tempo sia trascorso.
 * Utilizzata per creare delay tra le azioni (ad esempio, tra i colpi di un giocatore).
 *
 * @author alesp
 */
public class Cooldown extends IteratableComponent {
    public float time; // Tempo rimanente per il cooldown
    public final float maxTime; // Tempo massimo (durata del cooldown)
    public boolean isReady; // Indica se il cooldown è pronto per essere resettato

    /**
     * Costruttore che inizializza il cooldown con un tempo massimo.
     * Imposta il tempo rimanente uguale al tempo massimo e imposta isReady su true.
     *
     * @param maxTime Durata del cooldown
     */
    public Cooldown(float maxTime) {
        this.maxTime = maxTime;
        this.time = maxTime; // Imposta il tempo rimanente uguale al tempo massimo
        this.isReady = true; // Il cooldown è pronto a partire
    }

    /**
     * Aggiorna il timer del cooldown. Se il cooldown non è pronto, riduce il tempo
     * rimanente. Se il tempo arriva a 0, imposta isReady su true.
     *
     * @param delta Tempo trascorso dall'ultimo aggiornamento
     */
    @Override
    public void update(float delta) {
        if (!isReady) { // Se il cooldown non è pronto
            time -= delta; // Diminuisci il tempo rimanente in base al delta
            time = Math.max(0, time); // Assicurati che il tempo non diventi negativo
            if (time <= 0) { // Se il tempo è scaduto
                isReady = true; // Imposta il cooldown come pronto
            }
        }
    }

    /**
     * Resetta il cooldown, impostando il tempo rimanente al valore massimo
     * e mettendo isReady su false.
     */
    public void reset() {
        isReady = false; // Il cooldown non è pronto
        time = maxTime; // Imposta il tempo rimanente al massimo
    }

    /**
     * Resetta il cooldown con un tempo personalizzato.
     *
     * @param time Il nuovo tempo per il cooldown
     */
    public void reset(float time) {
        isReady = false; // Il cooldown non è pronto
        this.time = time; // Imposta il tempo rimanente al valore passato
    }

    /**
     * Restituisce il tempo rimanente del cooldown.
     *
     * @return Il tempo rimanente
     */
    public float getTimer() {
        return time;
    }

    /**
     * Restituisce la durata massima del cooldown.
     *
     * @return La durata massima
     */
    public float getMaxTime() {
        return maxTime;
    }
}
