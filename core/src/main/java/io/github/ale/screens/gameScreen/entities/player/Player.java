package io.github.ale.screens.gameScreen.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.gameScreen.entitytypes.EntityManager;
import io.github.ale.screens.gameScreen.entitytypes.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entitytypes.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entitytypes.combatEntity.CombatEntity;
import io.github.ale.screens.gameScreen.entities.player.movement.PlayerMovementManager;
import io.github.ale.screens.gameScreen.entities.skills.skillist.Punch;
import io.github.ale.screens.settings.Settings;

public class Player extends CombatEntity {

    private PlayerMovementManager movement;

    private final Array<Vector2> direzioni = new Array<>();
    private final Cooldown direzione = new Cooldown(0.5f);
    private int count=0;

    // Costruttore
    public Player(EntityConfig config, EntityManager manager, float attackcooldown) {
        super(config, manager, attackcooldown);
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

    @Override
    public void attack(){
        getSkill(Punch.class).execute();
    }

    @Override
    public void updateEntityType() {
        movement.update(this);
        setIsAttacking(Gdx.input.isKeyPressed(Settings.getPulsanti()[6]));
        checkIfDead();
    }

    @Override
    public void cooldown(){
        attackcooldown();
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
