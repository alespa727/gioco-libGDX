package io.github.ale.screens.game.entities.player.movement;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.game.entities.player.Player;
import io.github.ale.screens.game.entityType.mobs.LivingEntity;
import io.github.ale.screens.game.entityType.mobs.movement.StatiDiMovimento;

public class PlayerMovementManager{
    private final Player player;

    private final KeyHandlerPlayer keyH;

    private boolean su;
    private boolean giu;
    private boolean sinistra;
    private boolean destra;

    DefaultStateMachine<Player, MovementState> movementState;

    public PlayerMovementManager(Player player) {
        this.player = player;
        movementState = new DefaultStateMachine<>(player);
        keyH = new KeyHandlerPlayer();
    }

    public void update(LivingEntity p) {
        keyH.input();
        movimento(p.delta);
        movementState.update();
    }

    private void movimento(float delta) {
        su = keyH.su;
        giu = keyH.giu;
        sinistra = keyH.sinistra;
        destra = keyH.destra;

        boolean anyKey = su || sinistra || destra || giu;

        if (!anyKey){
            movementState.changeState(MovementState.NOTMOVING);
        }
        else if (su && giu){
            movementState.changeState(MovementState.OPPOSTOY);
        }
        else if (sinistra && destra){
            movementState.changeState(MovementState.OPPOSTOX);
        }
        else{
            movementState.changeState(MovementState.MOVING);
        }

    }

    public void oppostoY(){
        player.body.setLinearDamping(20f);
        if (sinistra || destra) {
            player.body.setLinearDamping(3f);
            player.direzione().y=0;
            aggiornaDirezione();

            muoviAsseX();
        }else{
            stopMovement();
        }
    }

    public void oppostoX(){
        player.body.setLinearDamping(20f);
        if (su || giu) {
            player.body.setLinearDamping(3f);
            player.direzione().x=0;
            aggiornaDirezione();
            muoviAsseY(); //MUOVE IL PLAYER SE PREME ALTRI TASTI
        }else{
            stopMovement();
        }
    }

    public void moving(){
        player.body.setLinearDamping(3f);
        aggiornaDirezione();
        muoviAsseY();
        aggiornaDirezione();
        muoviAsseX();
    }

    public void notMoving(){
        player.body.setLinearDamping(20f);
        stopMovement();
    }

    private void stopMovement() {
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
        if (sinistra || destra) {
            player.body.applyForceToCenter(getForce().scl(5),true);
            player.body.setLinearDamping(3f);
        }
    }

    private void muoviAsseY() {
        if (su || giu) {
            player.body.applyForceToCenter(getForce().scl(5),true);
            player.body.setLinearDamping(3f);
        }
    }

    public Vector2 getForce(){
        boolean diagonale = (su && destra) || (su && sinistra) || (giu && destra) || (giu && sinistra);
        if (diagonale){
            return new Vector2(player.direzione()).scl(player.body.getMass()*player.speed()*1/1.41f).scl(1/1.41f);
        }
        return new Vector2(player.direzione()).scl(player.speed()).scl(player.body.getMass());
    }
}
