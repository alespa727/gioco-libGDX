package io.github.ale.screens.game.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.ale.music.MusicPlayer;
import io.github.ale.screens.game.entityType.EntityManager;

public class MapManager {
    private final EntityManager manager;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final World world;
    private final MusicPlayer playlist;
    private final int totalMaps = 2;

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

        this.changeMap();
        this.playlist = new MusicPlayer("music/mymusic.mp3");
        this.currentMap = new Map(camera, this.nome, manager, this);
    }

    public static int currentMap() {
        return currentMapNum;
    }

    public void debugDraw(ShapeRenderer renderer) {
        this.currentMap.debugDraw(renderer);
    }

    public void draw() {
        this.currentMap.draw();
    }

    private void changeMap() {
        if (currentMap != null) {
            currentMap.dispose();
            System.out.println("Mappa: " + currentMapNum);
        }

        System.out.println("Mappa: " + currentMapNum);
        switch (currentMapNum) {
            case 1 -> {
                nome = "map3";
                ambienteAperto = true;
                viewport.setWorldSize(20f, 20f * 9 / 16f);
            }
            case 2 -> {
                nome = "map2";
                ambienteAperto = true;
                viewport.setWorldSize(15f, 15 * 9 / 16f);
            }
            default -> {}
        }

        if (currentMapNum >= totalMaps) {
            currentMapNum = 0;
        }
        currentMapNum++;

        currentMap = new Map(camera, nome, manager, this);
        currentMap.createCollision();
        viewport.apply();
    }

    /**
     * Controlla per eventuale cambio mappa
     */
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            changeMap();
        }
    }

    public boolean getAmbiente() {
        return ambienteAperto;
    }

    public MusicPlayer getPlaylist() {
        return this.playlist;
    }
}
