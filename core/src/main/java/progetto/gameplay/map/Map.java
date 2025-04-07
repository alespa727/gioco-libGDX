package progetto.gameplay.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import progetto.gameplay.manager.camera.CameraManager;
import progetto.gameplay.manager.map.WorldManager;
import progetto.gameplay.manager.entity.EntityManager;
import progetto.gameplay.manager.map.MapManager;
import progetto.gameplay.map.events.HealthEvent;
import progetto.gameplay.map.events.ChangeMapEvent;
import progetto.gameplay.map.events.EventListener;
import progetto.gameplay.map.events.MapEvent;
import progetto.gameplay.map.graph.GameGraph;
import progetto.utils.BodyBuilder;

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
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final EntityManager entityManager;
    private final MapManager mapManager;

    private final Array<MapEvent> events;

    /* Creazione nuova mappa */
    public Map(String name, EntityManager manager, MapManager mapManager, float x, float y) {

        this.nome = name;

        TiledMap map = new TmxMapLoader().load("maps/".concat(name).concat(".tmx")); // Carico il file dalla memoria
        mapRenderer = new OrthogonalTiledMapRenderer(map, MapManager.TILE_SIZE); // Inizializzazione map renderer

        events = new Array<>(); // Array di eventi

        this.mapManager = mapManager;
        this.entityManager = manager;

        this.collisionLayer = (TiledMapTileLayer) map.getLayers().get("collisioni"); // Layer collisioni
        this.eventLayer = map.getLayers().get("eventi"); // Layer eventi

        Gdx.app.postRunnable(() -> entityManager.player().teleport(new Vector2(x, y))); // Teletrasporto player al punto di spawn definito
        CameraManager.getInstance().position.set(entityManager.player().getPosition(), 0);
        CameraManager.getInstance().update();

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
                if (tile != null && tile.getTile() != null && tile.getTile().getProperties().containsKey("solido")) {
                    collisions[i][j] = (Boolean) tile.getTile().getProperties().get("solido");
                }
            }
        }
    }

    /**
     * Crea i corpi delle collisioni
     */
    public Map createCollision() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (collisions[i][j]) {
                    // Definizione del blocco
                    BodyDef bodyDef = BodyBuilder.createBodyDef(BodyDef.BodyType.StaticBody, i+0.5f, j+0.5f);

                    // Definizione della forma
                    Shape boxShape = BodyBuilder.createPolygonShape(0.5f, 0.5f);

                    // Definizione delle proprietà fisiche
                    FixtureDef fixtureDef = BodyBuilder.createFixtureDef(boxShape, 1f, 0, 0);

                    // Creazione del blocco
                    BodyBuilder.createBody("map", bodyDef, fixtureDef, boxShape);

                    fixtureDef.filter.groupIndex = EntityManager.WALL;
                }
            }
        }
        createBorders();
        return this;
    }

    /**
     * Crea bordi della mappa
     */
    public void createBorders() {

        Vector2[] bordi = new Vector2[5];

        bordi[0] = new Vector2(4, 4);
        bordi[1] = new Vector2(width - 4, 4);
        bordi[2] = new Vector2(width - 4, height - 4);
        bordi[3] = new Vector2(4, height - 4);
        bordi[4] = new Vector2(4, 4);

        // Definizione della forma
        Shape chainShape = BodyBuilder.createChainShape(bordi);

        // Definizione del corpo
        BodyDef bodyDef = BodyBuilder.createBodyDef(BodyDef.BodyType.StaticBody, 0, 0);

        // Definizione delle caratteristiche fisiche
        FixtureDef fixtureDef = BodyBuilder.createFixtureDef(chainShape, 1f, 0, 0);

        // Creazione del corpo
        BodyBuilder.createBody("map", bodyDef, fixtureDef, chainShape);
    }

    /**
     * Crea gli eventi caricati dalla mappa
     */
    public void createEvents() {
        for (MapObject object : eventLayer.getObjects()) {

            String eventType = (String) object.getProperties().get("eventType", String.class);
            float x = object.getProperties().get("x", Float.class) * MapManager.TILE_SIZE;
            float y = object.getProperties().get("y", Float.class) * MapManager.TILE_SIZE;
            float radius = object.getProperties().get("eventRadius", Float.class);
            int map = object.getProperties().get("map", Integer.class);
            float spawnx = object.getProperties().get("spawnx", Float.class);
            float spawny = object.getProperties().get("spawny", Float.class);
            if ("changeMap".equals(eventType)) {
                events.add(new ChangeMapEvent(new Vector2(x, y), radius, this.mapManager, map, spawnx, spawny));
                System.out.println("evento aggiunto");
            }

        }
        EventListener listener = new EventListener();

        WorldManager.getInstance().setContactListener(listener);
    }

    @Override
    public void dispose() {
        WorldManager.clearMap();
    }
}
