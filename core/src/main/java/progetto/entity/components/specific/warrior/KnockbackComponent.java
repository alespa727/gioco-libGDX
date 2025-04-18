package progetto.entity.components.specific.warrior;

import com.badlogic.gdx.physics.box2d.Body;
import progetto.entity.components.base.IteratableComponent;
import progetto.entity.components.specific.entity.Cooldown;
import progetto.entity.specific.specific.living.combat.Warrior;

public class KnockbackComponent extends IteratableComponent {

    private static final float KNOCKBACK_COOLDOWN_TIME = 0.22f;
    private final Warrior owner;
    private final Cooldown cooldown = new Cooldown(KNOCKBACK_COOLDOWN_TIME);

    public KnockbackComponent(Warrior owner) {
        this.owner = owner;
    }

    public void reset() {
        cooldown.reset();
    }

    @Override
    public void update(float delta) {
        cooldown.update(owner.manager.delta);
        Body body = owner.getPhysics().getBody();
        if (!cooldown.isReady) {
            body.applyLinearImpulse(owner.getHitDirection(), body.getWorldCenter(), true);
        }
    }
}
