package progetto.gameplay.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.ai.StatemachineComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.systems.base.AutomaticSystem;

public class StatemachineSystem extends AutomaticSystem {

    public StatemachineSystem() {
        super(Array.with(new StatemachineComponent<>()));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.components.contains(StatemachineComponent.class)) return;

        entity.components.get(StatemachineComponent.class).getStateMachine().update();
    }
}
