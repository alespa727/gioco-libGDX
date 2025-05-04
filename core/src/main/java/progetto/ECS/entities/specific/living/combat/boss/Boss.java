package progetto.ECS.entities.specific.living.combat.boss;

import progetto.ECS.EntityEngine;
import progetto.ECS.components.specific.combat.PhaseComponent;
import progetto.ECS.components.specific.general.Saveable;
import progetto.ECS.entities.specific.EntityConfig;
import progetto.ECS.entities.specific.living.HumanoidInstances;
import progetto.ECS.entities.specific.living.combat.Warrior;

public abstract class Boss extends Warrior {
    public Boss(HumanoidInstances instance, EntityEngine entityEngine) {
        super(instance, entityEngine);
        add(new PhaseComponent());
        add(new Saveable());
    }

    public Boss(EntityConfig config, EntityEngine entityEngine) {
        super(config, entityEngine);
        add(new PhaseComponent());
        add(new Saveable());
    }
}
