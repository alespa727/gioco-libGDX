package progetto.gameplay.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.ai.StateComponent;
import progetto.gameplay.entities.components.specific.combat.MortalComponent;
import progetto.gameplay.entities.components.specific.general.BulletComponent;
import progetto.gameplay.entities.components.specific.graphics.DespawnAnimationComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.specific.living.Humanoid;
import progetto.gameplay.player.Player;
import progetto.gameplay.systems.base.AutomaticSystem;
import progetto.gameplay.systems.base.System;

public class DeathSystem extends AutomaticSystem {

    public DeathSystem() {
        super(Array.with(new StateComponent()));
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
