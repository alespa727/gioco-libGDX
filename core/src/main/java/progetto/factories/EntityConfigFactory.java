package progetto.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import progetto.core.ResourceManager;
import progetto.ECS.entities.specific.EntityConfig;

public class EntityConfigFactory {
    public static EntityConfig createEntityConfig(String type, float x, float y) {
        return switch (type) {
            case "Enemy" -> enemy(x, y);
            case "Lich" -> lich(x, y);
            default -> null;
        };
    }

    private static EntityConfig swordConfig(float x, float y) {
        EntityConfig config = new EntityConfig();
        config = new EntityConfig();
        config.nome = "Spada";
        config.descrizione = "Spada tagliente";
        config.x = x;
        config.y = y;
        config.img = ResourceManager.get().get("entities/Enemy/attack/sword.png", Texture.class);
        config.radius = 10 / 32f;
        config.direzione = new Vector2(0, -0.5f);
        config.isAlive = true;
        config.inCollisione = false;
        config.isMoving = false;
        config.hp = 100;
        config.speed = 1.25f;
        config.attackdmg = 20;
        config.imageHeight = 2f;
        config.imageWidth = 2f;
        return config;
    }

    private static EntityConfig enemy(float x, float y) {
        EntityConfig config = new EntityConfig();
        config = new EntityConfig();
        config.nome = "Enemy";
        config.descrizione = "Nemico pericoloso";
        config.x = x;
        config.y = y;
        config.img = ResourceManager.get().get("entities/Enemy.png", Texture.class);
        config.radius = 10 / 32f;
        config.direzione = new Vector2(0, -0.5f);
        config.isAlive = true;
        config.inCollisione = false;
        config.isMoving = false;
        config.hp = 100;
        config.speed = 1.25f;
        config.attackdmg = 20;
        config.imageHeight = 2f;
        config.imageWidth = 2f;
        return config;
    }

    public static EntityConfig lich(float x, float y) {
        EntityConfig lich = new EntityConfig();
        lich.nome = "Enemy";
        lich.descrizione = "Nemico pericoloso";
        lich.x = x;
        lich.y = y;
        lich.img = ResourceManager.get().get("entities/Lich.png", Texture.class);
        lich.radius = 25 / 32f;
        lich.direzione = new Vector2(0, -0.5f);
        lich.isAlive = true;
        lich.inCollisione = false;
        lich.isMoving = false;
        lich.hp = 400;
        lich.speed = 1.25f;
        lich.attackdmg = 20;
        lich.imageHeight = 5f;
        lich.imageWidth = 5f;
        return lich;
    }

    public static EntityConfig createPlayerConfig() {
        EntityConfig player = new EntityConfig();
        player.id = 0;
        player.nome = "player";
        player.img = ResourceManager.get().get("entities/Player.png", Texture.class);
        player.radius = 10 / 32f;
        player.direzione = new Vector2(0, -0.5f);
        player.isAlive = true;
        player.inCollisione = false;
        player.isMoving = false;
        player.hp = 100;
        player.speed = 2.5f;
        player.attackdmg = 10;
        player.imageHeight = 2f;
        player.imageWidth = 2f;
        return player;
    }
}
