package progetto.ECS.systems.specific;

import progetto.ECS.components.ComponentManager;
import progetto.ECS.components.base.ComponentFilter;
import progetto.ECS.components.specific.base.StateComponent;
import progetto.ECS.components.specific.base.StatusComponent;
import progetto.ECS.entities.Entity;
import progetto.ECS.systems.base.IteratingSystem;

public class HitSystem extends IteratingSystem {

    public HitSystem() {
        super(ComponentFilter.all(StateComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.get(StateComponent.class).shouldBeUpdated()) return;
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
