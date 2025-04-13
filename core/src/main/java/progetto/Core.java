package progetto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Json;
import progetto.audio.AudioManager;
import progetto.gameplay.GameScreen;
import progetto.gameplay.entity.types.living.HumanoidInstances;
import progetto.gameplay.manager.ManagerCamera;
import progetto.gameplay.manager.ManagerWorld;
import progetto.menu.MainScreen;
import progetto.utils.TerminalCommand;

import java.sql.Array;
import java.util.HashMap;

// Classe principale del gioco, estende Game di libGDX
public class Core extends com.badlogic.gdx.Game {

    public static AssetManager assetManager = new AssetManager(); // Gestione risorse
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
        ManagerWorld.getInstance().dispose();
        batch.dispose();          // Libera risorse grafiche
        renderer.dispose();
        assetManager.dispose();   // Libera le risorse caricate
    }
}
