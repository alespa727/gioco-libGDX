package progetto.factories;

import progetto.gameplay.entity.types.living.combat.enemy.Enemy;
import progetto.gameplay.entity.types.living.combat.enemy.EnemyInstance;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.living.combat.enemy.Finn;
import progetto.gameplay.manager.ManagerEntity;

public class EntityFactory {
    public static Enemy createEnemy(String type, EntityConfig config, ManagerEntity manager, float attackCooldown) {
        return switch (type) {
            case "Finn" -> new Finn(config, manager, attackCooldown);
            case null -> null;
            default -> throw new IllegalArgumentException("Tipo di nemico sconosciuto: " + type);
        };
    }
    public static Enemy createEnemy(String type, EnemyInstance instance, ManagerEntity manager, float attackCooldown) {
        return switch (type) {
            case "Finn" -> new Finn(instance, manager);
            case null -> null;
            default -> throw new IllegalArgumentException("Tipo di nemico sconosciuto: " + type);
        };
    }
}
