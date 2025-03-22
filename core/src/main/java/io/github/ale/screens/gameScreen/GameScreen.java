package io.github.ale.screens.gameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.ale.MyGame;
import io.github.ale.screens.gameScreen.camera.CameraManager;
import io.github.ale.screens.gameScreen.entity.EntityManager;
import io.github.ale.screens.gameScreen.gui.Gui;
import io.github.ale.screens.gameScreen.maps.Map;
import io.github.ale.screens.gameScreen.maps.MapManager;

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

    private boolean loaded=false;
    public boolean isPaused=false;

    public GameScreen(MyGame game){
        this.game=game;
    }

    @Override
    public void show() { //METODO CREATE
        System.err.println(loaded);
        if (!loaded) {
            rect = new Gui(this);
            gui = new ShapeRenderer();
            stage = new Stage(new ScreenViewport());
            root = new Table();
            root.setFillParent(true);
            stage.addActor(root);

            loaded=true;

            camera = new CameraManager();   // Configura la camera
            viewport = new FitViewport(32f, 18f, camera.get()); // grandezza telecamera
            viewport.apply(); // applica cosa si vede
            entities = new EntityManager(this.game);
            maps = new MapManager(camera.get(), viewport, entities.player(), 1); // map manager
            maps.getPlaylist().setVolume(0.1f);
            maps.getPlaylist().setLooping(0, true);
        }
        maps.getPlaylist().play(0);
        maps.getPlaylist().playfromthestart(); //opzionale
        entities.player().respawn();
    }

    @Override
    public void render(float delta) {
        if (!isPaused) {
            update();
        }
        draw();
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !isPaused) {
            pause();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && isPaused) {
            resume();
        }
    }

    /**
     * aggiorna tutto il necessario
     */
    private void update() {
        // aggiorna ogni cosa nel gioco
        maps.checkInput(); // update mappa, in caso di input
        entities.render();
        camera.update(maps, entities, viewport); // update telecamera
        maps.render(); // update visualizzazione mappa
    }

    /**
     * disegna tutto il necessario
     */

    private void draw() {
        elapsedTime += Gdx.graphics.getDeltaTime();
        // pulisce lo schermo

        ScreenUtils.clear(Color.BLACK);

        // non l'ho capito bene neanche io ma funziona con la videocamera

        viewport.apply();
        game.batch.setProjectionMatrix(camera.get().combined);
        game.renderer.setProjectionMatrix(camera.get().combined);

        maps.draw();
        drawOggetti();
        drawHitboxes();
        

        drawGUI();
    }

    public void drawGUI(){
        rect.draw();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        isPaused=true;
    }

    @Override
    public void resume() {
        isPaused=false;
        maps.getPlaylist().play(0);
        maps.getPlaylist().setVolume(0.1f);
        maps.getPlaylist().setLooping(0, true);
    }

    /**
     * disegna hitbox
     */
    public void drawHitboxes() {
        game.renderer.begin(ShapeType.Line);
        Map.getGraph().drawConnections(game.renderer);
        game.renderer.end();
        game.renderer.begin(ShapeType.Filled);
        Map.getGraph().drawNodes(game.renderer);
        entities.drawPath(game.renderer);
        game.renderer.end();
        game.renderer.begin(ShapeType.Line);
        entities.checkEachCollision(game.renderer);
        //maps.collisions(game.renderer);
        entities.hitbox(game.renderer);
        entities.range(game.renderer);
        game.renderer.end();
    }

    /**
     * disegna immagini in generale
     */

    public void drawOggetti() {
        game.batch.begin();

        entities.draw(elapsedTime);

        game.batch.end();
    }

    @Override
    public void hide() {
        maps.getPlaylist().stop();
    }

    public EntityManager entities(){ return entities; }
    public MapManager maps(){ return maps; }
}
