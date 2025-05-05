package progetto.ECS.entities.specific.notliving;

import com.badlogic.gdx.graphics.Texture;
import progetto.ECS.EntityEngine;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.ECS.entities.specific.EntityConfig;
import progetto.ECS.entities.specific.EntityInstance;

public class Casa extends GameObject {
    /**
     * Crea un'entità a partire da un'istanza salvata (es. caricata da un file).
     *
     * @param instance l'entità salvata {@link EntityInstance}
     * @param manager  il gestore delle entità {@link EntityEngine}
     * @param radius
     */
    public Casa(EntityInstance instance, EntityEngine manager, float radius) {
        super(instance, manager, radius);
    }

    /**
     * Crea un'entità a partire da una configurazione personalizzata.
     *
     * @param config  configurazione dell'entità {@link EntityConfig}
     * @param manager il gestore delle entità {@link EntityEngine}
     * @param radius
     */
    public Casa(EntityConfig config, EntityEngine manager, float radius) {
        super(config, manager, radius);
        this.texture = new Texture("entities/Casa.png");
    }


    @Override
    public void create() {
        super.create();
        get(PhysicsComponent.class).getBody().getFixtureList().get(0).setSensor(true);

    }
}
