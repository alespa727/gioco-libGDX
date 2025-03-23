package io.github.ale.screens.gameScreen.entityType;

import static java.lang.System.err;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.ale.MyGame;
import io.github.ale.screens.gameScreen.camera.CameraManager;
import io.github.ale.screens.gameScreen.entityType.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entityType.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entityType.combatEntity.CombatEntity;
import io.github.ale.screens.gameScreen.entityType.abstractEnemy.Nemico;
import io.github.ale.screens.gameScreen.entities.enemy.umani.Finn;
import io.github.ale.screens.gameScreen.entityType.livingEntity.LivingEntity;
import io.github.ale.screens.gameScreen.entities.player.Player;
import io.github.ale.screens.gameScreen.maps.Map;
import io.github.ale.screens.gameScreen.pathfinding.Node;

public final class EntityManager {
    private final MyGame game;

    private final Player player;
    private final Array<Entity> entity;

    private int nextEntityId =0;


    public EntityManager(MyGame game) {
        entity = new Array<>();
        this.game = game;
        EntityConfig p = new EntityConfig();
        p.id = nextEntityId;
        p.x = 8.5f;
        p.y = 5.5f;
        p.imgpath = "./entities/Finn.png";
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
        e.imgpath = "./entities/Finn.png";
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

        for (int index = 0; index < 2; index++) {
            e.y++;
            addNemico(Finn.class, e, 1f);
        }


    }

    public void addNemico(Class<? extends Nemico> e, EntityConfig config, float attackcooldown) {
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

    public Entity entita(int id){
        for (int i = 0; i < entity.size; i++) {
            if (entity.get(i).id() == id) {
                //System.out.println("Entità trovata!");
                return entity.get(i);
            }
        }
        System.err.println("Entità non trovata!");
        return null;
    }


    /**
     * renderizza tutte le entità
     */
    public void render() {
        if (!player.stati().isAlive()) {
            game.setScreen(game.gameOver);
        }

        for (Entity e : entity) {
            if (CameraManager.isWithinFrustumBounds(e.coordinateCentro().x, e.coordinateCentro().y)) {
                e.render();
            }
        }
    }

    /**
     * disegna tutte le entità in ordine (verticale)
     * @param elapsedTime delta time
     */
    public void draw(float elapsedTime) {
        sort();
        for (Entity e : entity) {
            if (CameraManager.isWithinFrustumBounds(e.coordinateCentro().x, e.coordinateCentro().y)) {
                try{
                    e.draw(game.batch, elapsedTime);
                } catch (Exception ex) {
                    System.out.println("ERRORE" + e.direzione());
                }
            }
        }
    }

    /**
     * disegna tutte le hitbox delle entità
     * @param renderer (disegnatore)
     */
    public void hitbox(ShapeRenderer renderer){

        for (Entity e : entity) {

            if (CameraManager.isWithinFrustumBounds(e.coordinateCentro().x, e.coordinateCentro().y)) e.drawHitbox(renderer);

        }
    }

    public void drawPath(ShapeRenderer renderer){
        for (Entity e : entity) {

            if (e instanceof Nemico nemico) {
                if (CameraManager.isWithinFrustumBounds(e.coordinateCentro().x, e.coordinateCentro().y)) nemico.drawPath(renderer);
            }

        }
    }

    public Array<Entity> entita(float x, float y, float width, float height){
        Array<Entity> array = new Array<>();
        for (int i = 0; i < entity.size; i++) {
            if (CameraManager.isWithinFrustumBounds(entity.get(i).coordinateCentro().x, entity.get(i).coordinateCentro().y) && entity.get(i).coordinateCentro().x > x && entity.get(i).coordinateCentro().y > y && entity.get(i).coordinateCentro().x < x + width && entity.get(i).coordinateCentro().y < y + height) {
                array.add(entity.get(i));
            }
        }

        return array;
    }

    public Array<Entity> entita(Class<? extends Entity> entityClass, float x, float y, float width, float height){
        Array<Entity> array = new Array<>();
        for (int i = 0; i < entity.size; i++) {
            if (entity.get(i).getClass().equals(entityClass) && CameraManager.isWithinFrustumBounds(entity.get(i).coordinateCentro().x, entity.get(i).coordinateCentro().y) && entity.get(i).coordinateCentro().x > x && entity.get(i).coordinateCentro().y > y && entity.get(i).coordinateCentro().x < x + width && entity.get(i).coordinateCentro().y < y + height) {
                array.add(entity.get(i));
            }
        }

        return array;
    }

    public Array<LivingEntity> entitaviventi(float x, float y, float width, float height){
        Array<LivingEntity> array = new Array<>();
        for (int i = 0; i < entity.size; i++) {
            if (entity.get(i) instanceof LivingEntity && CameraManager.isWithinFrustumBounds(entity.get(i).coordinateCentro().x, entity.get(i).coordinateCentro().y) && entity.get(i).coordinateCentro().x > x && entity.get(i).coordinateCentro().y > y && entity.get(i).coordinateCentro().x < x + width && entity.get(i).coordinateCentro().y < y + height) {
                array.add((LivingEntity) entity.get(i));
            }
        }

        return array;
    }

    public Array<LivingEntity> entitaviventi(Rectangle rect){
        Array<LivingEntity> array = new Array<>();
        for (int i = 0; i < entity.size; i++) {
            if (entity.get(i) instanceof LivingEntity && CameraManager.isWithinFrustumBounds(entity.get(i).coordinateCentro().x, entity.get(i).coordinateCentro().y) && entity.get(i).coordinateCentro().x > rect.x && entity.get(i).coordinateCentro().y > rect.y && entity.get(i).coordinateCentro().x < rect.x + rect.width && entity.get(i).coordinateCentro().y < rect.y + rect.height) {
                array.add((LivingEntity) entity.get(i));
            }
        }

        return array;
    }

    public Array<Entity> entita(Rectangle rect){
        Array<Entity> array = new Array<>();
        for (int i = 0; i < entity.size; i++) {
            if (entity.get(i).coordinateCentro().x > rect.x && entity.get(i).coordinateCentro().y > rect.y && entity.get(i).coordinateCentro().x < rect.x + rect.width && entity.get(i).coordinateCentro().y < rect.y + rect.height) {
                array.add(entity.get(i));
            }
        }

        return array;
    }

    public boolean isentityinrect(int id, float x, float y, float width, float height){
        return entita(id).coordinateCentro().x > x && entita(id).coordinateCentro().y > y && entita(id).coordinateCentro().x < x + width && entita(id).coordinateCentro().y < y + height;
    }

    public boolean isAnyLivingEntityInRect(float x, float y, float width, float height){
        for (int i = 0; i < entity.size; i++) {
            if (entity.get(i) instanceof LivingEntity && CameraManager.isWithinFrustumBounds(entity.get(i).coordinateCentro().x, entity.get(i).coordinateCentro().y) && entity.get(i).coordinateCentro().x > x && entity.get(i).coordinateCentro().y > y && entity.get(i).coordinateCentro().x < x + width && entity.get(i).coordinateCentro().y < y + height) {
                return true;
            }
        }
        return false;
    }

    public boolean isAnyDifferentEntityInRect(Entity entity, float x, float y, float width, float height){
        for (int i = 0; i < this.entity.size; i++) {
            if (!this.entity.get(i).getClass().equals(entity.getClass()) && CameraManager.isWithinFrustumBounds(this.entity.get(i).coordinateCentro().x, this.entity.get(i).coordinateCentro().y) && this.entity.get(i).coordinateCentro().x > x && this.entity.get(i).coordinateCentro().y > y && this.entity.get(i).coordinateCentro().x < x + width && this.entity.get(i).coordinateCentro().y < y + height) {
                return true;
            }
        }
        return false;
    }


    /**
     * disegna tutti i range delle entità
     * @param renderer
     */
    public void range(ShapeRenderer renderer){

        for (Entity e : entity) {
            if (e instanceof CombatEntity && CameraManager.isWithinFrustumBounds(e.coordinateCentro().x, e.coordinateCentro().y)) ((CombatEntity)e).drawRange(renderer);
        }

    }

    /**
     * controlla se ci sono collisioni
     * @param renderer
     */
    public void checkEachCollision(ShapeRenderer renderer){
        for (Entity e : entity) {
            if (e.stati().inCollisione()) {
                renderer.setColor(Color.RED);
                return;
            } else renderer.setColor(Color.BLACK);
        }
    }



    /**
     * inverte la posizione di due entità nella lista
     * @param i
     * @param j
     */
    public void swap(int i, int j) {
        Entity temp = entity.get(i);
        entity.set(i, entity.get(j));
        entity.set(j, temp);
    }

    /**
     * riordina le entità in base alla coordinata y
     */
    public void sort() {
        int n = entity.size;
        for (int i = 2; i <= n; i++)
            for (int j = 0; j <= n - i; j++)
                if (entity.get(j).getY() < entity.get(j + 1).getY()) {
                    swap(j, j + 1);
                }
    }

    /**
     * restituisce il player
     * @return
     */
    public Player player() {
        return player;
    }
    /**
     * restituisce il nemico con indice @param index (senza contare le altre entità)
     * @return
     */
    public Nemico nemico(int index){
        Nemico nemico;
        int count=0;
        for (Entity e : entity) {
            if (!(e instanceof Nemico)) {
                //System.out.println("non un nemico");
            } else {
                if (index>=count) {
                    nemico = (Nemico) e;
                    return nemico;
                }
                count++;
            }
        }
        return null;
    }

    public void removeEntity(Entity e){
        entity.removeValue(e, false);
        entity.shrink();
    }

    public boolean ispathclear(Entity e, Node node){
        for(int j = 0; j< entity.size; j++){
            if(!entity.get(j).equals(e) && Map.getGraph().getClosestNode(entity.get(j).coordinateCentro().x, entity.get(j).coordinateCentro().y).equals(node)){
                //System.out.println("not clear");
                return false;
            }
        }
        return true;
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
