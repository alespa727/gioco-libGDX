package io.github.ale;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.ale.screens.gameplay.GameScreen;
import io.github.ale.utils.camera.CameraManager;
import io.github.ale.screens.menu.MainScreen;


public class Game extends com.badlogic.gdx.Game {

    public static AssetManager assetManager;
    public SpriteBatch batch;
    public ShapeRenderer renderer;
    public GameScreen gameScreen;

    @Override
    public void create() {
        CameraManager.init();
        assetManager = new AssetManager();
        batch = new SpriteBatch(); // praticamente la cosa per disegnare
        renderer = new ShapeRenderer(); // disegna forme
        gameScreen = new GameScreen(this);
        setScreen(new MainScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
        renderer.dispose();
    }


}
