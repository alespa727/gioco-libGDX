package progetto.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import progetto.core.ResourceManager;
import progetto.entity.EntityEngine;
import progetto.entity.entities.Entity;
import progetto.entity.entities.specific.EntityConfig;
import progetto.entity.entities.specific.living.HumanoidInstances;
import progetto.entity.entities.specific.living.combat.boss.Boss;
import progetto.entity.entities.specific.living.combat.boss.Lich;
import progetto.entity.entities.specific.living.combat.enemy.BaseEnemy;
import progetto.entity.entities.specific.living.combat.enemy.Enemy;
import progetto.entity.entities.specific.living.combat.enemy.EnemyInstance;
import progetto.entity.entities.specific.notliving.Bullet;
import progetto.entity.entities.specific.notliving.Sword;

import java.util.Objects;

public class EntityFactory {

    public static Entity createEntity(String type, EntityConfig config, EntityEngine manager) {
        Objects.requireNonNull(type);
        return switch (type){
            case "Enemy" -> new Enemy(config, manager);
            case "Lich" -> new Lich(config, manager);

            default -> throw new IllegalArgumentException("Tipo di nemico sconosciuto: " + type);
        };
    }

    public static Boss createBoss(String type, HumanoidInstances instance, EntityEngine manager) {
        Objects.requireNonNull(type);
        return switch (type) {
            case null -> null;
            case "Lich" -> new Lich(instance, manager);
            default -> throw new IllegalArgumentException("Tipo di boss sconosciuto: " + type);
        };
    }

    public static BaseEnemy createEnemy(String type, EnemyInstance instance, EntityEngine manager) {
        Objects.requireNonNull(type);
        return switch (type) {
            case "Enemy" -> new Enemy(instance, manager);
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
        config.img = ResourceManager.get().get("entities/Enemy.png", Texture.class);
        return new Bullet(config, target.entityEngine, radius, speed, damage, target);
    }

    public static Bullet createBullet(float x, float y, Vector2 direction, float radius, float speed, float damage, EntityEngine entityEngine, Class<? extends Entity> target) {
        EntityConfig config = new EntityConfig();
        config.nome = "Bullet";
        config.x = x;
        config.y = y;
        config.radius = radius;
        config.descrizione = "fa male";
        config.direzione = direction;
        config.isAlive = true;
        config.img = ResourceManager.get().get("entities/Enemy.png", Texture.class);
        return new Bullet(config, entityEngine, radius, speed, damage, target);
    }

    public static Sword createSword(float x, float y, float hitboxRadius, float drawingRadius, Vector2 direction, float damage, EntityEngine entityEngine, Entity target) {
        EntityConfig config = new EntityConfig();
        config.nome = "Sword";
        config.x = x;
        config.y = y;
        config.img = ResourceManager.get().get("entities/Sword.png", Texture.class);
        config.radius = hitboxRadius;
        config.descrizione = "fa male";
        config.direzione = direction;
        config.isAlive = true;
        return new Sword(config, entityEngine, drawingRadius);
    }
}
