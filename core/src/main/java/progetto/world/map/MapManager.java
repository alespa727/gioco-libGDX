package progetto.world.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.FitViewport;
import progetto.core.game.GameScreen;
import progetto.ECS.EntityEngine;
import progetto.ECS.entities.specific.EntityInstance;
import progetto.core.loading.Loading;

import java.util.HashMap;

/**
 * Manager principale che gestisce le mappe di gioco.
 * Si occupa di caricare e salvare le entità delle mappe, cambiare mappa,
 * e gestire le informazioni utili.
 */
public class MapManager {

    /** Grandezza di un tile espresso in unità di gioco */
    public static final float TILE_SIZE = 1 / 16f;

    /** Numero della mappa attuale */
    private static int currentMapNum;

    /** Mappa delle entità per ogni mappa */
    public final HashMap<String, Array<EntityInstance>> mapEntityInstances;

    // Riferimenti utili per la gestione
    private final GameScreen game;
    private final EntityEngine entityEngine;
    private final FitViewport viewport;

    /** Mappa attualmente caricata */
    private Map currentMap;

    /** Nome del file della mappa corrente */
    private String nome;

    /** Indica se l'ambiente è aperto (es. esterno/interiore) */
    private boolean ambienteAperto;

    /**
     * Costruttore del MapManager.
     *
     * @param game          Il GameScreen principale
     * @param viewport      Il viewport della telecamera
     * @param manager       L'EntityEngine (gestore delle entità)
     * @param startingMap   ID della mappa iniziale
     */
    public MapManager(GameScreen game, FitViewport viewport, EntityEngine manager, int startingMap) {
        this.game = game;
        this.entityEngine = manager;
        this.viewport = viewport;
        currentMapNum = startingMap;
        this.ambienteAperto = true;

        int defaultMap = 3;
        this.changeMap(defaultMap, 7, 12, true);
        mapEntityInstances = new HashMap<>();
    }

    /**
     * @return L'indice numerico della mappa attuale
     */
    public static int getMapIndex() {
        return currentMapNum;
    }

    /**
     * Cambia la mappa attuale.
     * - Salva le entità della mappa precedente.
     * - Carica la nuova mappa e genera collisioni.
     * - Ripristina le entità salvate in precedenza per quella mappa.
     *
     * @param map Numero della mappa da caricare
     * @param x   Posizione X di spawn del player nella nuova mappa
     * @param y   Posizione Y di spawn del player nella nuova mappa
     */
    public void changeMap(int map, float x, float y, boolean loading) {
        if (currentMap != null) {
            saveMapEntities();  // Salva entità mappa attuale su file
            if (loading) new Loading(entityEngine.game.app, entityEngine.game, 3, 4);
        }

        chooseMap(map);  // Imposta nome mappa e configurazioni
        currentMapNum = map;

        currentMap = new Map(nome, game, entityEngine, this, x, y);
        currentMap.generateCollisions();  // Genera le collisioni

        loadMapEntities();  // Carica entità

        viewport.apply();  // Aggiorna la telecamera
    }

    /**
     * Aggiorna la mappa corrente (es. eventi).
     *
     * @param delta Tempo trascorso dall'ultimo frame
     */
    public void render(float delta) {
        currentMap.render(delta);
    }

    /**
     * Imposta le configurazioni (nome, ambiente, viewport) in base al numero della mappa.
     *
     * @param map Numero della mappa
     */
    public void chooseMap(int map) {
        switch (map) {
            case 1 -> {
                nome = "corridoio";
                ambienteAperto = true;
                viewport.setWorldSize(25f, 25f * 9 / 16f);
            }
            case 2 -> {
                nome = "fuori1";
                ambienteAperto = true;
                viewport.setWorldSize(22f, 22f * 9 / 16f);
            }
            case 3 -> {
                nome = "stanza_sorella";
                ambienteAperto = true;
                viewport.setWorldSize(22f, 22f * 9 / 16f);
            }
            case 4 -> {
                nome = "bossroom";
                ambienteAperto = true;
                viewport.setWorldSize(28f, 28f * 9 / 16f);
            }
            default -> {
                nome = "stanza";
                ambienteAperto = true;
                viewport.setWorldSize(28f, 28f * 9 / 16f);
            }
        }
    }

    /**
     * Carica le entità salvate in precedenza per la mappa corrente da file JSON.
     * Ripristina anche le texture delle entità.
     */
    public void loadMapEntities() {
        FileHandle file = Gdx.files.local("save/maps/" + nome + ".json");
        if (file.exists()) {
            Json json = new Json();
            @SuppressWarnings("unchecked")
            Array<EntityInstance> loadedInstances = json.fromJson(Array.class, EntityInstance.class, file);
            for (EntityInstance instance : loadedInstances) {
                instance.loadTexture();  // Ricarica texture
            }
            entityEngine.summon(loadedInstances);  // Aggiunge entità
        }
    }

    /**
     * Salva le entità della mappa attuale su file JSON in background (nuovo thread).
     * Inoltre cancella le entità dall'engine e libera risorse.
     */
    public void saveMapEntities() {
        new Thread(() -> {
            FileHandle fileHandle = Gdx.files.local("save/maps/" + currentMap.nome + ".json");
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);

            // Se esiste già un file della mappa, svuota la lista
            if (mapEntityInstances.get(currentMap.nome) != null)
                mapEntityInstances.get(currentMap.nome).clear();

            // Pulisce tutte le entità
            Array<EntityInstance> instances = entityEngine.clear();
            mapEntityInstances.put(currentMap.nome, instances);

            // Salva in JSON
            String string = json.prettyPrint(instances);

            fileHandle.writeString(string, false);  // Scrive su file
        }).start();
        currentMap.dispose();
    }

    /**
     * @return La mappa attualmente caricata
     */
    public Map getMap() {
        return currentMap;
    }

    /**
     * @return True se l'ambiente è aperto (serve per decidere se disegnare UI, HUD ecc.)
     */
    public boolean disegnaUI() {
        return ambienteAperto;
    }
}
