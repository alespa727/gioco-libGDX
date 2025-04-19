package progetto.gameplay.systems.specific;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.base.PhysicsComponent;
import progetto.gameplay.entities.components.specific.humanoid.EntityMovementComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.specific.living.Humanoid;
import progetto.gameplay.systems.base.System;
import progetto.gameplay.world.Map;

public class MovementSystem extends System {

    @Override
    public void update(float delta, Array<Entity> entities) {
        for (Entity entity : entities) {
            if (!entity.componentManager.contains(EntityMovementComponent.class)) continue;
            if (!entity.componentManager.contains(PhysicsComponent.class)) continue;

            if (entity instanceof Humanoid e){
                EntityMovementComponent movement = e.componentManager.get(EntityMovementComponent.class);
                PhysicsComponent physics = e.componentManager.get(PhysicsComponent.class);

                movement.cooldown.update(delta);

                if (movement.stepIndex >= movement.path.size || movement.cooldown.isReady) {
                    movement.stepIndex = 0;
                    movement.isReady = true;
                    movement.cooldown.reset();
                    continue;
                }

                if (movement.path.size == 0) continue;

                // Direzione
                if (movement.stepIndex > 0) {
                    Vector2 prev = movement.path.get(movement.stepIndex - 1);
                    Vector2 curr = movement.path.get(movement.stepIndex);
                    movement.direction.set(curr).sub(prev).nor();
                    if (!movement.direction.epsilonEquals(0, 0)) {
                        e.getDirection().set(movement.direction);
                    }
                }

                Vector2 target = movement.path.get(movement.stepIndex);
                Body body = physics.getBody();

                if (body == null) continue;

                if (e.getPosition().dst(target) < 8 / 16f) {
                    body.setLinearDamping(20f);
                    movement.stepIndex++;

                    for (Vector2 node : movement.path) {
                        if (!Map.getGraph().getClosestNode(node.x, node.y).isWalkable()) {
                            movement.isReady = true;
                            break;
                        }
                    }

                } else {
                    body.setLinearDamping(3f);
                    Vector2 moveDir = new Vector2(target).sub(e.getPosition()).nor();
                    float speed = e.getMaxSpeed();
                    Vector2 force = moveDir.scl(speed * speed * body.getMass());
                    body.applyLinearImpulse(force, e.getPosition(), true);
                }
            }
        }
    }
}
