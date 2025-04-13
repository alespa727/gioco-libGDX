package progetto.gameplay.entity.types.living.combat.boss;

import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.living.HumanoidInstances;
import progetto.gameplay.manager.ManagerEntity;

public class BossInstance extends HumanoidInstances {
    public BossInstance(Boss e) {
        super(e);
    }

    public BossInstance(){
        super();
    }
}
