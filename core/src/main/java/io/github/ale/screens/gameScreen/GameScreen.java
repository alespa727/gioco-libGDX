package io.github.ale.screens.gameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.ale.MyGame;
import io.github.ale.screens.gameScreen.maps.Map;
import io.github.ale.screens.gameScreen.maps.MapManager;
import io.github.ale.screens.gameScreen.camera.CameraStyles;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.enemy.umani.Finn;
import io.github.ale.screens.gameScreen.entity.player.Player;

public class GameScreen implements Screen {

    MyGame game;

    private float elapsedTime;
    private SpriteBatch hud;

    FitViewport viewport;
    OrthographicCamera camera;


    private MapManager maps;

    private Finn enemy;
    private Player player;

    public GameScreen(MyGame game){
        this.game=game;
    }

    public void create(float delta) {
        
        inizializzaCamera();

        
        hud = new SpriteBatch(); // praticamente la cosa per disegnare la ui
        // Configura la camera e la viewport

        inizializzaOggetti();

        maps = new MapManager(camera, player, 1); // map manager
    }

    @Override
    public void render(float delta) {

        update();
        draw();
        // System.err.println(player.getWorldX());
        // System.err.println(player.getWorldY());

    }

    /**
     * inizializza la telecamera
     */

     public void inizializzaCamera() {

        camera = new OrthographicCamera(); // telecamera
        camera.update(); // aggiornamento camera
        viewport = new FitViewport(32f, 18f, camera); // grandezza telecamera
        viewport.apply(); // applica cosa si vede

    }

    /**
     * aggiorna tutto il necessario
     */
    private void update() {

        float delta = Gdx.graphics.getDeltaTime(); // Ottiene il delta time
        // aggiorna ogni cosa nel gioco
        maps.update(camera, player); // update mappa, in caso di input
        player.update(); // update player
        enemy.updateEntity(delta, player);
        enemy.updateEntityType();
        updateCameraView(); // update telecamera
        maps.getMap().update(camera); // update visualizzazione mappa

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
        game.batch.setProjectionMatrix(camera.combined);
        game.renderer.setProjectionMatrix(camera.combined);

        maps.getMap().draw(camera);

        
        drawOggetti();
        drawHitboxes();
        // drawHUD();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        game.batch.dispose();
        game.renderer.dispose();
        hud.dispose();
        maps.getPlaylist().empty();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    // METODI AGGIUNTIVI

    /**
     * disegna hud
     */
    public void drawHUD() {

        hud.begin();
        player.getStatistiche().drawHealth(hud);
        hud.end();

    }

    /**
     * disegna hitbox
     */
    public void drawHitboxes() {

        

        game.renderer.begin(ShapeType.Line);

        if (player.getStati().inCollisione()) {
            game.renderer.setColor(Color.RED);
        } else game.renderer.setColor(Color.BLACK);
        maps.getMap().drawBoxes(game.renderer);

        player.drawHitbox(game.renderer);
        enemy.drawHitbox(game.renderer);

        enemy.drawEnemyRange(game.renderer);

        game.renderer.end();
        
        if (Player.loadedLos) {
            game.renderer.begin(ShapeType.Filled);
            player.drawLineOfSight(game.renderer);
            game.renderer.end();
        }
    }

    /**
     * disegna immagini in generale
     */

    public void drawOggetti() {
        game.batch.begin();
        if (player.getY() > enemy.getY()) {
            player.draw(game.batch, elapsedTime);
            enemy.draw(game.batch, elapsedTime);
        } else {
            enemy.draw(game.batch, elapsedTime);
            player.draw(game.batch, elapsedTime);
        }
        game.batch.end();

    }

    /***
     * aggiorna cosa la telecamera deve seguire/modalità della telecamera
     */

    public void updateCameraView() {

        float x = camera.viewportWidth / 2;
        float y = camera.viewportHeight / 2;
        if (!maps.getAmbiente()) { // tipo di telecamera

            CameraStyles.lerpTo(camera, new Vector2(Map.getWidth() / 2f, player.getY() + 2f / 2));
            CameraStyles.boundaries(camera, new Vector3(x, y, 0), Map.getWidth() - x * 2, Map.getHeight() - y * 2);

            viewport.setWorldSize(Map.getWidth(), Map.getHeight() / 16f * 9f);
            camera.update();
            viewport.apply();

        } else {

            CameraStyles.lerpTo(camera, new Vector2(player.getX() + 2f / 2, player.getY() + 2f / 2));
            CameraStyles.boundaries(camera, new Vector3(x, y, 0), Map.getWidth() - x * 2, Map.getHeight() - y * 2);

            viewport.setWorldSize(20f, 20f * 9f / 16f);
            viewport.apply();
            camera.update();

        }

    }

    /**
     * inizializza tutti gli oggetti
     */

    public void inizializzaOggetti() {
        EntityConfig p = new EntityConfig();
        p.x = 5f;
        p.y = 5f;
        p.imgpath="Finn.png";
        p.width=0.65f;
        p.height=0.4f;
        p.direzione="fermoS";
        p.isAlive=true;
        p.inCollisione=false;
        p.isMoving=false;
        p.hp = 100;
        p.speed = 2.5f;
        p.attackdmg=10;
        p.imageHeight=2f;
        p.imageWidth=2f;

        player = new Player(p);
        EntityConfig e = new EntityConfig();
        e.x = 3f;
        e.y = 15f;
        e.imgpath="Finn.png";
        e.width=0.65f;
        e.height=0.4f;
        e.direzione="fermoS";
        e.isAlive=true;
        e.inCollisione=false;
        e.isMoving=false;
        e.hp = 100;
        e.speed = 1.5f;
        e.attackdmg=10;
        e.imageHeight=2f;
        e.imageWidth=2f;

        this.enemy = new Finn(e);

        

    }

    

    @Override
    public void show() {
        
    }

    @Override
    public void hide() {
       
    }

}