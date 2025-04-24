package progetto.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import progetto.core.Core;
import progetto.graphics.shaders.specific.ColorFilter;
import progetto.graphics.shaders.specific.Vignette;
import progetto.world.WorldManager;

public class LoadingScreen implements Screen {

    OrthographicCamera camera;
    Viewport viewport;
    LoadingScreenDrawer drawer;
    NinePatchDrawable loadingBarDrawable;
    NinePatchDrawable loadingBarDrawable2;
    SpriteBatch spriteBatch;
    Stage stage;
    Screen screen;
    Core core;
    float time;
    float accumulator = 0;

    public LoadingScreen(Core core, Screen nextScreen, float minTime, float maxTime) {
        core.setScreen(this);
        drawer = new LoadingScreenDrawer(this);
        time = MathUtils.random(minTime, maxTime);
        screen = nextScreen;
        this.core = core;
        NinePatch loadingBar = new NinePatch(new Texture("WindowUi.png"), 7, 7, 7, 7);
        loadingBarDrawable = new NinePatchDrawable(loadingBar);
        loadingBar = new NinePatch(new Texture("ProgressBar2.png"), 7, 7, 7, 7);
        loadingBarDrawable2 = new NinePatchDrawable(loadingBar);
        spriteBatch = new SpriteBatch();
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

        table.add(new TextButton("CIAO", buttonStyle));


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

        drawer.draw(spriteBatch);

        if (accumulator >= time) {
            core.setScreen(screen);
        }
        stage.act(delta);
        stage.draw();
    }

    public void draw() {
        viewport.apply();
        ScreenUtils.clear(0.3f, 0.3f, 0.3f, 1);
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        loadingBarDrawable.draw(spriteBatch, 10, 10, 700, 100);
        loadingBarDrawable2.draw(spriteBatch, 10, 10, accumulator/time*700, 100);
        spriteBatch.end();
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
