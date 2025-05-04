package progetto.ECS.entities.specific.notliving;

import progetto.ECS.EntityEngine;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.ECS.components.specific.general.ConfigComponent;
import progetto.ECS.components.specific.item.ItemComponent;
import progetto.ECS.entities.specific.EntityConfig;
import progetto.ECS.entities.specific.EntityInstance;
import progetto.world.WorldManager;

public class Sword extends GameObject {
    /**
     * Crea un'entità a partire da una configurazione personalizzata.
     *
     * @param config  configurazione dell'entità {@link EntityConfig}
     * @param manager il gestore delle entità {@link EntityEngine}
     */
    public Sword(EntityConfig config, EntityEngine manager, float radius) {
        super(config, manager, radius);
        this.texture = get(ConfigComponent.class).getConfig().img;
    }

    /**
     * Crea un'entità a partire da un'istanza salvata (es. caricata da un file).
     *
     * @param instance l'entità salvata {@link EntityInstance}
     * @param manager  il gestore delle entità {@link EntityEngine}
     */
    public Sword(EntityInstance instance, EntityEngine manager, float radius) {
        super(instance, manager, radius);
        this.texture = get(ConfigComponent.class).getConfig().img;


    }

    /**
     * Viene chiamato dopo la creazione per inizializzare comportamenti specifici.
     */
    @Override
    public void create() {
        add(
            new ItemComponent(null)
        );
    }

    /**
     * Rimuove l'entità dal mondo e restituisce un oggetto che la rappresenta.
     *
     * @return l'entità salvabile {@link EntityInstance}
     */
    @Override
    public EntityInstance unregister() {
        entityEngine.remove(this);
        WorldManager.destroyBody(get(PhysicsComponent.class).getBody());
        return null;
    }
}
