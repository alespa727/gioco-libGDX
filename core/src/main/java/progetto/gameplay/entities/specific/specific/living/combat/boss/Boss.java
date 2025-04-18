 package progetto.gameplay.entities.specific.specific.living.combat.boss;

import progetto.gameplay.entities.components.specific.boss.PhaseComponent;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.specific.living.HumanoidInstances;
import progetto.gameplay.entities.specific.specific.living.combat.Warrior;
import progetto.manager.entities.Engine;

 public abstract class Boss extends Warrior {
    public Boss(HumanoidInstances instance, Engine engine) {
        super(instance, engine);
        componentManager.add(new PhaseComponent(0));
    }

    public Boss(EntityConfig config, Engine engine) {
        super(config, engine);
        componentManager.add(new PhaseComponent(0));
    }
}
