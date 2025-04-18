package progetto.entity.components.specific.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import progetto.entity.components.base.IteratableComponent;
import progetto.manager.player.Player;

import static progetto.manager.input.KeyHandler.*;

public class PlayerMovementManager extends IteratableComponent {
    private final Player player;
    public PlayerMovementManager(Player player) {
        this.player = player;
    }

    public void oppostoY() {
        Body body = player.getPhysics().getBody();
        body.setLinearDamping(20f);
        if (sinistra || destra) {
            body.setLinearDamping(3f);
            player.getDirection().y = 0;
            aggiornaDirezione();

            muoviAsseX();
        } else {
            stopMovement();
        }
    }

    public void oppostoX() {
        Body body = player.getPhysics().getBody();
       body.setLinearDamping(20f);
        if (su || giu) {
            body.setLinearDamping(3f);
            player.getDirection().x = 0;
            aggiornaDirezione();
            muoviAsseY(); //MUOVE IL PLAYER SE PREME ALTRI TASTI
        } else {
            stopMovement();
        }
    }

    public void moving() {
        Body body = player.getPhysics().getBody();
        body.setLinearDamping(3f);
        aggiornaDirezione();
        muoviAsseY();
        aggiornaDirezione();
        muoviAsseX();
    }

    public void notMoving() {
        Body body = player.getPhysics().getBody();
        body.setLinearDamping(20f);
        stopMovement();
    }

    private void stopMovement() {
        if (player.getDirection().x == 1f || player.getDirection().x == -1f) player.getDirection().scl(0.5f, 1f);
        if (player.getDirection().y == 1f || player.getDirection().y == -1f) player.getDirection().scl(1f, 0.5f);
    }

    private void aggiornaDirezione() {
        Vector2 dir = new Vector2(0, 0);

        if (sinistra) dir.x -= 1;
        if (destra) dir.x += 1;
        if (su) dir.y += 1;
        if (giu) dir.y -= 1;

        player.getDirection().set(dir);
    }

    private void muoviAsseX() {
        if (sinistra || destra) {
            Body body = player.getPhysics().getBody();
            body.applyForceToCenter(getForce().scl(5), true);
            body.setLinearDamping(3f);
        }
    }

    private void muoviAsseY() {
        if (su || giu) {
            Body body = player.getPhysics().getBody();
            body.applyForceToCenter(getForce().scl(5), true);
            body.setLinearDamping(3f);
        }
    }

    public Vector2 getForce() {
        boolean diagonale = (su && destra) || (su && sinistra) || (giu && destra) || (giu && sinistra);
        Body body = player.getPhysics().getBody();
        if (diagonale) {
            return new Vector2(player.getDirection()).scl(body.getMass() * player.getMaxSpeed() * 1 / 1.41f).scl(1 / 1.41f);
        }
        return new Vector2(player.getDirection()).scl(player.getMaxSpeed()).scl(body.getMass());
    }

    @Override
    public void update(float delta) {
        if (!(su || giu || sinistra || destra)) {
            notMoving();
        }else if(destra && sinistra && su && giu){
            notMoving();
        }else if (su && giu) {
            oppostoY();
        } else if (sinistra && destra) {
            oppostoX();
        } else {
            moving();
        }
    }
}
