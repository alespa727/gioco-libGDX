package progetto.gameplay.manager.entity.movement;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.math.Vector2;
import progetto.gameplay.entities.types.LivingEntity;
import progetto.gameplay.entities.types.Player;

import static progetto.utils.KeyHandler.*;

public class PlayerMovementManager {
    private final Player player;
    public PlayerMovementManager(Player player) {
        this.player = player;
    }

    public void update(LivingEntity p) {

        if (!(su || giu || sinistra || destra)) {
            notMoving();
        } else if (su && giu) {
            oppostoY();
        } else if (sinistra && destra) {
            oppostoX();
        } else {
            moving();
        }

    }

    public void oppostoY() {
        player.body.setLinearDamping(20f);
        if (sinistra || destra) {
            player.body.setLinearDamping(3f);
            player.direzione().y = 0;
            aggiornaDirezione();

            muoviAsseX();
        } else {
            stopMovement();
        }
    }

    public void oppostoX() {
        player.body.setLinearDamping(20f);
        if (su || giu) {
            player.body.setLinearDamping(3f);
            player.direzione().x = 0;
            aggiornaDirezione();
            muoviAsseY(); //MUOVE IL PLAYER SE PREME ALTRI TASTI
        } else {
            stopMovement();
        }
    }

    public void moving() {
        player.body.setLinearDamping(3f);
        aggiornaDirezione();
        muoviAsseY();
        aggiornaDirezione();
        muoviAsseX();
    }

    public void notMoving() {
        player.body.setLinearDamping(20f);
        stopMovement();
    }

    private void stopMovement() {
        if (player.direzione().x == 1f || player.direzione().x == -1f) player.direzione().scl(0.5f, 1f);
        if (player.direzione().y == 1f || player.direzione().y == -1f) player.direzione().scl(1f, 0.5f);
    }

    private void aggiornaDirezione() {
        Vector2 dir = new Vector2(0, 0);

        if (sinistra) dir.x -= 1;
        if (destra) dir.x += 1;
        if (su) dir.y += 1;
        if (giu) dir.y -= 1;

        player.direzione().set(dir);
    }

    private void muoviAsseX() {
        if (sinistra || destra) {
            player.body.applyForceToCenter(getForce().scl(5), true);
            player.body.setLinearDamping(3f);
        }
    }

    private void muoviAsseY() {
        if (su || giu) {
            player.body.applyForceToCenter(getForce().scl(5), true);
            player.body.setLinearDamping(3f);
        }
    }

    public Vector2 getForce() {
        boolean diagonale = (su && destra) || (su && sinistra) || (giu && destra) || (giu && sinistra);
        if (diagonale) {
            return new Vector2(player.direzione()).scl(player.body.getMass() * player.speed() * 1 / 1.41f).scl(1 / 1.41f);
        }
        return new Vector2(player.direzione()).scl(player.speed()).scl(player.body.getMass());
    }
}
