package progetto.gameplay.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.base.ComponentManager;
import progetto.gameplay.entities.components.specific.StatusComponet;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.systems.base.System;

public class HitSystem extends System {
    @Override
    public void update(float delta, Array<Entity> list) {
        for(Entity e : list){
            if (e.componentManager.contains(StatusComponet.class)){
                ComponentManager cm = e.componentManager;
                if (cm.get(StatusComponet.class).hasBeenHit) {

                    cm.get(StatusComponet.class).cooldown.update(delta);
                    if (cm.get(StatusComponet.class).cooldown.isReady){
                        cm.get(StatusComponet.class).cooldown.reset();
                        cm.get(StatusComponet.class).hasBeenHit = false;
                    }
                }
            }
        }
    }
}
