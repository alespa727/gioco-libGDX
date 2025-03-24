package io.github.ale.screens.gameScreen.maps;

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

import io.github.ale.screens.gameScreen.camera.CameraManager;
import io.github.ale.screens.gameScreen.entityType.EntityManager;
import io.github.ale.screens.gameScreen.entityType.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.pathfinding.GameGraph;

public class Map {
    public static boolean isGraphLoaded=false;
    private static GameGraph graph;

    private final OrthographicCamera camera;
    private TiledMap map;
    private final OrthogonalTiledMapRenderer mapRenderer;

    private static Rectangle temp;

    private TiledMapTileLayer collisionLayer;

    private static boolean[][] collisions;
    private static Rectangle[][] collisionBoxes;

    private static int width;
    private static int height;

    public static boolean isLoaded=false;
    private EntityManager manager;

    public Map(OrthographicCamera camera, String name, EntityManager manager){
        temp = new Rectangle();
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
     * aggiorna la vista per disegnare la mappa
     */

    public void render(){
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    /**
     * disegna la mappa in generale
     */
    public void draw(){
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    /**
     * disegna la hitbox dei tile con collisioni
     * @param renderer
     */
    public void collisions(ShapeRenderer renderer){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                renderer.setColor(Color.BLACK);
                renderer.rect(i*1f, j*1f, 1f, 1f);
                if (collisionBoxes[i][j]!=null) {
                    renderer.setColor(Color.RED);
                    renderer.rect(collisionBoxes[i][j].x, collisionBoxes[i][j].y, collisionBoxes[i][j].width, collisionBoxes[i][j].height);

                }
            }
        }
        //renderer.rect(temp.x, temp.y, temp.width, temp.height);
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
                float size;
                float posX, posY;
                if(collisions[i][j]==true){
                    posX=i;
                    posY=j;
                    size=1f;
                }else{
                    posX=0;
                    posY=0;
                    size=0f;
                }
                collisionBoxes[i][j] = new Rectangle(1f * posX, 1f * posY, size, size);
            }
        }
    }



    public static int width(){
        return width;
    }
    public static int height(){
        return height;
    }



    public static boolean checkLineCollision(Vector2 p1, Vector2 p2){
        //int controlli=0;
        int minTileX = Math.max(0, (int) (CameraManager.getFrustumCorners()[0].x-2f));
        int maxTileX = Math.min(width - 1, (int) (CameraManager.getFrustumCorners()[2].x+2f));
        int minTileY = Math.max(0, (int) (CameraManager.getFrustumCorners()[0].y-2f));
        int maxTileY = Math.min(height - 1, (int) (CameraManager.getFrustumCorners()[2].y+2f));
        /*System.out.print(CameraManager.limiti()[0].x);
        System.out.print(" "+CameraManager.limiti()[0].y);
        System.out.print(" "+CameraManager.limiti()[2].x);
        System.out.println(" "+CameraManager.limiti()[2].y);*/
        for (int i = minTileX; i <= maxTileX; i++) {
            for (int j = minTileY; j <= maxTileY; j++) {
                if (collisionBoxes[i][j]!=null && Intersector.intersectSegmentRectangle(p1, p2, collisionBoxes[i][j])) {
                    return true;
                }
                //controlli++;
            }
        }
        //System.out.println(controlli);
        return false;
    }

    public static boolean checkRectangleCollision(float x, float y, float width, float height){
        temp.set(x, y, width, height);
        int minTileX = Math.max(0, (int) (CameraManager.getFrustumCorners()[0].x-2f));
        int maxTileX = Math.min(Map.width - 1, (int) (CameraManager.getFrustumCorners()[2].x+2f));
        int minTileY = Math.max(0, (int) (CameraManager.getFrustumCorners()[0].y-2f));
        int maxTileY = Math.min(Map.height - 1, (int) (CameraManager.getFrustumCorners()[2].y+2f));
        /*System.out.print(CameraManager.limiti()[0].x);
        System.out.print(" "+CameraManager.limiti()[0].y);
        System.out.print(" "+CameraManager.limiti()[2].x);
        System.out.println(" "+CameraManager.limiti()[2].y);*/
        for (int i = minTileX; i <= maxTileX; i++) {
            for (int j = minTileY; j <= maxTileY; j++) {
                if (collisionBoxes[i][j]!=null && temp.overlaps(collisionBoxes[i][j])) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * controlla collisioni sull'asse delle x, ritorna se il personaggio è in collisione
     * @return
     */
    public static boolean checkCollisionX(Entity entity){
        //int count=0;
        boolean inCollision=false;
        Rectangle hitbox = new Rectangle(entity.hitbox());
        if (entity.direzione().x < 0) {
            hitbox.x-=1/16f;
        }
        if (entity.direzione().x > 0) {
            hitbox.x+=1/16f;
        }
        int minTileX = Math.max(0, (int) (hitbox.x));
        int maxTileX = Math.min(width - 1, (int) ((hitbox.x + hitbox.width)));
        int minTileY = Math.max(0, (int) (hitbox.y));
        int maxTileY = Math.min(height - 1, (int) ((hitbox.y + hitbox.height)));

        for (int i = minTileX; i <= maxTileX; i++) {
            for (int j = minTileY; j <= maxTileY; j++) {
                if (collisionBoxes[i][j] != null && hitbox.overlaps(collisionBoxes[i][j])) {
                    inCollision = true;
                }
                //count++;
            }
        }
        //System.out.println(count);
        //System.out.println(inCollision);
        return inCollision;
    }

    /**
     * controlla collisioni sull'asse delle x, ritorna se il personaggio è in collisione
     * @return collisione in asse x
     */
    public static boolean checkCollisionX(Entity entity, float offset, float angolo){
        //int count=0;
        boolean inCollision=false;
        Rectangle hitbox = new Rectangle(entity.hitbox());
        if (angolo > 90 && angolo < 270) {
            hitbox.x-=offset;
        }
        if (angolo < 90 || angolo > 270) {
            hitbox.x+=offset;
        }
        int minTileX = Math.max(0, (int) (hitbox.x));
        int maxTileX = Math.min(width - 1, (int) ((hitbox.x + hitbox.width)));
        int minTileY = Math.max(0, (int) (hitbox.y));
        int maxTileY = Math.min(height - 1, (int) ((hitbox.y + hitbox.height)));

        for (int i = minTileX; i <= maxTileX; i++) {
            for (int j = minTileY; j <= maxTileY; j++) {
                if (collisionBoxes[i][j]!=null && hitbox.overlaps(collisionBoxes[i][j])) {
                    inCollision = true;
                }
                //count++;
            }
        }
        //System.out.println(count);
        //System.out.println(inCollision);
        return inCollision;
    }

    /**
     * controlla collisioni sull'asse delle y, ritorna se il personaggio è in collisione
     * @return collisione asse y
     */
    public static boolean checkCollisionY(Entity entity){
        //int count=0;
        boolean inCollision=false;
        Rectangle hitbox = new Rectangle(entity.hitbox());
        if (entity.direzione().y > 0) {
            hitbox.y+=1/8f;
        }
        if (entity.direzione().y < 0) {
            hitbox.y-=1/16f;
        }

        int minTileX = Math.max(0, (int) (hitbox.x));
        int maxTileX = Math.min(width - 1, (int) ((hitbox.x + hitbox.width)));
        int minTileY = Math.max(0, (int) (hitbox.y));
        int maxTileY = Math.min(height - 1, (int) ((hitbox.y + hitbox.height)));

        for (int i = minTileX; i <= maxTileX; i++) {
            for (int j = minTileY; j <= maxTileY; j++) {
                if (collisionBoxes[i][j] != null && hitbox.overlaps(collisionBoxes[i][j])) {
                    inCollision = true;
                }
                //count++;
            }
        }
        //System.out.println(count);
        //System.out.println(inCollision);
        return inCollision;
    }

    /**
     * controlla collisioni sull'asse delle y, ritorna se il personaggio è in collisione
     * @return colllisione asse y
     */
    public static boolean checkCollisionY(Entity entity, float offset, float angolo){
        //int count=0;
        boolean inCollision=false;
        Rectangle hitbox = new Rectangle(entity.hitbox());
        if (angolo < 180) {
            hitbox.y+=offset;
        }
        if (angolo > 180) {
            hitbox.y-=offset;
        }

        int minTileX = Math.max(0, (int) (hitbox.x));
        int maxTileX = Math.min(width - 1, (int) ((hitbox.x + hitbox.width)));
        int minTileY = Math.max(0, (int) (hitbox.y));
        int maxTileY = Math.min(height - 1, (int) ((hitbox.y + hitbox.height)));

        for (int i = minTileX; i <= maxTileX; i++) {
            for (int j = minTileY; j <= maxTileY; j++) {
                if (collisionBoxes[i][j]!=null && hitbox.overlaps(collisionBoxes[i][j])) {
                    inCollision = true;
                }
                //count++;
            }
        }
        //System.out.println(count);
        //System.out.println(inCollision);
        return inCollision;
    }
}
