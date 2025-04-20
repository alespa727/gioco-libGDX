package progetto.gameplay.systems.specific;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.UserControllable;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.specific.living.Humanoid;
import progetto.gameplay.systems.base.System;

import static progetto.manager.input.KeyHandler.*;

public class UserInputSystem extends System {

    @Override
    public void update(float delta, Array<Entity> list) {
        for (Entity e : list){
            if(!e.shouldRender()) continue;
            if (e.componentManager.contains(UserControllable.class) && e instanceof Humanoid humanoid){
                if (!(su || giu || sinistra || destra)) {
                    notMoving(humanoid);
                }else if(destra && sinistra && su && giu){
                    notMoving(humanoid);
                }else if (su && giu) {
                    oppostoY(humanoid);
                } else if (sinistra && destra) {
                    oppostoX(humanoid);
                } else {
                    moving(humanoid);
                }
            }
        }
    }

    public void oppostoY(Humanoid entity) {
        Body body = entity.getPhysics().getBody();
        body.setLinearDamping(20f);
        if (sinistra || destra) {
            body.setLinearDamping(3f);
            entity.getDirection().y = 0;
            aggiornaDirezione(entity);
            muoviAsseX(entity);
        } else {
            stopMovement(entity);
        }
    }

    public void oppostoX(Humanoid entity) {
        Body body = entity.getPhysics().getBody();
        body.setLinearDamping(20f);
        if (su || giu) {
            body.setLinearDamping(3f);
            entity.getDirection().x = 0;
            aggiornaDirezione(entity);
            muoviAsseY(entity);
        } else {
            stopMovement(entity);
        }
    }

    public void moving(Humanoid entity) {
        Body body = entity.getPhysics().getBody();
        body.setLinearDamping(3f);
        aggiornaDirezione(entity);
        muoviAsseY(entity);
        aggiornaDirezione(entity);
        muoviAsseX(entity);
    }

    public void notMoving(Humanoid entity) {
        Body body = entity.getPhysics().getBody();
        body.setLinearDamping(20f);
        stopMovement(entity);
    }

    private void stopMovement(Humanoid entity) {
        if (entity.getDirection().x == 1f || entity.getDirection().x == -1f)
            entity.getDirection().scl(0.5f, 1f);
        if (entity.getDirection().y == 1f || entity.getDirection().y == -1f)
            entity.getDirection().scl(1f, 0.5f);
    }

    private void aggiornaDirezione(Humanoid entity) {
        Vector2 dir = new Vector2(0, 0);

        if (sinistra) dir.x -= 1;
        if (destra) dir.x += 1;
        if (su) dir.y += 1;
        if (giu) dir.y -= 1;

        entity.getDirection().set(dir);
    }

    private void muoviAsseX(Humanoid entity) {
        if (sinistra || destra) {
            Body body = entity.getPhysics().getBody();
            body.applyForceToCenter(getForce(entity).scl(5), true);
            body.setLinearDamping(3f);
        }
    }

    private void muoviAsseY(Humanoid entity) {
        if (su || giu) {
            Body body = entity.getPhysics().getBody();
            body.applyForceToCenter(getForce(entity).scl(5), true);
            body.setLinearDamping(3f);
        }
    }

    public Vector2 getForce(Humanoid entity) {
        boolean diagonale = (su && destra) || (su && sinistra) || (giu && destra) || (giu && sinistra);
        Body body = entity.getPhysics().getBody();
        if (diagonale) {
            return new Vector2(entity.getDirection())
                .scl(body.getMass() * entity.getMaxSpeed() / 1.41f)
                .scl(1 / 1.41f);
        }
        return new Vector2(entity.getDirection())
            .scl(entity.getMaxSpeed())
            .scl(body.getMass());
    }
}
