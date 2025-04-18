package progetto.gameplay.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import org.fusesource.jansi.Ansi;
import progetto.gameplay.entities.components.specific.player.DashCooldown;
import progetto.gameplay.entities.components.specific.player.DashInvulnerability;
import progetto.gameplay.entities.components.specific.player.PlayerDeathController;
import progetto.gameplay.entities.components.specific.player.PlayerMovementManager;
import progetto.gameplay.entities.components.specific.warrior.AttackCooldown;
import progetto.gameplay.entities.skills.specific.player.PlayerDash;
import progetto.gameplay.entities.skills.specific.player.PlayerRangedAttack;
import progetto.gameplay.entities.skills.specific.player.PlayerSwordAttack;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.base.EntityInstance;
import progetto.gameplay.entities.specific.specific.living.combat.Warrior;
import progetto.manager.entities.EntityManager;

public class Player extends Warrior {

    private final Array<Warrior> inRange;

    // === COSTRUTTORE ===
    public Player(EntityConfig config, EntityManager manager) {
        super(config, manager);
        this.inRange = new Array<>();
        addComponent(new PlayerDeathController(this));
        addComponent(new DashInvulnerability(this));
        addComponent(new PlayerMovementManager(this));
        addComponent(new AttackCooldown(0.8f));
        addComponent(new DashCooldown(1f));

        getAttackCooldown().reset(0.8f);
        getDashCooldown().reset(1f);

        ManagerCamera.getInstance().position.set(config.x, config.y, 0);

        getSkillset().add(new PlayerDash(this, "", "", 12.5f));
        getSkillset().add(new PlayerSwordAttack(this, "", "",10));
        getSkillset().add(new PlayerRangedAttack(this, "", "", 5,25f, 5f));
    }

    // === METODI DI ACCESSO ===

    public PlayerMovementManager getMovement() {
        return getComponent(PlayerMovementManager.class);
    }

    public Array<Warrior> getInRange() {
        return inRange;
    }

    public AttackCooldown getAttackCooldown(){
        return getComponent(AttackCooldown.class);
    }

    public DashCooldown getDashCooldown(){
        return getComponent(DashCooldown.class);
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
        if(getAttackCooldown().isReady) {
            getSkillset().getSkill(PlayerRangedAttack.class).execute();
            getAttackCooldown().reset();
        }
    }

    public void useSword(){
        if(getAttackCooldown().isReady) {
            getSkillset().getSkill(PlayerSwordAttack.class).execute();
            getAttackCooldown().reset();
        }
    }

    public void dash() {
        if (getDashCooldown().isReady) {
            getSkillset().getSkill(PlayerDash.class).execute();
            getDashCooldown().reset();
        }
    }

    public void hit(Warrior entity, float damage, float hitForce) {
        super.hit(entity, damage, hitForce);
        ManagerCamera.shakeTheCamera(0.1f, 0.025f);
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
    }

}
