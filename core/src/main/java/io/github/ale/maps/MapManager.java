package io.github.ale.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

import io.github.ale.player.Player;

public class MapManager {
    private Map map;
    private int numMap;
    private String nome;
    private boolean ambienteAperto;

    private final int totalMaps=2;

    boolean flag;

    public MapManager(OrthographicCamera camera, Player p){
        flag=false;
        numMap=1;
        ambienteAperto=true;
        changeMap(camera);
        p.setWorldX(6.5f);
        p.setWorldY(5f);
        map = new Map(camera, nome);
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void changeMap(OrthographicCamera camera){
        System.out.println(numMap);
        switch (numMap) {
            case 1 -> { nome = "map2"; 
            ambienteAperto=true;
            }
            case 2 -> { nome = "map";
            ambienteAperto=false;
            }
            default -> {
            }
        }
        if (numMap>=totalMaps) {
            numMap=0;
        }
        numMap++;
        map = new Map(camera, nome);
        
    }
    
    public void update(OrthographicCamera camera, Player p){
        boolean e = Gdx.input.isKeyPressed(Input.Keys.E);

        if (e) {
            if (!flag) {
                changeMap(camera);
                p.setWorldX(7.5f);
                p.setWorldY(5f);
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
