package progetto.entity.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.entity.components.base.ComponentFilter;
import progetto.entity.components.specific.base.StateComponent;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.general.PlayerComponent;
import progetto.entity.entities.Entity;
import progetto.entity.systems.base.IteratingSystem;
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
