package io.github.ale;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Map {
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private static int width;
    private static int height;

    public Map(OrthographicCamera camera){
        map = new TmxMapLoader().load("maps/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);
        width=50;
        height=50;
        update(camera);
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
