package io.github.ale.screens.gameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.ale.MyGame;
import io.github.ale.screens.gameScreen.camera.CameraManager;
import io.github.ale.screens.gameScreen.entity.EntityManager;
import io.github.ale.screens.gameScreen.entity.player.Player;
import io.github.ale.screens.gameScreen.maps.MapManager;

public class GameScreen implements Screen {

    private final MyGame game;

    private EntityManager entities;
    private CameraManager camera;
    private MapManager maps;
    private FitViewport viewport;

    private float elapsedTime;

    public GameScreen(MyGame game){
        this.game=game;
    }

    @Override
    public void show() { //METODO CREATE
        camera = new CameraManager();   // Configura la camera
        viewport = new FitViewport(32f, 18f, camera.get()); // grandezza telecamera
        viewport.apply(); // applica cosa si vede
        entities = new EntityManager(game);
        maps = new MapManager(camera.get(), viewport, entities.player(), 1); // map manager
    }

    @Override
    public void render(float delta) {

        update();
        draw();

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
        drawLineOfSight();

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
    }

    @Override
    public void resume() {
    }

    /**
     * disegna hitbox
     */
    public void drawHitboxes() {
        game.renderer.begin(ShapeType.Line);

        entities.checkEachCollision(game.renderer);
        maps.collisions(game.renderer);
        entities.hitbox(game.renderer);
        entities.range(game.renderer);

        game.renderer.end();
        
    }

    public void drawLineOfSight(){
        if (Player.loadedLos) {
            game.renderer.begin(ShapeType.Filled);
            entities.player().drawLineOfSight(game.renderer);
            game.renderer.end();
        }
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
       
    }

}