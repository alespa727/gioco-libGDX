package io.github.ale;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.ale.entity.nemici.Nemico;
import io.github.ale.entity.player.Player;
import io.github.ale.maps.Map;
import io.github.ale.maps.MapManager;

public class Main implements ApplicationListener {

    private SpriteBatch batch;
    private SpriteBatch hud;
    private FitViewport viewport;

    private OrthographicCamera camera;
    private ShapeRenderer renderer;

    private MapManager maps;

    private Nemico enemy;
    private Player player;
    
    public static Music music;
    
    @Override
    public void create() { 
        
        batch = new SpriteBatch(); //praticamente la cosa per disegnare
        hud = new SpriteBatch(); //praticamente la cosa per disegnare la ui
        renderer = new ShapeRenderer(); //disegna forme
        // Configura la camera e la viewport

        inizializzaOggetti();
        inizializzaCamera();
        
        music = Gdx.audio.newMusic(Gdx.files.internal("mymusic.mp3"));
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

        float delta = Gdx.graphics.getDeltaTime(); // Ottiene il delta time
        //aggiorna ogni cosa nel gioco
        maps.update(camera, player); //update mappa, in caso di input
        player.update(); //update player
        enemy.update(delta, player);
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
        //drawHUD();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        renderer.dispose();
        music.dispose();
        hud.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}



    //METODI AGGIUNTIVI


    /**
     * disegna hud
     */
    public void drawHUD(){

        hud.begin();
        player.getHealth().draw(hud);
        hud.end();

    }

    /**
     * disegna hitbox
     */
    public void drawHitboxes(){

        renderer.begin(ShapeType.Line);
        if (player.inCollisione()) {
            renderer.setColor(Color.RED);
        }else renderer.setColor(Color.BLACK);
        maps.getMap().drawBoxes(renderer);
        if (player.inCollisione()) {
            renderer.setColor(Color.RED);
        }else renderer.setColor(Color.BLACK);
        player.drawHitbox(renderer);
        enemy.drawHitbox(renderer);
        enemy.drawEnemyRange(renderer);
        renderer.end();

    }

    /**
     * disegna immagini in generale
     */

    public void drawOggetti(){
        batch.begin();
        if (player.getY() > enemy.getY()) {
            player.draw(batch);
            enemy.draw(batch);
        }else{
            enemy.draw(batch);
            player.draw(batch);
        }
        batch.end();
        
    }

    /***
     * aggiorna cosa la telecamera deve seguire/modalità della telecamera
     */

    public void updateCameraView(){

        float x = camera.viewportWidth/2;
        float y = camera.viewportHeight/2;
        if (!maps.getAmbiente()) { //tipo di telecamera

            CameraStyles.lerpTo(camera, new Vector2(Map.getWidth() / 2f, player.getY() + 2f / 2));
            CameraStyles.boundaries(camera, new Vector3(x, y, 0), Map.getWidth()  - x * 2, Map.getHeight()  - y * 2);

            viewport.setWorldSize(Map.getWidth(), Map.getHeight()/16f*9f);
            camera.update();
            viewport.apply();

        }else{
            
            CameraStyles.lerpTo(camera, new Vector2(player.getX() + 2f / 2, player.getY() + 2f / 2));
            CameraStyles.boundaries(camera, new Vector3(x, y, 0), Map.getWidth()  - x * 2, Map.getHeight()  - y * 2);
            
            viewport.setWorldSize(9f, 9f*9f/16f);
            viewport.apply();
            camera.update();

        }
        
    }

    /**
     * inizializza tutti gli oggetti
     */

    public void inizializzaOggetti(){

        player = new Player();
        enemy = new Nemico();

    }

    /**
     * inizializza la telecamera
     */
    
    public void inizializzaCamera(){

        camera = new OrthographicCamera(); //telecamera
        camera.update(); //aggiornamento camera
        viewport = new FitViewport(32f, 18f, camera); //grandezza telecamera
        viewport.apply(); //applica cosa si vede
        
    }
}
