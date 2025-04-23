package progetto.entity.systems.specific;

import com.badlogic.gdx.physics.box2d.Body;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.entities.base.Entity;
import progetto.entity.entities.specific.living.Humanoid;
import progetto.entity.systems.base.AutomaticSystem;
import progetto.entity.components.base.ComponentFilter;

public class SpeedLimiterSystem extends AutomaticSystem {

    public SpeedLimiterSystem() {
        super(ComponentFilter.all(PhysicsComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.shouldRender()) return;
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
