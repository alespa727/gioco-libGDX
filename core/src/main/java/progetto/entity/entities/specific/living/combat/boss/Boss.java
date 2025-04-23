package progetto.entity.entities.specific.living.combat.boss;

import progetto.entity.components.specific.combat.PhaseComponent;
import progetto.entity.entities.base.EntityConfig;
import progetto.entity.entities.specific.living.HumanoidInstances;
import progetto.entity.entities.specific.living.combat.Warrior;
import progetto.entity.Engine;

public abstract class Boss extends Warrior {
    public Boss(HumanoidInstances instance, Engine engine) {
        super(instance, engine);
        components.add(new PhaseComponent());
    }

    public Boss(EntityConfig config, Engine engine) {
        super(config, engine);
        components.add(new PhaseComponent());
    }
}
