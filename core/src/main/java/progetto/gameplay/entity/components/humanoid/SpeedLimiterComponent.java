package progetto.gameplay.entity.components.humanoid;

import com.badlogic.gdx.physics.box2d.Body;
import progetto.gameplay.entity.components.IteratableComponent;
import progetto.gameplay.entity.types.living.Humanoid;

public class SpeedLimiterComponent extends IteratableComponent {
    private final Humanoid humanoid;

    public SpeedLimiterComponent(Humanoid humanoid) {
        this.humanoid = humanoid;
    }

    @Override
    public void update(float delta) {
        Body body = humanoid.getPhysics().getBody();
        if (body == null) {
            return;
        }
        if (body.getLinearVelocity().len() > humanoid.getMaxSpeed()) {
            body.applyLinearImpulse(body.getLinearVelocity().scl(-1), body.getWorldCenter(), true);
        }
    }
}
