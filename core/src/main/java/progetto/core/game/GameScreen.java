package progetto.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import progetto.core.Core;
import progetto.core.Gui;
import progetto.entity.Engine;
import progetto.entity.components.specific.ai.StateComponent;
import progetto.entity.components.specific.base.Cooldown;
import progetto.entity.entities.base.EntityConfig;
import progetto.entity.systems.specific.*;
import progetto.factories.EntityConfigFactory;
import progetto.factories.EntityFactory;
import progetto.graphics.shaders.specific.ColorFilter;
import progetto.graphics.shaders.specific.PlayerLight;
import progetto.graphics.shaders.specific.Vignette;
import progetto.input.DebugWindow;
import progetto.input.TerminalCommand;
import progetto.player.ManagerCamera;
import progetto.player.Player;
import progetto.player.inventory.Inventory;
import progetto.screens.DefeatScreen;
import progetto.statemachines.ManagerGame;
import progetto.world.CollisionManager;
import progetto.world.WorldManager;
import progetto.world.map.Map;
import progetto.world.map.MapManager;

public class GameScreen implements Screen {

    // Costante per il passo fisico (60Hz)
    public static final float STEP = 1 / 60f;

    private final TerminalCommand terminalCommand;
    private final GameTime time;
    private final GameDrawer drawer;
    public FitViewport viewport;
    GameInfo info;
    DefaultStateMachine<GameScreen, ManagerGame> state;
    Cooldown resetTimeScale;
    Box2DDebugRenderer debugHitbox;
    private DebugWindow debugWindow;
    private Inventory inventory;
    private Gui gui;
    private float timeScale = 1f;
    private boolean loaded = false;

    private Player player;

    /**
     * Costruttore del gioco
     *
     * @param core gestore degli schermi
     */
    public GameScreen(final Core core) {
        GameLoader.loadWorld();
        drawer = new GameDrawer(this);
        this.time = new GameTime();

        this.terminalCommand = new TerminalCommand(this);
        this.terminalCommand.start();
        this.loadGame(core);
        CollisionManager listener = new CollisionManager();
        WorldManager.getInstance().setContactListener(listener);

    }

    public GameDrawer getGameDrawer() {
        return drawer;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void show() {
        // Inizializza la finestra di debug
        initializeDebugWindow();

        // Carica gli asset
        loadAssets();

        // Inizializza gli oggetti di gioco
        initializeGameObjects();

        // Inizializza il renderer di Box2D
        initializeBox2DRenderer();

        // Verifica lo stato del giocatore e gestisci il respawn
        checkPlayerRespawn();

        // Imposta la posizione della telecamera
        updateCameraPosition();
    }

    private void initializeDebugWindow() {
        Skin skin = new Skin(Gdx.files.internal("skins/metal-ui.json"));
        debugWindow = new DebugWindow(this, "Debug", skin);
        Window.WindowStyle style = new Window.WindowStyle();
        NinePatch patch = new NinePatch(new Texture(Gdx.files.internal("WindowUi.png")), 7, 7, 7, 7 );
        NinePatchDrawable drawable = new NinePatchDrawable(patch);
        style.background = drawable;
        style.titleFont = new BitmapFont();
        inventory = new Inventory(this, "Inventory", style);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(debugWindow.getStage());   // Processa gli input per la scena principale
        inputMultiplexer.addProcessor(inventory.getStage());  // Processa gli input per la finestra di debug

        // Imposta l'input processor globale
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void loadAssets() {
        // Carica gli asset necessari
        Core.assetManager.load("entities/Finn.png", Texture.class);
        Core.assetManager.load("particle/particle.png", Texture.class);
        Core.assetManager.load("sounds/gunshot.mp3", Sound.class);
        Core.assetManager.load("sounds/fireball.mp3", Sound.class);
        Core.assetManager.load("entities/Lich.png", Texture.class);
        Core.assetManager.load("entities/nemico.png", Texture.class);
        Core.assetManager.load("entities/Finn/attack/sword.png", Texture.class);
        Core.assetManager.finishLoading();

    }

    private void initializeGameObjects() {
        if (!loaded) {
            info.engine = new Engine(this.info);


            Core.assetManager.finishLoading();

            EntityConfig p = EntityConfigFactory.createPlayerConfig();
            player = new Player(p, info.engine);

            info.engine.summon(player);

//            for (int i = 0; i < 10; i++) {
//                for (int j = 0; j < 10; j++) {
//                    EntityConfig e = EntityConfigFactory.createEntityConfig("Finn", 8 + i, 10 + j);
//                    info.engine.summon(EntityFactory.createEnemy("Finn", e, info.engine, 4));
//                    //
//                }
//            }
            info.engine.summon(EntityFactory.createSword(10, 10, 0.2f, 1f, new Vector2(0, -0.5f), 50, info.engine, null));

            info.engine.addSystem(
                new CullingSystem(),
                new CooldownSystem(),
                new UserInputSystem(),
                new PlayerSystem(),
                new DeathSystem(),
                new MovementSystem(),
                new SpeedLimiterSystem(),
                new NodeTrackerSystem(),
                new StatemachineSystem(),
                new SkillSystem(),
                new RangeSystem(),
                new HitSystem(),
                new KnockbackSystem(),
                new ItemHoldingSystem()
            );
            info.mapManager = new MapManager(viewport, this.info.engine, 1);
            loaded = true;
            drawer.addShader(Vignette.getInstance());
            drawer.addShader(ColorFilter.getInstance(0.5f, 0.5f, 0.55f));
            drawer.addShader(PlayerLight.getInstance(getEntityManager().player(), 0.10f));
        }
    }

    private void checkPlayerRespawn() {
        StateComponent state = getEntityManager().player().components.get(StateComponent.class);
        if (!state.isAlive()) {
            getEntityManager().player().respawn();
        }
    }

    private void updateCameraPosition() {

        ManagerCamera.getInstance().update();
    }

    private void initializeBox2DRenderer() {
        debugHitbox = new Box2DDebugRenderer();
        debugHitbox.setDrawVelocities(true);
    }

    @Override
    public void render(float delta) {
        //info.screen.setTimeScale(3f, 1f);
        // Controlla se il giocatore è morto
        checkPlayerDeath();

        // Aggiorna la scala del tempo
        updateTimeScale(delta);

        // Gestisci il rendering della vignetta
        drawWithShaders(delta);

        // Disegna la finestra di debug
        debugWindow.updateDebugInfo(Gdx.graphics.getFramesPerSecond(), Gdx.app.getJavaHeap());
        debugWindow.update();
        inventory.update();
    }

    public void updateWorld() {
        WorldManager.update();
    }

    private void checkPlayerDeath() {
        Player player = this.info.engine.player();
        StateComponent state = player.components.get(StateComponent.class);
        if (!state.isAlive()) {
            info.core.setScreen(new DefeatScreen(info.core));
        }
    }

    public void drawWithShaders(float delta) {
        //Lettura dello schermo

        time.update(delta); // Tempo aggiornato
        state.update(); // Aggiorno il gioco


    }

    /**
     * Aggiorna lo stato di gioco
     *
     * @param delta tempo trascorso dall'ultimo frame
     */
    public void update(float delta) {
        // Aggiorna la logica delle entità
        updateEntities(delta);

        // Aggiorna la mappa
        updateMap();

        // Aggiorna la telecamera
        updateCamera();

        // Gestisci la scala del tempo
        updateTimeScale(delta);
    }

    private void updateEntities(float delta) {
        this.getEntityManager().render(delta);
    }

    private void updateMap() {
        this.getMapManager().render();
    }

    private void updateCamera() {
        this.updateCamera(this.getMapManager().disegnaUI());
    }

    private void updateTimeScale(float delta) {
        this.resetTimeScale.update(delta);
        if (this.resetTimeScale.isReady) {
            this.timeScale = 1f;
        }
    }

    /**
     * Disegna il gioco (Mappa, entità, ui)
     */
    public void draw() {
        //Impostazione dello sfondo
        Color darkGray = new Color(0.3f, 0.3f, 0.3f, 1.0f); // Grigio scuro
        ScreenUtils.clear(darkGray); // Clear dello schermo
        // Renderizza la mappa
        renderMap();
        // Renderizza le entità
        renderEntities();

        // Esegui il rendering del debug se attivato
        if (DebugWindow.renderHitboxes()) debug();

        // Se necessario, disegna la GUI
        if (getMapManager().disegnaUI()) {
            renderGUI();
        }
    }

    private void renderMap() {
        OrthogonalTiledMapRenderer mapRenderer = this.info.mapManager.getMap().getMapRenderer();
        ManagerCamera.getInstance().update();
        mapRenderer.setView(ManagerCamera.getInstance());

        mapRenderer.render();
        if (DebugWindow.renderPathfinding()) {
            Map.getGraph().drawConnections(info.core.renderer);
            Map.getGraph().drawNodes(info.core.renderer);
        }
    }

    private void renderEntities() {
        this.getEntityManager().draw();
    }

    private void renderGUI() {
        gui.draw();
    }

    // Getter per variabili principali
    public Engine getEntityManager() {
        return this.info.engine;
    }

    public MapManager getMapManager() {
        return this.info.mapManager;
    }

    public float getTimeScale() {
        return timeScale;
    }

    public GameInfo getInfo() {
        return this.info;
    }

    /**
     * Carica il gioco
     *
     * @param core core del programma, gestore degli schermi
     */
    public void loadGame(final Core core) {
        this.state = new DefaultStateMachine<>(this);
        this.state.changeState(ManagerGame.PLAYING);
        this.info = new GameInfo();
        this.info.core = core;
        this.info.screen = this;
        this.resetTimeScale = new Cooldown(0);
        this.gui = new Gui(this);
        this.viewport = new FitViewport(16f, 9f, ManagerCamera.getInstance());
        this.viewport.apply();
    }

    /**
     * Imposta la velocità del tempo
     *
     * @param timeScale velocità del tempo
     * @param time      per quanto tempo
     */
    public void setTimeScale(float timeScale, float time) {
        this.timeScale = timeScale;
        resetTimeScale.reset(time);
    }

    /**
     * Disegna le hitbox di gioco
     */
    private void debug() {
        // Renderizza il debug di Box2D
        debugHitbox.render(WorldManager.getInstance(), ManagerCamera.getInstance().combined);
    }

    /**
     * Aggiorna la telecamera
     *
     * @param boundaries richiesta di controllare i bordi della mappa
     */
    public void updateCamera(boolean boundaries) {
        // Aggiorna la posizione della telecamera
        ManagerCamera.update(info.engine, viewport, time.delta, false);

        this.info.core.batch.setProjectionMatrix(ManagerCamera.getInstance().combined);
        this.info.core.renderer.setProjectionMatrix(ManagerCamera.getInstance().combined);
    }

    public GameTime getTime() {
        return time;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void resize(int width, int height) {
        // Gestisce il ridimensionamento della finestra
        viewport.update(width, height, false);
    }

    @Override
    public void dispose() {
        // Pulisce le risorse utilizzate
        this.info.core.batch.dispose();
        this.info.core.renderer.dispose();
        this.terminalCommand.stopRunning();
    }
}
