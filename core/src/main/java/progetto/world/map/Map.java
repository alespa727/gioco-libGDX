package progetto.world.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import progetto.core.game.GameScreen;
import progetto.ECS.EntityEngine;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.core.CameraManager;
import progetto.world.WorldManager;
import progetto.world.collision.CollisionGenerator;
import progetto.world.events.EventManager;
import progetto.world.graph.GameGraph;

/**
 * Classe che rappresenta una mappa di gioco.
 * Gestisce il caricamento della mappa da file tmx, la generazione delle collisioni,
 * la gestione degli eventi e il rendering.
 */
public class Map implements Disposable {

    /** Indica se il grafo è stato caricato */
    public static boolean isGraphLoaded = false;
    /** Indica se la mappa è stata caricata */
    public static boolean isLoaded = false;
    private static int width;
    private static int height;
    private static GameGraph graph;

    /** Nome identificativo della mappa */
    public final String nome;

    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;
    private final EventManager eventManager;
    private final CollisionGenerator generator;

    /**
     * Costruttore: carica e inizializza la mappa.
     *
     * @param nome Nome della mappa (senza estensione)
     * @param game Istanza del GameScreen principale
     * @param entityEngine Motore delle entità
     * @param mapManager Manager delle mappe
     * @param x Posizione X iniziale dove teletrasportare il player
     * @param y Posizione Y iniziale dove teletrasportare il player
     */
    public Map(String nome, GameScreen game, EntityEngine entityEngine, MapManager mapManager, float x, float y) {
        this.nome = nome;
        this.map = new TmxMapLoader().load("maps/".concat(nome).concat(".tmx"));
        this.renderer = new OrthogonalTiledMapRenderer(map, MapManager.TILE_SIZE, entityEngine.game.batch);
        loadTextureFilter();


        System.out.println(x + "" + y);

        // Inizializza il gestore degli eventi
        this.eventManager = new EventManager(game, this, mapManager, entityEngine);
        this.eventManager.create(map.getLayers().get("eventi"));

        // Salva le dimensioni della mappa
        width = (Integer) map.getProperties().get("width");
        height = (Integer) map.getProperties().get("height");

        // Genera collisioni e grafo per pathfinding
        this.generator = new CollisionGenerator((TiledMapTileLayer) map.getLayers().get("collisioni"), this);
        graph = new GameGraph(width, height, generator.getCollision());

        // Teletrasporta il player e posiziona la camera
        if (entityEngine.player().contains(PhysicsComponent.class)) {
            Gdx.app.postRunnable(() -> entityEngine.player().get(PhysicsComponent.class).teleport(new Vector2(x, y)));
            CameraManager.getInstance().position.set(entityEngine.player().get(PhysicsComponent.class).getPosition(), 0);
            CameraManager.getInstance().update();
        }

        isGraphLoaded = true;
        isLoaded = true;
    }

    /**
     * Genera le collisioni fisiche basate sul layer "collisionobjects".
     * Va chiamato separatamente se servono collisioni specifiche da un altro layer.
     */
    public void generateCollisions() {
        generator.generateCollisions(map.getLayers().get("collisionobjects"));
    }

    /**
     * Imposta il filtro di texture su Nearest per tutte le tile,
     * in modo da mantenere uno stile pixel-art (no smoothing).
     */
    private void loadTextureFilter() {
        for (TiledMapTileSet tileset : map.getTileSets()) {
            for (TiledMapTile tile : tileset) {
                Texture texture = tile.getTextureRegion().getTexture();
                texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            }
        }
    }

    /**
     * @return Il grafo di pathfinding basato sulla mappa corrente
     */
    public static GameGraph getGraph() {
        return graph;
    }

    /**
     * @return Larghezza della mappa in tile
     */
    public static int width() {
        return width;
    }

    /**
     * @return Altezza della mappa in tile
     */
    public static int height() {
        return height;
    }

    /**
     * @return Renderer utilizzato per disegnare la mappa
     */
    public OrthogonalTiledMapRenderer getRenderer() {
        return renderer;
    }

    /**
     * Aggiorna gli eventi della mappa. Va chiamato ad ogni frame.
     *
     * @param delta Tempo trascorso dall'ultimo frame
     */
    public void render(float delta) {
        eventManager.update(delta);
    }

    /**
     * Libera le risorse associate alla mappa (eventi e riferimenti vari).
     */
    @Override
    public void dispose() {
        eventManager.destroy();
        WorldManager.clearMap();
    }
}