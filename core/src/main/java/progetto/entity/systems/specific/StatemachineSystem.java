package progetto.entity.systems.specific;

import progetto.entity.components.base.ComponentFilter;
import progetto.entity.components.specific.ai.StatemachineComponent;
import progetto.entity.entities.base.Entity;
import progetto.entity.systems.base.IterableSystem;

public class StatemachineSystem extends IterableSystem {

    public StatemachineSystem() {
        super(ComponentFilter.all(StatemachineComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.components.contains(StatemachineComponent.class)) return;

        entity.components.get(StatemachineComponent.class).getStateMachine().update();
    }
}
