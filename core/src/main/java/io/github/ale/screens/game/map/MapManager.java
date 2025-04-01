package io.github.ale.screens.game.map;

import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.ale.screens.game.entityType.EntityManager;

public class MapManager {
    private final int defaultMap = 0;
    private final EntityManager manager;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final World world;

    private boolean inChangeMapEvent=false;



    public static final float TILE_SIZE = 1/16f;

    private Map currentMap;
    private static int currentMapNum;
    private String nome;
    private boolean ambienteAperto;



    public MapManager(OrthographicCamera camera, FitViewport viewport, EntityManager manager, int startingMap, World world) {
        this.world = world;
        this.manager = manager;
        this.camera = camera;
        this.viewport = viewport;
        currentMapNum = startingMap;
        this.ambienteAperto = true;

        float defaultx=11, defaulty=11;

        this.changeMap(defaultMap, defaultx, defaulty);
        this.currentMap = new Map(camera, this.nome, manager, this, defaultx, defaulty);
    }

    public static int currentMap() {
        return currentMapNum;
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    public void debugDraw(ShapeRenderer renderer) {
        this.currentMap.debugDraw(renderer);
    }

    public void changeMap(int map, float x, float y) {
        System.out.println("cambio mappa");
        if (currentMap != null) {
            currentMap.dispose();
            System.out.println("Mappa: " + currentMapNum);
        }

        System.out.println("Mappa: " + currentMapNum);
        switch (map) {
            case 1 -> {
                nome = "corridoio";
                ambienteAperto = true;
                viewport.setWorldSize(20f, 20f * 9 / 16f);
            }

            default -> {
                nome = "stanza";
                ambienteAperto = false;
                viewport.setWorldSize(16f, 16f * 9 / 16f);
            }
        }

        currentMapNum=map;

        currentMap = new Map(camera, nome, manager, this, x, y);
        currentMap.createCollision();
        viewport.apply();
    }

    public boolean getAmbiente() {
        return ambienteAperto;
    }

    public boolean isInChangeMapEvent() {
        return inChangeMapEvent;
    }

    public void setInChangeMapEvent(boolean inChangeMapEvent) {
        this.inChangeMapEvent = inChangeMapEvent;
    }
}
