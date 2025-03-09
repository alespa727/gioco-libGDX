package io.github.ale.entity.abstractEntity.movement;

import com.badlogic.gdx.Gdx;

import io.github.ale.entity.abstractEntity.Entity;
import io.github.ale.maps.Map;

public class EntityMovement {

    private static boolean collisioneY;
    private static boolean collisioneX;

    /**
     * sposta il nemico nella casella specificata dell'asse x
     * 
     * @param x
     */
    public static void spostaX(Entity entity, float x) {

        if (Math.abs(entity.getX() - x) > 0.1f) {
            aggiornaDirezioneX(entity, x);
            aggiornaCollisioni(entity);
            muoviAsseX(entity);
        } else {
            entity.getStati().setInCollisione(false);
            setFermo(entity);
        }

        // System.out.println(entity.getIsMoving());
    }

    /**
     * sposta il nemico nella casella specificata dell'asse y
     * 
     * @param y
     */
    public static void spostaY(Entity entity, float y) {

        if (Math.abs(entity.getY() - y) > 0.1f) {
            aggiornaDirezioneY(entity, y);
            aggiornaCollisioni(entity);
            muoviAsseY(entity);
        } else {
            entity.getStati().setInCollisione(false);
            setFermo(entity);
        }
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
     * autoesplicativa (cambia coordinate x in base a direzione)
     * @param entity
     */
    private static void muoviAsseX(Entity entity) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        if (!collisioneX) {
            entity.getStati().setInCollisione(false);
            if (entity.getDirezione().equals("D")) {
                entity.setX(entity.getX() + entity.getStatistiche().getSpeed() * deltaTime);
            } else {
                entity.setX(entity.getX() - entity.getStatistiche().getSpeed() * deltaTime);
            }
        } else
            setFermo(entity);

        if (collisioneX) {
            entity.getStati().setInCollisione(true);
        }
    }

    /**
     * autoesplicativa (cambia coordinate y in base a direzione)
     * @param entity
     */
    private static void muoviAsseY(Entity entity) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        if (!collisioneY) {
            entity.getStati().setInCollisione(false);
            if (entity.getDirezione().equals("W")) {
                entity.setY(entity.getY() + entity.getStatistiche().getSpeed() * deltaTime);
            } else {
                entity.setY(entity.getY() - entity.getStatistiche().getSpeed() * deltaTime);
            }
        } else
            setFermo(entity);
        if (collisioneY) {
            entity.getStati().setInCollisione(true);
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
