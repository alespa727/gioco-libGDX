package progetto.gameplay.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.MortalComponent;
import progetto.gameplay.entities.components.specific.DespawnAnimationComponent;
import progetto.gameplay.entities.components.specific.BulletComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.specific.living.Humanoid;
import progetto.gameplay.systems.base.System;
import progetto.gameplay.player.Player;

public class DeathSystem extends System {
    @Override
    public void update(float delta, Array<Entity> list) {
        for (Entity e : list) {
            if (!e.shouldRender()) continue;
            if (e instanceof Player player){
                if (player.getHealth() <= 0) {
                    player.setDead();
                    player.getStats().health = 100;
                }
                continue;
            }

            if(e.components.contains(BulletComponent.class)){
                e.components.get(BulletComponent.class).cooldown.update(delta);
                if (e.components.get(BulletComponent.class).cooldown.isReady || !e.getState().shouldRender()){
                    e.despawn();
                }
            }

            if (e instanceof Humanoid h && e.components.contains(MortalComponent.class)) {
                if (h.getHealth() <= 0 && h.getState().isAlive()) {
                    h.setDead();
                    h.components.add(new DespawnAnimationComponent());
                }
            }

        }
    }
}
