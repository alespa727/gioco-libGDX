package progetto.ECS.entities.specific.notliving;

import com.badlogic.gdx.graphics.Texture;
import progetto.ECS.EntityEngine;
import progetto.ECS.components.specific.general.RadiusComponent;
import progetto.ECS.entities.specific.EntityConfig;
import progetto.ECS.entities.specific.EntityInstance;
import progetto.ECS.entities.specific.BaseEntity;

public class GameObject extends BaseEntity {

    /**
     * Texture del proiettile
     */
    public Texture texture;

    /**
     * Crea un'entità a partire da un'istanza salvata (es. caricata da un file).
     *
     * @param instance l'entità salvata {@link EntityInstance}
     * @param manager  il gestore delle entità {@link EntityEngine}
     */
    public GameObject(EntityInstance instance, EntityEngine manager, float radius) {
        super(instance, manager);
        components.add(new RadiusComponent(radius));
    }

    /**
     * Crea un'entità a partire da una configurazione personalizzata.
     *
     * @param config  configurazione dell'entità {@link EntityConfig}
     * @param manager il gestore delle entità {@link EntityEngine}
     */
    public GameObject(EntityConfig config, EntityEngine manager, float radius) {
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
