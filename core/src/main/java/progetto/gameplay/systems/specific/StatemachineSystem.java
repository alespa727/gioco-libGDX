package progetto.gameplay.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.ai.StatemachineComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.systems.base.System;

public class StatemachineSystem extends System {

    @Override
    public void update(float delta, Array<Entity> list) {
        for (Entity e : list) {
            if (!e.components.contains(StatemachineComponent.class)) continue;

            e.components.get(StatemachineComponent.class).getStateMachine().update();
        }
    }
}
