package io.github.ale.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

import io.github.ale.entity.player.Player;
import io.github.ale.musicPlayer.MusicPlayer;

public class MapManager {
    private Map currentMap;
    private int currentMapNum;
    private String nome;
    private boolean ambienteAperto;
    private MusicPlayer playlist;
    private final int totalMaps=2;

    boolean flag;

    public MapManager(OrthographicCamera camera, Player p, int startingMap){
        this.flag=false;
        this.currentMapNum=startingMap;
        this.ambienteAperto=true;
        this.changeMap(camera);
        p.setX(6.5f);
        p.setY(5f);
        playlist = new MusicPlayer("mymusic.mp3");
        playlist.play(0);
        playlist.setVolume(0.1f);
        playlist.setLooping(0, true);
        this.currentMap = new Map(camera, this.nome);
    }

    /**
     * restituisce la mappa attuale
     * @return
     */
    public Map getMap() {
        return currentMap;
    }

    private void changeMap(OrthographicCamera camera){  
    
        System.out.println("Mappa: "+currentMapNum);
        switch (currentMapNum) {
            case 1 -> { nome = "map2"; 
            ambienteAperto=true;
            }
            case 2 -> { nome = "map";
            ambienteAperto=true;
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
        
    }
    
    /** 
     * controlla per eventuale cambio mappa 
    */
    public void update(OrthographicCamera camera, Player p){
        boolean e = Gdx.input.isKeyPressed(Input.Keys.E);

        if (e) {
            if (!flag) {
                changeMap(camera);
                p.setX(7.5f);
                p.setY(5f);
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

}
