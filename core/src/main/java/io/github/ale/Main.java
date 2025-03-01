package io.github.ale;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

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
        player = new Player();
        renderer = new ShapeRenderer();
        // Configura la camera e la viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(11f, 6.2f, camera);
        maps = new MapManager(camera);

        viewport.apply();

        camera.position.set(player.getWorldX(), player.getWorldX(), 0);
        camera.update();
    }

    @Override
    public void render() {

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        logic();
        draw();
        //System.err.println(player.getWorldX());
        //System.err.println(player.getWorldY());
        
    }
        
    private void logic() {
        
        //aggiorna ogni cosa nel gioco
        maps.update(camera, player);
        player.update();
        camera.position.set(player.getWorldX() + 2f / 2, player.getWorldY() + 2f / 2, 0);
        camera.update();

        maps.getMap().update(camera);
    }        
        
    

    private void draw() {
        
        //pulisce lo schermo

        ScreenUtils.clear(Color.BLACK);

        //non l'ho capito bene neanche io ma funziona con la videocamera

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);

        //disegna mappa

        maps.getMap().draw(camera);
        
        //Disegna figure geometriche / hitbox

        renderer.begin(ShapeType.Line);

        //map.drawBoxes(renderer);
        //renderer.setColor(Color.BLACK);
        //player.drawHitbox(renderer);
       
        renderer.end();
        
        //disegna immagini

        batch.begin();
        
        player.draw(batch);

        batch.end();

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
}
