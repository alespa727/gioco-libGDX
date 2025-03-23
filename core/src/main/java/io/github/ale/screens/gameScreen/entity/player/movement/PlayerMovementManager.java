package io.github.ale.screens.gameScreen.entity.player.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.gameScreen.entity.livingEntity.LivingEntity;
import io.github.ale.screens.gameScreen.entity.player.KeyHandlerPlayer;
import io.github.ale.screens.gameScreen.enums.StatiDiMovimento;
import io.github.ale.screens.gameScreen.maps.Map;

public class PlayerMovementManager{
    private final KeyHandlerPlayer keyH;
    private boolean su, giu, sinistra, destra, sprint;
    private double elapsedTime;

    private boolean collisioneY;
    private boolean collisioneX;

    private StatiDiMovimento stato;
    private Vector2 last;

    private final float sprintSpeedMultiplier = 1.5f;
    private final float baseSpeedMultiplier = 1f;

    public PlayerMovementManager() {
        keyH = new KeyHandlerPlayer();
        last = new Vector2();
    }

    /**
     * aggiorna i vari parametri in base agli input
     *
     * @param p
     */

    public void update(LivingEntity p) {
        keyH.inputmodificato();
        speedMultiplier(p);
        movimento(p);
    }

    /**
     * input sprint
     */

    private void speedMultiplier(LivingEntity p) {

        sprint = keyH.sprint;

        boolean diagonale = (su && destra) || (su && sinistra) || (giu && destra) || (giu && sinistra);
        boolean fermo = (!su && !giu && !sinistra && !destra);

        float speed;

        if (sprint && !fermo) {
            // System.out.println("SPRINT!");
            if (diagonale) speed = sprintSpeedMultiplier/1.41f;
            else speed = sprintSpeedMultiplier;

            p.statistiche().setSpeedBuff(p.statistiche().getSpeedBuff() + (speed - p.statistiche().getSpeedBuff()) *0.1f);

        }

        if (!sprint && !fermo) {
            if (diagonale) speed = baseSpeedMultiplier/1.41f;
            else speed = baseSpeedMultiplier;
            // System.out.println("NIENTE SPRINT");
            p.statistiche().setSpeedBuff(p.statistiche().getSpeedBuff() + (speed - p.statistiche().getSpeedBuff()) *0.1f);
        }

        if (fermo) {
            speed = 0;
            float newSpeedBuff = p.statistiche().getSpeedBuff() + (speed - p.statistiche().getSpeedBuff()) * 0.4f;
            if (newSpeedBuff < 0.01f) newSpeedBuff = 0f;  // Se troppo vicino sinistra 0, azzeralo
            p.statistiche().setSpeedBuff(newSpeedBuff);

        }

    }

    /**
     * gestisce il movimento in base agli input
     *
     * @param p
     */
    private void movimento(LivingEntity p) {
        elapsedTime = Gdx.graphics.getDeltaTime(); // moltiplicatore del movimento in base al framerate

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

        else if (anyKey)
            stato = StatiDiMovimento.ANYKEY;

        switch (stato) {
            case OPPOSTOY -> {
                if (sinistra || destra) {
                    p.direzione().y=0;
                    aggiornaDirezioneX(p);
                    aggiornaCollisioni(p);

                    muoviAsseX(p); //MUOVE IL PLAYER SE PREME ALTRI TASTI
                    aggiornaStatoCollisione(p);
                }else{
                    addNotMoving(p);
                }
            }
            case OPPOSTOX -> {
                if (su || giu) {
                    p.direzione().x=0;
                    aggiornaDirezioneY(p);
                    aggiornaCollisioni(p);

                    muoviAsseY(p); //MUOVE IL PLAYER SE PREME ALTRI TASTI

                    aggiornaStatoCollisione(p);
                }else{

                    addNotMoving(p);
                }
            }
            case ANYKEY -> {

                aggiornaDirezioneY(p);

                aggiornaCollisioni(p);
                muoviAsseY(p);

                aggiornaStatoCollisione(p);
                aggiornaDirezioneX(p);

                aggiornaCollisioni(p);
                muoviAsseX(p);

                aggiornaStatoCollisione(p);

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


    private void aggiornaCollisioni(LivingEntity p) {
        collisioneY = Map.checkCollisionY(p);
        collisioneX = Map.checkCollisionX(p);
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

    private void aggiornaStatoCollisione(LivingEntity p) {
        if (collisioneX || collisioneY)
            p.stati().setInCollisione(true);

    }

    private void muoviAsseX(LivingEntity p){

        if (!collisioneX) {
            float speed = p.statistiche().speed();
            float x = p.getX();

            if (sinistra)
                p.setX(x - speed * (float) elapsedTime);
            if (destra)
                p.setX(x + speed * (float) elapsedTime);
            p.stati().setInCollisione(false);
        }

    }
    private void muoviAsseY(LivingEntity p){

        if (!collisioneY) {
            float speed = p.statistiche().speed();
            float y = p.getY();

            if (giu)
                p.setY(y - speed * (float) elapsedTime);
            if (su)
                p.setY(y + speed * (float) elapsedTime);
            p.stati().setInCollisione(false);
        }

    }
}
