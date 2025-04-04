package progetto.screens.gameplay.entities.types.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;

import progetto.screens.gameplay.entities.skills.player.CloseRangeCombatSkill;
import progetto.screens.gameplay.entities.skills.player.Dash;
import progetto.screens.gameplay.entities.types.combat.CombatEntity;
import progetto.screens.gameplay.entities.types.entity.EntityConfig;
import progetto.screens.gameplay.manager.entity.EntityManager;
import progetto.screens.gameplay.manager.entity.PlayerMovementManager;
import progetto.utils.Cooldown;
import progetto.utils.camera.CameraManager;

public class Player extends CombatEntity {

    private final PlayerMovementManager movement;
    private final Cooldown attackCooldown;
    private final Cooldown dashCooldown;
    private final Array<CombatEntity> inRange;

    // Costruttore
    public Player(EntityConfig config, EntityManager manager) {
        super(config, manager);

        this.movement = new PlayerMovementManager(this);
        this.inRange = new Array<>();

        this.attackCooldown = new Cooldown(0.8f);
        this.attackCooldown.reset(0);
        this.dashCooldown = new Cooldown(1f);
        this.dashCooldown.reset(0);

        getSkillset().add(new Dash(this, "", "", 25f));
        getSkillset().add(new CloseRangeCombatSkill(this, "", "", 10));
        createRange(1.6f);
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
            dash();
        }
    }

    public void hit(CombatEntity entity, float damage) {
        super.hit(entity, damage);
        CameraManager.shakeTheCamera(0.1f, 0.05f);
    }

    public void dash() {
        if (dashCooldown.isReady) {
            getSkill(Dash.class).execute();
            dashCooldown.reset();
        }
    }

    /**
     * Gestione dei cooldown
     */
    @Override
    public void cooldown(float delta) {
        damageCooldown(delta);
        dashCooldown.update(delta);
        attackCooldown.update(delta);
    }
}
