package progetto.screens.gameplay.entities.factories;

import progetto.screens.gameplay.entities.types.enemy.Enemy;
import progetto.screens.gameplay.entities.types.entity.EntityConfig;
import progetto.screens.gameplay.entities.types.umani.Finn;
import progetto.screens.gameplay.manager.entity.EntityManager;

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
