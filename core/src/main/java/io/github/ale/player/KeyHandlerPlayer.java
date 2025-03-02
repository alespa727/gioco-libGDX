package io.github.ale.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import io.github.ale.maps.Map;

public class KeyHandlerPlayer {
    float elapsedTime;
    public KeyHandlerPlayer(){

    }

    public void input(Player p){
        elapsedTime = Gdx.graphics.getDeltaTime();
        sprint(p);
        boolean w = Gdx.input.isKeyPressed(Input.Keys.W), s = Gdx.input.isKeyPressed(Input.Keys.S), a = Gdx.input.isKeyPressed(Input.Keys.A), d = Gdx.input.isKeyPressed(Input.Keys.D);
        if(!w && !s && !a && !d){
            switch (p.direzione.getDirezione()) {
                case "W" -> p.direzione.setDirezione("fermoW");
                case "S" -> p.direzione.setDirezione("fermoS");
                //} else if (direzione.getDirezione().equals("A")) {
                //    animation = new Animation<>(1f / 2f, playerFramesLeft);
                case "A" -> p.direzione.setDirezione("fermoA");
                case "D" -> p.direzione.setDirezione("fermoD");
                default -> {
                }
            }
        }

        if (w && s && d && a) {
            p.direzione.setDirezione("fermoS");
        }else{
            if (w && s) {
                if (p.direzione.getDirezione().equals("A")) p.direzione.setDirezione("fermoA");
                else if (p.direzione.getDirezione().equals("D")) p.direzione.setDirezione("fermoD");

                if (a) p.direzione.setDirezione("A");
                if (d) p.direzione.setDirezione("D");

                if (!Map.checkCollisionX(p.direzione.getDirezione())) {
                    if (a) p.worldX -= p.speed * elapsedTime;
                    if (d) p.worldX += p.speed * elapsedTime;
                    p.inCollisione=false;
                }else p.inCollisione=true;

                if (p.direzione.getDirezione().equals("W")) p.direzione.setDirezione("fermoW");
                else if (p.direzione.getDirezione().equals("S")) p.direzione.setDirezione("fermoS");

            }else if(a && d){
                if (p.direzione.getDirezione().equals("W")) p.direzione.setDirezione("fermoW");
                else if (p.direzione.getDirezione().equals("S")) p.direzione.setDirezione("fermoS");

                if (w) p.direzione.setDirezione("W");
                if (s) p.direzione.setDirezione("S");

                if (!Map.checkCollisionY(p.direzione.getDirezione())) {
                    if (s) p.worldY -= p.speed * elapsedTime;
                    if (w) p.worldY += p.speed * elapsedTime;
                    p.inCollisione=false;
                }else p.inCollisione=true;

                if (p.direzione.getDirezione().equals("D")) p.direzione.setDirezione("fermoD");
                else if (p.direzione.getDirezione().equals("A")) p.direzione.setDirezione("fermoA");

            }else{

                if (w) p.direzione.setDirezione("W");
                if (s) p.direzione.setDirezione("S");

                if (!Map.checkCollisionY(p.direzione.getDirezione())) {
                    if (s) p.worldY -= p.speed * elapsedTime;
                    if (w) p.worldY += p.speed * elapsedTime;
                    p.inCollisione=false;
                }else p.inCollisione=true;
                
                if (d) p.direzione.setDirezione("D");
                if (a) p.direzione.setDirezione("A");
    
                if (!Map.checkCollisionX(p.direzione.getDirezione())) {
                    if (a) p.worldX -= p.speed * elapsedTime;
                    if (d) p.worldX += p.speed * elapsedTime;
                    p.inCollisione=false;
                }else p.inCollisione=true;

                
            }
        }
        if (Map.checkCollisionY("W") || Map.checkCollisionY("S") || Map.checkCollisionX("A") || Map.checkCollisionX("S")) {
            p.inCollisione=true;
        }
        attack();
    }

    public void attack(){
 
    }

    /**
     * input sprint
     */

     private void sprint(Player p){
        boolean shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);

        if (shift) {
            //System.out.println("SPRINT!");
            p.delta = 1.5f;
            p.speed = p.baseSpeed*p.delta;
        }

        if (!shift) {
            //System.out.println("NIENTE SPRINT");
            p.delta = 1f;
            p.speed = p.baseSpeed*p.delta;
        }
    }
}
