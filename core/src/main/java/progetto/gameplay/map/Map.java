package progetto.gameplay.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import progetto.gameplay.manager.ManagerCamera;
import progetto.gameplay.manager.ManagerWorld;
import progetto.gameplay.manager.entity.ManagerEntity;
import progetto.gameplay.map.events.ChangeMapEvent;
import progetto.gameplay.manager.ManagerEvent;
import progetto.gameplay.map.events.MapEvent;
import progetto.gameplay.map.graph.GameGraph;
import progetto.factories.BodyFactory;

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
    private final ManagerEntity managerEntity;
    private final MapManager mapManager;

    private final Array<MapEvent> events;

    /* Creazione nuova mappa */
    public Map(String name, ManagerEntity manager, MapManager mapManager, float x, float y) {

        this.nome = name;

        TiledMap map = new TmxMapLoader().load("maps/".concat(name).concat(".tmx")); // Carico il file dalla memoria
        mapRenderer = new OrthogonalTiledMapRenderer(map, MapManager.TILE_SIZE); // Inizializzazione map renderer

        events = new Array<>(); // Array di eventi

        this.mapManager = mapManager;
        this.managerEntity = manager;

        this.collisionLayer = (TiledMapTileLayer) map.getLayers().get("collisioni"); // Layer collisioni
        this.eventLayer = map.getLayers().get("eventi"); // Layer eventi

        Gdx.app.postRunnable(() -> managerEntity.player().teleport(new Vector2(x, y))); // Teletrasporto player al punto di spawn definito
        ManagerCamera.getInstance().position.set(managerEntity.player().getPosition(), 0);
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
                if (tile != null && tile.getTile() != null && tile.getTile().getProperties().containsKey("solido")) {
                    collisions[i][j] = (Boolean) tile.getTile().getProperties().get("solido");
                }
            }
        }
    }

    public Map createCollision() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (collisions[i][j]) {
                    final int x = i;
                    final int y = j;


                    // Crea oggetti nuovi per ogni Runnable
                    BodyDef bodyDef = BodyFactory.createBodyDef(BodyDef.BodyType.StaticBody, x + 0.5f, y + 0.5f);
                    Shape boxShape = BodyFactory.createPolygonShape(0.5f, 0.5f);

                    FixtureDef fixtureDef = BodyFactory.createFixtureDef(boxShape, 1f, 0.1f, 0.1f);
                    fixtureDef.filter.groupIndex = ManagerEntity.WALL;

                    BodyFactory.createBody("map", bodyDef, fixtureDef, boxShape);

                }
            }
        }
        //createBorders();
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
        Shape chainShape = BodyFactory.createChainShape(bordi);

        // Definizione del corpo
        BodyDef bodyDef = BodyFactory.createBodyDef(BodyDef.BodyType.StaticBody, 0, 0);

        // Definizione delle caratteristiche fisiche
        FixtureDef fixtureDef = BodyFactory.createFixtureDef(chainShape, 1f, 0, 0);

        // Creazione del corpo
        Gdx.app.postRunnable(()->BodyFactory.createBody("map", bodyDef, fixtureDef, chainShape));
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
    }

    @Override
    public void dispose() {
        ManagerWorld.clearMap();
    }
}
