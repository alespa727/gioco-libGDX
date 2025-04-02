package io.github.ale.screens.gameplay;

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
import io.github.ale.Game;
import io.github.ale.utils.KeyHandler;
import io.github.ale.screens.gameplay.gui.Gui;
import io.github.ale.screens.gameplay.manager.GameManager;
import io.github.ale.utils.camera.CameraManager;
import io.github.ale.screens.gameplay.manager.entity.EntityManager;
import io.github.ale.screens.gameplay.manager.map.MapManager;

public class GameScreen implements Screen {
    // Constants
    public static final float STEP = 1 / 60f; // Fixed step for logic (60Hz)

    // State Variables
    public final Game game;
    final World world;
    private final DefaultStateMachine<GameScreen, GameManager> gameState;
    public float delta;
    public boolean loaded = false;
    public boolean isPaused = false;
    public float accumulator = 0f;
    private float elapsedTime;
    private float timeScale = 1f;

    // UI and Viewport
    public FitViewport viewport;
    private Gui rect;
    private Stage stage;
    private Table root;

    // Rendering and Effects
    ShaderProgram shaderProgram;
    FrameBuffer fbo1;
    FrameBuffer fbo2;
    private Box2DDebugRenderer debugRenderer;

    // Game Components
    private EntityManager entityManager;
    private MapManager mapManager;

    GameContext gameContext;

    public GameScreen(Game game) {
        this.game = game;
        this.gameState = new DefaultStateMachine<>(this);
        this.gameState.changeState(GameManager.PLAYING);
        Box2D.init();
        this.world = new World(new Vector2(0, 0), true);
    }

    // Initialization Methods
    private void buildFBO(int width, int height) {
        fbo1 = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
        fbo2 = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
    }

    private void createShaderProgram() {
        String vertexShader = Gdx.files.internal("shaders/vertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("shaders/fragment.glsl").readString();
        shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
    }

    private void load() {
        gameContext = new GameContext(this.game, this, this.world, this.entityManager, this.mapManager);

        System.out.println("GameScreen loaded");

        rect = new Gui(this);
        stage = new Stage(new ScreenViewport());
        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        viewport = new FitViewport(32f, 18f, CameraManager.getCamera());
        viewport.apply();

        entityManager = new EntityManager(this.game, world);
        mapManager = new MapManager(viewport, entityManager, 1, world);

        loaded = true;
    }

    // Screen Interface Methods
    @Override
    public void show() {
        Game.assetManager.load("entities/Finn.png", Texture.class);
        Game.assetManager.finishLoading();
        createShaderProgram();
        buildFBO(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (!loaded) load();

        if (!entityManager.player().stati().isAlive()) {
            entityManager.player().respawn();
        }

        debugRenderer = new Box2DDebugRenderer();
        debugRenderer.setDrawVelocities(true);
        isPaused = false;
    }

    @Override
    public void render(float delta) {
        KeyHandler.input();
        updateDeltaTime(delta);

        fbo1.begin();
        ScreenUtils.clear(0, 0, 0, 1);
        gameState.update();
        mapManager.getMap().render();
        //Box2DDebugRender();
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
        game.batch.dispose();
        game.renderer.dispose();
        stage.dispose();
    }

    // Update and Rendering Methods
    private void updateDeltaTime(float deltaTime) {
        this.delta = deltaTime;
    }

    public void update(float delta) {
        elapsedTime += delta;
        entityManager.render(delta);
        boolean ambiente = getMapManager().getAmbiente();
        updateCamera(ambiente);
    }

    private void applyShader() {
        Texture fboText = fbo1.getColorBufferTexture();
        TextureRegion fboTextReg = new TextureRegion(fboText);
        fboTextReg.flip(false, true);

        fbo2.begin();
        game.batch.begin();
        game.batch.setShader(null);
        game.batch.draw(fboTextReg, CameraManager.getFrustumCorners()[0].x, CameraManager.getFrustumCorners()[0].y, CameraManager.getViewportWidth(), CameraManager.getViewportHeight());
        game.batch.end();
        fbo2.end();

        fboText = fbo2.getColorBufferTexture();
        fboTextReg = new TextureRegion(fboText);
        fboTextReg.flip(false, true);

        game.batch.begin();
        game.batch.draw(fboTextReg, CameraManager.getFrustumCorners()[0].x, CameraManager.getFrustumCorners()[0].y, CameraManager.getViewportWidth(), CameraManager.getViewportHeight());
        game.batch.end();
    }

    private void Box2DDebugRender() {
        debugRenderer.render(world, CameraManager.getCamera().combined);
    }

    public void draw() {
        mapManager.getMap().getMapRenderer().setView(CameraManager.getCamera());
        mapManager.getMap().getMapRenderer().render();
        entityManager.drawDebug();
        entityManager.draw(elapsedTime);
        if (getMapManager().getAmbiente()) drawGUI();
    }

    private void drawGUI() {
        rect.draw();
        stage.act();
        stage.draw();
    }

    public void updateCamera(boolean boundaries) {
        CameraManager.update(entityManager, viewport, delta, boundaries);
        viewport.apply();
        game.batch.setProjectionMatrix(CameraManager.getCamera().combined);
        game.renderer.setProjectionMatrix(CameraManager.getCamera().combined);
    }

    // Getters
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public World getWorld() {
        return world;
    }

    public float getTimeScale() {
        return timeScale;
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
