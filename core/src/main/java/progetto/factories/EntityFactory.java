package progetto.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import progetto.core.Core;
import progetto.entity.Engine;
import progetto.entity.entities.base.Entity;
import progetto.entity.entities.base.EntityConfig;
import progetto.entity.entities.specific.living.HumanoidInstances;
import progetto.entity.entities.specific.living.combat.boss.Boss;
import progetto.entity.entities.specific.living.combat.boss.Lich;
import progetto.entity.entities.specific.living.combat.enemy.Enemy;
import progetto.entity.entities.specific.living.combat.enemy.EnemyInstance;
import progetto.entity.entities.specific.living.combat.enemy.Finn;
import progetto.entity.entities.specific.notliving.Bullet;
import progetto.entity.entities.specific.notliving.Sword;

public class EntityFactory {
    public static Enemy createEnemy(String type, EntityConfig config, Engine manager) {
        return switch (type) {
            case "Finn" -> new Finn(config, manager);
            case null -> null;
            default -> throw new IllegalArgumentException("Tipo di nemico sconosciuto: " + type);
        };
    }

    public static Boss createBoss(String type, EntityConfig config, Engine manager) {
        return switch (type) {
            case null -> null;
            case "Lich" -> new Lich(config, manager);
            default -> throw new IllegalArgumentException("Tipo di boss sconosciuto: " + type);
        };
    }

    public static Boss createBoss(String type, HumanoidInstances instance, Engine manager) {
        return switch (type) {
            case null -> null;
            case "Lich" -> new Lich(instance, manager);
            default -> throw new IllegalArgumentException("Tipo di boss sconosciuto: " + type);
        };
    }

    public static Enemy createEnemy(String type, EnemyInstance instance, Engine manager, float attackCooldown) {
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
        return new Bullet(config, target.engine, radius, speed, damage, target);
    }

    public static Bullet createBullet(float x, float y, Vector2 direction, float radius, float speed, float damage, Engine engine, Class<? extends Entity> target) {
        EntityConfig config = new EntityConfig();
        config.nome = "Bullet";
        config.x = x;
        config.y = y;
        config.radius = radius;
        config.descrizione = "fa male";
        config.direzione = direction;
        config.isAlive = true;
        config.img = Core.assetManager.get("entities/Finn.png", Texture.class);
        return new Bullet(config, engine, radius, speed, damage, target);
    }

    public static Sword createSword(float x, float y, float hitboxRadius, float drawingRadius, Vector2 direction, float damage, Engine engine, Entity target) {
        EntityConfig config = new EntityConfig();
        config.nome = "Sword";
        config.x = x;
        config.y = y;
        config.img = Core.assetManager.get("entities/Finn.png", Texture.class);
        config.radius = hitboxRadius;
        config.descrizione = "fa male";
        config.direzione = direction;
        config.isAlive = true;
        return new Sword(config, engine, drawingRadius);
    }
}
