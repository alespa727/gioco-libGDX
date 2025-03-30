package io.github.ale.screens.game.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.map.events.ChangeMapEvent;
import io.github.ale.screens.game.map.events.MapEvent;
import io.github.ale.screens.game.map.events.EventListener;
import io.github.ale.screens.game.pathfinding.graph.GameGraph;

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
    private final OrthographicCamera camera;
    private final EntityManager entityManager;
    private final MapManager mapManager;

    public Map(OrthographicCamera camera, String name, EntityManager manager, MapManager mapManager) {
        TiledMap map = new TmxMapLoader().load("maps/".concat(name).concat(".tmx"));

        this.mapManager = mapManager;
        this.entityManager = manager;
        this.camera = camera;
        this.collisionLayer = (TiledMapTileLayer) map.getLayers().get("collisioni");
        this.eventLayer = map.getLayers().get("eventi");

        entityManager.player().body.setTransform(eventLayer.getProperties().get("spawnX", Float.class), eventLayer.getProperties().get("spawnY", Float.class), 0);

        width = (Integer) map.getProperties().get("width");
        height = (Integer) map.getProperties().get("height");
        collisions = new boolean[width][height];

        mapRenderer = new OrthogonalTiledMapRenderer(map, MapManager.TILE_SIZE);

        loadCollisionMap();
        graph = new GameGraph(width, height, collisions, manager);

        isGraphLoaded = true;
        isLoaded = true;
    }

    public OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }

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

    public static GameGraph getGraph() {
        return graph;
    }

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

    public void createCollision() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (collisions[i][j]) {
                    // Definizione del blocco
                    BodyDef bodyDef = new BodyDef();
                    bodyDef.type = BodyDef.BodyType.StaticBody;
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
                    fixtureDef.restitution = 0f;

                    // Collegamento delle proprietà fisiche al corpo
                    body.createFixture(fixtureDef);

                    // Pulizia della memoria
                    boxShape.dispose();
                }
            }
        }
        createBorders();
        createEvents();
    }

    public void createBorders(){

        Vector2[] bordi = new Vector2[5];

        bordi[0] = new Vector2(4, 4);
        bordi[1] = new Vector2(width-4, 4);
        bordi[2] = new Vector2(width-4, height-4);
        bordi[3] = new Vector2(4, height-4);
        bordi[4] = new Vector2(4, 4);

        ChainShape chainShape = new ChainShape();
        chainShape.createChain(bordi);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = chainShape;

        Body body1 = entityManager.world.createBody(bodyDef);
        body1.createFixture(fixtureDef);
        body1.setUserData("map");

        chainShape.dispose();

    }

    public void createEvents(){
        for (MapObject object : eventLayer.getObjects()) {

            String eventType = (String) object.getProperties().get("eventType", String.class);
            float x = object.getProperties().get("x", Float.class)*MapManager.TILE_SIZE;
            float y = object.getProperties().get("y", Float.class)*MapManager.TILE_SIZE;
            float radius = object.getProperties().get("eventRadius", Float.class);
            int map = object.getProperties().get("map", Integer.class);
            if ("changeMap".equals(eventType)) {

                new ChangeMapEvent(new Vector2(x, y), radius, entityManager.world, this.mapManager, map);
            }

        }
        EventListener listener = new EventListener();

        this.entityManager.world.setContactListener(listener);
    }

    public static int width() {
        return width;
    }

    public static int height() {
        return height;
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
