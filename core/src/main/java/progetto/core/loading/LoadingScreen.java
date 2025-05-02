package progetto.core.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import progetto.core.Core;
import progetto.core.ResourceManager;
import progetto.core.game.GameScreen;
import progetto.core.game.TextDrawer;
import progetto.entity.components.specific.base.Cooldown;
import progetto.graphics.animations.DefaultAnimationSet;
import progetto.graphics.shaders.specific.ColorFilter;
import progetto.graphics.shaders.specific.Vignette;
import progetto.ui.ProgressBar;
import progetto.world.WorldManager;

public class LoadingScreen implements Screen {

    // Costanti e variabili di configurazione
    private static final int LARGHEZZA = 16;
    private static final int ALTEZZA = 9;

    // Variabili grafiche e di gioco
    private float larghezzaBlocco;
    private DefaultAnimationSet animation;
    private DefaultAnimationSet shadow;
    private Vector2 direction;
    private Vector2 speed;
    private Vector2 position;
    private Vector2 objective;
    private Vector2 size;
    private float time;
    private float accumulator = 0;
    private Cooldown cooldown;

    // Componenti di disegno
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    SpriteBatch mapBatch;
    private LoadingScreenDrawer drawer;

    // Componenti UI
    private ProgressBar progressBar;
    private TextButton textButton;
    private Stage stage;
    private OrthographicCamera camera;
    private OrthographicCamera camera2;
    private Viewport viewport;
    private Viewport viewport2;
    private Table table;
    private TextDrawer textDrawer;

    // Stato di gioco
    private final Screen nextScreen;
    private final Core core;

    TiledMap map;
    TiledMapRenderer renderer;

    public LoadingScreen(Core core, Screen nextScreen, float minTime, float maxTime) {
        this.core = core;
        this.nextScreen = nextScreen;
        core.setScreen(this);
        cooldown = new Cooldown(0.4f);
        cooldown.reset();

        initGameVariables(minTime, maxTime);
        initGraphics();
        initUI();
    }


    private void initGameVariables(float minTime, float maxTime) {
        time = MathUtils.random(minTime, maxTime);
        larghezzaBlocco = (float) Gdx.graphics.getWidth() / LARGHEZZA;

        animation = new DefaultAnimationSet(ResourceManager.get().get("entities/Finn.png"));
        shadow = new DefaultAnimationSet(ResourceManager.get().get("entities/Player_shadow.png"));

        position = new Vector2(5 * larghezzaBlocco, 4.5f * larghezzaBlocco);
        objective = new Vector2(11 * larghezzaBlocco, 4.5f * larghezzaBlocco);
        size = new Vector2(larghezzaBlocco*1.5f, larghezzaBlocco*1.5f);
        speed = new Vector2(position.dst(objective) / time / larghezzaBlocco, 0);
        direction = new Vector2(1, 0);
    }

    private void initGraphics() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        drawer = new LoadingScreenDrawer(this);

        drawer.addShader(Vignette.getInstance());
        drawer.addShader(ColorFilter.getInstance(0.8f, 0.8f, 0.86f));
    }

    private void initUI() {
        textDrawer = new TextDrawer();
        textDrawer.setColor(Color.BLACK.cpy());

        progressBar = new ProgressBar(new Texture("WindowUi.png"), new Texture("ProgressBar2.png"));
        progressBar.setPosition(0.5f * larghezzaBlocco, 0.5f * larghezzaBlocco);
        progressBar.setSize(new Vector2(5 * larghezzaBlocco, 0.75f * larghezzaBlocco));
    }

    private void setupStage() {
        stage = new Stage(new ScreenViewport());
        stage.setDebugAll(false);
        table = new Table();
        table.top().left();
        table.setFillParent(true);
        stage.addActor(table);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/myfont2.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        BitmapFont font = generator.generateFont(parameter);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK;


        textButton = new TextButton("Loading...", buttonStyle);

        textButton.getLabel().setAlignment(Align.left);
        textButton.getLabelCell().padLeft(larghezzaBlocco*0.25f);

        table.add(textButton)
            .width(larghezzaBlocco * 5)
            .height(larghezzaBlocco)
            .minSize(larghezzaBlocco * 5, larghezzaBlocco)
            .prefSize(larghezzaBlocco * 5, larghezzaBlocco)
            .maxSize(larghezzaBlocco * 5, larghezzaBlocco)
            .padLeft(larghezzaBlocco * 0.5f)
            .padTop(larghezzaBlocco * 0.5f);
    }

    private void setupCamera() {
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        camera2 = new OrthographicCamera();
        viewport2 = new ScreenViewport(camera);
        camera2.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera2.update();
        viewport2.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    // --- METODI DI LOGICA ---

    private void move(Vector2 speed) {
        position.add(speed.scl(Gdx.graphics.getDeltaTime() * larghezzaBlocco));
    }

    private void updateLoading(float delta) {
        accumulator += delta;
        cooldown.update(delta);
        progressBar.setProgress(accumulator / time);

        if (accumulator >= time) {
            core.setScreen(nextScreen);
        }

        if(cooldown.isReady){
            cooldown.reset();
            textButton.setText(getLoadingText(textButton.getText().toString()));
        }
    }

    public String getLoadingText(String currentText) {
        String baseText = currentText.replaceAll("\\.*$", ""); // Elimina i puntini
        String dots = currentText.substring(baseText.length()); // Ottiene i puntini

        dots = switch (dots.length()) {
            case 0 -> "."; // Un puntino
            case 1 -> ".."; // Due puntini
            case 2 -> "..."; // Tre puntini
            default -> ""; // Reset
        };

        return baseText + dots;
    }

    // --- DISEGNO ---

    public void draw(float delta) {
        ScreenUtils.clear(Color.WHITE);
        viewport.apply();
        viewport2.apply();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        progressBar.draw(batch, Color.BLACK, 0.5f);
        batch.draw(animation.play(direction, accumulator), position.x - size.x / 2, position.y - size.y / 2, size.x, size.y);
        batch.draw(shadow.play(direction, accumulator), position.x - size.x / 2, position.y - size.y / 2, size.x, size.y);
        batch.end();
        //drawGridDebug();
    }

    public void drawMap(){
        int mapWidth = map.getProperties().get("width", Integer.class);       // numero di tile in larghezza
        int mapHeight = map.getProperties().get("height", Integer.class);     // numero di tile in altezza
        int tilePixelWidth = map.getProperties().get("tilewidth", Integer.class);  // pixel di un tile
        int tilePixelHeight = map.getProperties().get("tileheight", Integer.class); // pixel di un tile

        float mapPixelWidth = mapWidth * tilePixelWidth;
        float mapPixelHeight = mapHeight * tilePixelHeight;
        camera2.zoom=0.4f;
        camera2.position.set(mapPixelWidth / 2f, mapPixelHeight / 2f, 0);
        camera2.update();
        renderer.setView(camera2);
        renderer.render();
        //drawGridDebug();
    }

    public void drawUI(float delta) {
        stage.act(delta);
        stage.draw();
    }

    private void drawGridDebug() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE.cpy().mul(0.4f));

        for (int i = 0; i < LARGHEZZA; i++) {
            shapeRenderer.line(i * larghezzaBlocco, 0, i * larghezzaBlocco, larghezzaBlocco * ALTEZZA);
        }
        for (int i = 0; i < ALTEZZA; i++) {
            shapeRenderer.line(0, i * larghezzaBlocco, larghezzaBlocco * LARGHEZZA, i * larghezzaBlocco);
        }

        shapeRenderer.setColor(Color.RED.cpy());
        shapeRenderer.rect(5 * larghezzaBlocco, 4f * larghezzaBlocco, larghezzaBlocco, larghezzaBlocco);
        shapeRenderer.rect(10 * larghezzaBlocco, 4f * larghezzaBlocco, larghezzaBlocco, larghezzaBlocco);
        shapeRenderer.rect(position.x - size.x / 2, position.y - size.y / 2, size.x, size.y);
        shapeRenderer.rectLine(position.x, position.y, objective.x, objective.y, 10);
        shapeRenderer.end();
    }

    // --- OVERRIDE SCREEN ---

    @Override
    public void show() {
        larghezzaBlocco = (float) Gdx.graphics.getWidth() / LARGHEZZA;
        setupStage();
        setupCamera();

        mapBatch = new SpriteBatch();
        mapBatch.setProjectionMatrix(viewport2.getCamera().combined);

        map = new TmxMapLoader().load("maps/loading.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, mapBatch);

    }

    @Override
    public void render(float delta) {
        WorldManager.update();
        GameScreen screen1 = (GameScreen) nextScreen;
        screen1.getEntityManager().processQueue();

        move(speed.cpy());
        drawer.draw(batch, delta);

        updateLoading(delta);



        //drawGridDebug(); // Debug opzionale
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.setToOrtho(false, width, height);
        camera.update();

        viewport2.update(width, height, true);
        camera2.setToOrtho(false, width, height);
        camera2.update();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
