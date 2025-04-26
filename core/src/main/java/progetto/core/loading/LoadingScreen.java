package progetto.core.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import progetto.core.Core;
import progetto.core.game.GameScreen;
import progetto.core.game.TextDrawer;
import progetto.graphics.animations.DefaultAnimationSet;
import progetto.graphics.shaders.specific.ColorFilter;
import progetto.graphics.shaders.specific.Vignette;
import progetto.ui.ProgressBar;
import progetto.world.WorldManager;

public class LoadingScreen implements Screen {

    // Constanti
    public final int larghezza = 16;
    public final int altezza = 9;
    private final float larghezzaBlocco;

    // Game variabili
    DefaultAnimationSet animation;
    DefaultAnimationSet shadow;
    Vector2 direction;
    Vector2 speed;
    Vector2 position;
    Vector2 objective;
    Vector2 size;
    float time;
    float accumulator = 0;

    // Drawing component
    ShapeRenderer shapeRenderer;
    SpriteBatch batch;
    LoadingScreenDrawer drawer;

    // UI component
    ProgressBar progressBar;
    Stage stage;
    OrthographicCamera camera;
    Viewport viewport;

    // Game state
    Screen screen;
    Core core;
    TextDrawer textDrawer;

    public LoadingScreen(Core core, Screen nextScreen, float minTime, float maxTime) {
        core.setScreen(this);
        Core.assetManager.load("entities/Player_shadow.png", Texture.class);
        Core.assetManager.finishLoading();
        this.core = core;
        this.textDrawer = new TextDrawer();

        time = MathUtils.random(minTime, maxTime);
        larghezzaBlocco = (float) Gdx.graphics.getWidth() / larghezza;

        // Game setup
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        drawer = new LoadingScreenDrawer(this);
        drawer.addShader(Vignette.getInstance());
        drawer.addShader(ColorFilter.getInstance(0.9f, 0.9f, 0.9f));
        this.screen = nextScreen;

        // Animazione setup
        animation = new DefaultAnimationSet(Core.assetManager.get("entities/Finn.png"));
        shadow = new DefaultAnimationSet(Core.assetManager.get("entities/Player_shadow.png"));
        position = new Vector2(5 * larghezzaBlocco, 4.5f * larghezzaBlocco);
        objective = new Vector2(11 * larghezzaBlocco, 4.5f * larghezzaBlocco);
        size = new Vector2(larghezzaBlocco * 1f, larghezzaBlocco * 1f);
        speed = new Vector2(position.dst(objective) / time / larghezzaBlocco, 0);
        direction = new Vector2(1, 0);
        // UI Setup
        progressBar = new ProgressBar(new Texture("WindowUi.png"), new Texture("ProgressBar2.png"));
        progressBar.setPosition(0.5f * larghezzaBlocco, 0.5f * larghezzaBlocco);
        progressBar.setSize(new Vector2(5 * larghezzaBlocco, 0.75f * larghezzaBlocco));
        textDrawer.setColor(Color.BLACK.cpy());
    }

    public void move(Vector2 speed) {
        position.add(speed.scl(Gdx.graphics.getDeltaTime() * larghezzaBlocco));
    }

    public void drawGrid() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE.cpy().mul(0.4f));

        for (int i = 0; i < larghezza; i++) {
            shapeRenderer.line(i * larghezzaBlocco, 0, i * larghezzaBlocco, larghezzaBlocco * altezza);
        }
        for (int i = 0; i < altezza; i++) {
            shapeRenderer.line(0, i * larghezzaBlocco, larghezzaBlocco * larghezza, i * larghezzaBlocco);
        }

        shapeRenderer.setColor(Color.RED.cpy());
        shapeRenderer.rect(5 * larghezzaBlocco, 4f * larghezzaBlocco, 80, 80);
        shapeRenderer.rect(10 * larghezzaBlocco, 4f * larghezzaBlocco, 80, 80);
        shapeRenderer.rect(position.x - size.x / 2, position.y - size.y / 2, size.x, size.y);
        shapeRenderer.rectLine(position.x, position.y, objective.x, objective.y, 10);
        shapeRenderer.end();
    }

    @Override
    public void show() {
        // Stage setup
        stage = new Stage(new ScreenViewport());
        stage.setDebugAll(true);
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/myfont2.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 25;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void render(float delta) {
        progressBar.setProgress(accumulator / time);
        accumulator += delta;


        WorldManager.update();
        GameScreen screen1 = (GameScreen) screen;
        screen1.getEntityManager().processQueue();

        move(speed.cpy());;
        drawer.draw(batch);
        drawGrid();

        if (accumulator >= time) {
            core.setScreen(screen);
        }

        stage.act(delta);
        stage.draw();
    }

    public void draw() {
        ScreenUtils.clear(Color.WHITE);
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        progressBar.draw(batch, Color.BLACK, 0.5f);
        batch.draw(animation.play(direction, accumulator), position.x - size.x / 2, position.y - size.y / 2, size.x, size.y);
        batch.draw(shadow.play(direction, accumulator), position.x - size.x / 2, position.y - size.y / 2, size.x, size.y);
        batch.end();
        textDrawer.drawText(batch, "LOADING..", larghezzaBlocco*0.75f, larghezzaBlocco*8.25f);
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);
        camera.setToOrtho(false, width, height);
        camera.update();
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
    public void dispose() {

    }
}
