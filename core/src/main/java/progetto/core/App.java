package progetto.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import progetto.core.game.GameScreen;
import progetto.core.main.MainMenu;
import progetto.world.WorldManager;

// Classe principale del gioco, estende Game di libGDX
public class App extends com.badlogic.gdx.Game {

    public final AssetManager assetManager = new AssetManager(); // Gestione risorse
    public SpriteBatch batch;         // Per disegnare sprite
    public ShapeRenderer renderer;    // Per disegnare forme geometriche
    public GameScreen gameScreen;                 // Logica del gameplay

    @Override
    public void create() {
        CameraManager.init();             // Inizializza la camera
        batch = new SpriteBatch();        // Per disegnare sprite
        renderer = new ShapeRenderer();   // Per forme geometriche
        gameScreen = new GameScreen(this); // Istanzia il gioco
        setScreen(new MainMenu(this));  // Mostra il menu principale
        loadAssets();
    }

    public void loadAssets() {
        ResourceManager.get().load("entities/Sword.png", Texture.class);
        ResourceManager.get().load("entities/Player.png", Texture.class);
        ResourceManager.get().load("particle/particle.png", Texture.class);
        ResourceManager.get().load("sounds/gunshot.mp3", Sound.class);
        ResourceManager.get().load("sounds/fireball.mp3", Sound.class);
        ResourceManager.get().load("entities/Lich.png", Texture.class);
        ResourceManager.get().load("entities/Enemy.png", Texture.class);
        ResourceManager.get().load("entities/Player_shadow.png", Texture.class);
        ResourceManager.get().load("entities/Casa.png", Texture.class);
        ResourceManager.get().finishLoading();
    }

    @Override
    public void dispose() {
        WorldManager.getInstance().dispose();
        batch.dispose();          // Libera risorse grafiche
        renderer.dispose();
        ResourceManager.get().dispose();
    }
}
