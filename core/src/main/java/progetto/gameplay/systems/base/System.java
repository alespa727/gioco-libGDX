package progetto.gameplay.systems.base;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.specific.base.Entity;

/**
 * Classe astratta che rappresenta un sistema nel paradigma ECS (Entity-Component-System).
 * Ogni sistema definisce una logica che viene applicata a una lista di entità.
 * <p>
 * I sistemi possono essere attivati o disattivati dinamicamente.
 */
public abstract class System {

    /**
     * Stato di attivazione del sistema. Se false, il sistema non viene aggiornato.
     */
    private boolean state = true;

    /**
     * Attiva il sistema, permettendone l'esecuzione durante l'update.
     */
    public final void activate() {
        this.state = true;
    }

    /**
     * Disattiva il sistema, impedendone l'esecuzione durante l'update.
     */
    public final void deactivate() {
        this.state = false;
    }

    /**
     * Verifica se il sistema è attivo.
     *
     * @return true se il sistema è attivo, false altrimenti.
     */
    public final boolean isActive() {
        return this.state;
    }

    /**
     * Metodo astratto che implementa la logica del sistema.
     * Viene chiamato ad ogni frame per aggiornare lo stato delle entità rilevanti.
     *
     * @param delta Tempo trascorso dall'ultimo aggiornamento (in secondi).
     * @param list  Lista di entità da processare.
     */
    public abstract void update(float delta, Array<Entity> list);
}
