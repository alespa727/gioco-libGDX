package progetto.gameplay.systems.specific;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.MovementComponent;
import progetto.gameplay.entities.components.specific.base.PhysicsComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.specific.living.Humanoid;
import progetto.gameplay.player.Player;
import progetto.gameplay.systems.base.System;
import progetto.gameplay.world.Map;
import progetto.gameplay.world.graph.node.Node;
import progetto.manager.input.DebugWindow;

public class MovementSystem extends System {

    @Override
    public void update(float delta, Array<Entity> entities) {
        if(!DebugWindow.renderEntities()){
            for (Entity entity : entities) {
                if (entity instanceof Player) continue;
                if (!entity.shouldRender()) continue;
                if (!entity.components.contains(MovementComponent.class)) continue;
                if (!entity.components.contains(PhysicsComponent.class)) continue;

                entity.getPhysics().getBody().setLinearVelocity(new Vector2(0, 0));
            }
            return;
        }
        for (Entity entity : entities) {
            if (!entity.shouldRender()) continue;
            if (!entity.components.contains(MovementComponent.class)) continue;
            if (!entity.components.contains(PhysicsComponent.class)) continue;


            MovementComponent movement = entity.components.get(MovementComponent.class);
            movement.cooldown.update(delta);

            if (movement.isAwake()){
                movement.setReady(false);

                if (movement.stepIndex > movement.getPath().size - 1 || movement.cooldown.isReady) {
                    movement.stepIndex = 0;
                    movement.setReady(true);
                    movement.cooldown.reset();
                }
                if (movement.getPath().size == 0) {
                    continue;
                }
                direzione(movement, entity);
                towards(movement, entity, movement.getPath().get(movement.stepIndex));
            }
        }
    }

    public void direzione(MovementComponent movement, Entity e) {
        if (movement.stepIndex == 0) return;

        Vector2 direction = new Vector2(movement.getPath().get(movement.stepIndex).x - movement.getPath().get(movement.stepIndex - 1).x, movement.getPath().get(movement.stepIndex).y - movement.getPath().get(movement.stepIndex - 1).y);
        if (!direction.epsilonEquals(0, 0)) {
            e.getDirection().set(direction);
        }

    }

    private void towards(MovementComponent movement, Entity e, Node target) {
        Body body = e.getPhysics().getBody();
        if (body == null) {
            return;
        }

        if (e.getPosition().dst(target.getPosition()) < 8/16f) {
            body.setLinearDamping(50f);
            movement.stepIndex++;
            for (Node node: movement.getPath()){
                if (Map.getGraph().getClosestNode(node.x, node.y)!=null && Map.getGraph().getClosestNode(node.x, node.y).isWalkable()){
                    movement.setReady(true);
                    break;
                }
            }
        } else {
            body.setLinearDamping(3f);
        }

        Vector2 movementDirection = new Vector2(target.getPosition()).sub(e.getPosition()).nor();
        float speed = ((Humanoid) e).getMaxSpeed();
        Vector2 direction = movementDirection.scl(speed);
        Vector2 force = new Vector2(direction).scl(speed);
        body.applyLinearImpulse(force, e.getPosition(), true);
    }
}
