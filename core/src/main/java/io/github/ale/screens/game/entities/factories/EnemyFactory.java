package io.github.ale.screens.game.entities.factories;

import com.badlogic.gdx.physics.box2d.Body;
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

    public static Enemy createEnemy(EnemyFactoryConfig factoryConfig) {
        return switch (factoryConfig.type) {
            case "Finn" -> new Finn(factoryConfig.entityConfig, factoryConfig.entityManager, factoryConfig.attackCooldown);
            case null -> null;
            default -> throw new IllegalArgumentException("Tipo di nemico sconosciuto: " + factoryConfig.type);
        };
    }
}
