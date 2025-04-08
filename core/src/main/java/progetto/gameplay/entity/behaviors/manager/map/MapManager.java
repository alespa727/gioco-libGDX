package progetto.gameplay.entity.behaviors.manager.map;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.entity.behaviors.EntityManager;
import progetto.gameplay.map.Map;

import java.util.HashMap;

public class MapManager {

    // Grandezza di un pixel
    public static final float TILE_SIZE = 1 / 16f;
    // Numero della mappa attuale
    private static int currentMapNum;
    // Mappa default
    private final int defaultMap = 0;
    // Reference utili
    private final EntityManager entityManager;
    private final FitViewport viewport;
    // Mappa attuale
    private Map currentMap;
    private String nome;
    private boolean ambienteAperto;

    public final HashMap<String, Array<EntityInstance>> mapEntityInstances = new HashMap<>();

    /**
     * Creazione manager delle mappe
     */
    public MapManager(FitViewport viewport, EntityManager manager, int startingMap) {
        // Inizializzazione
        this.entityManager = manager;
        this.viewport = viewport;
        currentMapNum = startingMap;
        this.ambienteAperto = true;
        float defaultx = 11, defaulty = 11;

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
            mapEntityInstances.put(currentMap.nome, entityManager.despawnEveryone()); // Salva le entità in una hashmap
            currentMap.dispose(); // Cancellazione mappa precedente
        }

        // Cambio mappa
        switch (map) {
            case 1 -> {
                nome = "corridoio"; // Nome file
                ambienteAperto = true; // Tipo ambiente
                viewport.setWorldSize(20f, 20f * 9 / 16f); // Grandezza telecamera
            }

            case 2 -> {
            }

            default -> {
                nome = "stanza";
                ambienteAperto = false;
                viewport.setWorldSize(16f, 16f * 9 / 16f);
            }
        }

        spawnInstances();

        // Riassegnazione index
        currentMapNum = map;

        // Creazione mappa e crea corpi/eventi rilevanti
        currentMap = new Map(nome, entityManager, this, x, y).createCollision();

        // Applico la telecamera
        viewport.apply();
    }

    public void spawnInstances(){
        if(mapEntityInstances.containsKey(nome)) {
            Array<EntityInstance> instances = mapEntityInstances.get(nome);
            entityManager.summon(instances);
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
    public boolean getAmbiente() {
        return ambienteAperto;
    }

}
