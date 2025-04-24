package progetto.screens.loading;

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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import progetto.core.Core;
import progetto.core.game.GameScreen;
import progetto.entity.entities.base.Entity;
import progetto.entity.entities.base.EntityConfig;
import progetto.factories.EntityConfigFactory;
import progetto.factories.EntityFactory;
import progetto.graphics.animations.DefaultAnimationSet;
import progetto.world.WorldManager;

public class LoadingScreen implements Screen {

    public final int larghezza = 16;
    public final int altezza = 9;

    private float larghezzaBlocco;

    DefaultAnimationSet animation;

    Vector2 direction;
    Vector2 speed;
    Vector2 position;
    Vector2 objective;
    Vector2 size;

    ShapeRenderer shapeRenderer;
    SpriteBatch batch;

    OrthographicCamera camera;
    Viewport viewport;
    LoadingScreenDrawer drawer;
    NinePatchDrawable loadingBarDrawable;
    NinePatchDrawable loadingBarDrawable2;

    Stage stage;
    Screen screen;
    Core core;
    float time;
    float accumulator = 0;

    public LoadingScreen(Core core, Screen nextScreen, float minTime, float maxTime) {
        core.setScreen(this);

        time = MathUtils.random(minTime, maxTime);
        larghezzaBlocco = (float) Gdx.graphics.getWidth() / larghezza;

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        drawer = new LoadingScreenDrawer(this);
        screen = nextScreen;

        animation = new DefaultAnimationSet(Core.assetManager.get("entities/Finn.png"));
        position = new Vector2(5*larghezzaBlocco, 4.5f*larghezzaBlocco);
        objective = new Vector2(11*larghezzaBlocco, 4.5f*larghezzaBlocco);
        size = new Vector2(larghezzaBlocco*1.5f, larghezzaBlocco*1.5f);
        speed = new Vector2(position.dst(objective)/time/larghezzaBlocco, 0);
        direction = new Vector2(1, 0);

        GameScreen screen1 = (GameScreen) screen;
        EntityConfig e = EntityConfigFactory.createEntityConfig("Finn", 8, 10);
        screen1.getEntityManager().summon(EntityFactory.createEnemy("Finn", e, screen1.getEntityManager(), 4));





        this.core = core;
        NinePatch loadingBar = new NinePatch(new Texture("WindowUi.png"), 7, 7, 7, 7);
        loadingBarDrawable = new NinePatchDrawable(loadingBar);
        loadingBar = new NinePatch(new Texture("ProgressBar2.png"), 7, 7, 7, 7);
        loadingBarDrawable2 = new NinePatchDrawable(loadingBar);

    }

    public void move(Vector2 speed) {
        position.add(speed.scl(Gdx.graphics.getDeltaTime()*larghezzaBlocco));
    }

    public void drawGrid(){
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED.cpy().mul(0.4f));
        for (int i = 0; i < larghezza; i++) {
            shapeRenderer.line(i*larghezzaBlocco, 0, i*larghezzaBlocco, larghezzaBlocco*altezza);
        }
        for (int i = 0; i < altezza; i++) {
            shapeRenderer.line(0, i*larghezzaBlocco, larghezzaBlocco*larghezza, i*larghezzaBlocco);
        }

        shapeRenderer.setColor(Color.BLUE.cpy());
        shapeRenderer.rect(5*larghezzaBlocco, 4f*larghezzaBlocco, 80, 80);
        shapeRenderer.rect(10*larghezzaBlocco, 4f*larghezzaBlocco, 80, 80);
        shapeRenderer.rect(position.x-size.x/2, position.y-size.y/2, size.x, size.y);
        shapeRenderer.rectLine(position.x, position.y, objective.x, objective.y, 10);
        shapeRenderer.end();
    }


    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        stage.setDebugAll(true);
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/myfont2.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        BitmapFont font;
        parameter.size = 25;
        font = generator.generateFont(parameter); // font size 12 pixels
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.BLACK;

        //table.add(new TextButton("CIAO", buttonStyle));

        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void render(float delta) {
        accumulator += delta;
        WorldManager.update();
        GameScreen screen1 = (GameScreen) screen;
        screen1.getEntityManager().processQueue();

        move(speed.cpy());
        drawer.draw(batch);

        if (accumulator >= time) {
            core.setScreen(screen);
        }

        stage.act(delta);
        stage.draw();
        drawGrid();
    }

    public void draw() {
        viewport.apply();
        ScreenUtils.clear(0.3f, 0.3f, 0.3f, 1);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        loadingBarDrawable.draw(batch, 0.5f*larghezzaBlocco, 0.5f*larghezzaBlocco, 5*larghezzaBlocco, 1*larghezzaBlocco);
        loadingBarDrawable2.draw(batch, 0.5f*larghezzaBlocco, 0.5f*larghezzaBlocco, accumulator/time*5*larghezzaBlocco, 1*larghezzaBlocco);


        batch.draw(animation.play(direction, accumulator), position.x-size.x/2, position.y-size.y/2, size.x, size.y);


        batch.end();
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
