package io.github.ale.screens.gameScreen.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.ale.music.MusicPlayer;
import io.github.ale.screens.gameScreen.entity.player.Player;

public class MapManager {
    private final Player player;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private Map currentMap;
    private int currentMapNum;
    private String nome;
    private boolean ambienteAperto;
    private final MusicPlayer playlist;
    private final int totalMaps=2;

    boolean flag;

    public MapManager(OrthographicCamera camera, FitViewport viewport, Player player, int startingMap){
        this.player=player;
        this.camera=camera;
        this.viewport=viewport;
        this.flag=false;
        this.currentMapNum=startingMap;
        this.ambienteAperto=true;
        this.changeMap();
        this.player.setX(6.5f);
        this.player.setY(5f);
        playlist = new MusicPlayer("mymusic.mp3");
        playlist.play(0);
        playlist.setVolume(0.1f);
        playlist.setLooping(0, true);
        this.currentMap = new Map(camera, this.nome);
    }

    public void drawCollisions(ShapeRenderer renderer){ this.currentMap.drawCollisions(renderer);}
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
                this.player.setX(7.5f);
                this.player.setY(5f);
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
