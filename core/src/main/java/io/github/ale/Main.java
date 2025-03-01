package io.github.ale;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main implements ApplicationListener {
    private SpriteBatch batch;

    private FitViewport viewport;
    private OrthographicCamera camera;
    private ShapeRenderer renderer;
    Map map;

    private Texture background;
    private Player player;

    @Override
    public void create() {
        batch = new SpriteBatch(); //praticamente la cosa per disegnare
        player = new Player();
        renderer = new ShapeRenderer();
        // Configura la camera e la viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(11f, 6.2f, camera);

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
        //System.err.println(player.getWorldX());
        //System.err.println(player.getWorldY());
        
    }
        
    private void logic() {
        

        player.update();
        camera.position.set(player.getWorldX() + 2f / 2, player.getWorldY() + 2f / 2, 0);
        camera.update();

        map.update(camera);
    }        
        
    

    private void draw() {
        
        ScreenUtils.clear(Color.BLACK);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
        map.draw(camera);
        
        batch.begin();
        
        player.draw(batch);

        batch.end();

        renderer.begin(ShapeType.Line);
        renderer.setColor(Color.BLACK);
        renderer.rect(player.hitbox.x, player.hitbox.y, player.hitbox.width, player.hitbox.height);
        renderer.end();
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
