package progetto.gameplay.entity.types.humanEntity.combatEntity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;

import progetto.gameplay.entity.skills.player.PlayerSwordAttack;
import progetto.gameplay.entity.skills.player.Dash;
import progetto.gameplay.entity.skills.player.MachineGun;
import progetto.gameplay.entity.types.abstractEntity.EntityConfig;
import progetto.gameplay.entity.types.abstractEntity.EntityInstance;
import progetto.gameplay.entity.types.humanEntity.combatEntity.CombatEntity;
import progetto.gameplay.manager.entity.EntityManager;
import progetto.gameplay.manager.entity.movement.PlayerMovementManager;
import progetto.gameplay.manager.camera.CameraManager;
import progetto.utils.Cooldown;

public class Player extends CombatEntity {

    // === ATTRIBUTI ===
    private final PlayerMovementManager movement;
    private final Cooldown attackCooldown;
    private final Cooldown dashCooldown;
    private final Array<CombatEntity> inRange;

    // === COSTRUTTORE ===
    public Player(EntityConfig config, EntityManager manager) {
        super(config, manager);

        this.movement = new PlayerMovementManager(this);
        this.inRange = new Array<>();
        this.attackCooldown = new Cooldown(0.8f);
        this.dashCooldown = new Cooldown(1f);

        attackCooldown.reset(0);
        dashCooldown.reset(0);

        CameraManager.getInstance().position.set(getPosition().x, getPosition().y, 0);

        getSkillset().add(new Dash(this, "", "", 25f));
        getSkillset().add(new PlayerSwordAttack(this, "", "", 10));
        getSkillset().add(new MachineGun(this, "", "", 25f, 5f));

        createRange(1.6f);
    }

    // === METODI DI ACCESSO ===

    public PlayerMovementManager getMovement() {
        return movement;
    }

    public Array<CombatEntity> getInRange() {
        return inRange;
    }

    // === GESTIONE ENTITÀ IN RANGE ===

    public void addEntity(CombatEntity entity) {
        inRange.add(entity);
    }

    public void removeEntity(CombatEntity entity) {
        inRange.removeValue(entity, false);
    }

    // === COMBATTIMENTO ===

    @Override
    public void attack() {
        if (attackCooldown.isReady) {
            getSkill(PlayerSwordAttack.class).execute();
            attackCooldown.reset();
        }
    }

    public void dash() {
        if (dashCooldown.isReady) {
            getSkill(Dash.class).execute();
            dashCooldown.reset();
        }
    }

    public void hit(CombatEntity entity, float damage) {
        super.hit(entity, damage);
        CameraManager.shakeTheCamera(0.1f, 0.05f);
    }

    // === GESTIONE VITA ===

    @Override
    public void checkIfDead() {
        if (getHealth() <= 0) {
            setDead();
            System.out.println("Il giocatore è morto");
            System.out.println("Rianimazione..");
            regenHealthTo(100);
        }
    }

    // === GESTIONE ENTITY ===

    @Override
    public final void create() {
        System.out.println("Player creato");
    }

    @Override
    public EntityInstance despawn() {
        return null;
    }

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

    // === GESTIONE COOLDOWN ===

    @Override
    public void cooldown(float delta) {
        damageCooldown(delta);
        dashCooldown.update(delta);
        attackCooldown.update(delta);
    }
}
