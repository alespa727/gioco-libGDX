package progetto.entity.systems.specific;

import com.badlogic.gdx.physics.box2d.Body;
import progetto.entity.components.base.ComponentFilter;
import progetto.entity.components.specific.base.StateComponent;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.entities.Entity;
import progetto.entity.entities.specific.living.Humanoid;
import progetto.entity.systems.base.IteratingSystem;

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
                h.getHumanStates().setInvulnerable(true);
            } else h.getHumanStates().setInvulnerable(false);
        }
    }

}
