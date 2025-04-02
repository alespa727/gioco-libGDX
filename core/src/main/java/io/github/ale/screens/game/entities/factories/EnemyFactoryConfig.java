package io.github.ale.screens.game.entities.factories;

import io.github.ale.screens.game.entities.types.entity.EntityConfig;
import io.github.ale.screens.game.manager.entity.EntityManager;

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
