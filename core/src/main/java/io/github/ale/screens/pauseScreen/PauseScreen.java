package io.github.ale.screens.pauseScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.ale.MyGame;
import io.github.ale.screens.gameScreen.GameScreen;
import io.github.ale.screens.settings.Settings;

public class PauseScreen implements Screen{
    MyGame game;
    GameScreen gameScreen;


    public PauseScreen(MyGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        gameScreen.entities().draw(delta);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        game.renderer.begin(ShapeRenderer.ShapeType.Filled);
        game.renderer.setColor(new Color(0, 0, 0, 0.5f));
        game.renderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.renderer.end();
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            gameScreen.resume();
            game.setScreen(gameScreen);
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
