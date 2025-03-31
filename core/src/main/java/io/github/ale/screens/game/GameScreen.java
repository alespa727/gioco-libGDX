package io.github.ale.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.ale.MyGame;
import io.github.ale.screens.game.camera.CameraManager;
import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.gui.Gui;
import io.github.ale.screens.game.map.MapManager;

public class GameScreen implements Screen {
    // Costanti
    public static final float STEP = 1 / 60f; // Durata fissa per logica (60Hz)

    // Variabili di stato
    public final MyGame game;
    public float delta;
    public boolean loaded = false;
    public boolean isPaused = false;
    private float elapsedTime;
    public float accumulator = 0f;

    // Gestione del mondo e della fisica
    final World world;
    private Box2DDebugRenderer debugRenderer;
    private DefaultStateMachine<GameScreen, GameStates> gameState;

    // Componenti di gioco
    private EntityManager entities;
    private CameraManager camera;
    private MapManager mapManager;
    private Gui rect;

    // UI e viewport
    public FitViewport viewport;
    private Stage stage;
    private Table root;

    ShaderProgram shaderProgram;
    FrameBuffer fbo1;
    FrameBuffer fbo2;


    public GameScreen(MyGame game) {
        this.game = game;
        this.gameState = new DefaultStateMachine<>(this);
        this.gameState.changeState(GameStates.PLAYING);

        Box2D.init();
        this.world = new World(new Vector2(0, 0), true);

    }

    private void buildFBO(int width, int height) {
        fbo1 = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
        fbo2 = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
    }

    private void createShaderProgram() {
        String vertexShader = Gdx.files.internal("shaders/vertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("shaders/fragment.glsl").readString();
        shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
    }

    @Override
    public void show() {
        createShaderProgram();
        buildFBO(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (!loaded) load();

        if (!entities.player().stati().isAlive()) {
            entities.player().respawn();
        }

        debugRenderer = new Box2DDebugRenderer();
        debugRenderer.setDrawVelocities(true);

        isPaused = false;
    }

    private void load() {
        System.out.println("GameScreen loaded");

        rect = new Gui(this);
        stage = new Stage(new ScreenViewport());
        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        camera = new CameraManager();
        viewport = new FitViewport(32f, 18f, camera.get());
        viewport.apply();

        entities = new EntityManager(this.game, world);
        mapManager = new MapManager(camera.get(), viewport, entities, 1, world);

        loaded = true;
    }

    @Override
    public void render(float delta) {
        updateDeltaTime(delta);
        fbo1.begin();
        ScreenUtils.clear(0, 0, 0, 1);
        gameState.update();
        Box2DDebugRender();
        fbo1.end();

        applyShader();

    }

    public void applyShader(){

        Texture fboText = fbo1.getColorBufferTexture();
        TextureRegion fboTextReg = new TextureRegion(fboText);
        fboTextReg.flip(false, true);

        fbo2.begin();
        game.batch.begin();
        game.batch.setShader(null);
        game.batch.draw(fboTextReg, CameraManager.getFrustumCorners()[0].x, CameraManager.getFrustumCorners()[0].y, camera.getViewportWidth(), camera.getViewportHeight());
        game.batch.setShader(null);
        game.batch.end();
        fbo2.end();

        fboText = fbo2.getColorBufferTexture();
        fboTextReg = new TextureRegion(fboText);
        fboTextReg.flip(false, true);

        game.batch.begin();
        game.batch.draw(fboTextReg, CameraManager.getFrustumCorners()[0].x, CameraManager.getFrustumCorners()[0].y, camera.getViewportWidth(), camera.getViewportHeight());
        game.batch.end();
    }

    private void updateDeltaTime(float deltaTime) {
        this.delta = deltaTime;
    }

    private void Box2DDebugRender() {
        debugRenderer.render(world, camera.get().combined);
    }

    public void update(float delta) {
        elapsedTime += delta;
        entities.render(delta);
        boolean ambiente = maps().getAmbiente();
        updateCamera(ambiente);
    }

    public void draw(float delta) {
        mapManager.getCurrentMap().getMapRenderer().setView(camera.get());
        mapManager.getCurrentMap().getMapRenderer().render();
        entities.draw(elapsedTime);
        if (maps().getAmbiente()) drawGUI(delta);
    }

    private void drawGUI(float delta) {
        rect.draw();
        stage.act();
        stage.draw();
    }

    public void updateCamera(boolean boundaries) {
        camera.update(mapManager, entities, viewport, boundaries);
        viewport.apply();
        game.batch.setProjectionMatrix(camera.get().combined);
        game.renderer.setProjectionMatrix(camera.get().combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
    }

    @Override
    public void dispose() {
        game.batch.dispose();
        game.renderer.dispose();
        stage.dispose();
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    // Getter per componenti principali
    public EntityManager entities() { return entities; }
    public MapManager maps() { return mapManager; }
    public CameraManager camera() { return camera; }
    public DefaultStateMachine<GameScreen, GameStates> gameState() { return gameState; }

    // Debugging performance
    public void performanceInfo() {
        System.out.println(Gdx.graphics.getFramesPerSecond() + " fps");
        System.out.println("Java Heap: " + Gdx.app.getJavaHeap() / (1024 * 1024) + " MB");
        System.out.println("Native Heap: " + Gdx.app.getNativeHeap() / (1024 * 1024) + " MB");
    }
}
