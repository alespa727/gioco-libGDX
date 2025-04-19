package progetto.gameplay.player;

import org.fusesource.jansi.Ansi;
import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.components.specific.InRangeListComponent;
import progetto.gameplay.entities.components.specific.ShadowComponent;
import progetto.gameplay.entities.components.specific.player.DashCooldown;
import progetto.gameplay.entities.components.specific.UserControllable;
import progetto.gameplay.entities.components.specific.warrior.AttackCooldown;
import progetto.gameplay.entities.skills.specific.player.PlayerDash;
import progetto.gameplay.entities.skills.specific.player.PlayerRangedAttack;
import progetto.gameplay.entities.skills.specific.player.PlayerSwordAttack;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.base.EntityInstance;
import progetto.gameplay.entities.specific.specific.living.combat.Warrior;
import progetto.manager.entities.Engine;

public class Player extends Warrior {
    // === COSTRUTTORE ===
    public Player(EntityConfig config, Engine manager) {
        super(config, manager);

        Component[] components = new Component[]{
            new UserControllable(),
            new AttackCooldown(0.8f),
            new DashCooldown(1f),
            new ShadowComponent(this),
            new InRangeListComponent(),
        };

        componentManager.add(components);

        getAttackCooldown().reset(0.8f);
        getDashCooldown().reset(1f);

        ManagerCamera.getInstance().position.set(config.x, config.y, 0);

        getSkillset().add(new PlayerDash(this, "", "", 12.5f));
        getSkillset().add(new PlayerSwordAttack(this, "", "",10));
        getSkillset().add(new PlayerRangedAttack(this, "", "", 5,25f, 5f));
    }

    @Override
    public void updateEntityType(float delta) {

    }

    public AttackCooldown getAttackCooldown(){
        return componentManager.get(AttackCooldown.class);
    }

    public DashCooldown getDashCooldown(){
        return componentManager.get(DashCooldown.class);
    }

    // === GESTIONE ENTITÀ IN RANGE ===

    public void hit(Warrior entity, float damage, float hitForce) {
        super.hit(entity, damage, hitForce);
        ManagerCamera.shakeTheCamera(0.04f, 0.025f);
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

}
