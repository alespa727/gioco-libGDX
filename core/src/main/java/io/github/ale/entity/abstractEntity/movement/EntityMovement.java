package io.github.ale.entity.abstractEntity.movement;

import com.badlogic.gdx.Gdx;

import io.github.ale.entity.abstractEntity.Entity;
import io.github.ale.maps.Map;

public class EntityMovement {

    private static boolean collisioneY;
    private static boolean collisioneX;

    public static void sposta(Entity e, float x, float y){
        aggiornaDirezione(e, x, y);
        aggiornaCollisioni(e);
        muovi(e);
    }

    private static void aggiornaDirezione(Entity e, float x, float y){
        if (Math.abs(e.getX() - x) > 0.1f && Math.abs(e.getY() - y) > 0.1f) {
            if (e.getX() < x && e.getY() > y) {
                e.setDirezione("SD");
            }
            if (e.getX() > x && e.getY() > y) {
                e.setDirezione("SA");
                
            }
            if (e.getX() < x && e.getY() < y) {
                
                e.setDirezione("WD");
            }
            if (e.getX() > x && e.getY() < y) {
                e.setDirezione("WA");
            }
            System.out.println(e.getDirezione());
        }
        else if (Math.abs(e.getX() - x) > 0.1f) aggiornaDirezioneX(e, x);
        else if (Math.abs(e.getY() - y) > 0.1f) aggiornaDirezioneY(e, y);
        else setFermo(e);
        
    }   

    private static void muovi(Entity e) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        float speed = e.getStatistiche().getSpeed() * deltaTime;

        if (!collisioneX && !collisioneY) {
            if (e.getDirezione().equals("A") || e.getDirezione().equals("WA") || e.getDirezione().equals("SA")) {
                e.setX(e.getX() - speed);
            } else if (e.getDirezione().equals("D") || e.getDirezione().equals("WD") || e.getDirezione().equals("SD")) {
                e.setX(e.getX() + speed);
            }
        }else setFermo(e);
        
       
        if (!collisioneY && !collisioneX) {
            if (e.getDirezione().equals("W") || e.getDirezione().equals("WA") || e.getDirezione().equals("WD")) {
                e.setY(e.getY() + speed);
            } else if (e.getDirezione().equals("S") || e.getDirezione().equals("SA") || e.getDirezione().equals("SD")) {
                e.setY(e.getY() - speed);
            }
        }else setFermo(e);
    
    }

    /**
     * decide la direzione da seguire in base alle coordinate da raggiungere
     * @param entity
     * @param y
     */
    private static void aggiornaDirezioneX(Entity entity, float x) {
        if (entity.getX() < x) {
            if (!entity.getDirezione().equals("D")) {
                entity.setDirezione("D");
            }
        } else {
            if (!entity.getDirezione().equals("A")) {
                entity.setDirezione("A");
            }
        }
    }

    /**
     * decide la direzione da seguire in base alle coordinate da raggiungere
     * @param entity
     * @param y
     */
    private static void aggiornaDirezioneY(Entity entity, float y) {
        if (entity.getY() < y) {
            if (!entity.getDirezione().equals("W")) {
                entity.setDirezione("W");
            }
        } else {
            if (!entity.getDirezione().equals("S")) {
                entity.setDirezione("S");
            }
        }
    }

    /**
     * setta direzione e variabile di stato a fermo
     * @param entity
     */
    private static void setFermo(Entity entity) {
        if (!entity.getDirezione().contains("fermo")) {
            entity.setDirezione("fermo".concat(entity.getDirezione()));
        }
        entity.getStati().setIsMoving(false);
    }

    /**
     * sposta il nemico con una "dash" nella casella specificata dell'asse x
     * 
     * @param x
     */
    public static void dashX(Entity entity, float x) {

    }

    /**
     * sposta il nemico con una "dash" nella casella specificata dell'asse y
     * 
     * @param x
     */
    public static void dashY(Entity entity, float y) {

    }

    
     /**
      * aggiorna le collisioni
      * @param entity
      */
    private static void aggiornaCollisioni(Entity entity) {
        collisioneY = Map.checkCollisionY(entity);
        collisioneX = Map.checkCollisionX(entity);
    }


}
