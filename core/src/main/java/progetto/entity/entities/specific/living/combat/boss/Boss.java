package progetto.entity.entities.specific.living.combat.boss;

import progetto.entity.Engine;
import progetto.entity.components.specific.combat.PhaseComponent;
import progetto.entity.components.specific.general.Saveable;
import progetto.entity.entities.specific.EntityConfig;
import progetto.entity.entities.specific.living.HumanoidInstances;
import progetto.entity.entities.specific.living.combat.Warrior;

public abstract class Boss extends Warrior {
    public Boss(HumanoidInstances instance, Engine engine) {
        super(instance, engine);
        add(new PhaseComponent());
        add(new Saveable());
    }

    public Boss(EntityConfig config, Engine engine) {
        super(config, engine);
        add(new PhaseComponent());
        add(new Saveable());
    }
}
