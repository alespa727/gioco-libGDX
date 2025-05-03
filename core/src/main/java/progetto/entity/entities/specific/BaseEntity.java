package progetto.entity.entities.specific;

import progetto.entity.EntityEngine;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.base.StateComponent;
import progetto.entity.components.specific.general.ConfigComponent;
import progetto.entity.components.specific.graphics.ColorComponent;
import progetto.entity.components.specific.graphics.ZLevelComponent;
import progetto.entity.components.specific.movement.DirectionComponent;
import progetto.entity.components.specific.movement.NodeComponent;
import progetto.entity.entities.Entity;

public abstract class BaseEntity extends Entity {

    /**
     * Crea un'entità a partire da un'istanza salvata (es. Caricata da un file).
     *
     * @param instance l'entità salvata {@link EntityInstance}
     * @param entityEngine   il gestore delle entità {@link EntityEngine}
     */
    public BaseEntity(EntityInstance instance, EntityEngine entityEngine) {
        super(entityEngine);

        add(
                new ConfigComponent(instance.config, id),
                new ZLevelComponent(0),
                new StateComponent(),
                new PhysicsComponent(this, instance.coordinate),
                new NodeComponent(),
                new DirectionComponent(),
                new ColorComponent()
        );

        if (contains(PhysicsComponent.class)) {
            get(PhysicsComponent.class).createBody();
        }
    }

    /**
     * Crea un'entità a partire da una configurazione personalizzata.
     *
     * @param config configurazione dell'entità {@link EntityConfig}
     * @param entityEngine il gestore delle entità {@link EntityEngine}
     */
    public BaseEntity(EntityConfig config, EntityEngine entityEngine) {
        super(entityEngine);

        add(
                new ConfigComponent(config, id),
                new ZLevelComponent(0),
                new StateComponent(),
                new PhysicsComponent(this, config),
                new NodeComponent(),
                new DirectionComponent(),
                new ColorComponent()
        );

        if (contains(PhysicsComponent.class)) {
            get(PhysicsComponent.class).createBody();
        }

        System.out.println(config.id);
    }
}
