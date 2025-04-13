package progetto.gameplay.map;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FitViewport;

import progetto.Core;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.entity.types.living.HumanoidInstances;
import progetto.gameplay.entity.types.living.combat.boss.Boss;
import progetto.gameplay.entity.types.living.combat.boss.BossInstance;
import progetto.gameplay.manager.ManagerEntity;

public class MapManager {

    // Grandezza di un pixel
    public static final float TILE_SIZE = 1 / 16f;
    // Numero della mappa attuale
    private static int currentMapNum;
    // Mappa default
    private final int defaultMap = 0;
    // Reference utili
    private final ManagerEntity managerEntity;
    private final FitViewport viewport;
    // Mappa attuale
    private Map currentMap;
    private String nome;
    private boolean ambienteAperto;

    public final HashMap<String, Array<EntityInstance>> mapEntityInstances = new HashMap<>();

    /**
     * Creazione manager delle mappe
     */
    public MapManager(FitViewport viewport, ManagerEntity manager, int startingMap) {
        // Inizializzazione
        this.managerEntity = manager;
        this.viewport = viewport;
        currentMapNum = startingMap;
        this.ambienteAperto = true;
        float defaultx = 11 , defaulty = 11;

        this.changeMap(defaultMap, defaultx, defaulty); // Cambio mappa
    }

    /**
     * Index mappa
     */
    public static int getMapIndex() {
        return currentMapNum;
    }

    /**
     * Cambio mappa
     */
    public void changeMap(int map, float x, float y) {
        if (currentMap != null) { // Se almeno una mappa è stata caricata
            if (mapEntityInstances.get(currentMap.nome)!=null)
                mapEntityInstances.get(currentMap.nome).clear();
            mapEntityInstances.put(currentMap.nome, managerEntity.clear()); // Salva le entità in una hashmap
            currentMap.dispose(); // Cancellazione mappa precedente
        }

        // Cambio mappa
        switch (map) {
            case 1 -> {
                nome = "corridoio"; // Nome file
                ambienteAperto = true; // Tipo ambiente
                viewport.setWorldSize(22f, 22f * 9 / 16f); // Grandezza telecamera
            }

            case 2 -> {
                nome = "bossroom";
                ambienteAperto = false;
                viewport.setWorldSize(22f, 12f * 9 / 16f);
            }

            default -> {
                nome = "stanza"; // Nome file
                ambienteAperto = true; // Tipo ambiente
                viewport.setWorldSize(22f, 22f * 9 / 16f); // Grandezza telecamera
            }
        }

        spawnInstances();

        // Riassegnazione index
        currentMapNum = map;

        // Creazione mappa e crea corpi/eventi rilevanti
        currentMap = new Map(nome, managerEntity, this, x, y);
        currentMap.createCollision();


        // Applico la telecamera
        viewport.apply();
    }

    public void render(){
        currentMap.render();
    }

    public void spawnInstances(){
        if(mapEntityInstances.containsKey(nome)) {
            Json json = new Json();
            BossInstance loaded = json.fromJson(BossInstance.class, Gdx.files.local("save/entities.json"));
            loaded.loadTexture();
            Array<EntityInstance> instances = mapEntityInstances.get(nome);
            instances.add(loaded);
            managerEntity.summon(instances);
        }
    }

    /**
     * Restituisce la mappa
     */
    public Map getMap() {
        return currentMap;
    }

    /**
     * Restituisce il tipo di ambiente
     */
    public boolean disegnaUI() {
        return ambienteAperto;
    }

}
