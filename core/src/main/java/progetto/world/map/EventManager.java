package progetto.world.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import progetto.entity.Engine;
import progetto.world.events.base.MapEvent;
import progetto.world.events.specific.ChangeMap;
import progetto.world.events.specific.DamageEvent;
import progetto.world.events.specific.FallEvent;
import progetto.world.events.specific.SpawnCasa;

public class EventManager {

    private final MapManager mapManager;
    private final Engine engine;
    private final Array<MapEvent> events = new Array<>();
    private Map map;

    public EventManager(Map map, MapManager mapManager, Engine engine) {
        this.map = map;
        this.mapManager = mapManager;
        this.engine = engine;
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

    private void spawnEvent(MapObject object) {
        String eventType = getProperty(object, "eventType", String.class, "");
        Vector2 position = getPosition(object);

        switch (eventType) {
            case "Damage":
                {
                    float width = getProperty(object, "width", Float.class, 0f) * MapManager.TILE_SIZE;
                    float height = getProperty(object, "height", Float.class, 0f) * MapManager.TILE_SIZE;
                    events.add(new DamageEvent(position, width, height));
                }
                break;

            case "ChangeMap":
                float radius = getProperty(object, "eventRadius", Float.class, 0f);
                int mapId = getProperty(object, "map", Integer.class, 0);
                float spawnX = getProperty(object, "spawnx", Float.class, 0f);
                float spawnY = getProperty(object, "spawny", Float.class, 0f);
                events.add(new ChangeMap(position, radius, mapManager, mapId, spawnX, spawnY));
                break;

            case "SpawnCasa":
                events.add(new SpawnCasa(position, 0, engine));
                break;

            case "Fall":
                {
                    float width = getProperty(object, "width", Float.class, 0f) * MapManager.TILE_SIZE;
                    float height = getProperty(object, "height", Float.class, 0f) * MapManager.TILE_SIZE;
                    events.add(new FallEvent(position, width, height));
                }
                break;
            default:

                break;
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