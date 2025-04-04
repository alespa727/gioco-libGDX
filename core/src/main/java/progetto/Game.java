package progetto;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import progetto.gameplay.GameScreen;
import progetto.utils.camera.CameraManager;
import progetto.menu.MainScreen;


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
