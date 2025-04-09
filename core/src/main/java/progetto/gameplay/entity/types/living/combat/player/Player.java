package progetto.gameplay.entity.types.living.combat.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;

import progetto.gameplay.entity.skills.player.PlayerSwordAttack;
import progetto.gameplay.entity.skills.player.PlayerDash;
import progetto.gameplay.entity.skills.player.PlayerRangedAttack;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.entity.types.living.combat.Warriors;
import progetto.gameplay.manager.ManagerEntity;
import progetto.gameplay.entity.behaviors.manager.entity.movement.PlayerMovementManager;
import progetto.gameplay.manager.ManagerCamera;
import progetto.utils.Cooldown;

public class Player extends Warriors {

    // === ATTRIBUTI ===
    private final PlayerMovementManager movement;
    private final Cooldown attackCooldown;
    private final Cooldown dashCooldown;
    private final Array<Warriors> inRange;

    // === COSTRUTTORE ===
    public Player(EntityConfig config, ManagerEntity manager) {
        super(config, manager);

        this.movement = new PlayerMovementManager(this);
        this.inRange = new Array<>();
        this.attackCooldown = new Cooldown(0.8f);
        this.dashCooldown = new Cooldown(1f);

        attackCooldown.reset(0);
        dashCooldown.reset(0);

        ManagerCamera.getInstance().position.set(getPosition().x, getPosition().y, 0);

        getSkillset().add(new PlayerDash(this, "", "", 25f));
        getSkillset().add(new PlayerSwordAttack(this, "", "",10));
        getSkillset().add(new PlayerRangedAttack(this, "", "", 5,25f, 5f));

        createRange(1.6f);
    }

    // === METODI DI ACCESSO ===

    public PlayerMovementManager getMovement() {
        return movement;
    }

    public Array<Warriors> getInRange() {
        return inRange;
    }

    // === GESTIONE ENTITÀ IN RANGE ===

    public void addEntity(Warriors entity) {
        inRange.add(entity);
    }

    public void removeEntity(Warriors entity) {
        inRange.removeValue(entity, false);
    }

    // === COMBATTIMENTO ===

    public void useBow(){
        if(attackCooldown.isReady) {
            getSkill(PlayerRangedAttack.class).execute();
            attackCooldown.reset();
        }
    }

    public void useSword(){
        if(attackCooldown.isReady) {
            getSkill(PlayerSwordAttack.class).execute();
            attackCooldown.reset();
        }
    }

    public void dash() {
        if (dashCooldown.isReady) {
            getSkill(PlayerDash.class).execute();
            dashCooldown.reset();
        }
    }

    public void hit(Warriors entity, float damage, float hitForce) {
        super.hit(entity, damage, hitForce);
        ManagerCamera.shakeTheCamera(0.1f, 0.025f);
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

        if(body.getLinearVelocity().len() > getSpeed()){
            invulnerable = true;
        }else invulnerable = false;

        checkIfDead();
        limitSpeed();

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
        damageCooldown(delta);
        dashCooldown.update(delta);
        attackCooldown.update(delta);
    }
}
