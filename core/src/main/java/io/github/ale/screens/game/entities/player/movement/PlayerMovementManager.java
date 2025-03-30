package io.github.ale.screens.game.entities.player.movement;

import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.game.entities.player.Player;
import io.github.ale.screens.game.entityType.mobs.LivingEntity;
import io.github.ale.screens.game.entities.player.KeyHandlerPlayer;
import io.github.ale.screens.game.entityType.mobs.movement.StatiDiMovimento;

public class PlayerMovementManager{
    private final Player player;
    private final KeyHandlerPlayer keyH;
    private boolean su;
    private boolean giu;
    private boolean sinistra;
    private boolean destra;

    private boolean isCollidingVertically;
    private boolean isCollidingHorizontally;

    private StatiDiMovimento stato;
    private Vector2 last;

    public PlayerMovementManager(Player player) {
        this.player = player;
        keyH = new KeyHandlerPlayer();
        last = new Vector2();
    }

    /**
     * aggiorna i vari parametri in base agli input
     *
     * @param p player
     */

    public void update(LivingEntity p) {
        keyH.input();
        speedMultiplier();
        movimento(p.delta);
    }

    /**
     * input sprint
     */

    private void speedMultiplier() {

        final float sprintSpeedMultiplier = 1.5f;
        final float baseSpeedMultiplier = 1f;

        boolean sprint = keyH.sprint;

        boolean diagonale = (su && destra) || (su && sinistra) || (giu && destra) || (giu && sinistra);

        if (sprint) {
            player.setSpeedMultiplier(sprintSpeedMultiplier);
        }else player.setSpeedMultiplier(baseSpeedMultiplier);

        if (diagonale) {
            if (sprint)
                player.setSpeedMultiplier(sprintSpeedMultiplier/1.41f);
            else player.setSpeedMultiplier(baseSpeedMultiplier/1.41f);
        }
    }

    /**
     * gestisce il movimento in base agli input
     *
     */
    private void movimento(float delta) {
        su = keyH.su;
        giu = keyH.giu;
        sinistra = keyH.sinistra;
        destra = keyH.destra;

        boolean oppostoY = su && giu;
        boolean oppostoX = sinistra && destra;
        boolean anyKey = su || sinistra || destra || giu;
        boolean notMoving = !anyKey || (oppostoX && oppostoY);

        if (notMoving)
            stato = StatiDiMovimento.NOTMOVING;

        else if (oppostoY)
            stato = StatiDiMovimento.OPPOSTOY;

        else if (oppostoX)
            stato = StatiDiMovimento.OPPOSTOX;

        else
            stato = StatiDiMovimento.ANYKEY;

        switch (stato) {
            case OPPOSTOY -> {
                player.body.setLinearDamping(20f);
                if (sinistra || destra) {
                    player.body.setLinearDamping(3f);
                    player.direzione().y=0;
                    aggiornaDirezione();

                    muoviAsseX();
                }else{
                    addNotMoving();
                }
            }
            case OPPOSTOX -> {
                player.body.setLinearDamping(20f);
                if (su || giu) {
                    player.body.setLinearDamping(3f);
                    player.direzione().x=0;
                    aggiornaDirezione();
                    muoviAsseY(); //MUOVE IL PLAYER SE PREME ALTRI TASTI
                }else{
                    addNotMoving();
                }
            }
            case ANYKEY -> {
                player.body.setLinearDamping(3f);
                aggiornaDirezione();
                muoviAsseY();
                aggiornaDirezione();
                muoviAsseX();

            }
            case NOTMOVING -> {
                player.body.setLinearDamping(20f);
                addNotMoving();
            }
            default -> {
            }
        }

    }

    private void addNotMoving() {
        Vector2 direction = player.direzione();
        if (direction.x == 1f || direction.x == -1f) {
            direction.scl(0.5f, 1f);
        }
        if(direction.y == 1f || direction.y == -1f){
            direction.scl(1f, 0.5f);
        }

    }


    private void aggiornaDirezione() {
        Vector2 dir = new Vector2(0, 0);

        if (sinistra) dir.x -= 1;
        if (destra) dir.x += 1;
        if (su) dir.y += 1;
        if (giu) dir.y -= 1;


        player.direzione().set(dir);
    }

    private void muoviAsseX(){

        float desiredVelocity = player.speed();

        Vector2 force = new Vector2(player.direzione()).scl(desiredVelocity).scl(player.body.getMass());

        if (sinistra || destra) {
            player.body.applyForceToCenter(force.scl(5),true);
            player.body.setLinearDamping(5f);
        }
    }

    private void muoviAsseY() {

        float desiredVelocity = player.speed();

        Vector2 force = new Vector2(player.direzione()).scl(desiredVelocity).scl(player.body.getMass());

        if (su || giu) {
            player.body.applyForceToCenter(force.scl(5),true);
            player.body.setLinearDamping(5f);
        }
    }
}
