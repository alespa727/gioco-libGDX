package io.github.ale;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main implements ApplicationListener {
    private SpriteBatch batch;

    private FitViewport viewport;
    private OrthographicCamera camera;

    private int worldWidth = 16;
    private int worldHeight = 16;

    Map map;

    private Texture background;
    private Player player;

    @Override
    public void create() {
        batch = new SpriteBatch(); //praticamente la cosa per disegnare
        player = new Player();
        // Configura la camera e la viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(16, 12, camera);

        map = new Map(camera);
        
        viewport.apply();

        camera.position.set(player.getWorldX(), player.getWorldX(), 0);
        camera.update();
    }

    @Override
    public void render() {

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        logic();
        draw();
        
    }
        
    private void logic() {
        map.update(camera);

        player.update();
        camera.position.set(player.getWorldX(), player.getWorldY(), 0);

        camera.update();
    }        
        
    

    private void draw() {
        
        ScreenUtils.clear(Color.BLACK);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        
        map.draw(camera);

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
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
