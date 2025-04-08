package progetto.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import progetto.Core;
import progetto.gameplay.entity.types.EntityConfig;

public class EntityConfigFactory {
    public static EntityConfig createEntityConfig(String type, float x, float y) {
        return switch (type) {
            case "Finn" -> finnConfig(x, y);
            case "Lich" -> lichConfig(x, y);
            default -> null;
        };
    }

    private static EntityConfig finnConfig(float x, float y) {
        EntityConfig config = new EntityConfig();
        config = new EntityConfig();
        config.nome = "Finn";
        config.descrizione = "Nemico pericoloso";
        config.x = x;
        config.y = y;
        config.img = Core.assetManager.get("entities/nemico.png", Texture.class);
        config.width = 25 / 32f;
        config.offsetX = 0;
        config.offsetY = -0.25f;
        config.direzione = new Vector2(0, -0.5f);
        config.isAlive = true;
        config.inCollisione = false;
        config.isMoving = false;
        config.hp = 200;
        config.speed = 1.25f;
        config.attackdmg = 20;
        config.imageHeight = 5f;
        config.imageWidth = 5f;
        return config;
    }

    public static EntityConfig lichConfig(float x, float y) {
        EntityConfig lich = new EntityConfig();
        lich.nome = "Finn";
        lich.descrizione = "Nemico pericoloso";
        lich.x = x;
        lich.y = y;
        lich.img = Core.assetManager.get("entities/nemico.png", Texture.class);
        lich.width = 25 / 32f;
        lich.offsetX = 0;
        lich.offsetY = -0.25f;
        lich.direzione = new Vector2(0, -0.5f);
        lich.isAlive = true;
        lich.inCollisione = false;
        lich.isMoving = false;
        lich.hp = 200;
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
        player.img = Core.assetManager.get("entities/Finn.png", Texture.class);
        player.width = 10 / 32f;
        player.offsetX = 0;
        player.offsetY = -0.25f;
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
