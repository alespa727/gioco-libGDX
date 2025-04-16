package progetto.gameplay.entity.components.humanoid;

import progetto.gameplay.entity.components.IteratableComponent;
import progetto.gameplay.entity.types.living.Humanoid;

public class CheckDeathComponent extends IteratableComponent {
    private final Humanoid owner;

    public CheckDeathComponent(Humanoid owner) {
        this.owner = owner;
    }

    @Override
    public void update(float delta) {
        if (owner.getHealth() <= 0 && owner.getState().isAlive()) {
            owner.setDead();
            owner.setAwake(false);
            owner.addComponent(new DespawnComponent(owner));
        }
    }
}
