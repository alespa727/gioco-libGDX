package progetto.screens.gameplay.entities.factories;

import progetto.screens.gameplay.entities.types.entity.EntityConfig;
import progetto.screens.gameplay.manager.entity.EntityManager;

public class EnemyFactoryConfig{

    public final String type;
    public final EntityConfig entityConfig;
    public final EntityManager entityManager;
    public final float attackCooldown;

    public EnemyFactoryConfig(String name, EntityConfig config, EntityManager entityManager, float attackCooldown) {
        type = name;
        entityConfig = config;
        this.entityManager = entityManager;
        this.attackCooldown = attackCooldown;
    }


}
