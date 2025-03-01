package io.github.ale.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import io.github.ale.player.Player;

public class Map {
    private TiledMap map;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer collisionLayer;
    private static Boolean collisionMap [][];
    private static Rectangle collisionBoxes [][];
    private static Integer width;
    private static int height;

    public Map(OrthographicCamera camera, String name){
        map = new TmxMapLoader().load("maps/".concat(name).concat(".tmx"));
        collisionLayer = (TiledMapTileLayer)map.getLayers().get(name);

        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);
        width=(Integer) map.getProperties().get("width");
        height=(Integer) map.getProperties().get("height");
        collisionMap = new Boolean[width][height];
        collisionBoxes = new Rectangle[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell tile = collisionLayer.getCell(i, j);
                collisionMap[i][j]=(Boolean) tile.getTile().getProperties().get("solido");
                if (collisionMap[i][j]!=null) {
                    collisionBoxes[i][j] = new Rectangle(1f*i, 1f*j, 1f,1f);
                }
            }
        }
        update(camera);
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
     * controlla collisioni sull'asse delle y, ritorna se il personaggio è in collisione
     * @param direzione
     * @return
     */
    public static boolean checkCollisionY(String direzione){
        boolean inCollision=false;
        Rectangle hitbox = new Rectangle(Player.hitbox);
        if (direzione.equals("W")) {
            hitbox.y+=1/64f;
        }
        if (direzione.equals("S")) {
            hitbox.y-=1/64f;
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (collisionMap[i][j]!=null) {
                    if (hitbox.overlaps(collisionBoxes[i][j])) {
                        inCollision=true;
                    }
                }
            }
        }
        //System.out.println(inCollision);
        return inCollision;
    }
    /**
     * controlla collisioni sull'asse delle x, ritorna se il personaggio è in collisione
     * @param direzione
     * @return
     */
    public static boolean checkCollisionX(String direzione){
        boolean inCollision=false;
        Rectangle hitbox = new Rectangle(Player.hitbox);
        if (direzione.equals("A")) {
            hitbox.x-=1/64f;
        }
        if (direzione.equals("D")) {
            hitbox.x+=1/64f;
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (collisionMap[i][j]!=null) {
                    if (hitbox.overlaps(collisionBoxes[i][j])) {
                        inCollision=true;
                    }
                }
            }
        }
        //System.out.println(inCollision);
        return inCollision;
    }

    public void update(OrthographicCamera camera){
        setCamera(camera);
    }
        
    public void setCamera(OrthographicCamera camera) {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    public void draw(OrthographicCamera camera){
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    public static int getWidth(){
        return width;
    }
    public static int getHeight(){
        return height;
    }
    

}
