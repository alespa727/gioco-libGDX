package progetto.gameplay.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import progetto.factories.BodyFactory;
import progetto.gameplay.player.ManagerCamera;
import progetto.manager.entities.EntityManager;
import progetto.manager.world.WorldManager;
import progetto.gameplay.world.events.ChangeMapEvent;
import progetto.gameplay.world.events.MapEvent;
import progetto.gameplay.world.graph.GameGraph;
import progetto.manager.world.MapManager;

public class Map implements Disposable {
    public final String nome;

    public static boolean isGraphLoaded = false;
    public static boolean isLoaded = false;

    private static int width;
    private static int height;
    private static boolean[][] collisions;
    private static GameGraph graph;

    private final TiledMapTileLayer collisionLayer;
    private final MapLayer eventLayer;
    private final MapLayer customCollisionLayer;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final EntityManager entityManager;
    private final MapManager mapManager;

    private final Array<MapEvent> events;

    /* Creazione nuova mappa */
    public Map(String name, EntityManager manager, MapManager mapManager, float x, float y) {

        this.nome = name;

        TiledMap map = new TmxMapLoader().load("maps/".concat(name).concat(".tmx")); // Carico il file dalla memoria
        mapRenderer = new OrthogonalTiledMapRenderer(map, MapManager.TILE_SIZE, manager.info.core.batch); // Inizializzazione map renderer

        for (TiledMapTileSet tileset : map.getTileSets()) {
            for (TiledMapTile tile : tileset) {
                Texture texture = tile.getTextureRegion().getTexture();
                texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            }
        }

        events = new Array<>(); // Array di eventi

        this.mapManager = mapManager;
        this.entityManager = manager;

        this.collisionLayer = (TiledMapTileLayer) map.getLayers().get("collisioni"); // Layer collisioni
        this.customCollisionLayer = map.getLayers().get("collisionobjects");

        this.eventLayer = map.getLayers().get("eventi"); // Layer eventi

        Gdx.app.postRunnable(() -> entityManager.player().teleport(new Vector2(x, y))); // Teletrasporto player al punto di spawn definito
        ManagerCamera.getInstance().position.set(entityManager.player().getPosition(), 0);
        ManagerCamera.getInstance().update();

        // Salvataggio grandezza mappa
        width = (Integer) map.getProperties().get("width");
        height = (Integer) map.getProperties().get("height");

        //Inizializzazioni collisioni
        collisions = new boolean[width][height];

        loadCollisionMap(); // Carica la mappa delle collisioni

        // Crea un grafo basatosi sulle collisioni
        graph = new GameGraph(width, height, collisions);

        // Variabili di controllo
        isGraphLoaded = true;
        isLoaded = true;

        // Crezione eventi
        createEvents();
    }

    /**
     * Restituisce il grafo
     */
    public static GameGraph getGraph() {
        return graph;
    }

    /**
     * Restituisce la larghezza della mappa
     */
    public static int width() {
        return width;
    }

    /**
     * Restituisce l'altezza della mappa
     */
    public static int height() {
        return height;
    }

    /**
     * Restituisce l'oggetto che disegna la mappa
     */
    public OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }

    /**
     * Update eventi della mappa
     */
    public void render() {
        for (int i = 0; i < events.size; i++) {
            MapEvent event = events.get(i);
            event.update();
        }
    }

    /**
     * Carica la mappa delle collisioni
     */
    private void loadCollisionMap() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                TiledMapTileLayer.Cell tile = collisionLayer.getCell(i, j);
                if (tile != null && tile.getTile() != null) {
                    collisions[i][j] = true;
                }
            }
        }
    }

    public void createCollision() {
        MapObjects objects = customCollisionLayer.getObjects();
        for(MapObject object : objects) {
            if (object instanceof RectangleMapObject) {  // Check if it's a rectangle, adjust if necessary
                float x = ((RectangleMapObject) object).getRectangle().x*MapManager.TILE_SIZE;
                float y = ((RectangleMapObject) object).getRectangle().y*MapManager.TILE_SIZE;
                float width = ((RectangleMapObject) object).getRectangle().width*MapManager.TILE_SIZE;
                float height = ((RectangleMapObject) object).getRectangle().height*MapManager.TILE_SIZE;
                BodyDef bodyDef = BodyFactory.createBodyDef(BodyDef.BodyType.StaticBody, x+width/2, y+height/2);
                Shape boxShape = BodyFactory.createPolygonShape(width/2, height/2);

                FixtureDef fixtureDef = BodyFactory.createFixtureDef(boxShape, 1f, 0.1f, 0.1f);
                fixtureDef.filter.groupIndex = EntityManager.WALL;

                BodyFactory.createBody("map", bodyDef, fixtureDef);
            }
        }
    }

    /**
     * Crea gli eventi caricati dalla mappa
     */
    public void createEvents() {
        for (MapObject object : eventLayer.getObjects()) {
            String eventType = object.getProperties().get("eventType", String.class);
            float x = object.getProperties().get("x", Float.class) * MapManager.TILE_SIZE;
            float y = object.getProperties().get("y", Float.class) * MapManager.TILE_SIZE;
            float radius = object.getProperties().get("eventRadius", Float.class);
            int map = object.getProperties().get("map", Integer.class);
            float spawnx = object.getProperties().get("spawnx", Float.class);
            float spawny = object.getProperties().get("spawny", Float.class);
            if ("changeMap".equals(eventType)) {
                events.add(new ChangeMapEvent(new Vector2(x, y), radius, this.mapManager, map, spawnx, spawny));
            }
        }
    }

    @Override
    public void dispose() {
        WorldManager.clearMap();
    }
}
