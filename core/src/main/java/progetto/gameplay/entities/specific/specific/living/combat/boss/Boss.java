package progetto.gameplay.entities.specific.specific.living.combat.boss;

import progetto.gameplay.entities.components.specific.combat.PhaseComponent;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.specific.living.HumanoidInstances;
import progetto.gameplay.entities.specific.specific.living.combat.Warrior;
import progetto.manager.entities.Engine;

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
