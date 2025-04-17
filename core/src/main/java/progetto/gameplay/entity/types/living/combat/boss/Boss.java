 package progetto.gameplay.entity.types.living.combat.boss;

import progetto.gameplay.entity.components.boss.PhaseComponent;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.living.HumanoidInstances;
import progetto.gameplay.entity.types.living.combat.Warrior;
import progetto.gameplay.manager.entity.EntityManager;

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
