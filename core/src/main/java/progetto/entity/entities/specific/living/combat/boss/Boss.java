package progetto.entity.entities.specific.living.combat.boss;

import progetto.entity.EntityEngine;
import progetto.entity.components.specific.combat.PhaseComponent;
import progetto.entity.components.specific.general.Saveable;
import progetto.entity.entities.specific.EntityConfig;
import progetto.entity.entities.specific.living.HumanoidInstances;
import progetto.entity.entities.specific.living.combat.Warrior;

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
