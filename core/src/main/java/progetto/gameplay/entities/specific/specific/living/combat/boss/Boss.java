 package progetto.gameplay.entities.specific.specific.living.combat.boss;

import progetto.gameplay.entities.components.specific.boss.PhaseComponent;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.specific.living.HumanoidInstances;
import progetto.gameplay.entities.specific.specific.living.combat.Warrior;
import progetto.manager.entities.EntityManager;

 public abstract class Boss extends Warrior {
    public Boss(HumanoidInstances instance, EntityManager entityManager) {
        super(instance, entityManager);
        addComponent(new PhaseComponent(0));
    }

    public Boss(EntityConfig config, EntityManager entityManager) {
        super(config, entityManager);
        addComponent(new PhaseComponent(0));
    }
}
