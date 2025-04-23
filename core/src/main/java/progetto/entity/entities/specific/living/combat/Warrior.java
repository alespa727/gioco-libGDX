package progetto.entity.entities.specific.living.combat;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import progetto.entity.Engine;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.combat.AttackRangeComponent;
import progetto.entity.components.specific.combat.KnockbackComponent;
import progetto.entity.entities.base.Entity;
import progetto.entity.entities.base.EntityConfig;
import progetto.entity.entities.specific.living.Humanoid;
import progetto.entity.entities.specific.living.HumanoidInstances;

public abstract class Warrior extends Humanoid {

    public Warrior(HumanoidInstances instance, Engine engine) {
        super(instance, engine);

        add(
            new AttackRangeComponent(),
            new KnockbackComponent()
        );

    }

    public Warrior(EntityConfig config, Engine manager) {
        super(config, manager);

        add(
            new AttackRangeComponent(),
            new KnockbackComponent()
        );

    }

    @Override
    public void create() {
        super.create();
    }

    public float getRangeRadius() {
        return get(AttackRangeComponent.class).getRangeRadius();
    }

    // === COMBATTIMENTO ===
    public void hit(Entity entity, float damage, float hitForce) {
        Body bodyThatHits = entity.components.get(PhysicsComponent.class).getBody();
        Body bodyToHit = components.get(PhysicsComponent.class).getBody();
        if (!getHumanStates().isInvulnerable()) {
            KnockbackComponent knockback = get(KnockbackComponent.class);
            knockback.hit.set(new Vector2(bodyThatHits.getPosition()).sub(bodyToHit.getPosition()).nor().scl(-1 * hitForce));
            this.inflictDamage(damage);
            knockback.cooldown.reset();
        }
    }
}
