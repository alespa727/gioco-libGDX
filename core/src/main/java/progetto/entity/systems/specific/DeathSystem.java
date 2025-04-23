package progetto.entity.systems.specific;

import progetto.entity.components.specific.ai.StateComponent;
import progetto.entity.components.specific.combat.MortalComponent;
import progetto.entity.components.specific.general.BulletComponent;
import progetto.entity.components.specific.graphics.DespawnAnimationComponent;
import progetto.entity.entities.base.Entity;
import progetto.entity.entities.specific.living.Humanoid;
import progetto.player.Player;
import progetto.entity.systems.base.AutomaticSystem;
import progetto.entity.components.base.ComponentFilter;

public class DeathSystem extends AutomaticSystem {

    public DeathSystem() {
        super(ComponentFilter.all(StateComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.shouldRender()) return;

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
            if (entity.components.get(BulletComponent.class).cooldown.isReady || !state.shouldRender()) {
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
