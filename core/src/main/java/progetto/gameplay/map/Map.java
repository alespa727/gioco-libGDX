package progetto.gameplay.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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
import progetto.gameplay.manager.ManagerCamera;
import progetto.gameplay.manager.ManagerEntity;
import progetto.gameplay.manager.ManagerWorld;
import progetto.gameplay.map.events.ChangeMapEvent;
import progetto.gameplay.map.events.MapEvent;
import progetto.gameplay.map.graph.GameGraph;

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
    private final ManagerEntity managerEntity;
    private final MapManager mapManager;

    private final Array<MapEvent> events;

    /* Creazione nuova mappa */
    public Map(String name, ManagerEntity manager, MapManager mapManager, float x, float y) {

        // Setup iniziale
        this.nome = name;
        this.mapManager = mapManager;
        this.managerEntity = manager;
        events = new Array<>(); // Array di eventi

        // Caricamento mappa
        TiledMap map = new TmxMapLoader().load("maps/".concat(name).concat(".tmx")); // Carica il file TMX
        mapRenderer = new OrthogonalTiledMapRenderer(map, MapManager.TILE_SIZE, manager.info.core.batch); // Renderer

        // Filtro texture per tileset
        for (TiledMapTileSet tileset : map.getTileSets()) {
            for (TiledMapTile tile : tileset) {
                Texture texture = tile.getTextureRegion().getTexture();
                texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            }
        }

        // Accesso ai layer della mappa
        this.collisionLayer = (TiledMapTileLayer) map.getLayers().get("collisioni");        // Layer collisioni base
        this.customCollisionLayer = map.getLayers().get("collisionobjects");                // Layer collisioni speciali
        this.eventLayer = map.getLayers().get("eventi");                                     // Layer eventi

        // Inizializzazione posizione del player
        Gdx.app.postRunnable(() -> managerEntity.player().teleport(new Vector2(x, y)));

        // Inizializzazione camera
        ManagerCamera.getInstance().position.set(managerEntity.player().getPosition(), 0);
        ManagerCamera.getInstance().update();

        // Proprietà mappa
        width = (Integer) map.getProperties().get("width");
        height = (Integer) map.getProperties().get("height");

        //  Setup collisioni
        collisions = new boolean[width][height];
        loadCollisionMap(); // Crea la mappa delle collisioni

        //  Creazione grafo di navigazione
        graph = new GameGraph(width, height, collisions);
        isGraphLoaded = true;

        //  Stato mappa
        isLoaded = true;

        //  Creazione eventi dalla mappa
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
                fixtureDef.filter.groupIndex = ManagerEntity.WALL;

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
        ManagerWorld.clearMap();
    }
}
