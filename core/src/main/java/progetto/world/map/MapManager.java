package progetto.world.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.FitViewport;
import progetto.entity.Engine;
import progetto.entity.entities.specific.EntityInstance;
import progetto.core.loading.LoadingScreen;

import java.util.HashMap;

public class MapManager {

    // Grandezza di un pixel
    public static final float TILE_SIZE = 1 / 16f;
    // Numero della mappa attuale
    private static int currentMapNum;
    public final HashMap<String, Array<EntityInstance>> mapEntityInstances;
    // Mappa default
    private final int defaultMap = 1;
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

        this.changeMap(defaultMap, 11, 8); // Cambio mappa
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
            new LoadingScreen(engine.game.core, engine.game, 5, 6);
        }

        chooseMap(map);

        // Riassegnazione index
        currentMapNum = map;

        // Creazione mappa e crea corpi/eventi rilevanti
        currentMap = new Map(nome, engine, this, x, y);
        currentMap.generateCollisions();

        loadMapEntities();

        // Applico la telecamera
        viewport.apply();
    }

    public void render(float delta) {
        currentMap.render(delta);
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
                nome = "fuori1";
                ambienteAperto = true;
                viewport.setWorldSize(22f, 22f * 9 / 16f);
            }


            default -> {
                nome = "stanza"; // Nome file
                ambienteAperto = true; // Tipo ambiente
                viewport.setWorldSize(28f, 28f * 9 / 16f); // Grandezza telecamera
            }
        }
    }

    public void loadMapEntities() {
        FileHandle file = Gdx.files.local("/save/maps/" + nome + ".json");
        if (file.exists()) {
            Json json = new Json();
            @SuppressWarnings("unchecked")
            Array<EntityInstance> loadedInstances = json.fromJson(Array.class, EntityInstance.class, file);
            for (int i = 0; i < loadedInstances.size; i++) {
                EntityInstance instance = loadedInstances.get(i);
                instance.loadTexture();
            }
            engine.summon(loadedInstances);
        }
    }

    public void saveMapEntities() {
        new Thread(() -> {
            FileHandle fileHandle = Gdx.files.local("save/maps/" + currentMap.nome + ".json");

            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);

            if (mapEntityInstances.get(currentMap.nome) != null)
                mapEntityInstances.get(currentMap.nome).clear();

            Array<EntityInstance> instances = engine.clear();
            mapEntityInstances.put(currentMap.nome, instances);

            String string = json.prettyPrint(instances);
            System.out.println(json.prettyPrint(string));

            currentMap.dispose();
        }).start();
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
