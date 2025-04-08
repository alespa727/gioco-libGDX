package progetto;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import progetto.audio.AudioManager;
import progetto.gameplay.Game;
import progetto.gameplay.manager.ManagerCamera;
import progetto.menu.MainScreen;

// Classe principale del gioco, estende Game di libGDX
public class Core extends com.badlogic.gdx.Game {

    public AudioManager audioManager;
    public static AssetManager assetManager = new AssetManager(); // Gestione risorse
    public SpriteBatch batch;         // Per disegnare sprite
    public ShapeRenderer renderer;    // Per disegnare forme geometriche
    public Game game;                 // Logica del gameplay

    @Override
    public void create() {
        ManagerCamera.init();             // Inizializza la camera
        batch = new SpriteBatch();        // Per disegnare sprite
        renderer = new ShapeRenderer();   // Per forme geometriche
        audioManager = new AudioManager(); // Manager per musica e suoni
        game = new Game(this);            // Istanzia il gioco
        audioManager.addMusic("music/music.ogg");
        audioManager.playMusic(0);
        setScreen(new MainScreen(this));  // Mostra il menu principale
    }

    @Override
    public void dispose() {
        batch.dispose();          // Libera risorse grafiche
        renderer.dispose();
        assetManager.dispose();   // Libera le risorse caricate
    }
}
