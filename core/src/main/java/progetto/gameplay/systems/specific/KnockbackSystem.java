package progetto.gameplay.systems.specific;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.base.ComponentManager;
import progetto.gameplay.entities.components.specific.base.Cooldown;
import progetto.gameplay.entities.components.specific.combat.KnockbackComponent;
import progetto.gameplay.entities.components.specific.base.PhysicsComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.systems.base.System;

public class KnockbackSystem extends System {
    @Override
    public void update(float delta, Array<Entity> list) {
        for (Entity e : list){
            if (!e.shouldRender()) continue;
            ComponentManager componentManager = e.components;
            if (componentManager.contains(KnockbackComponent.class)){
                KnockbackComponent knockbackComponent = componentManager.get(KnockbackComponent.class);
                Cooldown cooldown = knockbackComponent.cooldown;
                cooldown.update(e.engine.delta);
                Body body = e.components.get(PhysicsComponent.class).getBody();
                if (!cooldown.isReady) {
                    body.applyLinearImpulse(knockbackComponent.hit, body.getWorldCenter(), true);
                }
            }
        }

    }
}
