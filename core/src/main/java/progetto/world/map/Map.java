package progetto.world.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import progetto.core.game.GameScreen;
import progetto.entity.EntityEngine;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.core.CameraManager;
import progetto.world.WorldManager;
import progetto.world.collision.CollisionGenerator;
import progetto.world.events.EventManager;
import progetto.world.graph.GameGraph;

public class Map implements Disposable {
    public static boolean isGraphLoaded = false;
    public static boolean isLoaded = false;
    private static int width;
    private static int height;
    private static GameGraph graph;

    public final String nome;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private EventManager eventManager;
    private CollisionGenerator generator;


    /* Creazione nuova mappa */
    public Map(String nome, GameScreen game, EntityEngine entityEngine, MapManager mapManager, float x, float y) {

        this.nome = nome;
        this.map = new TmxMapLoader().load("maps/".concat(nome).concat(".tmx"));
        this.renderer = new OrthogonalTiledMapRenderer(map, MapManager.TILE_SIZE, entityEngine.game.batch);
        loadTextureFilter();
        this.eventManager = new EventManager(game, this, mapManager, entityEngine);
        this.eventManager.create(map.getLayers().get("eventi"));

        // Salvataggio grandezza mappa
        width = (Integer) map.getProperties().get("width");
        height = (Integer) map.getProperties().get("height");

        // Crea un grafo basatosi sulle collisioni
        this.generator = new CollisionGenerator((TiledMapTileLayer) map.getLayers().get("collisioni"), this);
        graph = new GameGraph(width, height, generator.getCollision());

        if (entityEngine.player().contains(PhysicsComponent.class)) {
            Gdx.app.postRunnable(() -> entityEngine.player().get(PhysicsComponent.class).teleport(new Vector2(x, y))); // Teletrasporto player al punto di spawn definito
            CameraManager.getInstance().position.set(entityEngine.player().get(PhysicsComponent.class).getPosition(), 0);
            CameraManager.getInstance().update();
        }

        // Variabili di controllo
        isGraphLoaded = true;
        isLoaded = true;

    }

    public void generateCollisions() {
        generator.generateCollisions(map.getLayers().get("collisionobjects"));
    }

    private void loadTextureFilter(){
        for (TiledMapTileSet tileset : map.getTileSets()) {
            for (TiledMapTile tile : tileset) {
                Texture texture = tile.getTextureRegion().getTexture();
                texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            }
        }
    }

    /**
     * Restituisce il grafo
     */
    public static GameGraph getGraph() {
        return graph;
    }

    /**
     * Restituisce la larghezza della mappa
     */
    public static int width() {
        return width;
    }

    /**
     * Restituisce l'altezza della mappa
     */
    public static int height() {
        return height;
    }

    /**
     * Restituisce l'oggetto che disegna la mappa
     */
    public OrthogonalTiledMapRenderer getRenderer() {
        return renderer;
    }

    /**
     * Update eventi della mappa
     */
    public void render(float delta) {
        eventManager.update(delta);
    }

    @Override
    public void dispose() {
        eventManager.destroy();
        WorldManager.clearMap();
    }
}
