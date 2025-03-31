package io.github.ale.screens.game.entityType;

import static java.lang.System.err;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import io.github.ale.Game;
import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.defeat.DefeatScreen;
import io.github.ale.screens.game.camera.CameraManager;
import io.github.ale.screens.game.entities.player.Player;
import io.github.ale.screens.game.entityType.enemy.Enemy;
import io.github.ale.screens.game.entityType.entity.Entity;
import io.github.ale.screens.game.entityType.entity.EntityConfig;
import io.github.ale.screens.game.entityType.combat.CombatEntity;
import io.github.ale.screens.game.entityType.factories.EnemyFactory;
import io.github.ale.screens.game.entityType.mobs.LivingEntity;

public final class EntityManager {
    private final Game game;

    private final Player player;
    private final Array<Entity> entity;

    private int nextEntityId =0;

    public final Comparator<Entity> comparator;
    public final World world;

    private final Cooldown cooldown;

    public static final short WALL = 0;
    public static final short RANGE = 0x0010;
    public static final short ENEMY = 0x0020;

    public EntityManager(Game game, World world) {

        this.world = world;
        this.cooldown = new Cooldown(0.5f);
        comparator = new Comparator<Entity>() {

            @Override
            public int compare(Entity o1, Entity o2) {
                return Float.compare(o2.coordinateCentro().y, o1.coordinateCentro().y);
            }

        };

        entity = new Array<>();
        this.game = game;
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
        e.speed = 1.5f;
        e.attackdmg = 20;
        e.imageHeight = 2f;
        e.imageWidth = 2f;


        Array<Entity> array = new Array<>();
        for (int index = 0; index < 1; index++) {
            e.y++;//Oltre le mille inizia a perdere colpi
            e.id = nextEntityId;
            array.add(EnemyFactory.createEnemy("Finn", e, player.manager, 0.75f));
            nextEntityId++;
        }
        entity.addAll(array);

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

    public void drawEntity(int id, float elapsedTime){
        game.batch.begin();
        entity.sort((o1, o2) -> Float.compare(o1.coordinateCentro().y, o2.coordinateCentro().y));
        for (Entity e : entity) {
            if (CameraManager.isWithinFrustumBounds(e.coordinateCentro().x, e.coordinateCentro().y) && e.id()==id) {
                try{
                    e.draw(game.batch, elapsedTime);
                    break;
                } catch (Exception ex) {
                    System.out.println("ERRORE" + e.direzione());
                    break;
                }
            }
        }
        game.batch.end();
    }

    public void drawEnemies(float elapsedTime){
        game.batch.begin();

        for (Entity e : entity) {
            if (CameraManager.isWithinFrustumBounds(e.coordinateCentro().x, e.coordinateCentro().y) && e instanceof Enemy) {
                try{
                    e.draw(game.batch, elapsedTime);
                } catch (Exception ex) {
                    System.out.println("ERRORE" + e.direzione());
                    break;
                }
            }
        }
        game.batch.setColor(Color.WHITE);
        game.batch.end();
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

    public Entity entita(int id){
        for (int i = 0; i < entity.size; i++) {
            if (entity.get(i).id() == id) {
                //System.out.println("Entità trovata!");
                return entity.get(i);
            }
        }
        err.println("Entità non trovata!");
        return null;
    }

    public Enemy nemico(int index){
        Enemy enemy;
        int count=0;
        for (Entity e : entity) {
            if (!(e instanceof Enemy)) {
                //System.out.println("non un nemico");
            } else {
                if (index>=count) {
                    enemy = (Enemy) e;
                    return enemy;
                }
                count++;
            }
        }
        return null;
    }


    public Array<Entity> entities(){
        return entity;
    }

    public Array<CombatEntity> combatEntity(CombatEntity entity1){
        Array<CombatEntity> array = new Array<>();
        for (int i = 0; i < entity.size; i++) {
            Entity e = entity.get(i);
            if(!e.isRendered() || !(entity.get(i) instanceof CombatEntity) ) continue;
            if (true) {
                array.add((CombatEntity) e);
            }
        }
        return array;
    }

    public void removeEntity(Entity e){
        entity.removeValue(e, false);
        entity.shrink();
    }
    public void createEnemy(Class<? extends Enemy> e, EntityConfig config, float attackcooldown) {
        try {
            //System.err.println("Creata entità! id." + entityidcount);
            Constructor<? extends Entity> c = e.getConstructor(EntityConfig.class, EntityManager.class, Float.class);//Cerca il costruttore
            config.id= nextEntityId;
            Entity newEntity = c.newInstance(config, this, attackcooldown);// Crea una nuova entità
            entity.add(newEntity); //aggiunge entità
            nextEntityId++;
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
            err.println("Errore nel creare l'entità");
        }
    }


    public void swap(int i, int j) {
        Entity temp = entity.get(i);
        entity.set(i, entity.get(j));
        entity.set(j, temp);
    }

    public void sort() {
        int n = entity.size;
        for (int i = 2; i <= n; i++)
            for (int j = 0; j <= n - i; j++)
                if (entity.get(j).getY() < entity.get(j + 1).getY()) {
                    swap(j, j + 1);
                }
    }


    public float getAngleToTarget(LivingEntity attaccante, LivingEntity attaccato) {
        float deltaX = attaccato.coordinateCentro().x - attaccante.coordinateCentro().x;
        float deltaY = attaccato.coordinateCentro().y - attaccante.coordinateCentro().y;
        float angle = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));
        return angle < 0 ? angle + 360 : angle;
    }

}
