package progetto.gameplay.player;

import org.fusesource.jansi.Ansi;
import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.components.specific.sensors.InRangeListComponent;
import progetto.gameplay.entities.components.specific.combat.MultiCooldownComponent;
import progetto.gameplay.entities.components.specific.graphics.ShadowComponent;
import progetto.gameplay.entities.components.specific.base.Cooldown;
import progetto.gameplay.entities.components.specific.control.UserControllable;
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
            new MultiCooldownComponent(),
            new ShadowComponent(this),
            new InRangeListComponent(),
        };

        this.components.add(components);

        this.components.get(MultiCooldownComponent.class).add("attack", new Cooldown(0.8f, true));
        this.components.get(MultiCooldownComponent.class).add("dash", new Cooldown(1f, true));

        getAttackCooldown().reset(0.8f);
        getDashCooldown().reset(1f);

        ManagerCamera.getInstance().position.set(config.x, config.y, 0);

        getSkillset().add(new PlayerDash(this, "", "", 12.5f));
        getSkillset().add(new PlayerSwordAttack(this, "", "",10));
        getSkillset().add(new PlayerRangedAttack(this, "", "", 5,25f, 5f));
    }

    public Cooldown getAttackCooldown(){
        return this.components.get(MultiCooldownComponent.class).getCooldown("attack");
    }

    public Cooldown getDashCooldown(){
        return this.components.get(MultiCooldownComponent.class).getCooldown("dash");
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
    public EntityInstance unregister() {
        return null;
    }

}
