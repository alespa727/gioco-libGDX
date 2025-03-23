package io.github.ale.screens.gameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.ale.MyGame;
import io.github.ale.screens.gameScreen.camera.CameraManager;
import io.github.ale.screens.gameScreen.entityType.EntityManager;
import io.github.ale.screens.gameScreen.gui.Gui;
import io.github.ale.screens.gameScreen.maps.Map;
import io.github.ale.screens.gameScreen.maps.MapManager;
import io.github.ale.screens.pauseScreen.PauseScreen;
import io.github.ale.screens.settings.Settings;

public class GameScreen implements Screen {

    private final MyGame game;

    private Stage stage;
    private Table root;

    private EntityManager entities;
    private CameraManager camera;
    private MapManager maps;
    private FitViewport viewport;

    @SuppressWarnings("unused")
    private ShapeRenderer gui;
    private Gui rect;

    private float elapsedTime;

    private boolean loaded = false;
    public boolean isPaused = false;

    public GameScreen(MyGame game) {
        this.game = game;
    }

    @Override
    public void show() { //METODO CREATE
        if (!loaded) {
            System.out.println("GameScreen loaded");
            rect = new Gui(this);
            gui = new ShapeRenderer();
            stage = new Stage(new ScreenViewport());
            root = new Table();
            root.setFillParent(true);
            stage.addActor(root);
            camera = new CameraManager();   // Configura la camera
            viewport = new FitViewport(32f, 18f, camera.get()); // grandezza telecamera
            viewport.apply(); // applica cosa si vede
            entities = new EntityManager(this.game);
            maps = new MapManager(camera.get(), viewport, entities.player(), 1); // map manager

            loaded = true;
        } else {
            maps.getPlaylist().play(0);
            if (!entities.player().stati().isAlive())
                entities.player().respawn();
        }
    }

    @Override
    public void render(float delta) {
        if (!isPaused) {
            update();
            draw();
        }
        if (Gdx.input.isKeyPressed(Settings.getPulsanti()[7]) && !isPaused) {
            isPaused = true;
            game.setScreen(new PauseScreen(game, this));
        }
    }

    /**
     * aggiorna tutto il necessario
     */
    public void update() {
        // aggiorna ogni cosa nel gioco
        maps.checkInput(); // update mappa, in caso di input
        entities.render();
        updateCamera();
        maps.render(); // update visualizzazione mappa
    }

    /**
     * disegna tutto il necessario
     */

    public void draw() {
        elapsedTime += Gdx.graphics.getDeltaTime();
        // pulisce lo schermo

        ScreenUtils.clear(Color.BLACK);

        maps.draw();

        //drawHitboxes();
        drawOggetti();
        drawGUI();
    }

    public void drawGUI() {
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
        maps.getPlaylist().empty();
    }

    @Override
    public void pause() {
        maps.getPlaylist().stop();
        isPaused = true;

        // Synchronize the camera with the player before pausing
        Vector2 playerPosition = entities.player().coordinateCentro();
        camera.get().position.set(playerPosition.x, playerPosition.y, 0);
        camera.get().update();
    }

    @Override
    public void resume() {
        isPaused = false;
        maps.getPlaylist().play(0);
        maps.getPlaylist().setVolume(0.1f);
        maps.getPlaylist().setLooping(0, true);
    }

    /**
     * disegna hitbox
     */
    @SuppressWarnings("unused")
    public void drawHitboxes() {
        Map.getGraph().drawConnections(game.renderer);
        Map.getGraph().drawNodes(game.renderer);
        entities.drawDebug();
    }

    /**
     * disegna immagini in generale
     */

    public void drawOggetti() {
        entities.draw(elapsedTime);
    }

    @Override
    public void hide() {
        maps.getPlaylist().stop();
    }

    public EntityManager entities() {
        return entities;
    }

    @SuppressWarnings("unused")
    public MapManager maps() {
        return maps;
    }

    public void updateCamera(){
        camera.update(maps, entities, viewport); // update telecamera
        viewport.apply();
        game.batch.setProjectionMatrix(camera.get().combined);
        game.renderer.setProjectionMatrix(camera.get().combined);

    }
}
