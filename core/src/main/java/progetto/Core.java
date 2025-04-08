package progetto;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import progetto.gameplay.Game;
import progetto.gameplay.entity.behaviors.manager.camera.CameraManager;
import progetto.menu.MainScreen;


public class Core extends com.badlogic.gdx.Game {

    public static AssetManager assetManager = new AssetManager();
    public SpriteBatch batch;
    public ShapeRenderer renderer;
    public Game game;

    @Override
    public void create() {
        CameraManager.init();
        batch = new SpriteBatch(); // praticamente la cosa per disegnare
        renderer = new ShapeRenderer(); // disegna forme
        game = new Game(this);
        setScreen(new MainScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
        renderer.dispose();
        assetManager.dispose();
    }


}
