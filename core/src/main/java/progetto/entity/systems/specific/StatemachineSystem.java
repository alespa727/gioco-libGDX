package progetto.entity.systems.specific;

import progetto.entity.components.specific.ai.StatemachineComponent;
import progetto.entity.entities.base.Entity;
import progetto.entity.systems.base.AutomaticSystem;
import progetto.entity.components.base.ComponentFilter;

public class StatemachineSystem extends AutomaticSystem {

    public StatemachineSystem() {
        super(ComponentFilter.all(StatemachineComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.components.contains(StatemachineComponent.class)) return;

        entity.components.get(StatemachineComponent.class).getStateMachine().update();
    }
}
