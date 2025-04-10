 package progetto.gameplay.entity.types.living.combat.boss;

import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.living.HumanoidInstances;
import progetto.gameplay.entity.types.living.combat.Warriors;
import progetto.gameplay.manager.entity.ManagerEntity;

 public abstract class Boss extends Warriors {

    protected int phase=0;

    public Boss(HumanoidInstances instance, ManagerEntity managerEntity) {
        super(instance, managerEntity);
        createRange(1.5f);
    }

    public Boss(EntityConfig config, ManagerEntity managerEntity) {
        super(config, managerEntity);
        createRange(1.5f);
    }
}
