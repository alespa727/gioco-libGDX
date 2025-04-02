package io.github.ale.screens.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import io.github.ale.screens.game.manager.entity.EntityManager;
import io.github.ale.screens.game.manager.map.MapManager;
import io.github.ale.screens.game.map.events.ChangeMapEvent;
import io.github.ale.screens.game.map.events.EventListener;
import io.github.ale.screens.game.map.events.MapEvent;
import io.github.ale.screens.game.map.graph.GameGraph;

public class Map implements Disposable {
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

        TiledMap map = new TmxMapLoader().load("maps/".concat(name).concat(".tmx")); // Carico il file dalla memoria
        mapRenderer = new OrthogonalTiledMapRenderer(map, MapManager.TILE_SIZE); // Inizializzazione map renderer
        mapManager.setInChangeMapEvent(false); // Evento cambio mappa disattivato

        events = new Array<>(); // Array di eventi

        this.mapManager = mapManager;
        this.entityManager = manager;

        this.collisionLayer = (TiledMapTileLayer) map.getLayers().get("collisioni"); // Layer collisioni
        this.eventLayer = map.getLayers().get("eventi"); // Layer eventi

        Gdx.app.postRunnable(() -> entityManager.player().teleport(new Vector2(x, y))); // Teletrasporto player al punto di spawn definito

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
     * Disegna le collisioni
     */
    public void debugDraw(ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (collisions[i][j]) {
                    renderer.setColor(Color.DARK_GRAY);
                    renderer.rect(i, j, 1, 1);
                }
            }
        }
        renderer.end();
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
                    BodyDef bodyDef = new BodyDef();
                    bodyDef.type = BodyDef.BodyType.StaticBody; // Corpo statico, non movibile
                    bodyDef.position.set(i + 0.5f, j + 0.5f);

                    // Creazione del blocco
                    Body body = entityManager.world.createBody(bodyDef);
                    body.setUserData("map");

                    // Definizione della forma
                    PolygonShape boxShape = new PolygonShape();
                    boxShape.setAsBox(0.5f, 0.5f);

                    // Definizione delle proprietà fisiche
                    FixtureDef fixtureDef = new FixtureDef();
                    fixtureDef.shape = boxShape;
                    fixtureDef.density = 1f;
                    fixtureDef.friction = 0f;
                    fixtureDef.restitution = 0f; // Rimbalzo del corpo
                    fixtureDef.isSensor = false;

                    fixtureDef.filter.groupIndex = EntityManager.WALL;


                    // Collegamento delle proprietà fisiche al corpo
                    body.createFixture(fixtureDef);

                    // Pulizia della memoria
                    boxShape.dispose();
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
        ChainShape chainShape = new ChainShape();
        chainShape.createChain(bordi);

        // Definizione del corpo
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);

        // Definizione delle caratteristiche fisiche
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = chainShape;

        // Creazione del corpo
        Body body = entityManager.world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData("map");

        // Cancello la forma dalla memoria
        chainShape.dispose();

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
                events.add(new ChangeMapEvent(new Vector2(x, y), radius, entityManager.world, this.mapManager, map, spawnx, spawny));
                System.out.println("evento aggiunto");
            }

        }
        EventListener listener = new EventListener();

        this.entityManager.world.setContactListener(listener);
    }

    @Override
    public void dispose() {
        Array<Body> bodies = new Array<>();
        entityManager.world.getBodies(bodies);
        for (Body body : bodies) {
            if ("map".equals(body.getUserData()) || body.getUserData() instanceof MapEvent) {
                entityManager.world.destroyBody(body);
            }
        }
    }
}
