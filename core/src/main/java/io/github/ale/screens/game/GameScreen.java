package io.github.ale.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.ale.MyGame;
import io.github.ale.screens.game.camera.CameraManager;
import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.gui.Gui;
import io.github.ale.screens.game.maps.Map;
import io.github.ale.screens.game.maps.MapManager;

public class GameScreen implements Screen {

    public final MyGame game;

    public float delta;

    private Stage stage;
    private Table root;

    private EntityManager entities;
    private CameraManager camera;
    private MapManager mapManager;

    public FitViewport viewport;
    private Gui rect;

    private float elapsedTime;
    public boolean loaded = false;
    public boolean isPaused = false;

    final World world;
    private Box2DDebugRenderer debugRenderer;

    Body body;

    // Aggiungi la variabile accumulator
    public static final float STEP = 1 / 60f; // Durata fissa per logica (60Hz)
    public float accumulator = 0f;

    private DefaultStateMachine<GameScreen, GameStates> gameState;

    public GameScreen(MyGame game) {
        this.game = game;
        gameState = new DefaultStateMachine<>(this);
        gameState.changeState(GameStates.PLAYING);
        Box2D.init();

        world = new World(new Vector2(0, 0), true);
    }

    @Override
    public void show() { //METODO CREATE
        if (!loaded) {
            System.out.println("GameScreen loaded");
            rect = new Gui(this);
            stage = new Stage(new ScreenViewport());
            root = new Table();
            root.setFillParent(true);
            stage.addActor(root);
            camera = new CameraManager();   // Configura la camera
            viewport = new FitViewport(32f, 18f, camera.get()); // grandezza telecamera
            viewport.apply(); // applica cosa si vede
            entities = new EntityManager(this.game, world);
            mapManager = new MapManager(camera.get(), viewport, entities, 1, world); // map manager
            loaded = true;

        } else {
            mapManager.getPlaylist().play(0);
            if (!entities.player().stati().isAlive())
                entities.player().respawn();
        }

        debugRenderer = new Box2DDebugRenderer();
        debugRenderer.setDrawVelocities(true);
        isPaused=false;

    }


    @Override
    public void render(float delta) {
        this.delta = delta;


        //System.out.println(Gdx.graphics.getFramesPerSecond() + " fps");
        //System.out.println(Gdx.app.getJavaHeap()/1000000 + "MB");

        // Pulizia dello schermo
        ScreenUtils.clear(0, 0, 0, 1);
        gameState.update();


        debugRenderer.render(world, camera.get().combined);

    }


    // Mostra i valori della memoria heap usati in byte
    public void mostraCalcoloHeap() {
        // Calcola la memoria Heap in byte
        System.out.println("Java Heap: " + Gdx.app.getJavaHeap() / (1024 * 1024) + " MB"); // Memoria Heap usata dal java
        System.out.println("Native Heap: " + Gdx.app.getNativeHeap() / (1024 * 1024) + " MB"); // Memoria Heap usata dal dispositivo
    }



    /**
     * Aggiorna tutto il necessario
     */
    public void update(float delta, boolean boundaries) {
        elapsedTime += delta; // Incrementa il tempo totale qui
        mapManager.checkInput(); // Gestisci input per la mappa
        entities.render(delta); // Aggiorna entità
        updateCamera(boundaries); // Aggiorna telecamera
    }

    /**
     * disegna tutto il necessario
     */
    public void draw(float delta) {
        mapManager.draw();
        //drawHitboxes(delta);
        drawOggetti(delta);
        drawGUI(delta);
    }

    public void drawGUI(float delta){
        rect.draw();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
    }

    @Override
    public void dispose() {
        game.batch.dispose();
        game.renderer.dispose();
        stage.dispose();
        mapManager.getPlaylist().empty();
    }

    @Override
    public void pause() {
        mapManager.getPlaylist().stop();
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
        mapManager.getPlaylist().play(0);
        mapManager.getPlaylist().setVolume(0.1f);
        mapManager.getPlaylist().setLooping(0, true);
    }

    /**
     * disegna hitbox
     */
    public void drawHitboxes(float delta) {
        maps().debugDraw(game.renderer);
        //Map.getGraph().drawConnections(game.renderer);
        //Map.getGraph().drawNodes(game.renderer);
        //entities.drawDebug();
    }

    /**
     * disegna immagini in generale
     */
    public void drawOggetti(float delta) {
        entities.draw(elapsedTime);
    }

    @Override
    public void hide() {
        mapManager.getPlaylist().stop();
    }

    public EntityManager entities() {
        return entities;
    }

    public MapManager maps() {
        return mapManager;
    }

    public void updateCamera(boolean boundaries) {
        // Aggiorna la posizione della camera con le entità o altri elementi
        camera.update(mapManager, entities, viewport, boundaries);

        // Applica il viewport (sincronizzazione con la scena)
        viewport.apply();

        // Imposta la matrice di proiezione per il batch (sprite) e renderer (tilemap)
        game.batch.setProjectionMatrix(camera.get().combined);
        game.renderer.setProjectionMatrix(camera.get().combined);

    }


    public CameraManager camera(){
        return camera;
    }

    public DefaultStateMachine<GameScreen, GameStates> gameState(){return gameState;}
}
