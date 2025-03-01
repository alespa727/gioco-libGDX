package io.github.ale.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

import io.github.ale.player.Player;

public class MapManager {
    private Map map;
    private int numMap;
    private String nome;

    private final int totalMaps=2;

    boolean flag;

    public MapManager(OrthographicCamera camera){
        flag=false;
        numMap=1;
        nome = "map2";
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
            }
            case 2 -> { nome = "map";
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
                p.setWorldX(3f);
                p.setWorldY(3f);
                flag=true;
            }
        }

        if (!e && flag) {
            flag=false;
        }
    }
}
