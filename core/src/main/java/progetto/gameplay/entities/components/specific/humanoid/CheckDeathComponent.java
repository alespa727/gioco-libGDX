package progetto.gameplay.entities.components.specific.humanoid;

import progetto.gameplay.entities.components.base.IteratableComponent;
import progetto.gameplay.entities.specific.specific.living.Humanoid;

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
