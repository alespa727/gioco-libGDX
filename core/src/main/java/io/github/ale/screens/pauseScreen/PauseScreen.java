package io.github.ale.screens.pauseScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;

import io.github.ale.MyGame;
import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.defeatScreen.DefeatScreen;
import io.github.ale.screens.gameScreen.GameScreen;
import io.github.ale.screens.gameScreen.camera.CameraManager;

public class PauseScreen implements Screen{
    MyGame game;
    GameScreen gameScreen;

    boolean resumeRequest;
    Cooldown resume;


    public PauseScreen(MyGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
    }

    @Override
    public void show() {
        System.out.println("PauseScreen loaded");
        resumeRequest = false;
        resume = new Cooldown(0.5f);
    }

    @Override
    public void render(float delta) {
        input();
        drawBackground(delta);
    }

    public void drawBackground(float delta){
        if (!gameScreen.entities().player().stati().isAlive()) {
            game.setScreen(new DefeatScreen(game));
            return;
        }

        if (resumeRequest) {
            drawGame(delta);
            return;
        }

        drawPausedGame(delta);
    }

    public void drawGame(float delta){
        resume.update(delta);
        gameScreen.updateCamera(true);
        gameScreen.update(delta);
        gameScreen.draw(delta);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        float progress = 1 - (resume.getTimer() / resume.getMaxTime());
        float alpha = Interpolation.smoother.apply(0.5f, 0f, progress);
        game.renderer.begin(ShapeRenderer.ShapeType.Filled);
        game.renderer.setColor(new Color(0, 0, 0, alpha));
        game.renderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.renderer.end();

        if (resume.isReady) {
            resumeRequest = false;
            gameScreen.resume();
            game.setScreen(gameScreen);
        }
    }

    public void drawPausedGame(float delta){
        gameScreen.updateCamera(false);
        gameScreen.entities().drawEntity(0, delta);
        gameScreen.entities().player().statistiche().gotDamaged=false;
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        float alpha = Interpolation.bounceOut.apply(0.0f, 0.5f, 0.2f); // Static alpha for non-resumeRequest state
        game.renderer.begin(ShapeRenderer.ShapeType.Filled);
        game.renderer.setColor(new Color(0, 0, 0, alpha));
        game.renderer.rect(CameraManager.getFrustumCorners()[0].x, CameraManager.getFrustumCorners()[0].y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.renderer.end();
    }

    public void input(){
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && !resumeRequest) {
            resume.reset();
            resumeRequest = true;
        }
    }

    @Override
    public void resize(int width, int height) {

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
