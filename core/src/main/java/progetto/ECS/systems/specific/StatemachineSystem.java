package progetto.ECS.systems.specific;

import progetto.ECS.components.base.ComponentFilter;
import progetto.ECS.components.specific.ai.StatemachineComponent;
import progetto.ECS.entities.Entity;
import progetto.ECS.systems.base.AsynchronousIteratingSystem;

public class StatemachineSystem extends AsynchronousIteratingSystem {

    public StatemachineSystem() {
        super(ComponentFilter.all(StatemachineComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if(!entity.contains(StatemachineComponent.class)) return;
        entity.components.get(StatemachineComponent.class).getStateMachine().update();
    }
}
