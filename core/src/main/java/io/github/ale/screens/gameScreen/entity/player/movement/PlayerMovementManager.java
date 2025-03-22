package io.github.ale.screens.gameScreen.entity.player.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.player.KeyHandlerPlayer;
import io.github.ale.screens.gameScreen.enums.StatiDiMovimento;
import io.github.ale.screens.gameScreen.maps.Map;

public class PlayerMovementManager{
    private final KeyHandlerPlayer keyH;
    private boolean w, s, a, d, shift;
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
            
            p.statistiche().setSpeedBuff(p.statistiche().getSpeedBuff() + (speed - p.statistiche().getSpeedBuff()) *0.1f);
    
        }

        if (!shift && !fermo) {
            if (diagonale) speed = baseSpeedMultiplier/1.41f;
            else speed = baseSpeedMultiplier;
            // System.out.println("NIENTE SPRINT");
            p.statistiche().setSpeedBuff(p.statistiche().getSpeedBuff() + (speed - p.statistiche().getSpeedBuff()) *0.1f);
        }

        if (fermo) {
            speed = 0;
            float newSpeedBuff = p.statistiche().getSpeedBuff() + (speed - p.statistiche().getSpeedBuff()) * 0.4f;
            if (newSpeedBuff < 0.01f) newSpeedBuff = 0f;  // Se troppo vicino a 0, azzeralo
            p.statistiche().setSpeedBuff(newSpeedBuff);
            
        }
        
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
                if (a || d) {
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
                if (w || s) {
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

    private void addNotMoving(Entity p) {

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
    

    private void aggiornaCollisioni(Entity p) {
        collisioneY = Map.checkCollisionY(p);
        collisioneX = Map.checkCollisionX(p);
    }

    private void aggiornaDirezioneX(Entity p) {
        float dx = 0;
        
        if (a && d) {
            return;
        }

        last = p.direzione();

        // Determina il movimento orizzontale
        if (a) dx = -1f;
        if (d) dx = 1f;
    
        p.direzione().set(dx, p.direzione().y);
        //System.out.println(p.direzione());
    }

    public void aggiornaDirezioneY(Entity p){
        float dy = 0;
        
        if (w && s) {
            return;
        }

        last = p.direzione();
    
        // Determina il movimento verticale
        if (w) dy = 1f;
        if (s) dy = -1f;
        
    
        p.direzione().set(p.direzione().x, dy);
        //System.out.println(p.direzione());
    }

    private void aggiornaStatoCollisione(Entity p) {
        if (collisioneX || collisioneY)
            p.stati().setInCollisione(true);
  
    }

    private void muoviAsseX(Entity p){
        
        if (!collisioneX) {
            float speed = p.statistiche().speed();
            float x = p.getX();
        
            if (a)
                p.setX(x - speed * (float) elapsedTime);
            if (d)
                p.setX(x + speed * (float) elapsedTime);
            p.stati().setInCollisione(false);
        }

    }
    private void muoviAsseY(Entity p){

        if (!collisioneY) {
            float speed = p.statistiche().speed();
            float y = p.getY();

            if (s)
                p.setY(y - speed * (float) elapsedTime);
            if (w)
                p.setY(y + speed * (float) elapsedTime);
            p.stati().setInCollisione(false);
        }

    }
}
