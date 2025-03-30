package io.github.ale.screens.pause;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.ale.MyGame;
import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.defeat.DefeatScreen;
import io.github.ale.screens.game.GameScreen;
import io.github.ale.screens.game.GameStates;
import io.github.ale.screens.game.camera.CameraManager;

public class PauseScreen implements Screen{
    MyGame game;
    GameScreen gameScreen;

    private Texture background;
    private Texture overlay;

    private Stage stage;
    private Table root;
    private Table table;

    boolean resumeRequest;
    boolean pauseRequest;
    Cooldown pause;
    Cooldown resume;

    ScreenViewport viewport;

    TextButton MainMenu;
    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/myfont.ttf"));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    BitmapFont font;

    float alpha;

    Vector3[] corners;

    public PauseScreen(MyGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
    }

    @Override
    public void show() {
        viewport = new ScreenViewport();

        stage = new Stage(new ScreenViewport());
        viewport.setCamera(gameScreen.camera().get());
        viewport.apply(false);
        background = new Texture("pause.png");
        overlay = new Texture("pause_overlay.png");
        Gdx.input.setInputProcessor(stage);
        root = new Table();
        root.setFillParent(true);
        table = new Table();
        table.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(root);
        root.add(table).pad(20).bottom().fill().expand();

        Table table1 = new Table();

        parameter.size = 50;
        font = generator.generateFont(parameter); // font size 12 pixels
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.LIGHT_GRAY;
        MainMenu = new TextButton("Menu principale", buttonStyle);
        MainMenu.getColor().a = 0;
        MainMenu.addAction(Actions.fadeIn(0.3f, Interpolation.linear));
        MainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.mainScreen);
            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                MainMenu.getColor().a = 0.7f;
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                MainMenu.getColor().a = 1f;
            }
        });


        table1.add().fill().expand().row();
        table1.add().fill().expand().row();
        table1.add().fill().expand().row();
        table1.add().fill().expand().row();
        table1.add(MainMenu).expand().row();


        table.add(table1).pad(20).fill().expand();
        Gdx.graphics.setForegroundFPS(60);

        System.out.println("PauseScreen loaded");
        resumeRequest = false;
        pauseRequest = true;
        resume = new Cooldown(0.3f);
        pause = new Cooldown(0.7f);
        pause.reset();
        resume.reset();
        gameScreen.entities().player().setHasBeenHit(false);

        stage.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        corners=CameraManager.getFrustumCorners();
        drawBackground(delta);
    }

    public void drawBackground(float delta){
        if (!gameScreen.entities().player().stati().isAlive()) {
            game.setScreen(new DefeatScreen(game));
            return;
        }

        if (pauseRequest) {
            transitionIn(delta);
            return;
        }

        if (resumeRequest) {
            transitionOut(delta);
            gameScreen.resume();
            return;
        }

        drawPauseMenu(delta);

    }

    public void transitionOut(float delta){
        resume.update(delta);
        boolean boundaries = gameScreen.maps().getAmbiente();
        gameScreen.updateCamera(boundaries);
        gameScreen.render(delta);
        gameScreen.draw(delta);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        float progress = 1 - (resume.getTimer() / resume.getMaxTime());
        float alpha = Interpolation.smoother.apply(0.5f, 0f, progress);
        game.renderer.begin(ShapeRenderer.ShapeType.Filled);
        game.renderer.setColor(new Color(0, 0, 0, alpha));
        game.renderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.renderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        if (resume.isReady) {
            game.setScreen(gameScreen);
            resumeRequest = false;
            Gdx.graphics.setForegroundFPS(Gdx.graphics.getDisplayMode().refreshRate);
        }
    }

    float x=0;
    float y=0;

    public void drawPauseMenu(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            resumeRequest = true;
        }
        ScreenUtils.clear(0, 0, 0, 1); // Pulisce lo schermo con nero

        gameScreen.draw(delta);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        game.renderer.begin(ShapeRenderer.ShapeType.Filled);
        game.renderer.setColor(Color.BLACK.cpy().mul(this.alpha));

        game.renderer.rect(CameraManager.getFrustumCorners()[0].x, CameraManager.getFrustumCorners()[0].y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        game.renderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        //stage.act();
        //stage.draw();
    }



    public void transitionIn(float delta){
        pause.update(delta);
        gameScreen.updateCamera(false);

        gameScreen.draw(delta);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        float alpha = Interpolation.fade.apply(0.0f, 0.4f, 0.8f);
        game.renderer.begin(ShapeRenderer.ShapeType.Filled);
        game.renderer.setColor(Color.BLACK.cpy().mul(alpha));

        game.renderer.rect(CameraManager.getFrustumCorners()[0].x, CameraManager.getFrustumCorners()[0].y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        game.renderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        if (pause.isReady) {
            pauseRequest = false;
            this.alpha = alpha;
            System.out.println("Pause request is ready");
        }
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
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
