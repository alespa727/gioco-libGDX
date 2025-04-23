package progetto.entity.systems.specific;

import progetto.entity.components.base.ComponentFilter;
import progetto.entity.components.specific.ai.StateComponent;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.general.PlayerComponent;
import progetto.entity.entities.base.Entity;
import progetto.entity.systems.base.IterableSystem;
import progetto.input.DebugWindow;
import progetto.player.ManagerCamera;

public class CullingSystem extends IterableSystem {

    public CullingSystem() {
        super(ComponentFilter.all(PhysicsComponent.class, StateComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (DebugWindow.renderEntities()){
            float x = entity.get(PhysicsComponent.class).getPosition().x;
            float y = entity.get(PhysicsComponent.class).getPosition().y;
            boolean shouldBeUpdated = isInFrustumBounds(x, y) || entity.contains(PlayerComponent.class);
            if (shouldBeUpdated) {
                entity.get(StateComponent.class).setShouldBeUpdated(true);
            }else entity.get(StateComponent.class).setShouldBeUpdated(false);
        }
    }

    public boolean isInFrustumBounds(float x, float y) {
        return ManagerCamera.isWithinFrustumBounds(x, y);
    }
}
