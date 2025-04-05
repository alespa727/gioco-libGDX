package progetto.gameplay.entity.factories;

import progetto.gameplay.entity.types.humanEntity.combatEntity.enemyEntity.Enemy;
import progetto.gameplay.entity.types.humanEntity.combatEntity.enemyEntity.EnemyInstance;
import progetto.gameplay.entity.types.abstractEntity.EntityConfig;
import progetto.gameplay.entity.types.humanEntity.combatEntity.enemyEntity.Finn;
import progetto.gameplay.manager.entity.EntityManager;

public class EntityFactory {
    public static Enemy createEnemy(String type, EntityConfig config, EntityManager manager, float attackCooldown) {
        return switch (type) {
            case "Finn" -> new Finn(config, manager, attackCooldown);
            case null -> null;
            default -> throw new IllegalArgumentException("Tipo di nemico sconosciuto: " + type);
        };
    }

    public static Enemy createEnemy(String type, EnemyInstance instance, EntityManager manager, float attackCooldown) {
        return switch (type) {
            case "Finn" -> new Finn(instance, manager);
            case null -> null;
            default -> throw new IllegalArgumentException("Tipo di nemico sconosciuto: " + type);
        };
    }
}
