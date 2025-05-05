package progetto.core.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import progetto.core.App;
import progetto.core.CustomScreen;
import progetto.core.ResourceManager;
import progetto.core.ScreenRenderer;
import progetto.core.game.GameRenderer;
import progetto.core.game.GameScreen;
import progetto.core.loading.Loading;
import progetto.core.settings.controller.ControllerImpostazioni;
import progetto.graphics.animations.DefaultAnimationSet;
import progetto.graphics.shaders.specific.ColorFilter;
import progetto.graphics.shaders.specific.Vignette;
import progetto.ui.style.Style;
import progetto.world.WorldManager;

public class MainMenu extends CustomScreen {

    private Color color=null;
    private final String title;
    private static final int LARGHEZZA = 16;
    private float UNIT;
    protected final App app;
    private OrthographicCamera camera;
    protected Viewport viewport;
    private SpriteBatch batch;
    private DefaultAnimationSet animation;
    private DefaultAnimationSet shadow;
    private Vector2 direction;
    private Vector2 position;
    private Vector2 size;
    private Stage stage;
    protected Group group;
    private ScreenRenderer renderer;
    private float accumulator = 0;

    public MainMenu(App app, String title) {
        this.app = app;
        this.title = title;
        this.renderer = new ScreenRenderer(this);
        GameRenderer.setTalking(false);
        loadResources();
        setupCamera();
        initGraphics();
        initGameVariables();
        initUI();
    }

    public MainMenu(App app, String title, Color color) {
        this.app = app;
        this.title = title;
        this.color = color;
        this.renderer = new ScreenRenderer(this);
        loadResources();
        setupCamera();
        initGraphics();
        initGameVariables();
        initUI();
    }

    private void loadResources() {
        ResourceManager.get().load("entities/Player.png", Texture.class);
        ResourceManager.get().load("entities/Player_shadow.png", Texture.class);
        ResourceManager.get().finishLoading();
    }

    private void setupCamera() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(1600, 900, camera);
        UNIT = camera.viewportWidth / LARGHEZZA;
    }

    private void initGraphics() {
        batch = new SpriteBatch();
        renderer.addShader(Vignette.getInstance());
        renderer.addShader(ColorFilter.getInstance());

        ColorFilter.setColor(0.8f, 0.8f, 0.86f);
        if (!title.equals("The loss")) {
            ColorFilter.setColor(color);
        }
    }

    private void initGameVariables() {
        animation = new DefaultAnimationSet(ResourceManager.get().get("entities/Player.png"));
        shadow = new DefaultAnimationSet(ResourceManager.get().get("entities/Player_shadow.png"));
        position = new Vector2(5 * UNIT, 4.5f * UNIT);
        size = new Vector2(UNIT * 1.5f, UNIT * 1.5f);
        direction = new Vector2(1, 0);
    }

    private void initUI() {
        stage = new Stage(viewport, batch);
        group = new Group();

        NinePatchDrawable buttonBackground = createButtonBackground();
        BitmapFont titleFont = generateFont(70);
        BitmapFont buttonFont = generateFont(60);

        createTitle(titleFont);
        createPlayButton(buttonFont, buttonBackground);
        createExitButton(buttonFont, buttonBackground);
        createSettingsButton();

        stage.addActor(group);
        Gdx.input.setInputProcessor(stage);
    }

    private NinePatchDrawable createButtonBackground() {
        return new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("background2.png")), 7, 7, 7, 7));
    }

    private BitmapFont generateFont(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/myfont2.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    private void createTitle(BitmapFont font) {
        TextButton.TextButtonStyle titleStyle = new TextButton.TextButtonStyle();
        titleStyle.font = font;
        titleStyle.fontColor = Color.BLACK.cpy().mul(0.60f);
        TextButton title = new TextButton(this.title, titleStyle);
        title.getLabel().setAlignment(Align.left);
        title.setSize(viewport.getWorldWidth() / 4, viewport.getWorldHeight() / 4);
        title.setPosition(viewport.getWorldWidth() / 2 - title.getWidth() / 2,
                viewport.getWorldHeight() - 250 - title.getHeight() / 2);
        group.addActor(title);
    }

    protected void createPlayButton(BitmapFont font, NinePatchDrawable background) {
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK.cpy().mul(0.40f);
        buttonStyle.up = background;

        TextButton play = new TextButton("Gioca", buttonStyle);
        play.getLabel().setAlignment(Align.center);
        play.setSize(300, 100);
        play.setPosition(viewport.getWorldWidth() / 2 - play.getWidth() / 2,
                viewport.getWorldHeight() / 2 - play.getHeight() / 2 + 90);

        play.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                app.gameScreen = new GameScreen(app);
                WorldManager.clear();
                app.setScreen(new Loading(app, app.gameScreen, 3, 4.5f));
            }
        });

        group.addActor(play);
    }

    private void createExitButton(BitmapFont font, NinePatchDrawable background) {
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK.cpy().mul(0.40f);
        buttonStyle.up = background;

        TextButton exit = new TextButton("Esci", buttonStyle);
        exit.getLabel().setAlignment(Align.center);
        exit.setSize(300, 100);
        exit.setPosition(viewport.getWorldWidth() / 2 - exit.getWidth() / 2,
                viewport.getWorldHeight() / 2 - exit.getHeight() / 2 - 15);

        exit.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                System.exit(0);
            }
        });

        group.addActor(exit);
    }

    private void createSettingsButton() {
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/settings.png"))));
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = drawable;
        buttonStyle.down = drawable;

        ImageButton settings = new ImageButton(buttonStyle);
        settings.setSize(100, 100);
        settings.setPosition(30, viewport.getWorldHeight() - settings.getHeight() - 30);

        settings.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                final Style style = new Style("skins/metal-ui.json", "fonts/myfont2.ttf", 30, Color.WHITE,
                        "fonts/myfont2.ttf", 30, Color.WHITE, null, null, null);
                ControllerImpostazioni controller = new ControllerImpostazioni(app, getScreen(), style);
                controller.setViewImpostazioni(controller.creaImpostazioni(controller.getModelImpostazioni()));
                controller.getViewImpostazioni().setActorStage(controller.getViewImpostazioni().getRoot());
                app.setScreen(controller.getViewImpostazioni());
            }
        });

        group.addActor(settings);
    }

    public CustomScreen getScreen() {
        return this;
    }

    private void move(Vector2 speed) {
        position.add(speed.scl(Gdx.graphics.getDeltaTime() * UNIT));
    }

    public void draw(float delta) {
        ScreenUtils.clear(0.7f, 0.7f, 0.7f, 1);
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        TextureRegion player = animation.play(new Vector2(direction), accumulator);
        TextureRegion playerShadow = shadow.play(new Vector2(direction), accumulator);
        batch.draw(player, position.x, position.y, 2 * UNIT, 2 * UNIT);
        batch.draw(playerShadow, position.x, position.y, 2 * UNIT, 2 * UNIT);
        batch.end();
    }

    public void drawUI(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        updateMovement(delta);
        accumulator += delta;
        move(direction.cpy());
        renderer.draw(batch, delta);
        drawUI(delta);
    }

    private void updateMovement(float delta) {
        if (position.x + size.x / 2 > Gdx.graphics.getWidth() - UNIT) {
            direction.x = -1;
        } else if (position.x + size.x / 2 < UNIT) {
            direction.x = 1;
        }
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
