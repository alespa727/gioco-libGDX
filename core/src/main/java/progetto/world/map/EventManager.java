package progetto.world.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import progetto.entity.Engine;
import progetto.world.events.base.MapEvent;
import progetto.world.events.specific.ChangeMap;
import progetto.world.events.specific.SpawnCasa;

public class EventManager {
    private Map map;
    private final MapManager mapManager;
    private final Engine engine;
    private final Array<MapEvent> events;

    public EventManager(Map map, MapManager mapManager, Engine engine) {
        events = new Array<>();
        this.map = map;
        this.engine = engine;
        this.mapManager = mapManager;
    }

    public void add(MapEvent event) {
        events.add(event);
    }

    public void update(){

        for (int i = 0; i < events.size; i++) {
            MapEvent event = events.get(i);
            event.update();
        }

    }

    public void create(MapLayer layer) {

        for (MapObject object : layer.getObjects()) {
            spawnEvent(object);
        }

    }

    public void spawnEvent(MapObject object) {
        String eventType = object.getProperties().get("eventType", String.class);
        float x = object.getProperties().get("x", Float.class) * MapManager.TILE_SIZE;
        float y = object.getProperties().get("y", Float.class) * MapManager.TILE_SIZE;
        float radius = object.getProperties().get("eventRadius", Float.class);
        if ("ChangeMap".equals(eventType)) {
            int map = object.getProperties().get("map", Integer.class);
            float spawnx = object.getProperties().get("spawnx", Float.class);
            float spawny = object.getProperties().get("spawny", Float.class);
            events.add(new ChangeMap(new Vector2(x, y), radius, this.mapManager, map, spawnx, spawny));
        }
        if("SpawnCasa".equals(eventType)){
            System.out.println("SpawnCasa " + x + "  " + y);
            events.add(new SpawnCasa(new Vector2(x, y), 0, engine));
        }
    }
}
