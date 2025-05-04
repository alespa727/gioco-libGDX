package progetto.ECS.systems.specific;

import com.badlogic.gdx.physics.box2d.Body;
import progetto.ECS.components.ComponentManager;
import progetto.ECS.components.base.ComponentFilter;
import progetto.ECS.components.specific.base.StateComponent;
import progetto.ECS.components.specific.base.Cooldown;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.ECS.components.specific.combat.KnockbackComponent;
import progetto.ECS.entities.Entity;
import progetto.ECS.systems.base.IteratingSystem;

public class KnockbackSystem extends IteratingSystem {

    public KnockbackSystem() {
        super(ComponentFilter.all(KnockbackComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.get(StateComponent.class).shouldBeUpdated()) return;
        ComponentManager componentManager = entity.components;
        if (componentManager.contains(KnockbackComponent.class)) {
            KnockbackComponent knockbackComponent = componentManager.get(KnockbackComponent.class);
            Cooldown cooldown = knockbackComponent.cooldown;
            cooldown.update(entity.entityEngine.delta);
            Body body = entity.components.get(PhysicsComponent.class).getBody();
            if (!cooldown.isReady) {
                body.applyLinearImpulse(knockbackComponent.hit, body.getWorldCenter(), true);
            }
        }
    }
}
