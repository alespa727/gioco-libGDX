package io.github.ale.screens.game.entities.types.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.game.entities.types.combat.CombatEntity;
import io.github.ale.screens.game.entities.types.entity.EntityConfig;
import io.github.ale.screens.game.entities.skills.player.CloseRangeCombatSkill;
import io.github.ale.screens.game.entities.skills.player.Dodge;
import io.github.ale.screens.game.manager.camera.CameraManager;
import io.github.ale.screens.game.manager.entity.EntityManager;
import io.github.ale.screens.game.manager.entity.PlayerMovementManager;

public class Player extends CombatEntity {

    private final PlayerMovementManager movement;
    private final Cooldown attackCooldown;
    private final Cooldown dodgeCooldown;
    private final Array<CombatEntity> inRange;

    // Costruttore
    public Player(EntityConfig config, EntityManager manager) {
        super(config, manager);

        this.movement = new PlayerMovementManager(this);
        this.inRange = new Array<>();

        this.attackCooldown = new Cooldown(0.8f);
        this.attackCooldown.reset(0);
        this.dodgeCooldown = new Cooldown(1f);
        this.dodgeCooldown.reset(0);

        getSkillset().add(new Dodge(this, "", "", 25f));
        getSkillset().add(new CloseRangeCombatSkill(this, "", "", 10));
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
     * Controlla se il Player è morto e lo rianima
     */
    @Override
    public void checkIfDead() {
        if (getHealth() <= 0) {
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
        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            dodge();
        }
    }

    public void hit(CombatEntity entity, float damage) {
        super.hit(entity, damage);
        CameraManager.shakeTheCamera(0.2f, damage/getMaxHealth());
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
        attackCooldown.update(delta);
    }
}
