package io.github.ale.screens.gameScreen.entityType;

import static java.lang.System.err;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.ale.MyGame;
import io.github.ale.screens.defeatScreen.DefeatScreen;
import io.github.ale.screens.gameScreen.camera.CameraManager;
import io.github.ale.screens.gameScreen.entities.enemy.umani.Finn;
import io.github.ale.screens.gameScreen.entities.player.Player;
import io.github.ale.screens.gameScreen.entityType.abstractEnemy.Enemy;
import io.github.ale.screens.gameScreen.entityType.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entityType.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entityType.combatEntity.CombatEntity;
import io.github.ale.screens.gameScreen.entityType.entityFactories.EnemyFactory;
import io.github.ale.screens.gameScreen.entityType.livingEntity.LivingEntity;

public final class EntityManager {
    private final MyGame game;

    private final Player player;
    private final Array<Entity> entity;

    private int nextEntityId =0;

    public final Comparator<Entity> comparator;

    public EntityManager(MyGame game) {

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
        p.x = 8.5f;
        p.y = 5.5f;
        p.img = new Texture("entities/Finn.png");
        p.width = 0.5f;
        p.height = 0.4f;
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

        player = new Player(p, this, 1.5f);

        nextEntityId++;

        entity.add(player);

        EntityConfig e = new EntityConfig();
        e.nome = "Finn";
        e.descrizione = "Nemico pericoloso";
        e.x = 8.5f;
        e.y = 8.5f;
        e.img = new Texture("entities/Finn.png");
        e.width = 0.5f;
        e.height = 0.4f;
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                Array<Entity> array = new Array<>();
                for (int index = 0; index < 1; index++) { //Oltre le mille inizia a perdere colpi
                    e.id = nextEntityId;
                    array.add(EnemyFactory.createEnemy("Finn", e, player.manager, 1.5f));
                    nextEntityId++;
                }
                Gdx.app.postRunnable(() -> entity.addAll(array));
            }
        }).start();

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
            if (CameraManager.isWithinFrustumBounds(e.coordinateCentro().x, e.coordinateCentro().y)) {
                e.render(delta);
                e.setRendered(true);
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
        entity.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                return Float.compare(o1.coordinateCentro().y, o2.coordinateCentro().y);
            }
        });
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

    public void drawHitbox(){
        game.renderer.begin(ShapeRenderer.ShapeType.Line);
        for (Entity e : entity) {
            if (e.stati().inCollisione()) game.renderer.setColor(Color.RED);
            else game.renderer.setColor(Color.BLACK);
            if (CameraManager.isWithinFrustumBounds(e.coordinateCentro().x, e.coordinateCentro().y)) e.drawHitbox(game.renderer);

        }
        game.renderer.end();
    }

    public void drawRange(){
        game.renderer.begin(ShapeRenderer.ShapeType.Line);
        for (Entity e : entity) {
            if (e instanceof CombatEntity && CameraManager.isWithinFrustumBounds(e.coordinateCentro().x, e.coordinateCentro().y)) ((CombatEntity)e).drawRange(game.renderer);
        }
        game.renderer.end();
    }

    public void drawDebug(){
        drawHitbox();
        drawRange();
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

    public Array<CombatEntity> combatEntity(Rectangle rect){
        Array<CombatEntity> array = new Array<>();
        for (int i = 0; i < entity.size; i++) {
            Entity e = entity.get(i);
            if(!e.isRendered() || !(entity.get(i) instanceof CombatEntity)) continue;
            if (e.hitbox().overlaps(rect)) {
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

    public boolean isPaused(){
        return game.gameScreen.isPaused;
    }
}
