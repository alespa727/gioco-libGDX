package progetto.gameplay.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import progetto.gameplay.entity.behaviors.movement.PlayerMovementManager;
import progetto.gameplay.entity.skills.player.PlayerSwordAttack;
import progetto.gameplay.entity.skills.player.PlayerDash;
import progetto.gameplay.entity.skills.player.PlayerRangedAttack;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.entity.types.living.combat.Warrior;
import progetto.gameplay.manager.ManagerEntity;
import progetto.gameplay.manager.ManagerCamera;
import progetto.gameplay.entity.components.entity.Cooldown;

public class Player extends Warrior {

    // === ATTRIBUTI ===
    private final PlayerMovementManager movement;
    private final Cooldown attackCooldown;
    private final Cooldown dashCooldown;
    private final Array<Warrior> inRange;

    // === COSTRUTTORE ===
    public Player(EntityConfig config, ManagerEntity manager) {
        super(config, manager);

        this.movement = new PlayerMovementManager(this);
        this.inRange = new Array<>();
        this.attackCooldown = new Cooldown(0.8f);
        this.dashCooldown = new Cooldown(1f);

        attackCooldown.reset(0);
        dashCooldown.reset(0);

        ManagerCamera.getInstance().position.set(config.x, config.y, 0);

        getSkillset().add(new PlayerDash(this, "", "", 12.5f));
        getSkillset().add(new PlayerSwordAttack(this, "", "",10));
        getSkillset().add(new PlayerRangedAttack(this, "", "", 5,25f, 5f));

    }

    // === METODI DI ACCESSO ===

    public PlayerMovementManager getMovement() {
        return movement;
    }

    public Array<Warrior> getInRange() {
        return inRange;
    }

    // === GESTIONE ENTITÀ IN RANGE ===

    public void addEntity(Warrior entity) {
        inRange.add(entity);
    }

    public void removeEntity(Warrior entity) {
        inRange.removeValue(entity, false);
    }

    // === COMBATTIMENTO ===

    public void useBow(){
        if(attackCooldown.isReady) {
            getSkillset().getSkill(PlayerRangedAttack.class).execute();
            attackCooldown.reset();
        }
    }

    public void useSword(){
        if(attackCooldown.isReady) {
            getSkillset().getSkill(PlayerSwordAttack.class).execute();
            attackCooldown.reset();
        }
    }

    public void dash() {
        if (dashCooldown.isReady) {
            getSkillset().getSkill(PlayerDash.class).execute();
            dashCooldown.reset();
        }
    }

    public void hit(Warrior entity, float damage, float hitForce) {
        super.hit(entity, damage, hitForce);
        ManagerCamera.shakeTheCamera(0.1f, 0.025f);
    }

    // === GESTIONE VITA ===

    public void checkIfDead() {
        if (getHealth() <= 0) {
            setDead();
            getStats().setHealth(100);
        }
    }

    // === GESTIONE ENTITY ===

    @Override
    public final void create() {
        System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).a("Player creato").reset());
    }

    @Override
    public EntityInstance despawn() {
        return null;
    }

    @Override
    public void updateEntityType(float delta) {

        movement.update(this);

        Body body = getPhysics().getBody();

        getHumanStates().setInvulnerable(body.getLinearVelocity().len() > getMaxSpeed());

        checkIfDead();

        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            useSword();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            useBow();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            dash();
        }
    }

    // === GESTIONE COOLDOWN ===

    @Override
    public void cooldown(float delta) {
        dashCooldown.update(delta);
        attackCooldown.update(delta);
    }
}
