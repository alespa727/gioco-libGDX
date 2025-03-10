package io.github.ale.entity.player.movement;

import com.badlogic.gdx.Gdx;

import io.github.ale.entity.abstractEntity.Entity;
import io.github.ale.enums.StatiDiMovimento;
import io.github.ale.maps.Map;

public class PlayerMovementManager{
    private final KeyHandlerPlayer keyH;
    private boolean w, s, a, d, shift;
    private double elapsedTime;

    private boolean collisioneY;
    private boolean collisioneX;

    private StatiDiMovimento stato;
    private String lastDirezione;

    private final float sprintSpeedMultiplier = 1.5f;
    private final float baseSpeedMultiplier = 1f;

    public PlayerMovementManager() {
        keyH = new KeyHandlerPlayer();
    }

    /**
     * aggiorna i vari parametri in base agli input
     * 
     * @param p
     */
    
    public void update(Entity p) {
        keyH.input();
        speedMultiplier(p);
        movimento(p);
    }

    /**
     * input sprint
     */

    private void speedMultiplier(Entity p) {
        
        shift = keyH.shift;

        boolean diagonale = (w && d) || (w && a) || (s && d) || (s && a);
        boolean fermo = (!w && !s && !a && !d);

        float speed;

        if (shift && !fermo) {
            // System.out.println("SPRINT!");
            if (diagonale) speed = sprintSpeedMultiplier/1.41f;
            else speed = sprintSpeedMultiplier;
            
            p.getStatistiche().setSpeedBuff(p.getStatistiche().getSpeedBuff() + (speed - p.getStatistiche().getSpeedBuff()) *0.1f);
    
        }

        if (!shift && !fermo) {
            if (diagonale) speed = baseSpeedMultiplier/1.41f;
            else speed = baseSpeedMultiplier;
            // System.out.println("NIENTE SPRINT");
            p.getStatistiche().setSpeedBuff(p.getStatistiche().getSpeedBuff() + (speed - p.getStatistiche().getSpeedBuff()) *0.1f);
        }

        if (fermo) {
            speed = 0;
            float newSpeedBuff = p.getStatistiche().getSpeedBuff() + (speed - p.getStatistiche().getSpeedBuff()) * 0.05f;
            if (newSpeedBuff < 0.01f) newSpeedBuff = 0f;  // Se troppo vicino a 0, azzeralo
            p.getStatistiche().setSpeedBuff(newSpeedBuff);
        }
        System.out.println(p.getStatistiche().getSpeedBuff());
    }

    /**
     * gestisce il movimento in base agli input
     * 
     * @param p
     */
    private void movimento(Entity p) {
        elapsedTime = Gdx.graphics.getDeltaTime(); // moltiplicatore del movimento in base al framerate
        
        w = keyH.w;
        s = keyH.s;
        a = keyH.a;
        d = keyH.d;
        
        boolean oppostoY = w && s;
        boolean oppostoX = a && d;
        boolean anyKey = w || s || a || d;
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
                addNotMoving(p);
                
                aggiornaDirezioneX(p);
                aggiornaCollisioni(p);

                muoviAsseX(p); //MUOVE IL PLAYER SE PREME ALTRI TASTI
                aggiornaStatoCollisione(p);
            }
            case OPPOSTOX -> {

                addNotMoving(p);

                aggiornaDirezioneY(p);
                aggiornaCollisioni(p);

                muoviAsseY(p); //MUOVE IL PLAYER SE PREME ALTRI TASTI

                aggiornaStatoCollisione(p);

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
        lastDirezione = p.getDirezione();
        if (!lastDirezione.equals(p.getDirezione())) {
            System.out.println(p.getDirezione());
        }
    
    }

    private void addNotMoving(Entity p){
        if (!p.getDirezione().contains("fermo")) {
            p.setDirezione("fermo".concat(p.getDirezione()));
        }
    }

    private void aggiornaCollisioni(Entity p) {
        collisioneY = Map.checkCollisionY(p);
        collisioneX = Map.checkCollisionX(p);
    }

    private void aggiornaDirezioneY(Entity p) {
        if (w)
            p.setDirezione("W");
        if (s)
            p.setDirezione("S");
    }

    private void aggiornaDirezioneX(Entity p) {
        if (a)
            p.setDirezione("A");
        if (d)
            p.setDirezione("D");

    }

    private void aggiornaStatoCollisione(Entity p) {
        if (collisioneX)
            p.getStati().setInCollisione(true);
        if (collisioneY)
            p.getStati().setInCollisione(true);
    }

    private void muoviAsseX(Entity p){
        
        if (!collisioneX) {
            float speed = p.getStatistiche().getSpeed();
            float x = p.getX();
        
            if (a)
                p.setX(x - speed * (float) elapsedTime);
            if (d)
                p.setX(x + speed * (float) elapsedTime);
            p.getStati().setInCollisione(false);
        }

    }
    private void muoviAsseY(Entity p){

        if (!collisioneY) {
            float speed = p.getStatistiche().getSpeed();
            float y = p.getY();

            if (s)
                p.setY(y - speed * (float) elapsedTime);
            if (w)
                p.setY(y + speed * (float) elapsedTime);
            p.getStati().setInCollisione(false);
        }

    }
}
