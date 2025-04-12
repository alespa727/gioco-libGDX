package progetto.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import progetto.Core;
import progetto.audio.AudioManager;
import progetto.gameplay.entity.components.entity.Cooldown;
import progetto.gameplay.entity.types.living.combat.player.Player;
import progetto.gameplay.manager.ManagerCamera;
import progetto.gameplay.manager.ManagerEvent;
import progetto.gameplay.manager.ManagerGame;
import progetto.gameplay.manager.ManagerWorld;
import progetto.gameplay.manager.entity.ManagerEntity;
import progetto.gameplay.map.MapManager;
import progetto.menu.DefeatScreen;
import progetto.utils.KeyHandler;

public class Game implements Screen {

    GameLoader gameLoader;

    // Costante per il passo fisico (60Hz)
    public static final float STEP = 1 / 60f;

    // Variabili principali per la gestione dello stato di gioco e del tempo
    private Player player;

    private GameInfo info;
    private DefaultStateMachine<Game, ManagerGame> state;
    private Cooldown timeScaleCooldown;

    // Shaders
    private ShaderProgram program;
    private FrameBuffer fbo1;

    // Ui e debug
    private Box2DDebugRenderer debug;
    private Gui gui;

    // Viewport
    public FitViewport viewport;

    // Variabili di stato
    public boolean flag = false;

    // Gestori del tempo
    public float delta;
    public float accumulator = 0f;
    private float tempoTrascorso;
    private float timeScale = 1f;

    /**
     * Costruttore del gioco
     * @param core gestore degli schermi
     */
    public Game(final Core core) {
        this.gameLoader = new GameLoader();
        this.gameLoader.loadWorld();
        this.loadGame(core);
        ManagerEvent listener = new ManagerEvent();
        ManagerWorld.getInstance().setContactListener(listener);
    }

    /**
     * Crea i frame buffer (posti in cui salvare tutto cio+ che è stato disegnato), per applicare la shader
     * @param width larghezza schermo
     * @param height altezza schermo
     */
    private void createFrameBuffer(int width, int height) {
        this.fbo1 = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
    }

    /**
     * Crea le shader
     */
    private void createShaderProgram() {
        // Carica e crea il programma shader (per effetti grafici avanzati)
        String vertexShader = Gdx.files.internal("shaders/vertex.vsh").readString();
        String fragmentShader = Gdx.files.internal("shaders/vignette.fsh").readString();
        this.program = new ShaderProgram(vertexShader, fragmentShader);
        ShaderProgram.pedantic = false; // se vuoi evitare errori per uniform "extra"
    }

    @Override
    public void show() {
        // Carica gli asset necessari all'inizio

        Core.assetManager.load("entities/Finn.png", Texture.class);
        Core.assetManager.load("entities/circle.png", Texture.class);
        Core.assetManager.finishLoading();
<<<<<<< Updated upstream
=======

        AudioManager.addSound("sounds/gunshot.mp3");
        AudioManager.addSound("sounds/fireball.mp3");
>>>>>>> Stashed changes

        // Crea e imposta gli shader e i framebuffer
        this.createShaderProgram();
        this.createFrameBuffer(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (!flag){
            loadGameManagers();
        }

        player = this.info.managerEntity.player();

        // Se il giocatore è morto, lo fa respawnare
        if (!player.getState().isAlive()) {
            player.respawn();
        }

        ManagerCamera.getInstance().position.set(this.info.managerEntity.player().getPosition(), 0);
        ManagerCamera.getInstance().update();

        // Inizializza il renderer di debug di Box2D
        debug = new Box2DDebugRenderer();
        debug.setDrawVelocities(true);
    }

    @Override
    public void render(float delta) {
        if (!player.getState().isAlive()) {
            info.core.setScreen(new DefeatScreen(info.core));
        }
        ManagerWorld.update();
        timeScaleCooldown.update(delta);
        if (timeScaleCooldown.isReady) timeScale = 1f;
        fbo1.begin();
        ScreenUtils.clear(0, 0, 0, 1);
        this.delta = delta;
        this.tempoTrascorso += delta;
        this.state.update();
        if (KeyHandler.debug) debug();  // Renderizza il debug di Box2D se attivato
        fbo1.end();

        applyShader();
    }

    /**
     * Aggiorna lo stato di gioco
     * @param delta tempo trascorso dall'ultimo frame
     */
    public void update(float delta) {
        // Aggiorna la logica di gioco
        this.getEntityManager().render(delta);
        this.getMapManager().render();
        this.updateCamera(this.getMapManager().disegnaUI());

        this.timeScaleCooldown.update(delta);
        if (this.timeScaleCooldown.isReady) {
            this.timeScale = 1f;
        }
    }

    /**
     * Disegna il gioco (Mappa, entità, ui)
     */
    public void draw() {
        // Renderizza gli oggetti sulla mappa e l'entità del giocatore
        OrthogonalTiledMapRenderer mapRenderer = this.info.mapManager.getMap().getMapRenderer();
        mapRenderer.setView(ManagerCamera.getInstance());
        mapRenderer.render();

        //Map.getGraph().drawNodes(info.core.renderer);

        this.getEntityManager().draw();

        if (getMapManager().disegnaUI()) drawGUI();
    }

    /**
     * Disegna la ui
     */
    private void drawGUI() {
        // Disegna la GUI
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
     * Performance
     */
    @SuppressWarnings("unused")
    public void performanceInfo() {
        System.out.println(Gdx.graphics.getFramesPerSecond() + " fps");
        System.out.println("Java Heap: " + Gdx.app.getJavaHeap() / (1024 * 1024) + " MB");
        System.out.println("Native Heap: " + Gdx.app.getNativeHeap() / (1024 * 1024) + " MB");
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
        this.timeScaleCooldown = new Cooldown(0);
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
        timeScaleCooldown.reset(time);
    }

    /**
     * Carica il gioco
     */
    private void loadGameManagers() {
        this.info.managerEntity = new ManagerEntity(this.info);
        this.info.mapManager = new MapManager(viewport, this.info.managerEntity, 1);
        this.flag = true;
    }

    /**
     * Applica la shader ai framebuffer
     */
    private void applyShader() {
        program.setUniformf("resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Applica gli shader per l'effetto grafico
        Texture fboText = fbo1.getColorBufferTexture();
        TextureRegion fboTextReg = new TextureRegion(fboText);
        fboTextReg.flip(false, true);

        ScreenUtils.clear(0, 0, 0, 1);
        this.info.core.batch.begin();
        this.info.core.batch.setShader(program);
        this.info.core.batch.draw(fboTextReg, ManagerCamera.getFrustumCorners()[0].x, ManagerCamera.getFrustumCorners()[0].y, ManagerCamera.getViewportWidth(), ManagerCamera.getViewportHeight());
        this.info.core.batch.setShader(null);
        this.info.core.batch.end();
    }

    /**
     * Disegna le hitbox di gioco
     */
    private void debug() {
        // Renderizza il debug di Box2D
        debug.render(ManagerWorld.getInstance(), ManagerCamera.getInstance().combined);
    }

    /**
     * Aggiorna la telecamera
     * @param boundaries richiesta di controllare i bordi della mappa
     */
    public void updateCamera(boolean boundaries) {
        // Aggiorna la posizione della telecamera
        ManagerCamera.update(info.managerEntity, viewport, delta, boundaries);
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
