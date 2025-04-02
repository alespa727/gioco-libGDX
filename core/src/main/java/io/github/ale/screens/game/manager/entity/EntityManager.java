package io.github.ale.screens.game.manager.entity;

import java.util.Comparator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.utils.ArrayMap;
import io.github.ale.Game;
import io.github.ale.screens.defeat.DefeatScreen;
import io.github.ale.screens.game.entities.entityTypes.player.Player;
import io.github.ale.screens.game.entities.entityTypes.enemy.Enemy;
import io.github.ale.screens.game.entities.entityTypes.entity.Entity;
import io.github.ale.screens.game.entities.entityTypes.entity.EntityConfig;
import io.github.ale.screens.game.entities.entityTypes.factories.EnemyFactory;
import io.github.ale.screens.game.manager.camera.CameraManager;

public final class EntityManager {
    private final Game game;

    private final Player player;
    private final Array<Entity> entity;
    private final ArrayMap<Integer, Entity> entityArrayMap;

    private int nextEntityId =0;

    public final Comparator<Entity> comparator;
    public final World world;
    public static final short WALL = 0;
    public static final short RANGE = 0x0010;
    public static final short ENEMY = 0x0020;

    public EntityManager(Game game, World world) {

        this.game = game;
        this.world = world;

        comparator = (o1, o2) -> Float.compare(o2.coordinateCentro().y, o1.coordinateCentro().y);

        entityArrayMap = new ArrayMap<>();
        entity = new Array<>();

        EntityConfig p = new EntityConfig();
        p.id = nextEntityId;
        p.nome = "player";
        p.x = 8.5f;
        p.y = 5.5f;
        p.img = Game.assetManager.get("entities/Finn.png", Texture.class);
        p.width = 16/32f;
        p.height = 8/16f;
        p.offsetX=0;
        p.offsetY=-0.25f;
        p.direzione = new Vector2(0, -0.5f);
        p.isAlive = true;
        p.inCollisione = false;
        p.isMoving = false;
        p.hp = 100;
        p.speed = 2.5f;
        p.attackdmg = 10;
        p.imageHeight = 2f;
        p.imageWidth = 2f;

        player = new Player(p, this);

        nextEntityId++;

        entity.add(player);

        EntityConfig e = new EntityConfig();
        e.nome = "Finn";
        e.descrizione = "Nemico pericoloso";
        e.x = 8.5f;
        e.y = 8.5f;
        e.img = new Texture("entities/nemico.png");
        e.width = 16/32f;
        e.height = 8/16f;
        e.offsetX=0;
        e.offsetY=-0.25f;
        e.direzione = new Vector2(0, -0.5f);
        e.isAlive = true;
        e.inCollisione = false;
        e.isMoving = false;
        e.hp = 100;
        e.speed = 1.75f;
        e.attackdmg = 20;
        e.imageHeight = 2f;
        e.imageWidth = 2f;

        for (int i = 0; i < 10; i++) {
            e.id = nextEntityId++;
            System.out.println(e.id);
            e.y+=0.3f;
            createEntity(EnemyFactory.createEnemy("Finn", e, this, 1.5f));
        }


    }

    public void createEntity(Entity e){
        entity.add(e);
    }

    public void draw(float elapsedTime){
        game.batch.begin();
        entity.sort(comparator);
        for (Entity e : entity) {
            if (CameraManager.isWithinFrustumBounds(e.coordinateCentro().x, e.coordinateCentro().y)) {
                try{
                    e.draw(game.batch, elapsedTime);
                } catch (Exception ex) {
                    System.out.println("ERRORE" + e.direzione());
                }
            }
        }
        game.batch.end();
    }

    public void render(float delta) {
        if (!player.stati().isAlive()) {
            game.setScreen(new DefeatScreen(game));
        }

        for (Entity e : entity) {
            if (CameraManager.isWithinFrustumBounds(e.coordinateCentro().x, e.coordinateCentro().y) || e instanceof Player) {
                e.render(delta);
                e.setRendered(true);
                e.updateNode();
            }else e.setRendered(false);
        }
    }

    public void drawPath(){
        game.renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity e : entity) {

            if (e instanceof Enemy enemy) {
                if (CameraManager.isWithinFrustumBounds(e.coordinateCentro().x, e.coordinateCentro().y)) enemy.drawPath(game.renderer);
            }

        }
        game.renderer.end();
    }

    public void drawHitbox(){
        game.renderer.begin(ShapeRenderer.ShapeType.Line);
        for (Entity e : entity) {
            if (e.stati().inCollisione()) game.renderer.setColor(Color.RED);
            else game.renderer.setColor(Color.BLACK);
            if (CameraManager.isWithinFrustumBounds(e.coordinateCentro().x, e.coordinateCentro().y)) e.drawHitbox(game.renderer);
            game.renderer.setColor(Color.WHITE);
            game.renderer.rectLine(e.body.getPosition(), player.body.getPosition(), 0.1f);
        }
        game.renderer.end();
    }

    public void drawDebug(){
        drawHitbox();
        drawPath();
    }

    public Player player() {
        return player;
    }

    public Array<Entity> entities(){
        return entity;
    }

    public void removeEntity(Entity e){
        entity.removeValue(e, false);
        entity.shrink();
    }

}
