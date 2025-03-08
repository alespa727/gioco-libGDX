package io.github.ale.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import io.github.ale.entity.abstractEnity.Dimensioni;
import io.github.ale.entity.abstractEnity.Entity;
import io.github.ale.maps.Map;

public class Player extends Entity{
    
    private PlayerMovementManager movement;

    // Costruttore
    public Player() {
        create();
    }

    /**
     * inizializzazione player
     */

    @Override
    public final void create() {
        inizializzaCoordinate(5f, 5f);

        setTexture("Finn.png");
        inizializzaHitbox(getX(), getY(), 0.65f, 0.4f);
        inizializzaDirezione("fermoS");

        setStati(true, false, false);
        setStatistiche(100, 2.5f, 10);
        inizializzaAnimazione();
        inizializzaDimensione(new Dimensioni(2f, 2f));
        movement = new PlayerMovementManager();
    }

    /**
     * disegna l'animazione del player
     * @param batch
     */
    @Override
    public void draw(SpriteBatch batch) { 
        elapsedTime += Gdx.graphics.getDeltaTime();

        setAnimation();

        batch.draw(getAnimazione().getKeyFrame(elapsedTime, true), getX(), getY(), getSize().getWidth(), getSize().getHeight());
    }

    /**
     * disegna l'hitbox del player
     * @param renderer
     */
    @Override
    public void drawHitbox(ShapeRenderer renderer) {
        if (inCollisione()) {
            renderer.setColor(Color.RED);
        }
        renderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        renderer.setColor(Color.BLACK);
    }

    /**
     * aggiorna le informazioni del player
     */
    
    public void update() {
        movement.update(this);
        
        setX(MathUtils.clamp(getX(), 0-0.65f, Map.getWidth()-hitbox.width-hitbox.width));
        setY(MathUtils.clamp(getY(), 0-0.55f, Map.getHeight()-hitbox.height*4f));

        hitbox.x = getX()+0.65f;
        hitbox.y = getY()+0.55f;

        checkIfDead();
    }

    /**
     * setta le animazioni del player a seconda della direzione
     */

    @Override
    public boolean checkIfDead() {
        // Logica per controllare se il giocatore è morto
        if (statistiche.getHealth() <= 0) {
            this.setIsAlive(false);
            System.out.println("Il giocatore è morto");
            System.out.println("Rianimazione..");
            statistiche.regenHealthTo(100);
        }
        return this.isAlive();
    }


    
}
