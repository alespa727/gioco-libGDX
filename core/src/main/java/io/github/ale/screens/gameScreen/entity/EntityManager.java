package io.github.ale.screens.gameScreen.entity;

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
import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.combatEntity.CombatEntity;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.Nemico;
import io.github.ale.screens.gameScreen.entity.enemy.umani.Finn;
import io.github.ale.screens.gameScreen.entity.livingEntity.LivingEntity;
import io.github.ale.screens.gameScreen.entity.player.Player;
import io.github.ale.screens.gameScreen.maps.Map;
import io.github.ale.screens.gameScreen.pathfinding.Node;

public final class EntityManager {
    private final MyGame game;

    private final Player player;
    private final EntityConfig p;
    private final Array<Entity> entita;

    private int entityidcount=0;


    public EntityManager(MyGame game) {
        entita = new Array<>();
        this.game = game;
        p = new EntityConfig();
        p.id = entityidcount;
        p.x = 5.5f;
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

        entityidcount++;

        entita.add(player);

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
            Constructor<? extends Entity> c = e.getConstructor(EntityConfig.class, EntityManager.class, Float.class, Player.class);//Cerca il costruttore 
            config.id=entityidcount;
            Entity newEntity = c.newInstance(config, this, attackcooldown, player);// Crea una nuova entità
            entita.add(newEntity); //aggiunge entità
            entityidcount++;
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
            err.println("Errore nel creare l'entità");
        }
    }
    
    public Entity entita(int id){
        for (int i = 0; i < entita.size; i++) {
            if (entita.get(i).id() == id) {
                //System.out.println("Entità trovata!");
                return entita.get(i);
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

        for (Entity e : entita) {
            if (CameraManager.inlimiti(e.coordinateCentro().x, e.coordinateCentro().y)) {
                e.render();
            }
        }
    }

    /**
     * disegna tutte le entità in ordine (verticale)
     * @param elapsedTime
     */
    public void draw(float elapsedTime) {
        sort();
        for (Entity e : entita) {
            if (CameraManager.inlimiti(e.coordinateCentro().x, e.coordinateCentro().y)) {
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
     * @param renderer
     */
    public void hitbox(ShapeRenderer renderer){
        
        for (Entity e : entita) {

            if (CameraManager.inlimiti(e.coordinateCentro().x, e.coordinateCentro().y)) e.drawHitbox(renderer);
            
        }
    }

    public void drawPath(ShapeRenderer renderer){
        for (Entity e : entita) {
            
            if (e instanceof Nemico nemico) {
                if (CameraManager.inlimiti(e.coordinateCentro().x, e.coordinateCentro().y)) nemico.drawPath(renderer);
            }
            
        }
    }

    public Array<Entity> entita(float x, float y, float width, float height){
        Array<Entity> array = new Array<>();
        for (int i = 0; i < entita.size; i++) {
            if (CameraManager.inlimiti(entita.get(i).coordinateCentro().x, entita.get(i).coordinateCentro().y) && entita.get(i).coordinateCentro().x > x && entita.get(i).coordinateCentro().y > y && entita.get(i).coordinateCentro().x < x + width && entita.get(i).coordinateCentro().y < y + height) {
                array.add(entita.get(i));
            }
        }

        return array;
    }

    public Array<LivingEntity> entitaviventi(float x, float y, float width, float height){
        Array<LivingEntity> array = new Array<>();
        for (int i = 0; i < entita.size; i++) {
            if (entita.get(i) instanceof LivingEntity && CameraManager.inlimiti(entita.get(i).coordinateCentro().x, entita.get(i).coordinateCentro().y) && entita.get(i).coordinateCentro().x > x && entita.get(i).coordinateCentro().y > y && entita.get(i).coordinateCentro().x < x + width && entita.get(i).coordinateCentro().y < y + height) {
                array.add((LivingEntity) entita.get(i));
            }
        }

        return array;
    }

    public Array<Entity> entita(Rectangle rect){
        Array<Entity> array = new Array<>();
        for (int i = 0; i < entita.size; i++) {
            if (entita.get(i).coordinateCentro().x > rect.x && entita.get(i).coordinateCentro().y > rect.y && entita.get(i).coordinateCentro().x < rect.x + rect.width && entita.get(i).coordinateCentro().y < rect.y + rect.height) {
                array.add(entita.get(i));
            }
        }

        return array;
    }

    public boolean isentityinrect(int id, float x, float y, float width, float height){
        boolean stato = false;
        if (entita(id).coordinateCentro().x > x && entita(id).coordinateCentro().y > y && entita(id).coordinateCentro().x < x + width && entita(id).coordinateCentro().y < y + height) {
            stato=true;
        }
        
        return stato;
    }

    public boolean isAnyLivingEntityInRect(float x, float y, float width, float height){
        for (int i = 0; i < entita.size; i++) {
            if (entita.get(i) instanceof LivingEntity && CameraManager.inlimiti(entita.get(i).coordinateCentro().x, entita.get(i).coordinateCentro().y) && entita.get(i).coordinateCentro().x > x && entita.get(i).coordinateCentro().y > y && entita.get(i).coordinateCentro().x < x + width && entita.get(i).coordinateCentro().y < y + height) {
                return true;
            }
        }
        return false;
    }

    public boolean isAnyDifferentEntityInRect(Entity entity, float x, float y, float width, float height){
        for (int i = 0; i < entita.size; i++) {
            if (!entita.get(i).getClass().equals(entity.getClass()) && CameraManager.inlimiti(entita.get(i).coordinateCentro().x, entita.get(i).coordinateCentro().y) && entita.get(i).coordinateCentro().x > x && entita.get(i).coordinateCentro().y > y && entita.get(i).coordinateCentro().x < x + width && entita.get(i).coordinateCentro().y < y + height) {
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
        
        for (Entity e : entita) {
            if (e instanceof CombatEntity && CameraManager.inlimiti(e.coordinateCentro().x, e.coordinateCentro().y)) ((CombatEntity)e).drawRange(renderer);
        }
        
    }

    /**
     * controlla se ci sono collisioni
     * @param renderer
     */
    public void checkEachCollision(ShapeRenderer renderer){
        for (Entity e : entita) {
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
        Entity temp = entita.get(i);
        entita.set(i, entita.get(j));
        entita.set(j, temp);
    }

    /**
     * riordina le entità in base alla coordinata y
     */
    public void sort() {
        int n = entita.size;
        for (int i = 2; i <= n; i++)
            for (int j = 0; j <= n - i; j++)
                if (entita.get(j).getY() < entita.get(j + 1).getY()) {
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
        for (Entity e : entita) {
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

    public void despawn(Entity e){
        entita.removeValue(e, false);
        entita.shrink();
    }

    public boolean ispathclear(Entity e, Node node){
        for(int j=0; j<entita.size; j++){
            if(!entita.get(j).equals(e) && Map.getGraph().getClosestNode(entita.get(j).coordinateCentro().x, entita.get(j).coordinateCentro().y).equals(node)){
                //System.out.println("not clear");
                return false;
            }
        }
        return true;
    }

    
    public float calcolaAngoloAttacco(LivingEntity attaccante, LivingEntity attaccato) {
        float deltaX = attaccato.coordinateCentro().x - attaccante.coordinateCentro().x;
        float deltaY = attaccato.coordinateCentro().y - attaccante.coordinateCentro().y;
        float angle = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));
        return angle < 0 ? angle + 360 : angle;
    }

    public boolean isPaused(){
        return game.gameScreen.isPaused;
    }
} 
