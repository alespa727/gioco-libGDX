package io.github.ale.entity.player;

import com.badlogic.gdx.Gdx;

import io.github.ale.maps.Map;

public class PlayerMovementManager {
    KeyHandlerPlayer keyH;
    boolean w, s, a, d, shift;
    double elapsedTime;

    boolean collisioneY;
    boolean collisioneX;

    StatiDiMovimento stato;
    String lastDirezione;

    public PlayerMovementManager() {
        keyH = new KeyHandlerPlayer();
    }

    /**
     * aggiorna i vari parametri in base agli input
     * 
     * @param p
     */

    public void update(Player p) {
        keyH.input();
        sprint(p);
        movimento(p);
    }

    /**
     * input sprint
     */

    private void sprint(Player p) {
        
        shift = keyH.shift;

        if (shift) {
            // System.out.println("SPRINT!");
            p.statistiche.setSpeedMultiplier(1.5f);
    
        }

        if (!shift) {
            // System.out.println("NIENTE SPRINT");
            p.statistiche.setSpeedMultiplier(1f);
        }
    }

    /**
     * gestisce il movimento in base agli input
     * 
     * @param p
     */
    private void movimento(Player p) {
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
        lastDirezione = p.direzione.getDirezione();
        if (!lastDirezione.equals(p.direzione.getDirezione())) {
            System.out.println(p.direzione.getDirezione());
        }
        

    }

    private void addNotMoving(Player p){
        if (!p.direzione.getDirezione().contains("fermo")) {
            p.direzione.setDirezione("fermo".concat(p.direzione.getDirezione()));
        }
    }

    private void aggiornaCollisioni(Player p) {
        collisioneY = Map.checkCollisionY(p);
        collisioneX = Map.checkCollisionX(p);
    }

    private void aggiornaDirezioneY(Player p) {
        if (w)
            p.direzione.setDirezione("W");
        if (s)
            p.direzione.setDirezione("S");
    }

    private void aggiornaDirezioneX(Player p) {
        if (a)
            p.direzione.setDirezione("A");
        if (d)
            p.direzione.setDirezione("D");
    }

    private void aggiornaStatoCollisione(Player p) {
        if (collisioneX)
            p.inCollisione = true;
        if (collisioneY)
            p.inCollisione = true;
    }

    private void muoviAsseX(Player p){
        if (!collisioneX) {
            if (a)
                p.setX(p.getX() - p.statistiche.getSpeed() * (float) elapsedTime);
            if (d)
                p.setX(p.getX() + p.statistiche.getSpeed() * (float) elapsedTime);
            p.inCollisione = false;
        }
    }
    private void muoviAsseY(Player p){
        if (!collisioneY) {
            if (s)
                p.setY(p.getY() - p.statistiche.getSpeed() * (float) elapsedTime);
            if (w)
                p.setY(p.getY() + p.statistiche.getSpeed() * (float) elapsedTime);
            p.inCollisione = false;
        }
    }
}
