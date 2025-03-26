package io.github.ale.screens.pauseScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.ale.MyGame;
import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.defeatScreen.DefeatScreen;
import io.github.ale.screens.gameScreen.GameScreen;
import io.github.ale.screens.gameScreen.camera.CameraManager;

public class PauseScreen implements Screen{
    MyGame game;
    GameScreen gameScreen;

    private Stage stage;
    private Table root;
    private Table table;

    boolean resumeRequest;
    boolean pauseRequest;
    Cooldown pause;
    Cooldown resume;

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
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);
        root = new Table();
        root.setFillParent(true);
        table = new Table();
        table.setSize(stage.getWidth(), stage.getHeight());
        table.setDebug(true);
        stage.addActor(root);
        root.add(table).pad(20).bottom().fill().expand();

        Table table1 = new Table();

        parameter.size = 50;
        font = generator.generateFont(parameter); // font size 12 pixels
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        TextButton label = new TextButton("Torna al menu principale", buttonStyle);
        label.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(game.mainScreen);
                return true;
            }
        });
        label.setDebug(true);
        TextButton label2 = new TextButton("CIAOOOO", buttonStyle);
        label2.setDebug(true);
        table.setDebug(true);
        root.setDebug(true);
        table1.add(label).fill().expand().row();
        table.add().fill().expand().row();
        table1.add(label2).fill().expand().row();
        table.add(table1).pad(20).fill().expand();
        Gdx.graphics.setForegroundFPS(60);

        System.out.println("PauseScreen loaded");
        resumeRequest = false;
        pauseRequest = true;
        resume = new Cooldown(0.3f);
        pause = new Cooldown(0.7f);
        pause.reset();
        resume.reset();
        gameScreen.entities().player().statistiche().gotDamaged=false;
    }

    @Override
    public void render(float delta) {
        corners=CameraManager.getFrustumCorners();
        input();
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
            return;
        }

        drawPauseMenu(delta);

    }

    public void transitionOut(float delta){
        resume.update(delta);
        ScreenUtils.clear(0, 0, 0, 1);
        gameScreen.update(delta, true);
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
            resumeRequest = false;
            gameScreen.resume();
            Gdx.graphics.setForegroundFPS(Gdx.graphics.getDisplayMode().refreshRate);
            game.setScreen(gameScreen);
        }
    }

    float x=0;
    float y=0;

    public void drawPauseMenu(float delta) {
        ScreenUtils.clear(0, 0, 0, 1); // Pulisce lo schermo con nero pieno


        // Disegna l'entità sopra il rettangolo
        gameScreen.entities().drawEntity(0, delta);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);



        // Disegna il rettangolo trasparente (background semi-opaco)
        game.renderer.begin(ShapeRenderer.ShapeType.Filled);
        game.renderer.setColor(new Color(0, 0, 0, alpha)); // Trasparenza 50%
        game.renderer.rect(CameraManager.getFrustumCorners()[0].x, CameraManager.getFrustumCorners()[0].y,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.renderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.renderer.begin(ShapeRenderer.ShapeType.Filled);
        game.renderer.setColor(Color.RED); // Trasparenza 50%
        x+=1f*delta;
        y+=1f*delta;
        game.renderer.rect(corners[0].x+x, corners[0].y+y, 1f, 1f);
        game.renderer.end();

        stage.act();
        stage.draw();
    }



    public void transitionIn(float delta){
        pause.update(delta);
        gameScreen.updateCamera(false);
        gameScreen.entities().drawEntity(0, delta);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        float alpha = Interpolation.fade.apply(0.0f, 0.4f, 0.8f);
        game.renderer.begin(ShapeRenderer.ShapeType.Filled);
        game.renderer.setColor(new Color(0, 0, 0, alpha));

        game.renderer.rect(CameraManager.getFrustumCorners()[0].x, CameraManager.getFrustumCorners()[0].y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        game.renderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        if (pause.isReady) {
            pauseRequest = false;
            this.alpha = alpha;
            System.out.println("Pause request is ready");
        }

    }

    public void input(){
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && !resumeRequest) {
            resumeRequest = true;
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
