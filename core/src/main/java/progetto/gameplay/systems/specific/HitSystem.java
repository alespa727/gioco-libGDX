package progetto.gameplay.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.base.ComponentManager;
import progetto.gameplay.entities.components.specific.ai.StateComponent;
import progetto.gameplay.entities.components.specific.ai.StatusComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.systems.base.AutomaticSystem;
import progetto.gameplay.systems.base.System;

public class HitSystem extends AutomaticSystem {

    public HitSystem() {
        super(Array.with(new StatusComponent()));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.shouldRender()) return;
        if (entity.components.contains(StatusComponent.class)) {
            ComponentManager cm = entity.components;
            if (cm.get(StatusComponent.class).hasBeenHit) {

                cm.get(StatusComponent.class).cooldown.update(delta);
                if (cm.get(StatusComponent.class).cooldown.isReady) {
                    cm.get(StatusComponent.class).cooldown.reset();
                    cm.get(StatusComponent.class).hasBeenHit = false;
                }
            }
        }
    }
}
