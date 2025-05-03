package progetto.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import progetto.core.AutoSave;
import progetto.core.App;
import progetto.entity.EntityEngine;
import progetto.entity.components.specific.base.StateComponent;
import progetto.graphics.shaders.specific.ColorFilter;
import progetto.graphics.shaders.specific.EntityLight;
import progetto.graphics.shaders.specific.Vignette;
import progetto.input.DebugWindow;
import progetto.core.CameraManager;
import progetto.core.game.player.Player;
import progetto.world.collision.CollisionManager;
import progetto.world.WorldManager;
import progetto.world.map.MapManager;

public class GameScreen implements Screen {

    public App app;
    public SpriteBatch batch;
    public FitViewport viewport;

    private AutoSave autoSave;
    private final Engine engine;
    private final GameRenderer renderer;
    private final Debug debug;
    private DebugWindow debugWindow;

    private boolean loaded = false;
    private final Pipeliner shaders;

    //private Inventory inventory; NON IMPLEMENTATO

    public GameScreen(final App app) {
        Loader.loadWorld();
        this.app = app;
        this.batch = app.batch;
        this.shaders = new Pipeliner(this);
        this.loadGame(app);
        CollisionManager listener = new CollisionManager();
        WorldManager.getInstance().setContactListener(listener);
        renderer = new GameRenderer(this, app.renderer);
        debug = new Debug(this);
        engine = new Engine(app, this);
    }

    public Engine getEngine() {
        return engine;
    }

    public Pipeliner getGameDrawer() {
        return shaders;
    }

    public EntityEngine getEntityManager() {
        return engine.getEntityEngine();
    }


    public MapManager getMap() {
        return engine.getMap();
    }

    public Player getPlayer() {
        return engine.getPlayer();
    }

    public GameRenderer getRenderer() {
        return renderer;
    }

    public Debug getDebugHandler() {
        return debug;
    }

    public void loadGame(final App app) {
        this.app = app;
        this.viewport = new FitViewport(16f, 9f, CameraManager.getInstance());
        this.viewport.apply();
    }

    private void initializeWindow() {
        debugWindow = new DebugWindow(this);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(debugWindow.getStage());
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void loadGameEngine() {
        if (!loaded) {
            loaded = true;
            engine.initializeEntityEngine();
            shaders.addShader(Vignette.getInstance());
            shaders.addShader(ColorFilter.getInstance(0.5f, 0.5f, 0.55f));
            shaders.addShader(new EntityLight(engine.getEntityEngine().player(), 0.10f, Color.WHITE.cpy().mul(0.8f)));
        }
    }

    public void setTimeScale(float timeScale, float time) {
        this.engine.getTime().setTimeScale(timeScale, time);
    }

    public Window.WindowStyle getWindowStyle() {
        Window.WindowStyle style = new Window.WindowStyle();
        NinePatch patch = new NinePatch(new Texture(Gdx.files.internal("WindowUi.png")), 7, 7, 7, 7 );
        NinePatchDrawable drawable = new NinePatchDrawable(patch);
        style.background = drawable;
        style.titleFont = new BitmapFont();
        return style;
    }

    @Override
    public void show() {
        debug.startTerminal();
        initializeWindow();
        loadGameEngine();
        StateComponent state = engine.getPlayer().get(StateComponent.class);
        if (!state.isAlive()) {
            engine.getPlayer().respawn();
        }
        CameraManager.getInstance().update();
        autoSave = new AutoSave(engine.getEntityEngine(), engine.getMap());
        autoSave.start();
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
        debugWindow.updateDebugInfo(Gdx.graphics.getFramesPerSecond(), Gdx.app.getJavaHeap());
        debugWindow.update();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        autoSave.stopSaving();
        debug.stopTerminal();
    }

    @Override
    public void dispose() {
        debug.stopTerminal();
    }
}
