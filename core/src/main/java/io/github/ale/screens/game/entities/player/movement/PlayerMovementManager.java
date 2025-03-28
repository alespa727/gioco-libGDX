package io.github.ale.screens.game.entities.player.movement;

import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.game.entityType.livingEntity.LivingEntity;
import io.github.ale.screens.game.entities.player.KeyHandlerPlayer;
import io.github.ale.screens.game.entityType.livingEntity.movement.StatiDiMovimento;

public class PlayerMovementManager{
    private final KeyHandlerPlayer keyH;
    private boolean su;
    private boolean giu;
    private boolean sinistra;
    private boolean destra;

    private boolean isCollidingVertically;
    private boolean isCollidingHorizontally;

    private StatiDiMovimento stato;
    private Vector2 last;

    public PlayerMovementManager() {
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
        speedMultiplier(p);
        movimento(p, p.delta);
    }

    /**
     * input sprint
     */

    private void speedMultiplier(LivingEntity p) {

        final float sprintSpeedMultiplier = 1.5f;
        final float baseSpeedMultiplier = 1f;

        boolean sprint = keyH.sprint;

        boolean diagonale = (su && destra) || (su && sinistra) || (giu && destra) || (giu && sinistra);
        boolean fermo = (!su && !giu && !sinistra && !destra);

        float speed;

        if (sprint) {
            p.statistiche().setSpeedBuff(sprintSpeedMultiplier);
        }else p.statistiche().setSpeedBuff(baseSpeedMultiplier);

        if (diagonale) {
            if (sprint)
                p.statistiche().setSpeedBuff(sprintSpeedMultiplier/1.41f);
            else p.statistiche().setSpeedBuff(baseSpeedMultiplier/1.41f);
        }
    }

    /**
     * gestisce il movimento in base agli input
     *
     * @param p player
     */
    private void movimento(LivingEntity p, float delta) {
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
                if (sinistra || destra) {
                    p.direzione().y=0;
                    aggiornaDirezioneX(p);

                    muoviAsseX(p, delta);
                }else{
                    addNotMoving(p);
                }
            }
            case OPPOSTOX -> {
                if (su || giu) {
                    p.direzione().x=0;
                    aggiornaDirezioneY(p);
                    muoviAsseY(p, delta); //MUOVE IL PLAYER SE PREME ALTRI TASTI
                }else{
                    addNotMoving(p);
                }
            }
            case ANYKEY -> {
                aggiornaDirezioneY(p);
                muoviAsseY(p, delta);
                aggiornaDirezioneX(p);
                muoviAsseX(p, delta);
            }
            case NOTMOVING -> {
                addNotMoving(p);
            }
            default -> {
            }
        }
    }

    private void addNotMoving(LivingEntity p) {

        if (last.x > 0) {
            p.direzione().set(0.5f, 0); // Annulla X, mantiene Y
        }else if(last.x < 0){
            p.direzione().set(-0.5f, 0); // Annulla X, mantiene Y
        }
        if (last.y > 0) {
            p.direzione().set(0, 0.5f); // Annulla X, mantiene Y
        }else if(last.y < 0){
            p.direzione().set(0, -0.5f); // Annulla X, mantiene Y
        }

    }


    private void aggiornaDirezioneX(LivingEntity p) {
        float dx = 0;

        if (sinistra && destra) {
            return;
        }

        last = p.direzione();

        // Determina il movimento orizzontale
        if (sinistra) dx = -1f;
        if (destra) dx = 1f;

        p.direzione().set(dx, p.direzione().y);
        //System.out.println(p.direzione());
    }

    public void aggiornaDirezioneY(LivingEntity p){
        float dy = 0;

        if (su && giu) {
            return;
        }

        last = p.direzione();

        // Determina il movimento verticale
        if (su) dy = 1f;
        if (giu) dy = -1f;


        p.direzione().set(p.direzione().x, dy);
        //System.out.println(p.direzione());
    }

    private void muoviAsseX(LivingEntity p, float delta){

        float desiredVelocity = p.statistiche().speed();

        if (sinistra) {
            p.body.setLinearVelocity(-desiredVelocity, p.body.getLinearVelocity().y);
        }

        if (destra) {
            p.body.setLinearVelocity(desiredVelocity, p.body.getLinearVelocity().y);
        }
    }

    private void muoviAsseY(LivingEntity p, float delta) {

        float desiredVelocity = p.statistiche().speed();

        if (su) {
            p.body.setLinearVelocity(p.body.getLinearVelocity().x, desiredVelocity);
        }

        if (giu) {
            p.body.setLinearVelocity(p.body.getLinearVelocity().x, -desiredVelocity);
        }
    }
}
