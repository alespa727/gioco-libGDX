package io.github.ale.screens.gameScreen.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.ale.screens.gameScreen.entity.EntityManager;
import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.player.lineofsight.LineOfSight;
import io.github.ale.screens.gameScreen.entity.player.movement.PlayerMovementManager;
import io.github.ale.screens.gameScreen.entity.player.skill.Punch;
import io.github.ale.screens.gameScreen.maps.Map;

public class Player extends Entity {
    Punch punch = new Punch(this);

    private PlayerMovementManager movement;

    
    float x=0;
    float y=0;

    Array<Vector2> direzioni = new Array<>();
    int count=0;
    float salvaDirezione = 0.5f;
    float countdown=1f;

    private final float maxDamageTime = 0.273f;
    private float countdownKnockback = 0.273f;
    private float countdownDamage = 0.273f;

    float dx;
    float dy;

    float delta;
    float angolo;

    private Circle circle;

    private static LineOfSight lineOfSight;
    public static boolean loadedLos = false;

    // Costruttore
    public Player(EntityConfig config, EntityManager manager) {
        super(config);
        this.manager = manager;
        range = new Rectangle(0, 0, 2f, 2f);
        create();
    }

    /**
     * inizializzazione player
     */

    @Override
    public final void create() {
        movement = new PlayerMovementManager();
        circle = new Circle(0, 0, 0.5f);
    }

    /**
     * disegna l'hitbox del player
     * 
     * @param renderer
     */
    @Override
    public void drawHitbox(ShapeRenderer renderer) {
        if (stati().inCollisione()) {
            renderer.setColor(Color.RED);
        }
        renderer.rect(hitbox().x, hitbox().y, hitbox().width, hitbox().height);
        renderer.setColor(Color.BLACK);
        if (punch.hit) {
            renderer.setColor(Color.RED);
        }
        renderer.rect(range.x, range.y, range.width, range.height);
        renderer.setColor(Color.BLACK);
    }

    public Vector2 predizione(Entity e){
        if (direzioni.size>=3 && e.coordinateCentro().dst(coordinateCentro()) > 3f) {
            if (direzioni.get(0).epsilonEquals(direzioni.get(1).x, direzioni.get(1).y)) {
                
                return new Vector2(coordinate()).add(direzioni.get(1).x * statistiche().getSpeed() * count, direzioni.get(1).y * statistiche().getSpeed() * count);
            
            }
        }
        count=0;
        return new Vector2(coordinate());
    }

    public Vector2 predizioneCentro(Entity e){
        return predizione(e).add(getSize().getWidth()/2, getSize().getHeight()/2);
    }

    /**
     * setta le animazioni del player a seconda della direzione
     */

    public boolean checkIfDead() {
        // Logica per controllare se il giocatore è morto
        if (statistiche().getHealth() <= 0) {
            this.stati().setIsAlive(false);
            System.out.println("Il giocatore è morto");
            System.out.println("Rianimazione..");
            statistiche().regenHealthTo(100);
        }
        return this.stati().isAlive();
    }

    /**
     * disegna punti da cui il player è visibile
     * 
     * @param renderer
     */
    public void drawLineOfSight(ShapeRenderer renderer) {
        renderer.setColor(Color.BLACK);
        lineOfSight.draw(renderer);
        // renderer.circle(circle.x, circle.y, circle.radius, 100);
        // renderer.circle(circle.x, circle.y, Player.getLineOfSight().getRaggio(),
        // 100);
    }

    /**
     * inizializza l'los solamente se non è mai stato caricato e la mappa è caricata
     */
    public void inizializzaLOS() {
        if (!loadedLos && Map.isLoaded) {
            lineOfSight = new LineOfSight();
            loadedLos = true;
        }
    }

    /**
     * returna l'oggetto dei punti da cui il player è visibile
     * 
     * @return
     */
    public LineOfSight los() {
        return lineOfSight;
    }

    public Circle circle() {
        return circle;
    }

    public void knockbackStart(float angolo){
        dx = (float) Math.cos(Math.toRadians(angolo)) * 1f;
        dy = (float) Math.sin(Math.toRadians(angolo)) * 1f;
        countdownKnockback=0.2f;
        this.angolo=angolo;
        knockback();
    }

    private void knockback() {
        if (countdownKnockback>=0f) {
            countdownKnockback-=delta;
            if(!Map.checkCollisionX(this, 0.4f, angolo)){
                x = dx * delta;
                setX(getX() + x); 
            }
                
            if (!Map.checkCollisionY(this, 0.4f, angolo)) {
                y = dy * delta;
                setY(getY() + y);
            }
                
        
            /*System.out.println(angolo);
            if (!Map.checkCollisionX(this, 0.4f, angolo)) {
                lastPos.x = getX();
                setX(getX() + dx*3f); 
            }
            
            if (!Map.checkCollisionY(this, 0.4f, angolo)) {
                lastPos.y = getY();
                setY(getY() + dy*3f);
            }
            System.out.println(dx);
            System.out.println(dy);*/
            //coordinate().add(x, y);
        }else{
            x = 0;
            y = 0;
        }
    }
    

    @Override
    public void updateEntity() {
        punch.attack();
        
        if(direzione().x > 0)range.x = coordinateCentro().x+ (float) Math.ceil(direzione().x)-getSize().getWidth()/2;
        else range.x = coordinateCentro().x+ (float) Math.floor(direzione().x)-getSize().getWidth()/2;
        if(direzione().y > 0) range.y = coordinateCentro().y+ (float) Math.ceil(direzione().y)-getSize().getWidth()/2;
        else range.y = coordinateCentro().y+ (float) Math.floor(direzione().y)-getSize().getWidth()/2;

        delta = Gdx.graphics.getDeltaTime();
        mantieniNeiLimiti();

        hitbox().x = getX() + 0.65f;
        hitbox().y = getY() + 0.55f;
    }

    @Override
    public void updateEntityType() {
        if (loadedLos) {
            los().update(this);
        }

        if (statistiche().gotDamaged) {
            countdownDamage -= delta;
            countdownKnockback -= delta;
            if (countdownDamage <= 0) {
                countdownDamage = maxDamageTime;
                countdownKnockback = maxDamageTime;
                statistiche().gotDamaged = false;
            }
        }

        inizializzaLOS();
        circle.x = getX() + getSize().getWidth() / 2;
        circle.y = getY() + getSize().getHeight() / 2;
        movement.update(this);

        if (countdown>0.01f) {
            countdown-=delta;
        }else{
            countdown=salvaDirezione;
            if (direzioni.size<3) {
                direzioni.add(new Vector2(direzione()));
                //System.out.println("aggiunta" + direzione());
            }else{
                //System.out.println(countdown);
                direzioni.set(2, new Vector2(direzioni.get(1)));
                direzioni.set(1, new Vector2(direzioni.get(0)));
                direzioni.set(0, new Vector2(direzione()));
                if (direzioni.get(0).epsilonEquals(direzioni.get(1).x, direzioni.get(1).y)){
                    if(count<2) count++;
                } else count=0;
                //System.out.println("1." + direzioni.get(0));
                //System.out.println("2." + direzioni.get(1));
                //System.out.println("3." + direzioni.get(2));
            }
            
        }
        
        
        knockback();
        checkIfDead();
    }

    @Override
    public void drawRange(ShapeRenderer renderer) {
       
    }
}
