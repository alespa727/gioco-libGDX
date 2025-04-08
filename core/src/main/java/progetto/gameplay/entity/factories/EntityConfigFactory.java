package progetto.gameplay.entity.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import progetto.Core;
import progetto.gameplay.entity.types.abstractEntity.EntityConfig;
import progetto.gameplay.entity.types.humanEntity.combatEntity.enemyEntity.Finn;

public class EntityConfigFactory {
    public static EntityConfig createEntityConfig(String type, float x, float y) {
        switch (type) {
            case "Finn":
                return finnConfig(x, y);
            default:
                return null;
        }
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
        config.height = 8 / 16f;
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
}
