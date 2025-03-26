package io.github.ale.screens.gameScreen.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.gameScreen.entityType.EntityManager;
import io.github.ale.screens.gameScreen.entityType.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entityType.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entityType.combatEntity.CombatEntity;
import io.github.ale.screens.gameScreen.entities.player.movement.PlayerMovementManager;
import io.github.ale.screens.gameScreen.entities.skill.skillist.Melee;
import io.github.ale.screens.settings.Settings;

public class Player extends CombatEntity {

    private PlayerMovementManager movement;

    private final Array<Vector2> recentDirections = new Array<>();
    private final Cooldown direzione = new Cooldown(0.5f);
    private int count=0;

    // Costruttore
    public Player(EntityConfig config, EntityManager manager, float attackcooldown) {
        super(config, manager, attackcooldown);
        range = new Rectangle(0, 0, 2.5f, 2.5f);
        this.movement = new PlayerMovementManager();
        skillset().add(new Melee(this, "pugno", "un pugno molto forte!", 20));
        stati().setImmortality(true);
    }


    @Override
    public void attack(){
        getSkill(Melee.class).execute();
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
            return new Vector2(coordinate()).add(recentDirections.get(1).x * statistiche().speed() * count, recentDirections.get(1).y * statistiche().speed() * count);
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
    public void checkIfDead() {
        // Logica per controllare se il giocatore è morto
        if (statistiche().health() <= 0) {
            this.stati().setIsAlive(false);
            System.out.println("Il giocatore è morto");
            System.out.println("Rianimazione..");
            statistiche().regenHealthTo(100);
        }
    }

    @Override
    public void updateEntityType(float delta) {
        movement.update(this);
        setIsAttacking(Gdx.input.isKeyPressed(Settings.getPulsanti()[6]));
        checkIfDead();
    }

    @Override
    public void cooldown(float delta){
        attackCooldown(delta);
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
