 package progetto.entity.specific.specific.living.combat.boss;

import progetto.entity.components.specific.boss.PhaseComponent;
import progetto.entity.specific.base.EntityConfig;
import progetto.entity.specific.specific.living.HumanoidInstances;
import progetto.entity.specific.specific.living.combat.Warrior;
import progetto.rendering.entity.EntityManager;

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
