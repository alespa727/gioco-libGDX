package io.github.ale;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.ale.maps.Map;
import io.github.ale.maps.MapManager;
import io.github.ale.player.Player;

public class Main implements ApplicationListener {

    private SpriteBatch batch;
    private FitViewport viewport;

    private OrthographicCamera camera;
    private ShapeRenderer renderer;

    private MapManager maps;

    private Player player;

    @Override
    public void create() { 
        
        batch = new SpriteBatch(); //praticamente la cosa per disegnare
        renderer = new ShapeRenderer(); //disegna forme
        // Configura la camera e la viewport

        inizializzaOggetti();
        inizializzaCamera();

        maps = new MapManager(camera, player, 2); //map manager
    }

    @Override
    public void render() {

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        update();
        draw();
        //System.err.println(player.getWorldX());
        //System.err.println(player.getWorldY());
        
    }
        
    /**
     * aggiorna tutto il necessario
     */
    private void update() {
        
        //aggiorna ogni cosa nel gioco
        maps.update(camera, player); //update mappa, in caso di input
        player.update(); //update player
        updateCameraView(); //update telecamera
        maps.getMap().update(camera); //update visualizzazione mappa

    }        
        
    /**
     * disegna tutto il necessario
     */

    private void draw() {
        
        //pulisce lo schermo

        ScreenUtils.clear(Color.BLACK);

        //non l'ho capito bene neanche io ma funziona con la videocamera

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);

        maps.getMap().draw(camera);
        drawHitboxes();
        drawOggetti();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        renderer.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}



    //METODI AGGIUNTIVI

    /**
     * disegna hitbox
     */
    public void drawHitboxes(){
        renderer.begin(ShapeType.Line);
        if (player.getInCollisione()) {
            renderer.setColor(Color.RED);
        }else renderer.setColor(Color.BLACK);
        maps.getMap().drawBoxes(renderer);
        if (player.getInCollisione()) {
            renderer.setColor(Color.RED);
        }else renderer.setColor(Color.BLACK);
        player.drawHitbox(renderer);
        renderer.end();
    }

    /**
     * disegna immagini in generale
     */

    public void drawOggetti(){
        batch.begin();
        player.draw(batch);
        batch.end();
    }

    /***
     * aggiorna cosa la telecamera deve seguire/modalità della telecamera
     */

    public void updateCameraView(){
        if (!maps.getAmbiente()) { //tipo di telecamera
            maps.getMap();
            maps.getMap();
            camera.position.set(Map.getWidth() / 2f, player.getWorldY() + 2f / 2, 0);
            viewport.setWorldSize(Map.getWidth(), Map.getHeight()/16f*9f);
            camera.update();
            viewport.apply();
        }else{
            camera.position.set(player.getWorldX() + 2f / 2, player.getWorldY() + 2f / 2, 0);
            viewport.setWorldSize(16f, 9f);
            viewport.apply();
            camera.update();
        }
        
    }

    public void inizializzaOggetti(){

        player = new Player();
        
    }


    public void inizializzaCamera(){
        camera = new OrthographicCamera(); //telecamera
        viewport = new FitViewport(16f, 9f, camera); //grandezza telecamera
        
        viewport.apply(); //applica cosa si vede

        camera.position.set(player.getWorldX(), player.getWorldX(), 0); //setta il centro della telecamera al player
        camera.update(); //aggiornamento camera
    }
}
