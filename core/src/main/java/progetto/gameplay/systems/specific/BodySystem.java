package progetto.gameplay.systems.specific;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.AttackRangeComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.systems.base.System;

public class BodySystem extends System {
    @Override
    public void update(float delta, Array<Entity> list) {
        for (Entity e : list) {
            if (e.componentManager.contains(AttackRangeComponent.class)){
                Body body = e.getPhysics().getBody();
                Vector2 direction = new Vector2(e.getDirection()).nor().scl(1.2f); // distanza davanti al player

            }
        }
    }
}
