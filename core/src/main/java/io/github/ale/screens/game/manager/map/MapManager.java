package io.github.ale.screens.game.manager.map;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.ale.screens.game.manager.entity.EntityManager;
import io.github.ale.screens.game.map.Map;

public class MapManager {

    // Grandezza di un pixel
    public static final float TILE_SIZE = 1 / 16f;
    // Numero della mappa attuale
    private static int currentMapNum;
    // Mappa default
    private final int defaultMap = 0;
    // Reference utili
    private final EntityManager manager;
    private final FitViewport viewport;
    private final World world;
    // Variabile di stato in caso di cambio mappa
    private boolean inChangeMapEvent = false;
    // Mappa attuale
    private Map currentMap;
    private String nome;
    private boolean ambienteAperto;

    /**
     * Creazione manager delle mappe
     */
    public MapManager(FitViewport viewport, EntityManager manager, int startingMap, World world) {

        // Inizializzazione
        this.world = world;
        this.manager = manager;
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

        // Riassegnazione index
        currentMapNum = map;

        // Creazione mappa e crea corpi/eventi rilevanti
        currentMap = new Map(nome, manager, this, x, y).createCollision();

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

    /**
     * Setter per possibilità cambio mappa
     */
    public void setInChangeMapEvent(boolean inChangeMapEvent) {
        this.inChangeMapEvent = inChangeMapEvent;
    }
}
