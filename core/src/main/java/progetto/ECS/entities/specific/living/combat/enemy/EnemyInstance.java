package progetto.ECS.entities.specific.living.combat.enemy;

import progetto.ECS.entities.specific.living.HumanoidInstances;

public class EnemyInstance extends HumanoidInstances {

    // === COSTRUTTORE ===
    public EnemyInstance(BaseEnemy e) {
        super(e);
    }

    public EnemyInstance() {
        super();
    }
}
