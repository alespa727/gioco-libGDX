package progetto.ECS.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.ECS.components.base.ComponentFilter;
import progetto.ECS.components.specific.base.StateComponent;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.ECS.components.specific.general.PlayerComponent;
import progetto.ECS.entities.Entity;
import progetto.ECS.systems.base.IteratingSystem;
import progetto.input.DebugWindow;
import progetto.core.CameraManager;

public class CullingSystem extends IteratingSystem {

    public CullingSystem() {
        super(ComponentFilter.all(PhysicsComponent.class, StateComponent.class));
    }


    @Override
    public void update(float delta, Array<Entity> entities) {
        for (Entity e : entities) {
            processEntity(e, delta);
        }
    }


    @Override
    public void processEntity(Entity entity, float delta) {
        if (DebugWindow.renderEntities()){

            PhysicsComponent physics = entity.get(PhysicsComponent.class);
            StateComponent states = entity.get(StateComponent.class);

            float x = physics.getPosition().x;
            float y = physics.getPosition().y;
            boolean shouldBeUpdated = isInFrustumBounds(x, y) || entity.contains(PlayerComponent.class);
            if (shouldBeUpdated) {
                states.setShouldBeUpdated(true);
                physics.getBody().setActive(true);
            }else{
                states.setShouldBeUpdated(false);
                physics.getBody().setActive(false);
            }
        }
    }

    public boolean isInFrustumBounds(float x, float y) {
        return CameraManager.isWithinFrustumBounds(x, y);
    }
}
