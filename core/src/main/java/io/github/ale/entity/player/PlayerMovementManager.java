package io.github.ale.entity.player;

import com.badlogic.gdx.Gdx;

import io.github.ale.maps.Map;

public class PlayerMovementManager {
    KeyHandlerPlayer keyH;
    boolean w, s, a, d, shift;
    double elapsedTime;
    public PlayerMovementManager(){
        keyH = new KeyHandlerPlayer();
    }

    /**
     * aggiorna i vari parametri in base agli input
     * @param p
     */

    public void update(Player p){
        sprint(p);
        movimento(p);
    }

    /**
     * input sprint
     */

    private void sprint(Player p) {
        keyH.input();
        
        shift = keyH.shift;

        if (shift) {
            // System.out.println("SPRINT!");
            p.delta = 1.5f;
            p.speed = p.baseSpeed * p.delta;
        }

        if (!shift) {
            // System.out.println("NIENTE SPRINT");
            p.delta = 1f;
            p.speed = p.baseSpeed * p.delta;
        }
    }

    /**
     * gestisce il movimento in base agli input
     * @param p
     */
    public void movimento(Player p){
        elapsedTime = Gdx.graphics.getDeltaTime(); //moltiplicatore del movimento in base al framerate
        keyH.input();
        w = keyH.w;
        s = keyH.s;
        a = keyH.a;
        d = keyH.d;
        if (!w && !s && !a && !d) {
            switch (p.direzione.getDirezione()) {
                case "W" -> p.direzione.setDirezione("fermoW");
                case "S" -> p.direzione.setDirezione("fermoS");
                // } else if (direzione.getDirezione().equals("A")) {
                // animation = new Animation<>(1f / 2f, playerFramesLeft);
                case "A" -> p.direzione.setDirezione("fermoA");
                case "D" -> p.direzione.setDirezione("fermoD");
                default -> {
                }
            }
        }

        if (w && s && d && a) {
            p.direzione.setDirezione("fermoS");
        } else {
            if (w && s) {
                if (p.direzione.getDirezione().equals("W"))
                    p.direzione.setDirezione("fermoW");
                else if (p.direzione.getDirezione().equals("S"))
                    p.direzione.setDirezione("fermoS");

                if (p.direzione.getDirezione().equals("A"))
                    p.direzione.setDirezione("fermoA");
                else if (p.direzione.getDirezione().equals("D"))
                    p.direzione.setDirezione("fermoD");

                if (a)
                    p.direzione.setDirezione("A");
                if (d)
                    p.direzione.setDirezione("D");

                if (!Map.checkCollisionX(p.direzione.getDirezione(), p)) {
                    if (a)
                        p.x -= p.speed * elapsedTime;
                    if (d)
                        p.x += p.speed * elapsedTime;
                    p.inCollisione = false;
                } else
                    p.inCollisione = true;

                if (Map.checkCollisionY(p.direzione.getDirezione(), p) && !p.inCollisione)
                    p.inCollisione = true;

            } else if (a && d) {

                if (p.direzione.getDirezione().equals("W"))
                    p.direzione.setDirezione("fermoW");
                else if (p.direzione.getDirezione().equals("S"))
                    p.direzione.setDirezione("fermoS");

                if (p.direzione.getDirezione().equals("D"))
                    p.direzione.setDirezione("fermoD");
                else if (p.direzione.getDirezione().equals("A"))
                    p.direzione.setDirezione("fermoA");

                if (w)
                    p.direzione.setDirezione("W");
                if (s)
                    p.direzione.setDirezione("S");

                if (!Map.checkCollisionY(p.direzione.getDirezione(), p)) {
                    if (s)
                        p.y -= p.speed * elapsedTime;
                    if (w)
                        p.y += p.speed * elapsedTime;
                    p.inCollisione = false;
                } else
                    p.inCollisione = true;

                if (Map.checkCollisionX(p.direzione.getDirezione(), p) && !p.inCollisione)
                    p.inCollisione = true;

            } else {

                if (w)
                    p.direzione.setDirezione("W");
                if (s)
                    p.direzione.setDirezione("S");

                if (!Map.checkCollisionY(p.direzione.getDirezione(), p)) {
                    if (s)
                        p.y -= p.speed * elapsedTime;
                    if (w)
                        p.y += p.speed * elapsedTime;
                    p.inCollisione = false;
                } else if (!p.inCollisione)
                    p.inCollisione = true;

                if (Map.checkCollisionX(p.direzione.getDirezione(), p) && !p.inCollisione)
                    p.inCollisione = true;
                if (Map.checkCollisionY(p.direzione.getDirezione(), p) && !p.inCollisione)
                    p.inCollisione = true;

                if (d)
                    p.direzione.setDirezione("D");
                if (a)
                    p.direzione.setDirezione("A");

                if (!Map.checkCollisionX(p.direzione.getDirezione(), p)) {
                    if (a)
                        p.x -= p.speed * elapsedTime;
                    if (d)
                        p.x += p.speed * elapsedTime;
                    p.inCollisione = false;
                } else if (!p.inCollisione)
                    p.inCollisione = true;

                if (Map.checkCollisionY(p.direzione.getDirezione(), p) && !p.inCollisione)
                    p.inCollisione = true;

            }

        }
    }
}
