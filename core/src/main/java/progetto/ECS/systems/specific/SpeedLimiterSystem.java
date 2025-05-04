package progetto.ECS.systems.specific;

import com.badlogic.gdx.physics.box2d.Body;
import progetto.ECS.components.base.ComponentFilter;
import progetto.ECS.components.specific.base.StateComponent;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.ECS.entities.Entity;
import progetto.ECS.entities.specific.living.Humanoid;
import progetto.ECS.systems.base.IteratingSystem;

public class SpeedLimiterSystem extends IteratingSystem {

    public SpeedLimiterSystem() {
        super(ComponentFilter.all(PhysicsComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.get(StateComponent.class).shouldBeUpdated()) return;
        if (entity instanceof Humanoid h) {
            Body body = entity.components.get(PhysicsComponent.class).getBody();
            if (body == null) {
                return;
            }
            // Da trasferire in sistema a parte
            if (body.getLinearVelocity().len() > h.getMaxSpeed()) {
                body.applyLinearImpulse(body.getLinearVelocity().scl(-1), body.getWorldCenter(), true);
            }

            h.getHumanStates().setInvulnerable(body.getLinearVelocity().len() > 10);
        }
    }

}
