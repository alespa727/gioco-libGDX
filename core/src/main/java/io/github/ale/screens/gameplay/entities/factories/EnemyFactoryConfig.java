package io.github.ale.screens.gameplay.entities.factories;

import io.github.ale.screens.gameplay.entities.types.entity.EntityConfig;
import io.github.ale.screens.gameplay.manager.entity.EntityManager;

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
