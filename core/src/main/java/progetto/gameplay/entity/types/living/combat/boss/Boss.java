 package progetto.gameplay.entity.types.living.combat.boss;

import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.living.HumanoidInstances;
import progetto.gameplay.entity.types.living.combat.Warriors;
import progetto.gameplay.entity.behaviors.EntityManager;

 public abstract class Boss extends Warriors {

    protected int phase=0;

    public Boss(HumanoidInstances instance, EntityManager entityManager) {
        super(instance, entityManager);
        createRange(1.5f);
    }

    public Boss(EntityConfig config, EntityManager entityManager) {
        super(config, entityManager);
        createRange(1.5f);
    }
}
