package io.github.ale.screens.game.entities.factories;

import io.github.ale.screens.game.entities.types.enemy.Enemy;
import io.github.ale.screens.game.entities.types.entity.EntityConfig;
import io.github.ale.screens.game.entities.types.umani.Finn;
import io.github.ale.screens.game.manager.entity.EntityManager;

public class EnemyFactory {
    public static Enemy createEnemy(String type, EntityConfig config, EntityManager manager, float attackCooldown) {
        return switch (type) {
            case "Finn" -> new Finn(config, manager, attackCooldown);
            case null -> null;
            default -> throw new IllegalArgumentException("Tipo di nemico sconosciuto: " + type);
        };
    }
}
