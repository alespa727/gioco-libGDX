package io.github.ale.screens.game.maps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import io.github.ale.screens.game.camera.CameraManager;
import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.entityType.abstractEntity.Entity;
import io.github.ale.screens.game.pathfinding.GameGraph;

public class Map implements Disposable {
    public static boolean isGraphLoaded=false;
    private static GameGraph graph;

    private final OrthographicCamera camera;
    private TiledMap map;
    private final OrthogonalTiledMapRenderer mapRenderer;

    private TiledMapTileLayer collisionLayer;

    private static boolean[][] collisions;
    private static Rectangle[][] collisionBoxes;

    private static int width;
    private static int height;

    public static boolean isLoaded=false;
    private final EntityManager manager;

    private final MapManager mapManager;

    public Map(OrthographicCamera camera, String name, EntityManager manager, MapManager mapManager){
        this.mapManager=mapManager;
        this.manager=manager;
        this.camera=camera;
        loadMap(name);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                collisions[i][j]=false;
            }
        }
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);
        loadCollisionMap();
        isLoaded = true;

        graph = new GameGraph(width, height, collisions, manager);
        isGraphLoaded=true;

    }

    public static GameGraph getGraph(){
        return graph;
    }
    /**
     * disegna la mappa in generale
     */
    public void draw(){

        mapRenderer.setView(camera);
        mapRenderer.render();

    }

    /**
     * carica la mappa
     * @param name
     */
    private void loadMap(String name){
        map = new TmxMapLoader().load("maps/".concat(name).concat(".tmx"));
        collisionLayer = (TiledMapTileLayer)map.getLayers().get(name);

        width=(Integer) map.getProperties().get("width");
        height=(Integer) map.getProperties().get("height");
        collisions = new boolean[width][height];
        collisionBoxes = new Rectangle[width][height];

    }

    public void debugDraw(ShapeRenderer renderer){
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (collisions[i][j]) {
                    renderer.setColor(Color.DARK_GRAY );
                    renderer.rect(i, j, 1, 1);
                }
            }
        }
        renderer.end();
    }

        /**
     * Carica la mappa delle collisioni
     */
    private void loadCollisionMap() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell tile = collisionLayer.getCell(i, j);

                if (tile != null && tile.getTile() != null && tile.getTile().getProperties().containsKey("solido")) {
                    collisions[i][j] = (Boolean) tile.getTile().getProperties().get("solido");
                }

            }
        }
    }

    public void createCollision() {

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (collisions[i][j]){
                    // definizione del blocco
                    BodyDef bodyDef = new BodyDef();
                    bodyDef.type = BodyDef.BodyType.StaticBody; // blocco statico
                    bodyDef.position.set(i+0.5f, j+0.5f);

                    // crea il blocco
                    Body body = manager.world.createBody(bodyDef);
                    body.setUserData("collisione");
                    // definisci la forma
                    PolygonShape boxShape = new PolygonShape();
                    boxShape.setAsBox(0.5f, 0.5f);


                    // definisci le proprietà fisiche
                    FixtureDef fixtureDef = new FixtureDef();
                    fixtureDef.shape = boxShape;
                    fixtureDef.density = 1f;
                    fixtureDef.friction = 0f;
                    fixtureDef.restitution = 0f;

                    // collega le proprietà fisiche al corpo
                    body.createFixture(fixtureDef);
                    // dispose
                    boxShape.dispose();
                }
            }
        }

    }

    public static int width(){
        return width;
    }
    public static int height(){
        return height;
    }

    @Override
    public void dispose() {
        Array<Body> bodies = new Array<>();
        manager.world.getBodies(bodies);
        for(Body body: bodies){
            if(body.getUserData().equals("collisione")){
                manager.world.destroyBody(body);
            }
        }

    }
}
