package io.github.ale.entity.abstractEnity.movement;

import com.badlogic.gdx.Gdx;

import io.github.ale.entity.abstractEnity.Entity;
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
            entity.setInCollisione(false);
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
            entity.setInCollisione(false);
            setFermo(entity);
        }
    }

    private static void aggiornaDirezioneX(Entity entity, float x) {
        if (entity.getX() < x) {
            entity.setDirezione("D");
        } else {
            entity.setDirezione("A");
        }
    }

    private static void aggiornaDirezioneY(Entity entity, float y) {
        if (entity.getY() < y) {
            entity.setDirezione("W");
        } else {
            entity.setDirezione("S");
        }
    }

    private static void muoviAsseX(Entity entity) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        if (!collisioneX) {
            entity.setInCollisione(false);
            if (entity.getDirezione().equals("D")) {
                entity.setX(entity.getX() + entity.statistiche.getSpeed() * deltaTime);
            } else {
                entity.setX(entity.getX() - entity.statistiche.getSpeed() * deltaTime);
            }
        } else
            setFermo(entity);

        if (collisioneX) {
            entity.setInCollisione(true);
        }
    }

    private static void muoviAsseY(Entity entity) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        if (!collisioneY) {
            entity.setInCollisione(false);
            if (entity.getDirezione().equals("W")) {
                entity.setY(entity.getY() + entity.statistiche.getSpeed() * deltaTime);
            } else {
                entity.setY(entity.getY() - entity.statistiche.getSpeed() * deltaTime);
            }
        } else
            setFermo(entity);
        if (collisioneY) {
            entity.setInCollisione(true);
        }
    }

    private static void setFermo(Entity entity) {
        if (!entity.getDirezione().contains("fermo")) {
            entity.setDirezione("fermo".concat(entity.getDirezione()));
        }
        entity.setIsMoving(false);
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

    private static void aggiornaCollisioni(Entity entity) {
        collisioneY = Map.checkCollisionY(entity);
        collisioneX = Map.checkCollisionX(entity);
    }

}
