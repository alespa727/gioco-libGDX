package progetto.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import progetto.Game;
import progetto.utils.Cooldown;
import progetto.utils.KeyHandler;
import progetto.gameplay.manager.GameManager;
import progetto.utils.camera.CameraManager;
import progetto.gameplay.manager.entity.EntityManager;
import progetto.gameplay.manager.map.MapManager;

public class GameScreen implements Screen {
    public static final float STEP = 1 / 60f; // Fixed step for logic (60Hz)
    private final GameInfo gameInfo;
    private final DefaultStateMachine<GameScreen, GameManager> gameState;

    public boolean loaded = false;

    public float delta;
    public float accumulator = 0f;
    private float elapsedTime;

    private float timeScale = 1f;

    public FitViewport viewport;

    private Gui rect;
    private Stage stage;
    private Table root;

    // Rendering and Effects
    private ShaderProgram shaderProgram;
    private FrameBuffer fbo1;
    private FrameBuffer fbo2;
    private Box2DDebugRenderer debugRenderer;

    private Cooldown timeScaleCooldown;

    public GameScreen(Game game) {
        this.gameState = new DefaultStateMachine<>(this);
        this.gameState.changeState(GameManager.PLAYING);
        Box2D.init();
        this.gameInfo = new GameInfo();
        this.gameInfo.game = game;
        this.gameInfo.screen = this;
        this.timeScaleCooldown = new Cooldown(0);
    }

    public void setTimeScale(float timeScale, float time) {
        this.timeScale = timeScale;
        timeScaleCooldown.reset(time);
    }

    public void resetTimeScale() {
        timeScaleCooldown.update(delta);
        if (timeScaleCooldown.isReady){
            timeScale=1f;
        }
    }

    // Initialization Methods
    private void buildFBO(int width, int height) {
        this.fbo1 = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
        this.fbo2 = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
    }

    private void createShaderProgram() {
        String vertexShader = Gdx.files.internal("shaders/vertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("shaders/fragment.glsl").readString();
        this.shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
    }

    private void load() {
        this.gameInfo.world = new World(new Vector2(0, 0), true);

        System.out.println("GameScreen loaded");

        this.rect = new Gui(this);
        this.stage = new Stage(new ScreenViewport());
        this.root = new Table();
        this.root.setFillParent(true);
        this.stage.addActor(root);

        this.viewport = new FitViewport(32f, 18f, CameraManager.getCamera());
        this.viewport.apply();

        this.gameInfo.entityManager = new EntityManager(this.gameInfo, this.gameInfo.world);
        this.gameInfo.mapManager = new MapManager(viewport, this.gameInfo.entityManager, 1, this.gameInfo.world);

        this.loaded = true;
    }

    // Screen Interface Methods
    @Override
    public void show() {
        Game.assetManager.load("entities/Finn.png", Texture.class);
        Game.assetManager.finishLoading();
        createShaderProgram();
        buildFBO(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (!loaded) load();

        if (!this.gameInfo.entityManager.player().stati().isAlive()) {
            this.gameInfo.entityManager.player().respawn();
        }

        debugRenderer = new Box2DDebugRenderer();
        debugRenderer.setDrawVelocities(true);
    }

    @Override
    public void render(float delta) {
        resetTimeScale();
        KeyHandler.input();
        updateDeltaTime(delta);

        fbo1.begin();
        ScreenUtils.clear(0, 0, 0, 1);
        gameState.update();
        this.gameInfo.mapManager.getMap().render();
        Box2DDebugRender();
        fbo1.end();

        applyShader();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        this.gameInfo.game.batch.dispose();
        this.gameInfo.game.renderer.dispose();
        stage.dispose();
    }

    // Update and Rendering Methods
    private void updateDeltaTime(float deltaTime) {
        this.delta = deltaTime;
    }

    public void update(float delta) {
        elapsedTime += delta;
        this.gameInfo.entityManager.render(delta);
        boolean ambiente = getMapManager().getAmbiente();
        updateCamera(ambiente);
    }

    private void applyShader() {
        Texture fboText = fbo1.getColorBufferTexture();
        TextureRegion fboTextReg = new TextureRegion(fboText);
        fboTextReg.flip(false, true);

        fbo2.begin();
        this.gameInfo.game.batch.begin();
        this.gameInfo.game.batch.setShader(null);
        this.gameInfo.game.batch.draw(fboTextReg, CameraManager.getFrustumCorners()[0].x, CameraManager.getFrustumCorners()[0].y, CameraManager.getViewportWidth(), CameraManager.getViewportHeight());
        this.gameInfo.game.batch.end();
        fbo2.end();

        fboText = fbo2.getColorBufferTexture();
        fboTextReg = new TextureRegion(fboText);
        fboTextReg.flip(false, true);

        this.gameInfo.game.batch.begin();
        this.gameInfo.game.batch.draw(fboTextReg, CameraManager.getFrustumCorners()[0].x, CameraManager.getFrustumCorners()[0].y, CameraManager.getViewportWidth(), CameraManager.getViewportHeight());
        this.gameInfo.game.batch.end();
    }

    private void Box2DDebugRender() {
        debugRenderer.render(this.gameInfo.world, CameraManager.getCamera().combined);
    }

    public void draw() {
        this.gameInfo.mapManager.getMap().getMapRenderer().setView(CameraManager.getCamera());
        this.gameInfo.mapManager.getMap().getMapRenderer().render();
        this.gameInfo.entityManager.drawDebug();
        this.gameInfo.entityManager.draw(elapsedTime);
        if (getMapManager().getAmbiente()) drawGUI();
    }

    private void drawGUI() {
        rect.draw();
        stage.act();
        stage.draw();
    }

    public void updateCamera(boolean boundaries) {
        CameraManager.update(this.gameInfo.entityManager, viewport, delta, boundaries);
        viewport.apply();
        this.gameInfo.game.batch.setProjectionMatrix(CameraManager.getCamera().combined);
        this.gameInfo.game.renderer.setProjectionMatrix(CameraManager.getCamera().combined);
    }

    // Getters
    public EntityManager getEntityManager() {
        return this.gameInfo.entityManager;
    }

    public MapManager getMapManager() {
        return this.gameInfo.mapManager;
    }

    public World getWorld() {
        return this.gameInfo.world;
    }

    public float getTimeScale() {
        return timeScale;
    }

    public GameInfo getGameInfo() {
        return this.gameInfo;
    }

    @SuppressWarnings("unused")
    public DefaultStateMachine<GameScreen, GameManager> gameState() {
        return gameState;
    }

    // Debugging
    @SuppressWarnings("unused")
    public void performanceInfo() {
        System.out.println(Gdx.graphics.getFramesPerSecond() + " fps");
        System.out.println("Java Heap: " + Gdx.app.getJavaHeap() / (1024 * 1024) + " MB");
        System.out.println("Native Heap: " + Gdx.app.getNativeHeap() / (1024 * 1024) + " MB");
    }
}
