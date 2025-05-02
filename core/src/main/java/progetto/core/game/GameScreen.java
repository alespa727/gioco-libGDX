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
import progetto.core.Core;
import progetto.entity.Engine;
import progetto.entity.components.specific.base.StateComponent;
import progetto.graphics.shaders.specific.ColorFilter;
import progetto.graphics.shaders.specific.EntityLight;
import progetto.graphics.shaders.specific.Vignette;
import progetto.input.DebugWindow;
import progetto.player.ManagerCamera;
import progetto.player.Player;
import progetto.player.inventory.Inventory;
import progetto.world.CollisionManager;
import progetto.world.WorldManager;
import progetto.world.map.MapManager;

public class GameScreen implements Screen {

    public Core core;
    public SpriteBatch batch;
    public FitViewport viewport;

    private final GameEngine engine;
    private final GameRenderer renderer;
    private final DebugHandler debugHandler;
    private DebugWindow debugWindow;
    private Inventory inventory;
    private boolean loaded = false;
    private final ShaderPipeliner shaders;

    public GameScreen(final Core core) {
        GameLoader.loadWorld();
        this.core = core;
        this.batch = core.batch;
        this.shaders = new ShaderPipeliner(this);
        this.loadGame(core);
        CollisionManager listener = new CollisionManager();
        WorldManager.getInstance().setContactListener(listener);
        renderer = new GameRenderer(this, core.renderer);
        debugHandler = new DebugHandler(this);
        debugHandler.startTerminal();
        engine = new GameEngine(core, this);
    }

    public GameEngine getEngine() {
        return engine;
    }

    public ShaderPipeliner getGameDrawer() {
        return shaders;
    }

    public Engine getEntityManager() {
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

    public DebugHandler getDebugHandler() {
        return debugHandler;
    }

    public void loadGame(final Core core) {
        this.core = core;
        this.viewport = new FitViewport(16f, 9f, ManagerCamera.getInstance());
        this.viewport.apply();
    }

    private void initializeWindow() {
        debugWindow = new DebugWindow(this);
        inventory = new Inventory(getWindowStyle());
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(debugWindow.getStage());
        inputMultiplexer.addProcessor(inventory.getStage());
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
        initializeWindow();
        loadGameEngine();
        StateComponent state = engine.getPlayer().get(StateComponent.class);
        if (!state.isAlive()) {
            engine.getPlayer().respawn();
        }
        ManagerCamera.getInstance().update();
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
        debugWindow.updateDebugInfo(Gdx.graphics.getFramesPerSecond(), Gdx.app.getJavaHeap());
        debugWindow.update();
        inventory.update();
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
        debugHandler.stopTerminal();
    }

    @Override
    public void dispose() {
        debugHandler.stopTerminal();
    }
}
