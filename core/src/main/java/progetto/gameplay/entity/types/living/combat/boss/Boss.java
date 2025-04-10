 package progetto.gameplay.entity.types.living.combat.boss;

import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.living.HumanoidInstances;
import progetto.gameplay.entity.types.living.combat.Warrior;
import progetto.gameplay.manager.entity.ManagerEntity;

 public abstract class Boss extends Warrior {

    protected int phase=0;

    public Boss(HumanoidInstances instance, ManagerEntity managerEntity) {
        super(instance, managerEntity);
    }

    public Boss(EntityConfig config, ManagerEntity managerEntity) {
        super(config, managerEntity);
    }
}
