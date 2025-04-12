package progetto.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import progetto.Core;
import progetto.gameplay.entity.components.entity.Cooldown;
import progetto.gameplay.map.Map;
import progetto.gameplay.player.Player;
import progetto.gameplay.manager.ManagerCamera;
import progetto.gameplay.manager.ManagerEvent;
import progetto.gameplay.manager.ManagerGame;
import progetto.gameplay.manager.ManagerWorld;
import progetto.gameplay.manager.ManagerEntity;
import progetto.gameplay.map.MapManager;
import progetto.menu.DebugWindow;
import progetto.menu.DefeatScreen;
import progetto.utils.*;

public class GameScreen implements Screen {

    // Costante per il passo fisico (60Hz)
    public static final float STEP = 1 / 60f;

    private TerminalCommand terminalCommand;
    private DebugWindow debugWindow;

    private Window window;
    public FitViewport viewport;
    private final GameTime time;
    private Gui gui;
    private final Vignette vignette;

    GameInfo info;
    DefaultStateMachine<GameScreen, ManagerGame> state;
    Cooldown resetTimeScale;

    Box2DDebugRenderer debugHitbox;

    private float timeScale = 1f;

    private boolean loaded = false;

    /**
     * Costruttore del gioco
     * @param core gestore degli schermi
     */
    public GameScreen(final Core core) {
        GameLoader.loadWorld();
        this.time = new GameTime();
        this.vignette = Vignette.getInstance();
        this.terminalCommand = new TerminalCommand(this);
        this.terminalCommand.start();
        this.loadGame(core);
        ManagerEvent listener = new ManagerEvent();
        ManagerWorld.getInstance().setContactListener(listener);
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
    }

    private void loadAssets() {
        // Carica gli asset necessari
        Core.assetManager.load("entities/Finn.png", Texture.class);
        Core.assetManager.load("entities/circle.png", Texture.class);
        Core.assetManager.load("sounds/gunshot.mp3", Sound.class);
        Core.assetManager.load("sounds/fireball.mp3", Sound.class);
        Core.assetManager.finishLoading();

    }

    private void initializeGameObjects() {
        if (!loaded) {
            info.managerEntity = new ManagerEntity(this.info);
            info.mapManager = new MapManager(viewport, this.info.managerEntity, 1);
            loaded = true;
        }
    }

    private void checkPlayerRespawn() {
        if (!getEntityManager().player().getState().isAlive()) {
            getEntityManager().player().respawn();
        }
    }

    private void updateCameraPosition() {
        ManagerCamera.getInstance().position.set(this.info.managerEntity.player().getPosition(), 0);
        ManagerCamera.getInstance().update();
    }

    private void initializeBox2DRenderer() {
        debugHitbox = new Box2DDebugRenderer();
        debugHitbox.setDrawVelocities(true);
    }

    @Override
    public void render(float delta) {
        // Aggiorna lo stato del mondo
        updateWorld();

        // Controlla se il giocatore è morto
        checkPlayerDeath();

        // Aggiorna la scala del tempo
        updateTimeScale(delta);

        // Gestisci il rendering della vignetta
        renderVignette(delta);

        // Esegui il rendering del debug se attivato
        if (DebugWindow.renderHitboxes()) debug();

        // Disegna la finestra di debug
        debugWindow.updateDebugInfo(Gdx.graphics.getFramesPerSecond(), Gdx.app.getJavaHeap());
        debugWindow.draw();
    }

    private void updateWorld() {
        ManagerWorld.update();
    }

    private void checkPlayerDeath() {
        Player player = this.info.managerEntity.player();
        if (!player.getState().isAlive()) {
            info.core.setScreen(new DefeatScreen(info.core));
        }
    }

    private void renderVignette(float delta) {
        vignette.begin();
        ScreenUtils.clear(0, 0, 0, 1);
        time.update(delta);
        this.state.update();
        vignette.end();
        vignette.draw(info.core.batch);
    }

    /**
     * Aggiorna lo stato di gioco
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
        // Renderizza la mappa
        renderMap();

        // Renderizza le entità
        renderEntities();

        // Se necessario, disegna la GUI
        if (getMapManager().disegnaUI()) {
            renderGUI();
        }
    }

    private void renderMap() {
        OrthogonalTiledMapRenderer mapRenderer = this.info.mapManager.getMap().getMapRenderer();
        mapRenderer.setView(ManagerCamera.getInstance());
        mapRenderer.render();
        if(DebugWindow.renderPathfinding()){
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
    public ManagerEntity getEntityManager() {
        return this.info.managerEntity;
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
     * @param timeScale velocità del tempo
     * @param time per quanto tempo
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
        debugHitbox.render(ManagerWorld.getInstance(), ManagerCamera.getInstance().combined);
    }

    /**
     * Aggiorna la telecamera
     * @param boundaries richiesta di controllare i bordi della mappa
     */
    public void updateCamera(boolean boundaries) {
        // Aggiorna la posizione della telecamera
        ManagerCamera.update(info.managerEntity, viewport, time.delta, boundaries);
        float PPM = 128f;
        OrthographicCamera camera = ManagerCamera.getInstance();

        // Dopo averla spostata
        camera.position.set(
            Math.round(camera.position.x * PPM) / PPM,
            Math.round(camera.position.y * PPM) / PPM,
            0
        );
        camera.update();

        this.info.core.batch.setProjectionMatrix(ManagerCamera.getInstance().combined);
        this.info.core.renderer.setProjectionMatrix(ManagerCamera.getInstance().combined);
    }

    public GameTime getTime() {
        return time;
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
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
    }
}
