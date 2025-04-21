package progetto.gameplay.entities.specific.specific.notliving;

import com.badlogic.gdx.graphics.Texture;
import progetto.gameplay.entities.components.specific.general.RadiusComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.base.EntityInstance;
import progetto.manager.entities.Engine;

public class GameObject extends Entity{

    /** Texture del proiettile */
    public Texture texture;

    /**
     * Crea un'entità a partire da un'istanza salvata (es. caricata da un file).
     *
     * @param instance l'entità salvata {@link EntityInstance}
     * @param manager  il gestore delle entità {@link Engine}
     */
    public GameObject(EntityInstance instance, Engine manager, float radius) {
        super(instance, manager);
        components.add(new RadiusComponent(radius));
    }

    /**
     * Crea un'entità a partire da una configurazione personalizzata.
     *
     * @param config  configurazione dell'entità {@link EntityConfig}
     * @param manager il gestore delle entità {@link Engine}
     */
    public GameObject(EntityConfig config, Engine manager, float radius) {
        super(config, manager);
        components.add(new RadiusComponent(radius));
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
    public EntityInstance unregister() {
        return new EntityInstance(this);
    }

}
