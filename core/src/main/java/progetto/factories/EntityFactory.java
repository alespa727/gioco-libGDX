package progetto.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import progetto.core.Core;
import progetto.entity.specific.base.Entity;
import progetto.entity.specific.base.EntityConfig;
import progetto.entity.specific.specific.living.HumanoidInstances;
import progetto.entity.specific.specific.living.combat.boss.Boss;
import progetto.entity.specific.specific.living.combat.boss.Lich;
import progetto.entity.specific.specific.living.combat.enemy.Enemy;
import progetto.entity.specific.specific.living.combat.enemy.EnemyInstance;
import progetto.entity.specific.specific.living.combat.enemy.Finn;
import progetto.entity.specific.specific.notliving.Bullet;
import progetto.rendering.entity.EntityManager;

public class EntityFactory {
    public static Enemy createEnemy(String type, EntityConfig config, EntityManager manager, float attackCooldown) {
        return switch (type) {
            case "Finn" -> new Finn(config, manager, attackCooldown);
            case null -> null;
            default -> throw new IllegalArgumentException("Tipo di nemico sconosciuto: " + type);
        };
    }

    public static Boss createBoss(String type, EntityConfig config, EntityManager manager) {
        return switch (type){
            case null -> null;
            case "Lich" -> new Lich(config, manager);
            default -> throw new IllegalArgumentException("Tipo di boss sconosciuto: " + type);
        };
    }

    public static Boss createBoss(String type, HumanoidInstances instance, EntityManager manager) {
        return switch (type){
            case null -> null;
            case "Lich" -> new Lich(instance, manager);
            default -> throw new IllegalArgumentException("Tipo di boss sconosciuto: " + type);
        };
    }

    public static Enemy createEnemy(String type, EnemyInstance instance, EntityManager manager, float attackCooldown) {
        return switch (type) {
            case "Finn" -> new Finn(instance, manager);
            case null -> null;
            default -> throw new IllegalArgumentException("Tipo di nemico sconosciuto: " + type);
        };
    }

    public static Bullet createBullet(float x, float y, Vector2 direction, float radius, float speed, float damage, Entity target) {
        EntityConfig config = new EntityConfig();
        config.nome = "Bullet";
        config.x = x;
        config.y = y;
        config.radius = radius;
        config.descrizione = "fa male";
        config.direzione = direction;
        config.isAlive = true;
        config.img = Core.assetManager.get("entities/Finn.png", Texture.class);
        return new Bullet(config, target.manager, radius, speed, damage, target);
    }

    public static Bullet createBullet(float x, float y, Vector2 direction, float radius, float speed, float damage, EntityManager entityManager, Class<? extends Entity> target) {
        EntityConfig config = new EntityConfig();
        config.nome = "Bullet";
        config.x = x;
        config.y = y;
        config.radius = radius;
        config.descrizione = "fa male";
        config.direzione = direction;
        config.isAlive = true;
        config.img = Core.assetManager.get("entities/Finn.png", Texture.class);
        return new Bullet(config, entityManager, radius, speed, damage, target);
    }
}
