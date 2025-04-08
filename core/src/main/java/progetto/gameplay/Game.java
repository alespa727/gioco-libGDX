package progetto.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import progetto.Core;
import progetto.gameplay.manager.ManagerCamera;
import progetto.gameplay.manager.ManagerGame;
import progetto.gameplay.manager.ManagerWorld;
import progetto.utils.Cooldown;
import progetto.utils.KeyHandler;
import progetto.gameplay.manager.ManagerEntity;
import progetto.gameplay.map.MapManager;

public class Game implements Screen {

    // Costante per il passo fisico (60Hz)
    public static final float STEP = 1 / 60f;

    // Variabili principali per la gestione dello stato di gioco e del tempo
    private final GameInfo gameInfo;
    private final DefaultStateMachine<Game, ManagerGame> gameState;
    public boolean loaded = false;
    public float delta;
    public float accumulator = 0f;
    private float elapsedTime;
    private float timeScale = 1f;
    private final Cooldown timeScaleCooldown;

    // Variabili per la gestione della grafica e degli effetti
    private ShaderProgram shaderProgram;
    private FrameBuffer fbo1;
    private FrameBuffer fbo2;
    private Box2DDebugRenderer debugRenderer;
    private Gui rect;
    private Stage stage;
    private Table root;
    public FitViewport viewport;

    // Costruttore
    public Game(Core game) {
        // Inizializza variabili
        ManagerWorld.init();
        Box2D.init();
        this.gameState = new DefaultStateMachine<>(this);
        this.gameState.changeState(ManagerGame.PLAYING);
        this.gameInfo = new GameInfo();
        this.gameInfo.game = game;
        this.gameInfo.screen = this;
        this.timeScaleCooldown = new Cooldown(0);
    }

    // Imposta la velocità del tempo
    public void setTimeScale(float timeScale, float time) {
        this.timeScale = timeScale;
        timeScaleCooldown.reset(time);
    }

    // Reset della velocità del tempo
    public void resetTimeScale() {
        timeScaleCooldown.update(delta);
        if (timeScaleCooldown.isReady) {
            timeScale = 1f;
        }
    }

    // Metodi di inizializzazione per la grafica
    private void buildFBO(int width, int height) {
        // Crea due FrameBuffer per l'elaborazione grafica
        this.fbo1 = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
        this.fbo2 = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
    }

    private void createShaderProgram() {
        // Carica e crea il programma shader (per effetti grafici avanzati)
        String vertexShader = Gdx.files.internal("shaders/vertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("shaders/fragment.glsl").readString();
        this.shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
    }

    // Metodo per caricare e inizializzare il gioco
    private void load() {
        System.out.println("GameScreen loaded");

        // Crea e imposta la GUI (interfaccia utente)
        this.rect = new Gui(this);
        this.stage = new Stage(new ScreenViewport());
        this.root = new Table();
        this.root.setFillParent(true);
        this.stage.addActor(root);

        // Imposta la visualizzazione
        this.viewport = new FitViewport(16f, 9f, ManagerCamera.getInstance());
        this.viewport.apply();

        // Inizializza i manager di entità e mappa
        this.gameInfo.managerEntity = new ManagerEntity(this.gameInfo);
        this.gameInfo.mapManager = new MapManager(viewport, this.gameInfo.managerEntity, 1);

        this.loaded = true;
    }

    // Metodi della Screen Interface
    @Override
    public void show() {
        // Carica gli asset necessari all'inizio

        Core.assetManager.load("entities/Finn.png", Texture.class);
        Core.assetManager.finishLoading();

        // Crea e imposta gli shader e i framebuffer
        createShaderProgram();
        buildFBO(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Carica la scena di gioco se non è già stata caricata
        if (!loaded){
            load();
        }

        // Se il giocatore è morto, lo fa respawnare
        if (!this.gameInfo.managerEntity.player().isAlive()) {
            this.gameInfo.managerEntity.player().respawn();
        }

        ManagerCamera.getInstance().position.set(this.gameInfo.managerEntity.player().getPosition(), 0);
        ManagerCamera.getInstance().update();


        // Inizializza il renderer di debug di Box2D
        debugRenderer = new Box2DDebugRenderer();
        debugRenderer.setDrawVelocities(true);

    }

    @Override
    public void render(float delta) {
        // Gestione della velocità del tempo
        resetTimeScale();
        KeyHandler.input();
        updateDeltaTime(delta);

        // Inizio della renderizzazione con il primo FBO
        fbo1.begin();
        ScreenUtils.clear(0, 0, 0, 1); // Pulisce lo schermo
        gameState.update();  // Aggiorna lo stato del gioco
        this.gameInfo.mapManager.getMap().render();  // Renderizza la mappa
        if (KeyHandler.debug) Box2DDebugRender();  // Renderizza il debug di Box2D se attivato
        fbo1.end();

        // Applica gli shader per gli effetti grafici
        applyShader();
    }

    @Override
    public void resize(int width, int height) {
        // Gestisce il ridimensionamento della finestra
        viewport.update(width, height, true);
        ManagerCamera.getInstance().update();
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {

    }
    @Override
    public void hide() {}

    @Override
    public void dispose() {
        // Pulisce le risorse utilizzate
        this.gameInfo.game.batch.dispose();
        this.gameInfo.game.renderer.dispose();
        stage.dispose();
    }

    // Metodi di aggiornamento e renderizzazione
    private void updateDeltaTime(float deltaTime) {
        // Aggiorna il delta tempo
        this.delta = deltaTime;
    }

    public void update(float delta) {
        // Aggiorna la logica di gioco
        elapsedTime += delta;
        this.gameInfo.managerEntity.render(delta);
        boolean ambiente = getMapManager().getAmbiente();
        updateCamera(ambiente);
    }

    private void applyShader() {
        // Applica gli shader per l'effetto grafico
        Texture fboText = fbo1.getColorBufferTexture();
        TextureRegion fboTextReg = new TextureRegion(fboText);
        fboTextReg.flip(false, true);

        fbo2.begin();
        ScreenUtils.clear(0, 0, 0, 1);
        this.gameInfo.game.batch.begin();
        this.gameInfo.game.batch.setShader(null);
        this.gameInfo.game.batch.draw(fboTextReg, ManagerCamera.getFrustumCorners()[0].x, ManagerCamera.getFrustumCorners()[0].y, ManagerCamera.getViewportWidth(), ManagerCamera.getViewportHeight());
        this.gameInfo.game.batch.end();
        fbo2.end();

        // Renderizza il secondo FBO
        fboText = fbo2.getColorBufferTexture();
        fboTextReg = new TextureRegion(fboText);
        fboTextReg.flip(false, true);

        this.gameInfo.game.batch.begin();
        this.gameInfo.game.batch.draw(fboTextReg, ManagerCamera.getFrustumCorners()[0].x, ManagerCamera.getFrustumCorners()[0].y, ManagerCamera.getViewportWidth(), ManagerCamera.getViewportHeight());
        this.gameInfo.game.batch.end();
    }

    private void Box2DDebugRender() {
        // Renderizza il debug di Box2D
        debugRenderer.render(ManagerWorld.getInstance(), ManagerCamera.getInstance().combined);
    }

    public void draw() {
        // Renderizza gli oggetti sulla mappa e l'entità del giocatore
        this.gameInfo.mapManager.getMap().getMapRenderer().setView(ManagerCamera.getInstance());
        this.gameInfo.mapManager.getMap().getMapRenderer().render();
        this.gameInfo.managerEntity.draw(elapsedTime);

        // Se l'ambiente è attivo, disegna l'interfaccia grafica
        if (getMapManager().getAmbiente()) drawGUI();
    }

    private void drawGUI() {
        // Disegna la GUI
        rect.draw();
        stage.act();
        stage.draw();
    }

    public void updateCamera(boolean boundaries) {
        // Aggiorna la posizione della telecamera
        ManagerCamera.update(gameInfo.managerEntity, viewport, delta, boundaries);
        this.gameInfo.game.batch.setProjectionMatrix(ManagerCamera.getInstance().combined);
        this.gameInfo.game.renderer.setProjectionMatrix(ManagerCamera.getInstance().combined);
    }

    // Getter per variabili principali
    public ManagerEntity getEntityManager() {
        return this.gameInfo.managerEntity;
    }

    public MapManager getMapManager() {
        return this.gameInfo.mapManager;
    }

    public float getTimeScale() {
        return timeScale;
    }

    public GameInfo getGameInfo() {
        return this.gameInfo;
    }

    @SuppressWarnings("unused")
    public DefaultStateMachine<Game, ManagerGame> gameState() {
        return gameState;
    }

    // Debugging delle performance
    @SuppressWarnings("unused")
    public void performanceInfo() {
        System.out.println(Gdx.graphics.getFramesPerSecond() + " fps");
        System.out.println("Java Heap: " + Gdx.app.getJavaHeap() / (1024 * 1024) + " MB");
        System.out.println("Native Heap: " + Gdx.app.getNativeHeap() / (1024 * 1024) + " MB");
    }
}
