package progetto.gameplay.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.ai.StateComponent;
import progetto.gameplay.entities.components.specific.combat.MortalComponent;
import progetto.gameplay.entities.components.specific.general.BulletComponent;
import progetto.gameplay.entities.components.specific.graphics.DespawnAnimationComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.specific.living.Humanoid;
import progetto.gameplay.player.Player;
import progetto.gameplay.systems.base.System;

public class DeathSystem extends System {
    @Override
    public void update(float delta, Array<Entity> list) {
        for (Entity e : list) {
            if (!e.shouldRender()) continue;

            if (e instanceof Player player) {
                StateComponent state = e.components.get(StateComponent.class);
                if (player.getHealth() <= 0) {
                    state.setAlive(false);
                    player.getStats().health = 100;
                }
                continue;
            }

            if (e.components.contains(BulletComponent.class)) {
                StateComponent state = e.components.get(StateComponent.class);
                e.components.get(BulletComponent.class).cooldown.update(delta);
                if (e.components.get(BulletComponent.class).cooldown.isReady || !state.shouldRender()) {
                    e.unregister();
                }
            }

            if (e instanceof Humanoid h && e.components.contains(MortalComponent.class)) {
                StateComponent state = e.components.get(StateComponent.class);
                if (h.getHealth() <= 0 && state.isAlive()) {
                    state.setAlive(false);
                    h.components.add(new DespawnAnimationComponent());
                }
            }

        }
    }
}
