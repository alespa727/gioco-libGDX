package io.github.ale;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Map {
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    TiledMapTileLayer layer;
    private static int width;
    private static int height;

    public Map(OrthographicCamera camera){
        map = new TmxMapLoader().load("maps/map2.tmx");
        layer = (TiledMapTileLayer) map.getLayers().get(0);

        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);
        width=50;
        height=50;
        boolean a = tileSolido(50, 0, layer);
        System.out.println(a);
        update(camera);
    }

    public void changeMap(){
        map = new TmxMapLoader().load("maps/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);
        width=50;
        width=50;
        
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
    
    
    public boolean tileSolido(int x, int y, TiledMapTileLayer layer) {
        Cell cella = layer.getCell(x, y);
        if (cella == null) return false;

        TiledMapTile tile = cella.getTile();
        return tile != null && tile.getProperties().containsKey("collisione");
    }

}
