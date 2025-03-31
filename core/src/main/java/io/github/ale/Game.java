package io.github.ale;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.ale.screens.game.GameScreen;
import io.github.ale.screens.mainmenu.MainScreen;


public class Game extends com.badlogic.gdx.Game {

    public SpriteBatch batch;
    public ShapeRenderer renderer;

    public static AssetManager assetManager;


    public GameScreen gameScreen;

    @Override
    public void create() {
        assetManager = new AssetManager();
        batch = new SpriteBatch(); // praticamente la cosa per disegnare
        renderer = new ShapeRenderer(); // disegna forme
        gameScreen = new GameScreen(this);
        setScreen(new MainScreen(this));
    }

    @Override
    public void dispose(){
        batch.dispose();
        renderer.dispose();
    }



}
