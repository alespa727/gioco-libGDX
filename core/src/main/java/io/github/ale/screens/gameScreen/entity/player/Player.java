package io.github.ale.screens.gameScreen.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.gameScreen.entity.EntityManager;
import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.livingEntity.LivingEntity;
import io.github.ale.screens.gameScreen.entity.player.movement.PlayerMovementManager;
import io.github.ale.screens.gameScreen.entity.skill.skillist.Punch;

public class Player extends LivingEntity {

    private PlayerMovementManager movement;

    private final Array<Vector2> direzioni = new Array<>();
    private int count=0;
    private final float salvaDirezione = 0.5f;
    private float countdown=1f;

    private final Cooldown direzione = new Cooldown(0.5f);
    // Costruttore
    public Player(EntityConfig config, EntityManager manager) {
        super(config, manager);
        this.range = new Rectangle(0, 0, 2f, 2f);
        skillset().add(new Punch(this, "pugno", "un pugno molto forte!"));
    }

    /**
     * inizializzazione player
     */

    @Override
    public final void create() {
        System.out.println("Player creato");
        this.movement = new PlayerMovementManager();
    }

    public Vector2 predizione(Entity e){
        if ((direzioni.size>=3) && (direzioni.get(0).epsilonEquals(direzioni.get(1).x, direzioni.get(1).y))) 
            return new Vector2(coordinate()).add(direzioni.get(1).x * statistiche().speed() * count, direzioni.get(1).y * statistiche().speed() * count);
        this.count=0;
        return new Vector2(coordinate());
    }

    public Vector2 predizioneCentro(Entity e){
        return predizione(e).add(getSize().width/2, getSize().height/2);
    }

    /**
     * setta le animazioni del player a seconda della direzione
     */

    @Override
    public boolean checkIfDead() {
        // Logica per controllare se il giocatore è morto
        if (statistiche().health() <= 0) {
            this.stati().setIsAlive(false);
            System.out.println("Il giocatore è morto");
            System.out.println("Rianimazione..");
            statistiche().regenHealthTo(100);
        }
        return this.stati().isAlive();
    }

    public void attack(){
        getSkill(Punch.class).execute();
    }

    @Override
    public void updateEntityType() {
        movement.update(this);
        checkIfDead();
    }

    @Override
    public void cooldown(){

        if (atkCooldown() >= 0) {
            setAtkCooldown(atkCooldown() - delta);
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.F)){
                setAtkCooldown(1.3f); 
                attack();
            }
        }
        damagecooldown();
        
        salvadirezionecooldown();
    }

    public void salvadirezionecooldown(){
        direzione.update(delta);
        if(direzione.isReady){
            direzione.reset();
            if (direzioni.size<3) {
                direzioni.add(new Vector2(direzione()));
            }else{
                direzioni.set(2, new Vector2(direzioni.get(1)));
                direzioni.set(1, new Vector2(direzioni.get(0)));
                direzioni.set(0, new Vector2(direzione()));
                if (direzioni.get(0).epsilonEquals(direzioni.get(1).x, direzioni.get(1).y)){
                    if(count<2) count++;
                } else count=0;
            }
        }
    }

    @Override
    public void drawRange(ShapeRenderer renderer) {
       
    }

}
