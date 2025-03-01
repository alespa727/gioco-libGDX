package io.github.ale;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Map {
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer collisionLayer;
    public static Boolean collisionMap [][];
    public static Rectangle collisionBoxes [][];
    private static int width;
    private static int height;

    public Map(OrthographicCamera camera){
        map = new TmxMapLoader().load("maps/map2.tmx");
        collisionLayer = (TiledMapTileLayer)map.getLayers().get("map2");

        

        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);
        width=50;
        height=50;
        collisionMap = new Boolean[width][height];
        collisionBoxes = new Rectangle[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell tile = collisionLayer.getCell(i, j);
                collisionMap[i][j]=(Boolean) tile.getTile().getProperties().get("solido");
                System.err.println(collisionMap[i][j]);
                if (collisionMap[i][j]!=null) {
                    collisionBoxes[i][j] = new Rectangle(1f*i, 1f*j, 1f,1f);
                }
            }
        }
        

        

        update(camera);
    }

    public void drawBoxes(ShapeRenderer renderer){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (collisionMap[i][j]!=null) {
                    renderer.rect(collisionBoxes[i][j].x, collisionBoxes[i][j].y, collisionBoxes[i][j].width, collisionBoxes[i][j].height);
                }
            }
        }
    }

    public void changeMap(){
        map = new TmxMapLoader().load("maps/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);
        width=50;
        height = 50;
        
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
    
    
    private boolean tileSolido(int x, int y, TiledMapTileLayer layer) {
       return true;
    }
    

}
