package io.github.ale.entity.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import io.github.ale.entity.abstractEntity.Entity;
import io.github.ale.entity.abstractEntity.caratteristiche.Dimensioni;
import io.github.ale.entity.abstractEntity.movement.EntityMovementManager;
import io.github.ale.entity.player.movement.PlayerMovementManager;
import io.github.ale.maps.Map;

public class Player extends Entity{
    
    private PlayerMovementManager movement;
    public EntityMovementManager entitymovement;

    // Costruttore
    public Player() {
        create();
    }

    /**
     * inizializzazione player
     */

    @Override
    public final void create() {
        inizializzaEntityGraphics();
        inizializzaCoordinate(5f, 5f);

        getEntityGraphics().setTexture("Finn.png");
        inizializzaHitbox(getX(), getY(), 0.65f, 0.4f);
        inizializzaDirezione("fermoS");

        inizializzaStati(true, false, false);
        inizializzaStatistiche(100, 2.5f, 10);
        inizializzaAnimazione();
        inizializzaDimensione(new Dimensioni(2f, 2f));
    
        movement = new PlayerMovementManager();
        entitymovement = new EntityMovementManager();
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
        Map.getLineOfSight().update(this);
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
}
