package progetto.gameplay.entities.specific.specific.living.combat;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.components.specific.combat.AttackRangeComponent;
import progetto.gameplay.entities.components.specific.combat.KnockbackComponent;
import progetto.gameplay.entities.components.specific.base.PhysicsComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.specific.living.Humanoid;
import progetto.gameplay.entities.specific.specific.living.HumanoidInstances;
import progetto.manager.entities.Engine;

public abstract class Warrior extends Humanoid {

    public Warrior(HumanoidInstances instance, Engine engine) {
        super(instance, engine);

        addComponents(
            new AttackRangeComponent(),
            new KnockbackComponent()
        );

    }

    public Warrior(EntityConfig config, Engine manager) {
        super(config, manager);

        addComponents(
            new AttackRangeComponent(),
            new KnockbackComponent()
        );

    }

    @Override
    public void create() {
        super.create();
    }

    public float getRangeRadius() {
        return getComponent(AttackRangeComponent.class).getRangeRadius();
    }

    // === COMBATTIMENTO ===
    public void hit(Entity entity, float damage, float hitForce) {
        Body bodyThatHits = entity.components.get(PhysicsComponent.class).getBody();
        Body bodyToHit = components.get(PhysicsComponent.class).getBody();
        if (!getHumanStates().isInvulnerable()){
            KnockbackComponent knockback = getComponent(KnockbackComponent.class);
            knockback.hit.set(new Vector2(bodyThatHits.getPosition()).sub(bodyToHit.getPosition()).nor().scl(-1*hitForce));
            this.inflictDamage(damage);
            knockback.cooldown.reset();
        }
    }
}
