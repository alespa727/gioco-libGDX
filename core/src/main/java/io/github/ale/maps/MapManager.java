package io.github.ale.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

import io.github.ale.entity.player.Player;

public class MapManager {
    private Map currentMap;
    private int currentMapNum;
    private String nome;
    private boolean ambienteAperto;

    private final int totalMaps=2;

    boolean flag;

    public MapManager(OrthographicCamera camera, Player p, int startingMap){
        this.flag=false;
        this.currentMapNum=startingMap;
        this.ambienteAperto=true;
        this.changeMap(camera);
        p.setX(6.5f);
        p.setY(5f);
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
