package io.github.ale.screens.game.entities.entityTypes.factories;

import io.github.ale.screens.game.entities.entityTypes.umani.Finn;
import io.github.ale.screens.game.manager.entity.EntityManager;
import io.github.ale.screens.game.entities.entityTypes.enemy.Enemy;
import io.github.ale.screens.game.entities.entityTypes.entity.EntityConfig;

public class EnemyFactory {
    public static Enemy createEnemy(String type, EntityConfig config, EntityManager manager, float attackCooldown) {
        return switch (type) {
            case "Finn" -> new Finn(config, manager, attackCooldown);
            case null -> null;
            default -> throw new IllegalArgumentException("Tipo di nemico sconosciuto: " + type);
        };
    }
}
