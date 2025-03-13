package io.github.ale.screens.gameScreen.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;

import io.github.ale.screens.gameScreen.maps.Map;
import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.abstractEntity.movement.EntityMovementManager;
import io.github.ale.screens.gameScreen.entity.player.lineofsight.LineOfSight;
import io.github.ale.screens.gameScreen.entity.player.movement.PlayerMovementManager;

public class Player extends Entity{
    
    private PlayerMovementManager movement;
    private EntityMovementManager entitymovement;

    private final float maxDamageTime = 0.273f;
    private float countdownKnockback = 0.273f;
    private float countdownDamage=0.273f;

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
        renderer.circle(circle.x, circle.y, 5.5f, 40);
    }

    /**
     * aggiorna le informazioni del player
     */
    
    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        if (loadedLos) {
            getLineOfSight().update(this);
        }

        if (getStatistiche().gotDamaged) {
            countdownDamage-=delta;
            countdownKnockback-=delta;
            if (countdownDamage<=0) {
                countdownDamage=maxDamageTime;
                countdownKnockback=maxDamageTime;
                getStatistiche().gotDamaged=false;
            }
            if (!Map.checkCollisionX(this)) {
                if (countdownKnockback>0) {
                    if (getStatistiche().direzioneDanno.contains("A")) {
                        setX(getX()+(getX()-1-getX())*0.02f);
                    }
                    if (getStatistiche().direzioneDanno.contains("D")) {
                        setX(getX()+(getX()+1-getX())*0.02f);
                    }
    
                    if (getStatistiche().direzioneDanno.contains("W")) {
                        setY(getY()+(getY()+1-getY())*0.02f);
                    }
                    if (getStatistiche().direzioneDanno.contains("S")) {
                        setY(getY()+(getY()-1-getY())*0.02f);
    
                    }
                    
                }
            }else{
                setX(getX()+(getX()-0.025f-getX())*0.2f);
                countdownKnockback=0f;
            }
            
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
        //renderer.circle(circle.x, circle.y, circle.radius, 100);
        //renderer.circle(circle.x, circle.y, Player.getLineOfSight().getRaggio(), 100);
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
