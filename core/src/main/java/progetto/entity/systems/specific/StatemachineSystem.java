package progetto.entity.systems.specific;

import progetto.entity.components.base.ComponentFilter;
import progetto.entity.components.specific.ai.StatemachineComponent;
import progetto.entity.entities.Entity;
import progetto.entity.systems.base.AsynchronousIteratingSystem;

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
