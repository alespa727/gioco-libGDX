package progetto.gameplay.entity.components.warrior;

import com.badlogic.gdx.physics.box2d.Body;
import progetto.gameplay.entity.components.IteratableComponent;
import progetto.gameplay.entity.components.entity.Cooldown;
import progetto.gameplay.entity.types.living.combat.Warrior;

public class KnockbackComponent extends IteratableComponent {

    private static final float KNOCKBACK_COOLDOWN_TIME = 0.22f;
    private Warrior owner;
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
