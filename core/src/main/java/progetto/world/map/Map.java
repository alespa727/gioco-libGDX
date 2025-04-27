package progetto.world.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Disposable;
import progetto.entity.Engine;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.factories.BodyFactory;
import progetto.player.ManagerCamera;
import progetto.world.WorldManager;
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
    public Map(String nome, Engine engine, MapManager mapManager, float x, float y) {

        this.nome = nome;
        this.map = new TmxMapLoader().load("maps/".concat(nome).concat(".tmx"));
        this.renderer = new OrthogonalTiledMapRenderer(map, MapManager.TILE_SIZE, engine.game.batch);
        loadTextureFilter();
        this.eventManager = new EventManager(this, mapManager);
        this.eventManager.create(map.getLayers().get("eventi"));

        // Salvataggio grandezza mappa
        width = (Integer) map.getProperties().get("width");
        height = (Integer) map.getProperties().get("height");

        // Crea un grafo basatosi sulle collisioni
        this.generator = new CollisionGenerator((TiledMapTileLayer) map.getLayers().get("collisioni"));
        graph = new GameGraph(width, height, generator.getCollision());

        Gdx.app.postRunnable(() -> engine.player().get(PhysicsComponent.class).teleport(new Vector2(x, y))); // Teletrasporto player al punto di spawn definito
        ManagerCamera.getInstance().position.set(engine.player().get(PhysicsComponent.class).getPosition(), 0);
        ManagerCamera.getInstance().update();

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
    public void render() {
        eventManager.update();
    }

    @Override
    public void dispose() {
        WorldManager.clearMap();
    }
}
