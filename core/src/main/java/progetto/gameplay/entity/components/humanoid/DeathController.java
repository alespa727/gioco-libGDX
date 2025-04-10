package progetto.gameplay.entity.components.humanoid;

import progetto.gameplay.entity.components.IteratableComponent;
import progetto.gameplay.entity.types.living.Humanoid;

public class DeathController extends IteratableComponent {
    private final Humanoid owner;

    public DeathController(Humanoid owner) {
        this.owner = owner;
    }

    @Override
    public void update(float delta) {
        if (owner.getHealth() <= 0) {
            owner.setDead();
            owner.despawn();
        }
    }
}
