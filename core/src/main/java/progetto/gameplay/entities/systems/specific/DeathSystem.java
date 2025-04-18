package progetto.gameplay.entities.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.humanoid.DeathComponent;
import progetto.gameplay.entities.components.specific.humanoid.DespawnComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.specific.living.Humanoid;
import progetto.gameplay.entities.systems.base.System;

public class DeathSystem extends System {
    @Override
    public void update(float delta, Array<Entity> list) {
        for (Entity e : list) {
            if (e instanceof Humanoid h && e.componentManager.contains(DeathComponent.class)) {
                if (h.getHealth() <= 0 && h.getState().isAlive()) {
                    h.setDead();
                    h.setAwake(false);
                    h.componentManager.add(new DespawnComponent(h));
                }
            }

        }
    }
}
