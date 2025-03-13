package io.github.ale.screens.gameScreen.maps;

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

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;

public class Map {
    private TiledMap map;
    private final OrthogonalTiledMapRenderer mapRenderer;

    private TiledMapTileLayer collisionLayer;

    private static Boolean collisionMap [][];
    private static Rectangle collisionBoxes [][];

    private static Integer width;
    private static int height;

    public static boolean isLoaded=false;

    public Map(OrthographicCamera camera, String name){
        loadMap(name);
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);
        loadCollisionMap();
        isLoaded = true;
    }

     /**
     * aggiorna la vista per disegnare la mappa
     */

    public void update(OrthographicCamera camera){
        mapRenderer.setView(camera);
        mapRenderer.render();   
    }

    /**
     * disegna la mappa in generale
     * @param camera
     */
    public void draw(OrthographicCamera camera){
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    /**
     * disegna la hitbox dei tile con collisioni
     * @param renderer
     */
    public void drawBoxes(ShapeRenderer renderer){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (collisionMap[i][j]!=null) {
                    renderer.rect(collisionBoxes[i][j].x, collisionBoxes[i][j].y, collisionBoxes[i][j].width, collisionBoxes[i][j].height);
                }
            }
        }
        
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
        collisionMap = new Boolean[width][height];
        collisionBoxes = new Rectangle[width][height];
        
    }

    /**
     * carica le collisioni
     */
    private void loadCollisionMap(){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell tile = collisionLayer.getCell(i, j);
                collisionMap[i][j]=(Boolean) tile.getTile().getProperties().get("solido");
                if (collisionMap[i][j]!=null) {
                    collisionBoxes[i][j] = new Rectangle(1f*i, 1f*j, 1f,1f);
                }
            }
        }
    }

    
    public static int getWidth(){
        return width;
    }
    public static int getHeight(){
        return height;
    }

    
    
    public static boolean checkLineCollision(Vector2 e1, Vector2 e2){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (collisionMap[i][j]!=null && Intersector.intersectSegmentRectangle(e1, e2, collisionBoxes[i][j])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * controlla collisioni sull'asse delle x, ritorna se il personaggio è in collisione
     * @param direzione
     * @return
     */
    public static boolean checkCollisionX(Entity entity){
        //int count=0;
        boolean inCollision=false;
        Rectangle hitbox = new Rectangle(entity.getHitbox());
        if (entity.getDirezione().contains("A")) {
            hitbox.x-=1/16f;
        }
        if (entity.getDirezione().contains("D")) {
            hitbox.x+=1/16f;
        }
        int minTileX = Math.max(0, (int) (hitbox.x));
        int maxTileX = Math.min(width - 1, (int) ((hitbox.x + hitbox.width)));
        int minTileY = Math.max(0, (int) (hitbox.y));
        int maxTileY = Math.min(height - 1, (int) ((hitbox.y + hitbox.height)));

        for (int i = minTileX; i <= maxTileX; i++) {
            for (int j = minTileY; j <= maxTileY; j++) {
                if (collisionMap[i][j] != null && hitbox.overlaps(collisionBoxes[i][j])) {
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
     * @param direzione
     * @return
     */
    public static boolean checkCollisionY(Entity entity){
        //int count=0;
        boolean inCollision=false;
        Rectangle hitbox = new Rectangle(entity.getHitbox());
        if (entity.getDirezione().contains("W")) {
            hitbox.y+=1/8f;
        }
        if (entity.getDirezione().contains("S")) {
            hitbox.y-=1/16f;
        }

        int minTileX = Math.max(0, (int) (hitbox.x));
        int maxTileX = Math.min(width - 1, (int) ((hitbox.x + hitbox.width)));
        int minTileY = Math.max(0, (int) (hitbox.y));
        int maxTileY = Math.min(height - 1, (int) ((hitbox.y + hitbox.height)));

        for (int i = minTileX; i <= maxTileX; i++) {
            for (int j = minTileY; j <= maxTileY; j++) {
                if (collisionMap[i][j] != null && hitbox.overlaps(collisionBoxes[i][j])) {
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
