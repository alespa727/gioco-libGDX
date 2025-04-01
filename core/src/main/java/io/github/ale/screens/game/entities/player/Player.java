package io.github.ale.screens.game.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.game.entities.skill.skillist.player.CloseRangeCombatSkill;
import io.github.ale.screens.game.entities.skill.skillist.player.Dodge;
import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.entityType.entity.Entity;
import io.github.ale.screens.game.entityType.entity.EntityConfig;
import io.github.ale.screens.game.entityType.combat.CombatEntity;
import io.github.ale.screens.game.entities.player.movement.PlayerMovementManager;

public class Player extends CombatEntity {

    private final PlayerMovementManager movement;
    private final Cooldown direzioneCooldown;
    private final Cooldown attackCooldown;
    private final Cooldown dodgeCooldown;
    private final Array<Vector2> recentDirections;
    private final Array<CombatEntity> inRange;
    private int count;

    // Costruttore
    public Player(EntityConfig config, EntityManager manager) {
        super(config, manager);

        this.movement = new PlayerMovementManager(this);
        this.recentDirections = new Array<>();
        this.inRange = new Array<>();

        this.direzioneCooldown = new Cooldown(0.5f);
        this.attackCooldown = new Cooldown(0.8f);
        this.attackCooldown.reset(0);
        this.dodgeCooldown = new Cooldown(1f);
        this.dodgeCooldown.reset(0);

        skillset().add(new Dodge(this, "", ""));
        skillset().add(new CloseRangeCombatSkill(this, "", "", 10));
    }

    public PlayerMovementManager getMovement() {
        return movement;
    }

    /**
     * Gestione entità in range
     */
    public void addEntity(CombatEntity entity) {
        inRange.add(entity);
    }

    public void removeEntity(CombatEntity entity) {
        inRange.removeValue(entity, false);
    }

    public Array<CombatEntity> getInRange() {
        return inRange;
    }

    /**
     * Attacco del giocatore
     */
    @Override
    public void attack() {
        if (attackCooldown.isReady) {
            getSkill(CloseRangeCombatSkill.class).execute();
            attackCooldown.reset();
        }
    }

    /**
     * Metodo chiamato alla creazione del Player
     */
    @Override
    public final void create() {
        System.out.println("Player creato");
    }

    /**
     * Predizione del movimento del Player
     */
    public Vector2 predizione(Entity e) {
        if (recentDirections.size >= 3 &&
            recentDirections.get(0).epsilonEquals(recentDirections.get(1).x, recentDirections.get(1).y)) {
            return new Vector2(coordinate())
                .add(recentDirections.get(1).x * speed() * count,
                    recentDirections.get(1).y * speed() * count);
        }
        this.count = 0;
        return new Vector2(coordinate());
    }

    public Vector2 predizioneCentro(Entity e) {
        return predizione(e).add(config().imageWidth / 2, config().imageHeight / 2);
    }

    /**
     * Controlla se il Player è morto e lo rianima
     */
    @Override
    public void checkIfDead() {
        if (health() <= 0) {
            stati().setIsAlive(false);
            System.out.println("Il giocatore è morto");
            System.out.println("Rianimazione..");
            regenHealthTo(100);
        }
    }

    /**
     * Aggiorna lo stato del Player ogni frame
     */
    @Override
    public void updateEntityType(float delta) {
        movement.update(this);
        checkIfDead();
        limitSpeed();

        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            attack();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.F)) {
            dodge();
        }
    }

    public void dodge() {
        if (dodgeCooldown.isReady) {
            getSkill(Dodge.class).execute();
            dodgeCooldown.reset();
        }
    }

    /**
     * Gestione dei cooldown
     */
    @Override
    public void cooldown(float delta) {
        damageCooldown(delta);
        dodgeCooldown.update(delta);
        salvadirezionecooldown(delta);
        attackCooldown.update(delta);
    }

    /**
     * Salva la direzione attuale del Player ogni intervallo di tempo
     */
    public void salvadirezionecooldown(float delta) {
        direzioneCooldown.update(delta);
        if (direzioneCooldown.isReady) {
            direzioneCooldown.reset();
            if (recentDirections.size < 3) {
                recentDirections.add(new Vector2(direzione()));
            } else {
                recentDirections.set(2, new Vector2(recentDirections.get(1)));
                recentDirections.set(1, new Vector2(recentDirections.get(0)));
                recentDirections.set(0, new Vector2(direzione()));

                if (recentDirections.get(0).epsilonEquals(recentDirections.get(1).x, recentDirections.get(1).y)) {
                    if (count < 2) count++;
                } else {
                    count = 0;
                }
            }
        }
    }
}
