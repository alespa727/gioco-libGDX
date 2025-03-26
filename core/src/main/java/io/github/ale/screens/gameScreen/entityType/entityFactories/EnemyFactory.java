package io.github.ale.screens.gameScreen.entityType.entityFactories;

import io.github.ale.screens.gameScreen.entities.enemy.umani.Finn;
import io.github.ale.screens.gameScreen.entityType.EntityManager;
import io.github.ale.screens.gameScreen.entityType.abstractEnemy.Enemy;
import io.github.ale.screens.gameScreen.entityType.abstractEntity.EntityConfig;

public class EnemyFactory {
    public static Enemy createEnemy(String type, EntityConfig config, EntityManager manager, float attackCooldown) {
        return switch (type) {
            case "Finn" -> new Finn(config, manager, attackCooldown);
            case null -> null;
            default -> throw new IllegalArgumentException("Tipo di nemico sconosciuto: " + type);
        };
    }
}
