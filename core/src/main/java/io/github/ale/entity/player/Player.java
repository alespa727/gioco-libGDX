package io.github.ale.entity.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;

import io.github.ale.entity.abstractEntity.Entity;
import io.github.ale.entity.abstractEntity.EntityConfig;
import io.github.ale.entity.abstractEntity.movement.EntityMovementManager;
import io.github.ale.entity.player.lineofsight.LineOfSight;
import io.github.ale.entity.player.movement.PlayerMovementManager;
import io.github.ale.maps.Map;

public class Player extends Entity{
    
    private PlayerMovementManager movement;
    private EntityMovementManager entitymovement;

    private Circle circle;

    private static LineOfSight lineOfSight;
    public static boolean loadedLos=false;

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
    }

    /**
     * disegna l'hitbox del player
     * @param renderer
     */
    @Override
    public void drawHitbox(ShapeRenderer renderer) {
        if (getStati().inCollisione()) {
            renderer.setColor(Color.RED);
        }
        renderer.rect(getHitbox().x, getHitbox().y, getHitbox().width, getHitbox().height);
        renderer.setColor(Color.BLACK);
       
    }

    /**
     * aggiorna le informazioni del player
     */
    
    public void update() {
        if (loadedLos) {
            getLineOfSight().update(this);
        }
        
        inizializzaLOS();

        circle.x = getX()+getSize().getWidth()/2;
        circle.y = getY()+getSize().getHeight()/2;

        movement.update(this);
        entitymovement.update(this);
        entitymovement.clearAzioni();

        setX(MathUtils.clamp(getX(), 0-0.65f, Map.getWidth()-getHitbox().width-getHitbox().width));
        setY(MathUtils.clamp(getY(), 0-0.55f, Map.getHeight()-getHitbox().height*4f));

        getHitbox().x = getX()+0.65f;
        getHitbox().y = getY()+0.55f;

        checkIfDead();
    }

    /**
     * setta le animazioni del player a seconda della direzione
     */

    public boolean checkIfDead() {
        // Logica per controllare se il giocatore è morto
        if (getStatistiche().getHealth() <= 0) {
            this.getStati().setIsAlive(false);
            System.out.println("Il giocatore è morto");
            System.out.println("Rianimazione..");
            getStatistiche().regenHealthTo(100);
        }
        return this.getStati().isAlive();
    }

    /**
     * disegna punti da cui il player è visibile
     * @param renderer
     */
    public void drawLineOfSight(ShapeRenderer renderer){
        renderer.setColor(Color.BLACK);
        lineOfSight.draw(renderer);
        renderer.circle(circle.x, circle.y, circle.radius, 100);
        renderer.circle(circle.x, circle.y, Player.getLineOfSight().getRaggio(), 100);
    }

    /**
     * inizializza l'los solamente se non è mai stato caricato e la mappa è caricata
     */
    public void inizializzaLOS(){
        if (!loadedLos && Map.isLoaded) {
            lineOfSight = new LineOfSight();
            loadedLos = true;
        }
    }

    /**
     * returna l'oggetto dei punti da cui il player è visibile
     * @return
     */
    public static LineOfSight getLineOfSight() {
        return lineOfSight;
    }

    public Circle circle(){
        return circle;
    }
}
