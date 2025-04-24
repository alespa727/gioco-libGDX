package progetto.world.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.FitViewport;
import progetto.entity.Engine;
import progetto.entity.entities.base.EntityInstance;
import progetto.screens.loading.LoadingScreen;

import java.util.HashMap;

public class MapManager {

    // Grandezza di un pixel
    public static final float TILE_SIZE = 1 / 16f;
    // Numero della mappa attuale
    private static int currentMapNum;
    public final HashMap<String, Array<EntityInstance>> mapEntityInstances;
    // Mappa default
    private final int defaultMap = 0;
    // Reference utili
    private final Engine engine;
    private final FitViewport viewport;
    // Mappa attuale
    private Map currentMap;
    private String nome;
    private boolean ambienteAperto;

    /**
     * Creazione manager delle mappe
     */
    public MapManager(FitViewport viewport, Engine manager, int startingMap) {
        // Inizializzazione
        this.engine = manager;
        this.viewport = viewport;
        currentMapNum = startingMap;
        this.ambienteAperto = true;
        float defaultx = 11, defaulty = 11;

        this.changeMap(defaultMap, defaultx, defaulty); // Cambio mappa
        mapEntityInstances = new HashMap<>();
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
            saveMapEntities();
            new LoadingScreen(engine.info.core, engine.info.screen, 5, 6);
        }

        chooseMap(map);

        // Riassegnazione index
        currentMapNum = map;

        // Creazione mappa e crea corpi/eventi rilevanti
        currentMap = new Map(nome, engine, this, x, y);
        currentMap.createCollision();

        //loadMapEntities();

        // Applico la telecamera
        viewport.apply();
    }

    public void render() {
        currentMap.render();
    }

    public void chooseMap(int map) {
        // Cambio mappa
        switch (map) {
            case 1 -> {
                nome = "corridoio"; // Nome file
                ambienteAperto = true; // Tipo ambiente
                viewport.setWorldSize(28f, 28f * 9 / 16f); // Grandezza telecamera
            }

            case 2 -> {
                nome = "bossroom";
                ambienteAperto = false;
                viewport.setWorldSize(22f, 12f * 9 / 16f);
            }

            default -> {
                nome = "stanza"; // Nome file
                ambienteAperto = true; // Tipo ambiente
                viewport.setWorldSize(28f, 28f * 9 / 16f); // Grandezza telecamera
            }
        }
    }

    public void loadMapEntities() {
        FileHandle file = Gdx.files.local("/save/maps/" + currentMap.nome + ".json");
        if (file.exists()) {
            Json json = new Json();
            @SuppressWarnings("unchecked")
            Array<EntityInstance> loadedInstances = json.fromJson(Array.class, EntityInstance.class, file);
            System.out.println("TESTO DA FILE" + json.prettyPrint(loadedInstances));
            for (int i = 0; i < loadedInstances.size; i++) {
                EntityInstance instance = loadedInstances.get(i);
                instance.loadTexture();
            }
            engine.summon(loadedInstances);
        }
    }

    public void saveMapEntities() {
        FileHandle fileHandle = Gdx.files.local("/save/maps/" + currentMap.nome + ".json");

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json); // output in formato JSON puro

        // Pulisce l'elenco delle entità associate alla mappa corrente
        if (mapEntityInstances.get(currentMap.nome) != null)
            mapEntityInstances.get(currentMap.nome).clear();

        Array<EntityInstance> instances = engine.clear();
        mapEntityInstances.put(currentMap.nome, instances); // Salva nella mappa

        String string = json.prettyPrint(instances); // Serializzazione ben formattata
        System.out.println(json.prettyPrint(string)); // Stampa leggibile

        fileHandle.writeString(string, false); // Salvataggio su file
        currentMap.dispose(); // Rilascio risorse mappa precedente
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
