package progetto.world.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import progetto.core.game.GameScreen;
import progetto.ECS.EntityEngine;
import progetto.world.events.base.MapEvent;
import progetto.world.events.specific.*;
import progetto.world.map.Map;
import progetto.world.map.MapManager;

public class EventManager {

    private final GameScreen game;
    private final MapManager mapManager;
    private final EntityEngine entityEngine;
    private final Array<MapEvent> events = new Array<>();
    private final Map map;

    public EventManager(GameScreen game, Map map, MapManager mapManager, EntityEngine entityEngine) {
        this.game = game;
        this.map = map;
        this.mapManager = mapManager;
        this.entityEngine = entityEngine;
    }

    public void add(MapEvent event) {
        events.add(event);
    }

    public void update(float delta) {
        for (MapEvent event : events) {
            event.update(delta);
        }
    }

    public void create(MapLayer layer) {
        for (MapObject object : layer.getObjects()) {
            spawnEvent(object);
        }
    }

    public void destroy() {
        for (MapEvent event : events) {
            event.destroy();
        }
    }

    private void spawnEvent(MapObject object) {
        String eventType = getProperty(object, "eventType", String.class, "");
        Vector2 position = getPosition(object);
        switch (eventType) {
            case "Damage" -> {
                float width = getProperty(object, "width", Float.class, 0f) * MapManager.TILE_SIZE;
                float height = getProperty(object, "height", Float.class, 0f) * MapManager.TILE_SIZE;
                events.add(new DamageEvent(position, width, height));
            }
            case "ChangeMap" -> {
                float radius = getProperty(object, "eventRadius", Float.class, 0f);
                int mapId = getProperty(object, "map", Integer.class, 0);
                float spawnX = getProperty(object, "spawnx", Float.class, 0f);
                float spawnY = getProperty(object, "spawny", Float.class, 0f);
                events.add(new ChangeMap(position, radius, mapManager, mapId, spawnX, spawnY));
            }
            case "SpawnCasa" -> {
                events.add(new SpawnCasa(position, 0, entityEngine));
            }
            case "Fall" -> {
                float width = getProperty(object, "width", Float.class, 0f) * MapManager.TILE_SIZE;
                float height = getProperty(object, "height", Float.class, 0f) * MapManager.TILE_SIZE;
                events.add(new FallEvent(position, width, height));
            }
            case "SpawnEntity" -> {
                FileHandle file = Gdx.files.local("/save/maps/" + map.nome + ".json");
                if (!file.exists()) {
                    String entityType = getProperty(object, "entityType", String.class, "");
                    events.add(new SpawnEntity(entityEngine, entityType, position, 0));
                }
            }
            case "Light" -> {
                Color color = getProperty(object, "color", Color.class, Color.WHITE);
                float intensity = getProperty(object, "intensity", Float.class, 0f);
                events.add(new SpawnLight(game, position, 0.3f, intensity, color));
            }
            case "Dialog" -> {
                String text = getProperty(object, "text", String.class, "");
                float width = getProperty(object, "width", Float.class, 0f) * MapManager.TILE_SIZE;
                float height = getProperty(object, "height", Float.class, 0f) * MapManager.TILE_SIZE;
                float delay = getProperty(object, "delay", Float.class, 0f);
                float duration = getProperty(object, "duration", Float.class, 0f);
                events.add(new Dialog(position, width, height, text, delay, duration, game));
            }
            case "Message" -> {
                String text = getProperty(object, "text", String.class, "");
                float width = getProperty(object, "width", Float.class, 0f) * MapManager.TILE_SIZE;
                float height = getProperty(object, "height", Float.class, 0f) * MapManager.TILE_SIZE;
                float delay = getProperty(object, "delay", Float.class, 0f);
                float duration = getProperty(object, "duration", Float.class, 0f);
                events.add(new Message(position, width, height, text, delay, duration, game));
            }
            case "DeathChangeMap" -> {
                float radius = getProperty(object, "eventRadius", Float.class, 0f);
                int mapId = getProperty(object, "map", Integer.class, 0);
                float spawnX = getProperty(object, "spawnx", Float.class, 0f);
                float spawnY = getProperty(object, "spawny", Float.class, 0f);
                events.add(new DeathChangeMap(position, radius, mapManager, mapId, spawnX, spawnY, game));
            }
            default -> {
                System.err.println("Unknown event type: " + eventType);
            }
        }

    }

    private Vector2 getPosition(MapObject object) {
        float x = getProperty(object, "x", Float.class, 0f) * MapManager.TILE_SIZE;
        float y = getProperty(object, "y", Float.class, 0f) * MapManager.TILE_SIZE;
        return new Vector2(x, y);
    }

    private <T> T getProperty(MapObject object, String key, Class<T> type, T defaultValue) {
        T value = object.getProperties().get(key, type);
        return value != null ? value : defaultValue;
    }
}