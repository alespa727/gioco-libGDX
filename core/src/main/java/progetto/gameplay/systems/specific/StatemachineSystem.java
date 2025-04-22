package progetto.gameplay.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.ai.StatemachineComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.systems.base.AutomaticSystem;
import progetto.gameplay.systems.base.System;

public class StatemachineSystem extends AutomaticSystem {

    @Override
    public void update(float delta, Array<Entity> list) {
        for (Entity e : list) {

        }
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.components.contains(StatemachineComponent.class)) return;

        entity.components.get(StatemachineComponent.class).getStateMachine().update();
    }
}
