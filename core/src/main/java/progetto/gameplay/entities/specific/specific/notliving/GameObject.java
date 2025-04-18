package progetto.gameplay.entities.specific.specific.notliving;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.base.EntityInstance;
import progetto.manager.entities.Engine;

public class GameObject extends Entity{
    /**
     * Crea un'entità a partire da un'istanza salvata (es. caricata da un file).
     *
     * @param instance l'entità salvata {@link EntityInstance}
     * @param manager  il gestore delle entità {@link Engine}
     */
    public GameObject(EntityInstance instance, Engine manager) {
        super(instance, manager);
    }

    /**
     * Crea un'entità a partire da una configurazione personalizzata.
     *
     * @param config  configurazione dell'entità {@link EntityConfig}
     * @param manager il gestore delle entità {@link Engine}
     */
    public GameObject(EntityConfig config, Engine manager) {
        super(config, manager);
    }

    /**
     * Aggiorna il comportamento base dell'entità.
     *
     * @param delta tempo trascorso dall'ultimo frame
     */
    @Override
    public void updateEntity(float delta) {

    }

    /**
     * Aggiorna il comportamento specifico di questo tipo di entità.
     *
     * @param delta tempo trascorso dall'ultimo frame
     */
    @Override
    public void updateEntityType(float delta) {

    }

    /**
     * Viene chiamato dopo la creazione per inizializzare comportamenti specifici.
     */
    @Override
    public void create() {

    }

    /**
     * Rimuove l'entità dal mondo e restituisce un oggetto che la rappresenta.
     *
     * @return l'entità salvabile {@link EntityInstance}
     */
    @Override
    public EntityInstance despawn() {
        return new EntityInstance(this);
    }

    /**
     * Disegna l'entità sullo schermo.
     *
     * @param batch          il disegnatore
     * @param tempoTrascorso tempo passato per l’animazione
     */
    @Override
    public void draw(SpriteBatch batch, float tempoTrascorso) {

    }
}
