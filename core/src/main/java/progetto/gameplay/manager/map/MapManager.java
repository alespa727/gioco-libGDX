package progetto.gameplay.manager.map;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import progetto.gameplay.entity.types.abstractEntity.EntityInstance;
import progetto.gameplay.manager.entity.EntityManager;
import progetto.gameplay.map.Map;

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
    // Variabile di stato in caso di cambio mappa
    // Mappa attuale
    private Map currentMap;
    private String nome;
    private boolean ambienteAperto;

    public final ArrayMap<String, Array<EntityInstance>> mapEntityInstances = new ArrayMap<>();

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
        if (currentMap != null) {
            mapEntityInstances.put(currentMap.nome, entityManager.despawnEveryone());
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
                if(mapEntityInstances.containsKey("stanza")) {
                    Array<EntityInstance> instances = mapEntityInstances.get("stanza");
                    entityManager.summon(instances);
                }
                ambienteAperto = false;
                viewport.setWorldSize(16f, 16f * 9 / 16f);
            }
        }

        // Riassegnazione index
        currentMapNum = map;

        // Creazione mappa e crea corpi/eventi rilevanti
        currentMap = new Map(nome, entityManager, this, x, y).createCollision();

        // Applico la telecamera
        viewport.apply();
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
