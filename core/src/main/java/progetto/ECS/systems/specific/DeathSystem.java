package progetto.ECS.systems.specific;

import progetto.ECS.components.base.ComponentFilter;
import progetto.ECS.components.specific.base.StateComponent;
import progetto.ECS.components.specific.combat.MortalComponent;
import progetto.ECS.components.specific.general.BulletComponent;
import progetto.ECS.components.specific.graphics.DespawnAnimationComponent;
import progetto.ECS.entities.Entity;
import progetto.ECS.entities.specific.living.Humanoid;
import progetto.ECS.systems.base.IteratingSystem;
import progetto.core.game.player.Player;

public class DeathSystem extends IteratingSystem {

    public DeathSystem() {
        super(ComponentFilter.all(StateComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.get(StateComponent.class).shouldBeUpdated()) return;

        if (entity instanceof Player player) {
            StateComponent state = entity.components.get(StateComponent.class);
            if (player.getHealth() <= 0) {
                state.setAlive(false);
                player.getStats().health = 100;
            }
            return;
        }

        if (entity.components.contains(BulletComponent.class)) {
            StateComponent state = entity.components.get(StateComponent.class);
            entity.components.get(BulletComponent.class).cooldown.update(delta);
            if (entity.components.get(BulletComponent.class).cooldown.isReady || !state.shouldBeUpdated()) {
                entity.unregister();
            }
        }

        if (entity instanceof Humanoid h && entity.components.contains(MortalComponent.class)) {
            StateComponent state = entity.components.get(StateComponent.class);
            if (h.getHealth() <= 0 && state.isAlive()) {
                state.setAlive(false);
                h.components.add(new DespawnAnimationComponent());
            }
        }
    }

}
