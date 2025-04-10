package progetto.gameplay.entity.components.humanoid;

import com.badlogic.gdx.physics.box2d.Body;
import progetto.gameplay.entity.components.IteratableComponent;
import progetto.gameplay.entity.types.living.Humanoid;

public class SpeedLimiter extends IteratableComponent {
    private final Humanoid humanoid;

    public SpeedLimiter(Humanoid humanoid) {
        this.humanoid = humanoid;
    }

    @Override
    public void update(float delta) {
        Body body = humanoid.getPhysics().getBody();
        if (body.getLinearVelocity().len() > humanoid.getSpeed()) {
            body.applyLinearImpulse(body.getLinearVelocity().scl(-1), body.getWorldCenter(), true);
        }
    }
}
