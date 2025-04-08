 package progetto.gameplay.entity.types.humanEntity.combatEntity.boss;

import progetto.gameplay.entity.types.abstractEntity.EntityInstance;
import progetto.gameplay.entity.types.humanEntity.HumanInstance;
import progetto.gameplay.entity.types.humanEntity.combatEntity.CombatEntity;
import progetto.gameplay.manager.entity.EntityManager;

public abstract class Boss extends CombatEntity {
    public Boss(HumanInstance instance, EntityManager entityManager) {
        super(instance, entityManager);
    }

    @Override
    public void attack() {

    }

    @Override
    public void cooldown(float delta) {

    }

    @Override
    public void updateEntityType(float delta) {

    }

    @Override
    public void create() {

    }

    @Override
    public EntityInstance despawn() {
        return null;
    }
}
