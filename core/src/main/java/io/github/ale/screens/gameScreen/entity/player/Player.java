package io.github.ale.screens.gameScreen.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.abstractEntity.movement.EntityMovementManager;
import io.github.ale.screens.gameScreen.entity.player.lineofsight.LineOfSight;
import io.github.ale.screens.gameScreen.entity.player.movement.PlayerMovementManager;
import io.github.ale.screens.gameScreen.maps.Map;

public class Player extends Entity {

    private PlayerMovementManager movement;
    private EntityMovementManager entitymovement;

    private final float maxDamageTime = 0.273f;
    private float countdownKnockback = 0.273f;
    private float countdownDamage = 0.273f;

    boolean flag=false;

    float angolo;
    float dx;
    float dy;

    float delta;

    private Circle circle;
    private Vector2 lastPos;

    private static LineOfSight lineOfSight;
    public static boolean loadedLos = false;

    // Costruttore
    public Player(EntityConfig config) {
        super(config);
        create();
    }

    /**
     * inizializzazione player
     */

    @Override
    public final void create() {
        movement = new PlayerMovementManager();
        entitymovement = new EntityMovementManager();
        circle = new Circle(0, 0, 0.5f);
        lastPos = new Vector2();
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
        renderer.circle(circle.x, circle.y, 5.5f, 40);
    }

    /**
     * aggiorna le informazioni del player
     */

    @Override
    public void render() {
        delta = Gdx.graphics.getDeltaTime();
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
        // knockback(delta);

        inizializzaLOS();
        circle.x = getX() + getSize().getWidth() / 2;
        circle.y = getY() + getSize().getHeight() / 2;
        movement.update(this);
        entitymovement.update(this);
        entitymovement.clearAzioni();
        mantieniNeiLimiti();

        hitbox().x = getX() + 0.65f;
        hitbox().y = getY() + 0.55f;

        checkIfDead();
        
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

    public void inverseKnockback(){
        
    }

    public void knockback(float angolo) {
        /*if (countdownKnockback>0) {
            ComandiAzioni azione = new ComandiAzioni(Azioni.sposta, firstX+dx, firstY+dy);
            System.out.println(getX());
            System.out.println(getY());
            System.out.println(firstX+dx);
            System.out.println(firstY+dy);
            entitymovement.addAzione(azione);
            entitymovement.update(this);
            countdownKnockback-=delta;
        }*/

        dx = (float) Math.cos(Math.toRadians(angolo)) * 0.2F;
        dy = (float) Math.sin(Math.toRadians(angolo)) * 0.2F;
        
       
        if (!Map.checkCollisionX(this) && !Map.checkCollisionY(this)) {
            lastPos.x = getX();
            setX(getX() + dx);
            lastPos.y = getY();
            setY(getY() + dy);
            flag=false;
        }else{
            flag=true;
        }
            
    }
    

    @Override
    public void updateEntity() {
        
    }

    @Override
    public void updateEntityType() {
        
    }

    @Override
    public void drawRange(ShapeRenderer renderer) {
       
    }
}
