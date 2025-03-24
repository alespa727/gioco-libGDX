package io.github.ale.screens.gameScreen.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.ale.music.MusicPlayer;
import io.github.ale.screens.gameScreen.entityType.EntityManager;

public class MapManager {
    @SuppressWarnings("unused")
    private final EntityManager manager;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private Map currentMap;
    private static int currentMapNum;
    private String nome;
    private boolean ambienteAperto;
    private final MusicPlayer playlist;
    private final int totalMaps;
    

    boolean flag;

    public MapManager(OrthographicCamera camera, FitViewport viewport, EntityManager manager, int startingMap){
        this.manager=manager;
        this.camera=camera;
        this.viewport=viewport;
        this.flag=false;
        MapManager.currentMapNum=startingMap;
        this.ambienteAperto=true;
        this.changeMap();
        playlist = new MusicPlayer("music/mymusic.mp3");
        this.currentMap = new Map(camera, this.nome);
        totalMaps = 2;
    }

    public static int currentmap(){ return currentMapNum; }

    public void collisions(ShapeRenderer renderer){ this.currentMap.collisions(renderer);}
    public void render(){ this.currentMap.render();}
    public void draw(){ this.currentMap.draw(); }

    private void changeMap(){

        System.out.println("Mappa: "+currentMapNum);
        switch (currentMapNum) {
            case 1 -> { nome = "map3";
            ambienteAperto=true;
            viewport.setWorldSize(20f, 20f*9/16f);
            }
            case 2 -> { nome = "map2";
            ambienteAperto=true;
            viewport.setWorldSize(15f, 15*9/16f);
            }
            default -> {
            }
        }
        if (currentMapNum>=totalMaps) {
            currentMapNum=0;
        }
        currentMapNum++;
        currentMap = null;
        currentMap = new Map(camera, nome);
        viewport.apply();
    }

    /**
     * controlla per eventuale cambio mappa
    */
    public void checkInput(){
        boolean e = Gdx.input.isKeyPressed(Input.Keys.E);

        if (e) {
            if (!flag) {
                changeMap();
                flag=true;
            }
        }

        if (!e && flag) {
            flag=false;
        }
    }

    public boolean getAmbiente(){
        return ambienteAperto;
    }

    public MusicPlayer getPlaylist(){
        return this.playlist;
    }

}
