package progetto.entity.systems.specific;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import progetto.entity.components.base.ComponentFilter;
import progetto.entity.components.specific.ai.StateComponent;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.control.UserControllable;
import progetto.entity.components.specific.movement.DirectionComponent;
import progetto.entity.entities.base.Entity;
import progetto.entity.entities.specific.living.Humanoid;
import progetto.entity.systems.base.IterableSystem;

import static progetto.input.KeyHandler.*;

public class UserInputSystem extends IterableSystem {

    public UserInputSystem() {
        super(ComponentFilter.all(UserControllable.class, PhysicsComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {

        if (!entity.get(StateComponent.class).shouldBeUpdated()) return;
        if (entity.contains(UserControllable.class) && entity instanceof Humanoid humanoid) {
            if (!(su || giu || sinistra || destra)) {
                notMoving(humanoid);
            } else if (destra && sinistra && su && giu) {
                notMoving(humanoid);
            } else if (su && giu) {
                oppostoY(humanoid);
            } else if (sinistra && destra) {
                oppostoX(humanoid);
            } else {
                moving(humanoid);
            }
        }
    }

    public void oppostoY(Humanoid entity) {
        Body body = entity.components.get(PhysicsComponent.class).getBody();
        body.setLinearDamping(20f);
        if (sinistra || destra) {
            body.setLinearDamping(3f);
            entity.get(DirectionComponent.class).direction.y = 0;
            aggiornaDirezione(entity);
            muoviAsseX(entity);
        } else {
            stopMovement(entity);
        }
    }

    public void oppostoX(Humanoid entity) {
        Body body = entity.components.get(PhysicsComponent.class).getBody();
        body.setLinearDamping(20f);
        if (su || giu) {
            body.setLinearDamping(3f);
            entity.get(DirectionComponent.class).direction.x = 0;
            aggiornaDirezione(entity);
            muoviAsseY(entity);
        } else {
            stopMovement(entity);
        }
    }

    public void moving(Humanoid entity) {
        Body body = entity.components.get(PhysicsComponent.class).getBody();
        body.setLinearDamping(3f);
        aggiornaDirezione(entity);
        muoviAsseY(entity);
        aggiornaDirezione(entity);
        muoviAsseX(entity);
    }

    public void notMoving(Humanoid entity) {
        Body body = entity.components.get(PhysicsComponent.class).getBody();
        body.setLinearDamping(20f);
        stopMovement(entity);
    }

    private void stopMovement(Humanoid entity) {
        if (entity.get(DirectionComponent.class).direction.x == 1f || entity.get(DirectionComponent.class).direction.x == -1f)
            entity.get(DirectionComponent.class).direction.scl(0.5f, 1f);

        if (entity.get(DirectionComponent.class).direction.y == 1f || entity.get(DirectionComponent.class).direction.y == -1f)
            entity.get(DirectionComponent.class).direction.scl(1f, 0.5f);
    }

    private void aggiornaDirezione(Humanoid entity) {
        Vector2 dir = new Vector2(0, 0);

        if (sinistra) dir.x -= 1;
        if (destra) dir.x += 1;
        if (su) dir.y += 1;
        if (giu) dir.y -= 1;

        entity.get(DirectionComponent.class).direction.set(dir);
    }

    private void muoviAsseX(Humanoid entity) {
        if (sinistra || destra) {
            Body body = entity.components.get(PhysicsComponent.class).getBody();
            body.applyForceToCenter(getForce(entity).scl(5), true);
            body.setLinearDamping(3f);
        }
    }

    private void muoviAsseY(Humanoid entity) {
        if (su || giu) {
            Body body = entity.components.get(PhysicsComponent.class).getBody();
            body.applyForceToCenter(getForce(entity).scl(5), true);
            body.setLinearDamping(3f);
        }
    }

    public Vector2 getForce(Humanoid entity) {
        boolean diagonale = (su && destra) || (su && sinistra) || (giu && destra) || (giu && sinistra);
        Body body = entity.components.get(PhysicsComponent.class).getBody();
        if (diagonale) {
            return new Vector2(entity.get(DirectionComponent.class).direction)
                .scl(body.getMass() * entity.getMaxSpeed() / 1.41f)
                .scl(1 / 1.41f);
        }
        return new Vector2(entity.get(DirectionComponent.class).direction)
            .scl(entity.getMaxSpeed())
            .scl(body.getMass());
    }
}
