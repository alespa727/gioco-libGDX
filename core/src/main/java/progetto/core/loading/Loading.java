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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import progetto.core.App;
import progetto.core.ResourceManager;
import progetto.core.game.GameScreen;
import progetto.entity.components.specific.base.Cooldown;
import progetto.graphics.animations.DefaultAnimationSet;
import progetto.graphics.shaders.specific.ColorFilter;
import progetto.graphics.shaders.specific.Vignette;
import progetto.ui.components.ProgressBar;
import progetto.world.WorldManager;

public class Loading implements Screen {

    private static final int LARGHEZZA = 16;

    private float UNIT;
    private final App app;
    private final Screen nextScreen;

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private DefaultAnimationSet animation;
    private DefaultAnimationSet shadow;
    private Vector2 direction;
    private Vector2 speed;
    private Vector2 position;
    private Vector2 objective;
    private Vector2 size;

    private ProgressBar progressBar;
    private TextButton textButton;
    private Group group;

    private float time;
    private float accumulator = 0;
    private Cooldown cooldown;
    private LoadingRenderer drawer;

    public Loading(App app, Screen nextScreen, float minTime, float maxTime) {
        this.app = app;
        this.nextScreen = nextScreen;
        app.setScreen(this);
        cooldown = new Cooldown(0.4f);
        cooldown.reset();
        initGameVariables(minTime, maxTime);
        initUI();
    }

    private void setupCamera() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(1600, 900, camera);
    }

    private void initGameVariables(float minTime, float maxTime) {
        time = MathUtils.random(minTime, maxTime);
        UNIT = camera.viewportWidth / LARGHEZZA;
        animation = new DefaultAnimationSet(ResourceManager.get().get("entities/Player.png"));
        shadow = new DefaultAnimationSet(ResourceManager.get().get("entities/Player_shadow.png"));
        position = new Vector2(5 * UNIT, 4.5f * UNIT);
        objective = new Vector2(11 * UNIT, 4.5f * UNIT);
        size = new Vector2(UNIT * 1.5f, UNIT * 1.5f);
        speed = new Vector2(position.dst(objective) / time / UNIT, 0);
        direction = new Vector2(1, 0);
    }

    private void initGraphics() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        drawer = new LoadingRenderer(this);
        drawer.addShader(Vignette.getInstance());
        drawer.addShader(ColorFilter.getInstance(0.8f, 0.8f, 0.86f));
    }

    private void initUI() {
        progressBar = new ProgressBar(new Texture("ProgressBar-2.png"), new Texture("Background.png"));
        progressBar.setPosition(viewport.getWorldWidth() * 0.045f, 0.5f * UNIT);
        progressBar.setSize(new Vector2(5 * UNIT, 0.75f * UNIT));
        group = new Group();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/myfont2.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;
        BitmapFont font = generator.generateFont(parameter);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK;
        textButton = new TextButton("Loading...", buttonStyle);

        textButton.getLabel().setAlignment(Align.left);
        textButton.setPosition(viewport.getWorldWidth() * 0.045f, viewport.getWorldHeight() - viewport.getWorldHeight() * 0.2f);
        textButton.setDebug(true);
        group.addActor(textButton);
    }

    private void move(Vector2 speed) {
        position.add(speed.scl(Gdx.graphics.getDeltaTime() * UNIT));
    }

    private void updateLoading(float delta) {
        accumulator += delta;
        cooldown.update(delta);
        progressBar.setProgress(accumulator / time);
        if (accumulator >= time) {
            app.setScreen(nextScreen);
        }
        if (cooldown.isReady) {
            cooldown.reset();
            textButton.setText(getLoadingText(textButton.getText().toString()));
        }
    }

    public String getLoadingText(String currentText) {
        String baseText = currentText.replaceAll("\\.*$", "");
        String dots = currentText.substring(baseText.length());
        dots = switch (dots.length()) {
            case 0 -> ".";
            case 1 -> "..";
            case 2 -> "...";
            default -> "";
        };
        return baseText + dots;
    }

    public void draw(float delta) {
        ScreenUtils.clear(Color.WHITE);
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(animation.play(direction, accumulator), position.x - size.x / 2, position.y - size.y / 2, size.x, size.y);
        batch.draw(shadow.play(direction, accumulator), position.x - size.x / 2, position.y - size.y / 2, size.x, size.y);
        progressBar.draw(batch, Color.BLACK, 0.6f);
        batch.end();
    }

    public void drawUI(float delta) {
        group.act(delta);
        batch.begin();
        group.draw(batch, 1);
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        group.drawDebug(shapeRenderer);
        shapeRenderer.end();
    }

    @Override
    public void show() {
        initGraphics();
        setupCamera();
        UNIT = camera.viewportWidth / LARGHEZZA;
    }

    @Override
    public void render(float delta) {
        WorldManager.update();
        GameScreen screen1 = (GameScreen) nextScreen;
        screen1.getEntityManager().processQueue();
        move(speed.cpy());
        drawer.draw(batch, delta);
        updateLoading(delta);
        drawUI(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.update();
        UNIT = camera.viewportWidth / LARGHEZZA;
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
