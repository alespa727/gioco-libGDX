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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import progetto.core.Core;
import progetto.core.CoreConfig;
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
import progetto.input.KeyHandler;
import progetto.input.TerminalCommand;
import progetto.player.ManagerCamera;
import progetto.player.Player;
import progetto.player.inventory.Inventory;
import progetto.screens.DefeatScreen;
import progetto.screens.PauseScreen;
import progetto.statemachines.ManagerGame;
import progetto.world.CollisionManager;
import progetto.world.WorldManager;
import progetto.world.map.Map;
import progetto.world.map.MapManager;

public class GameScreen implements Screen {

    // Costante per il passo fisico (60Hz)
    public static final float STEP = 1 / 60f;



    private Player player;

    private final GameTime time;
    private final ShaderPipeliner shaders;

    private TerminalCommand terminal;
    public Engine engine;
    public MapManager map;
    public Core core;
    public SpriteBatch batch;

    public FitViewport viewport;



    DefaultStateMachine<GameScreen, ManagerGame> state;
    Cooldown resetTimeScale;
    Box2DDebugRenderer debug;
    private DebugWindow debugWindow;
    private Inventory inventory;
    private Gui gui;
    private float timeScale = 1f;
    private boolean loaded = false;

    /**
     * Costruttore del gioco
     *
     * @param core gestore degli schermi
     */
    public GameScreen(final Core core) {
        GameLoader.loadWorld();
        this.core = core;
        this.batch = core.batch;
        this.shaders = new ShaderPipeliner(this);
        this.time = new GameTime();

        this.terminal = new TerminalCommand(this);
        this.terminal.start();

        this.loadGame(core);
        CollisionManager listener = new CollisionManager();
        WorldManager.getInstance().setContactListener(listener);

    }

    public void update(float delta) {

        time.update(delta); // Tempo aggiornato
        SpriteBatch batch = core.batch;

        if (Gdx.input.isKeyJustPressed(CoreConfig.getFERMAGIOCO())) {
            core.setScreen(new PauseScreen(core, this));
        }

        // Aggiorna il gioco finché necessario
        while (time.getAccumulator() >= STEP) {
            float scaledTime = STEP * getTimeScale();
            WorldManager.getInstance().step(scaledTime, 8, 8);
            // Disegna il gioco
            step(scaledTime);
            WorldManager.update();
            time.setAccumulator(time.getAccumulator() - STEP);
            KeyHandler.input();
        }

        getGameDrawer().draw(batch);

    }

    /**
     * Aggiorna lo stato di gioco
     * @param delta tempo trascorso dall'ultimo frame
     */
    public void step(float delta) {

        Player player = engine.player();
        StateComponent state = player.components.get(StateComponent.class);
        if (!state.isAlive()) {
            core.setScreen(new DefeatScreen(core));
        }

        // Aggiorna la logica delle entità
        engine.render(delta);

        // Aggiorna la mappa
        map.render();

        // Aggiorna la telecamera
        ManagerCamera.update(engine, viewport, time.delta, false);

        this.core.batch.setProjectionMatrix(ManagerCamera.getInstance().combined);
        this.core.renderer.setProjectionMatrix(ManagerCamera.getInstance().combined);

        // Gestisci la scala del tempo
        this.resetTimeScale.update(delta);
        if (this.resetTimeScale.isReady) {
            this.timeScale = 1f;
        }
    }

    @Override
    public void show() {
        // Inizializza la finestra di debug
        initializeDebugWindow();

        // Inizializza gli oggetti di gioco
        loadGameEngine();

        // Inizializza il renderer di Box2D
        debug = new Box2DDebugRenderer();
        debug.setDrawVelocities(true);

        // Verifica lo stato del giocatore e gestisci il respawn
        StateComponent state = player.get(StateComponent.class);
        if (!state.isAlive()) {
            player.respawn();
        }

        // Imposta la posizione della telecamera
        ManagerCamera.getInstance().update();
    }

    private void initializeDebugWindow() {
        debugWindow = new DebugWindow(this);
        inventory = new Inventory(getWindowStyle());

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(debugWindow.getStage());   // Processa gli input per la scena principale
        inputMultiplexer.addProcessor(inventory.getStage());  // Processa gli input per la finestra di debug

        // Imposta l'input processor globale
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public Window.WindowStyle getWindowStyle() {
        Window.WindowStyle style = new Window.WindowStyle();
        NinePatch patch = new NinePatch(new Texture(Gdx.files.internal("WindowUi.png")), 7, 7, 7, 7 );
        NinePatchDrawable drawable = new NinePatchDrawable(patch);
        style.background = drawable;
        style.titleFont = new BitmapFont();
        return style;
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

    private void loadGameEngine() {
        if (!loaded) {
            this.loadAssets();
            Core.assetManager.finishLoading();

            engine = new Engine(this);


            EntityConfig p = EntityConfigFactory.createPlayerConfig();
            player = new Player(p, engine);

            engine.summon(player);

//            for (int i = 0; i < 10; i++) {
//                for (int j = 0; j < 10; j++) {
//                    EntityConfig e = EntityConfigFactory.createEntityConfig("Finn", 8 + i, 10 + j);
//                    info.engine.summon(EntityFactory.createEnemy("Finn", e, info.engine, 4));
//                    //
//                }
//            }
            engine.summon(EntityFactory.createSword(10, 10, 0.2f, 1f, new Vector2(0, -0.5f), 50, engine, null));

            engine.addSystem(
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

            map = new MapManager(viewport, engine, 1);

            loaded = true;
            shaders.addShader(Vignette.getInstance());
            shaders.addShader(ColorFilter.getInstance(0.5f, 0.5f, 0.55f));
            shaders.addShader(PlayerLight.getInstance(engine.player(), 0.10f));
        }
    }

    @Override
    public void render(float delta) {

        // Gestisci il rendering della vignetta
        update(delta);

        // Disegna la finestra di debug
        debugWindow.updateDebugInfo(Gdx.graphics.getFramesPerSecond(), Gdx.app.getJavaHeap());
        debugWindow.update();
        inventory.update();

    }


    /**
     * Disegna il gioco (Mappa, entità, ui)
     */
    public void draw() {
        //Impostazione dello sfondo
        Color darkGray = new Color(0.3f, 0.3f, 0.3f, 1.0f); // Grigio scuro
        ScreenUtils.clear(darkGray); // Clear dello schermo
        // Renderizza la mappa
        this.drawMap();
        // Renderizza le entità
        engine.draw();

        // Esegui il rendering del debug se attivato
        if (DebugWindow.renderHitboxes()) debug.render(WorldManager.getInstance(), ManagerCamera.getInstance().combined);

        // Se necessario, disegna la GUI
        if (map.disegnaUI()) {
            gui.draw();
        }
    }

    private void drawMap() {
        OrthogonalTiledMapRenderer mapRenderer = map.getMap().getMapRenderer();
        ManagerCamera.getInstance().update();
        mapRenderer.setView(ManagerCamera.getInstance());

        mapRenderer.render();
        if (DebugWindow.renderPathfinding()) {
            Map.getGraph().drawConnections(core.renderer);
            Map.getGraph().drawNodes(core.renderer);
        }
    }

    // Getter per variabili principali
    public Engine getEntityManager() {
        return engine;
    }

    public MapManager getMap() {
        return map;
    }

    public float getTimeScale() {
        return timeScale;
    }

    /**
     * Carica il gioco
     *
     * @param core core del programma, gestore degli schermi
     */
    public void loadGame(final Core core) {
        this.state = new DefaultStateMachine<>(this);
        this.state.changeState(ManagerGame.PLAYING);
        this.core = core;
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

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        this.terminal.stopRunning();
    }

    @Override
    public void resize(int width, int height) {
        // Gestisce il ridimensionamento della finestra
        viewport.update(width, height, false);
    }

    @Override
    public void dispose() {
        // Pulisce le risorse utilizzate
        this.core.batch.dispose();
        this.core.renderer.dispose();
        this.terminal.stopRunning();
    }


    public ShaderPipeliner getGameDrawer() {
        return shaders;
    }

    public Player getPlayer() {
        return player;
    }

}
