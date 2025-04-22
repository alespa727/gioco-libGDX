package progetto.gameplay.systems.specific;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.base.PhysicsComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.specific.living.Humanoid;
import progetto.gameplay.systems.base.AutomaticSystem;
import progetto.gameplay.systems.base.System;

public class SpeedLimiterSystem extends AutomaticSystem {

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.shouldRender()) return;
        if (entity instanceof Humanoid h) {
            Body body = entity.components.get(PhysicsComponent.class).getBody();
            if (body == null) {
                return;
            }
            if (body.getLinearVelocity().len() > h.getMaxSpeed()) {
                body.applyLinearImpulse(body.getLinearVelocity().scl(-1), body.getWorldCenter(), true);
                h.getHumanStates().setInvulnerable(true);
            } else h.getHumanStates().setInvulnerable(false);
        }
    }

}
