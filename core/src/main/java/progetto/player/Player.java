package progetto.player;

import org.fusesource.jansi.Ansi;
import progetto.entity.Engine;
import progetto.entity.components.base.Component;
import progetto.entity.components.specific.base.Cooldown;
import progetto.entity.components.specific.combat.MultiCooldownComponent;
import progetto.entity.components.specific.control.UserControllable;
import progetto.entity.components.specific.general.PlayerComponent;
import progetto.entity.components.specific.general.skills.specific.player.PlayerDash;
import progetto.entity.components.specific.general.skills.specific.player.PlayerRangedAttack;
import progetto.entity.components.specific.general.skills.specific.player.PlayerSwordAttack;
import progetto.entity.components.specific.graphics.ShadowComponent;
import progetto.entity.components.specific.sensors.InRangeListComponent;
import progetto.entity.entities.base.EntityConfig;
import progetto.entity.entities.base.EntityInstance;
import progetto.entity.entities.specific.living.combat.Warrior;

public class Player extends Warrior {
    // === COSTRUTTORE ===
    public Player(EntityConfig config, Engine manager) {
        super(config, manager);

        Component[] components = new Component[]{
            new UserControllable(),
            new MultiCooldownComponent(),
            new ShadowComponent(this),
            new InRangeListComponent(),
            new PlayerComponent()
        };

        this.components.add(components);

        this.components.get(MultiCooldownComponent.class).add("attack", new Cooldown(0.8f, true));
        this.components.get(MultiCooldownComponent.class).add("dash", new Cooldown(1f, true));

        getAttackCooldown().reset(0.8f);
        getDashCooldown().reset(1f);

        ManagerCamera.getInstance().position.set(config.x, config.y, 0);

        getSkillset().add(new PlayerDash(this, "", "", 12.5f));
        getSkillset().add(new PlayerSwordAttack(this, "", "", 10));
        getSkillset().add(new PlayerRangedAttack(this, "", "", 5, 25f, 5f));
    }

    public Cooldown getAttackCooldown() {
        return this.components.get(MultiCooldownComponent.class).getCooldown("attack");
    }

    public Cooldown getDashCooldown() {
        return this.components.get(MultiCooldownComponent.class).getCooldown("dash");
    }

    // === GESTIONE ENTITÀ IN RANGE ===

    public void hit(Warrior entity, float damage, float hitForce) {
        super.hit(entity, damage, hitForce);
    }

    // === GESTIONE ENTITY ===

    @Override
    public final void create() {
        System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).a("Player creato").reset());
    }

    @Override
    public EntityInstance unregister() {
        return null;
    }

}
