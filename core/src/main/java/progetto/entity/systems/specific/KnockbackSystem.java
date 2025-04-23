package progetto.entity.systems.specific;

import com.badlogic.gdx.physics.box2d.Body;
import progetto.entity.components.ComponentManager;
import progetto.entity.components.specific.base.Cooldown;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.combat.KnockbackComponent;
import progetto.entity.entities.base.Entity;
import progetto.entity.systems.base.AutomaticSystem;
import progetto.entity.components.base.ComponentFilter;

public class KnockbackSystem extends AutomaticSystem {

    public KnockbackSystem() {
        super(ComponentFilter.all(KnockbackComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.shouldRender()) return;
        ComponentManager componentManager = entity.components;
        if (componentManager.contains(KnockbackComponent.class)) {
            KnockbackComponent knockbackComponent = componentManager.get(KnockbackComponent.class);
            Cooldown cooldown = knockbackComponent.cooldown;
            cooldown.update(entity.engine.delta);
            Body body = entity.components.get(PhysicsComponent.class).getBody();
            if (!cooldown.isReady) {
                body.applyLinearImpulse(knockbackComponent.hit, body.getWorldCenter(), true);
            }
        }
    }
}
