 package progetto.gameplay.entity.types.living.combat.boss;

import progetto.gameplay.entity.components.boss.PhaseComponent;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.living.HumanoidInstances;
import progetto.gameplay.entity.types.living.combat.Warrior;
import progetto.gameplay.manager.ManagerEntity;

 public abstract class Boss extends Warrior {
    public Boss(HumanoidInstances instance, ManagerEntity managerEntity) {
        super(instance, managerEntity);
        addComponent(new PhaseComponent(0));
    }

    public Boss(EntityConfig config, ManagerEntity managerEntity) {
        super(config, managerEntity);
        addComponent(new PhaseComponent(0));
    }
}
