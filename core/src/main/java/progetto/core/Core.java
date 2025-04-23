package progetto.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import progetto.core.game.GameScreen;
import progetto.player.ManagerCamera;
import progetto.world.WorldManager;
import progetto.screens.MainScreen;

// Classe principale del gioco, estende Game di libGDX
public class Core extends com.badlogic.gdx.Game {

    public static final AssetManager assetManager = new AssetManager(); // Gestione risorse
    public SpriteBatch batch;         // Per disegnare sprite
    public ShapeRenderer renderer;    // Per disegnare forme geometriche
    public GameScreen gameScreen;                 // Logica del gameplay

    @Override
    public void create() {
        ManagerCamera.init();             // Inizializza la camera
        batch = new SpriteBatch();        // Per disegnare sprite
        renderer = new ShapeRenderer();   // Per forme geometriche
        gameScreen = new GameScreen(this);            // Istanzia il gioco
        setScreen(new MainScreen(this));  // Mostra il menu principale
        Preferences prefs = Gdx.app.getPreferences("Preferences");
    }

    @Override
    public void dispose() {
        WorldManager.getInstance().dispose();
        batch.dispose();          // Libera risorse grafiche
        renderer.dispose();
        assetManager.dispose();   // Libera le risorse caricate
    }
}
