package progetto.entity.systems.specific;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import progetto.entity.components.base.ComponentFilter;
import progetto.entity.components.specific.base.StateComponent;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.movement.DirectionComponent;
import progetto.entity.components.specific.movement.MovementComponent;
import progetto.entity.entities.Entity;
import progetto.entity.entities.specific.living.Humanoid;
import progetto.entity.systems.base.IteratingSystem;
import progetto.input.DebugWindow;
import progetto.core.game.player.Player;
import progetto.world.graph.node.Node;

public class MovementSystem extends IteratingSystem {

    public MovementSystem() {
        super(ComponentFilter.all(MovementComponent.class, PhysicsComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.get(StateComponent.class).shouldBeUpdated()) return;
        if (!entity.components.contains(MovementComponent.class)) return;
        if (!entity.components.contains(PhysicsComponent.class)) return;

        if (!DebugWindow.renderEntities()) {
                if (entity instanceof Player) return;
                entity.components.get(PhysicsComponent.class).getBody().setLinearVelocity(new Vector2(0, 0));
            return;
        }

        MovementComponent movement = entity.components.get(MovementComponent.class);
        movement.cooldown.update(delta);

        if (movement.isAwake()) {
            movement.setReady(false);
            movement.stepIndex = 1;
            movement.setReady(true);
            if (movement.getPath().size == 0) {
                return;
            }
            direzione(movement, entity);
            towards(movement, entity, movement.getPath().get(movement.stepIndex));
        }
    }

    public void direzione(MovementComponent movement, Entity e) {
        if (movement.stepIndex == 0) return;

        Vector2 direction = new Vector2(movement.getPath().get(movement.stepIndex).x - movement.getPath().get(movement.stepIndex - 1).x, movement.getPath().get(movement.stepIndex).y - movement.getPath().get(movement.stepIndex - 1).y);
        if (!direction.epsilonEquals(0, 0)) {
            e.get(DirectionComponent.class).direction.set(direction);
        }

    }

    private void towards(MovementComponent movement, Entity e, Node target) {
        Body body = e.components.get(PhysicsComponent.class).getBody();
        if (body == null) {
            return;
        }

        if (body.getPosition().dst(target.getPosition()) < 1/2f) {
            body.setLinearDamping(50f);
            movement.stepIndex++;
            movement.setReady(true);
        } else {
            body.setLinearDamping(3f);
        }

        Vector2 movementDirection = new Vector2(target.getPosition()).sub(e.get(PhysicsComponent.class).getPosition()).nor();
        float speed = ((Humanoid) e).getMaxSpeed();
        Vector2 direction = movementDirection.scl(speed);
        Vector2 force = new Vector2(direction).scl(speed);
        body.applyLinearImpulse(force, e.get(PhysicsComponent.class).getPosition(), true);
    }
}
