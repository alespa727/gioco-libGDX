package progetto.entity.entities.specific.notliving;

import com.badlogic.gdx.graphics.Texture;
import progetto.entity.Engine;
import progetto.entity.components.specific.general.RadiusComponent;
import progetto.entity.entities.base.Entity;
import progetto.entity.entities.base.EntityConfig;
import progetto.entity.entities.base.EntityInstance;

public class GameObject extends Entity {

    /**
     * Texture del proiettile
     */
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
        return null;
    }

}
