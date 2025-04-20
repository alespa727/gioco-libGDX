package progetto.gameplay.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.base.ComponentManager;
import progetto.gameplay.entities.components.specific.StatusComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.systems.base.System;

public class HitSystem extends System {
    @Override
    public void update(float delta, Array<Entity> list) {
        for(Entity e : list){
            if (e.componentManager.contains(StatusComponent.class)){
                ComponentManager cm = e.componentManager;
                if (cm.get(StatusComponent.class).hasBeenHit) {

                    cm.get(StatusComponent.class).cooldown.update(delta);
                    if (cm.get(StatusComponent.class).cooldown.isReady){
                        cm.get(StatusComponent.class).cooldown.reset();
                        cm.get(StatusComponent.class).hasBeenHit = false;
                    }
                }
            }
        }
    }
}
