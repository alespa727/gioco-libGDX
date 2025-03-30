package io.github.ale.screens.game.entities.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.entityType.entity.Entity;
import io.github.ale.screens.game.entityType.entity.EntityConfig;
import io.github.ale.screens.game.entityType.combat.CombatEntity;
import io.github.ale.screens.game.entities.player.movement.PlayerMovementManager;
import io.github.ale.screens.game.entities.skill.skillist.enemy.Slash;

public class Player extends CombatEntity {

    private PlayerMovementManager movement;

    private final Array<Vector2> recentDirections = new Array<>();
    private final Cooldown direzione = new Cooldown(0.5f);
    private int count=0;



    // Costruttore
    public Player(EntityConfig config, EntityManager manager) {
        super(config, manager);
        this.movement = new PlayerMovementManager(this);
        skillset().add(new Slash(this, "pugno", "un pugno molto forte!", 20));
        stati().setImmortality(true);
    }


    @Override
    public void attack(){

        getSkill(Slash.class).execute();
    }


    /**
     * inizializzazione player
     */
    @Override
    public final void create() {
        System.out.println("Player creato");
    }

    public Vector2 predizione(Entity e){
        if ((recentDirections.size>=3) && (recentDirections.get(0).epsilonEquals(recentDirections.get(1).x, recentDirections.get(1).y)))
            return new Vector2(coordinate()).add(recentDirections.get(1).x * speed() * count, recentDirections.get(1).y * speed() * count);
        this.count=0;
        return new Vector2(coordinate());
    }

    public Vector2 predizioneCentro(Entity e){
        return predizione(e).add(config().imageWidth/2, config().imageHeight/2);
    }

    /**
     * setta le animazioni del player a seconda della direzione
     */

    @Override
    public void checkIfDead() {
        // Logica per controllare se il giocatore è morto
        if (health() <= 0) {
            this.stati().setIsAlive(false);
            System.out.println("Il giocatore è morto");
            System.out.println("Rianimazione..");
            regenHealthTo(100);
        }
    }

    @Override
    public void updateEntityType(float delta) {
        movement.update(this);
        checkIfDead();
        limitSpeed();
    }

    @Override
    public void cooldown(float delta){
        damageCooldown(delta);
        salvadirezionecooldown(delta);
    }

    public void salvadirezionecooldown(float delta){
        direzione.update(delta);
        if(direzione.isReady){
            direzione.reset();
            if (recentDirections.size<3) {
                recentDirections.add(new Vector2(direzione()));
            }else{
                recentDirections.set(2, new Vector2(recentDirections.get(1)));
                recentDirections.set(1, new Vector2(recentDirections.get(0)));
                recentDirections.set(0, new Vector2(direzione()));
                if (recentDirections.get(0).epsilonEquals(recentDirections.get(1).x, recentDirections.get(1).y)){
                    if(count<2) count++;
                } else count=0;
            }
        }
    }
}
