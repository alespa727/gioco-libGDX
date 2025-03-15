package io.github.ale.screens.gameScreen.entity.abstractEntity.movement;

import com.badlogic.gdx.Gdx;

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.maps.Map;

public class EntityMovement {

    private static boolean collisioneY;
    private static boolean collisioneX;

    public static void sposta(Entity e, float x, float y) {
        aggiornaDirezione(e, x, y);
        aggiornaCollisioni(e);
        muovi(e);
    }

    private static void aggiornaDirezione(Entity e, float x, float y) {
        e.statistiche().setSpeedBuff(1f);

        if (Math.abs(e.getX() - x) > 0.01f && Math.abs(e.getY() - y) > 0.01f) {
        
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

            e.statistiche().setSpeedBuff(1 / 1.41f);

        } else if (Math.abs(e.getX() - x) > 0.01f)
            aggiornaDirezioneX(e, x);
        else if (Math.abs(e.getY() - y) > 0.01f)
            aggiornaDirezioneY(e, y);
        else
            setFermo(e);

    }

    private static void muovi(Entity e) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        float speed = e.statistiche().getSpeed() * deltaTime;

        if (collisioneX || collisioneY) {
            e.statistiche().setSpeedBuff(1f);
        }


        if (!collisioneX) {
            if (e.direzione().equals("A") || e.direzione().equals("WA") || e.direzione().equals("SA")) {
                e.setX(e.getX() - speed);
            } else if (e.direzione().equals("D") || e.direzione().equals("WD") || e.direzione().equals("SD")) {
                e.setX(e.getX() + speed);
            }
        }

        if (!collisioneY) {
            if (e.direzione().equals("W") || e.direzione().equals("WA") || e.direzione().equals("WD")) {
                e.setY(e.getY() + speed);
            } else if (e.direzione().equals("S") || e.direzione().equals("SA") || e.direzione().equals("SD")) {
                e.setY(e.getY() - speed);
            }
        }

    }

    /**
     * decide la direzione da seguire in ba se alle coordinate da raggiungere
     * @param entity
     * @param y
     */
    private static void aggiornaDirezioneX(Entity entity, float x) {
        if (entity.getX() < x) {
            if (!entity.direzione().equals("D")) {
                entity.setDirezione("D");
            }
        } else {
            if (!entity.direzione().equals("A")) {
                entity.setDirezione("A");
            }
        }
    }

    /**
     * decide la direzione da seguire in base alle coordinate da raggiungere
     * 
     * @param entity
     * @param y
     */
    private static void aggiornaDirezioneY(Entity entity, float y) {
        if (entity.getY() < y) {
            if (!entity.direzione().equals("W")) {
                entity.setDirezione("W");
            }
        } else {
            if (!entity.direzione().equals("S")) {
                entity.setDirezione("S");
            }
        }
    }

    /**
     * setta direzione e variabile di stato a fermo
     * 
     * @param entity
     */
    private static void setFermo(Entity entity) {
        if (!entity.direzione().contains("fermo")) {
            entity.setDirezione("fermo".concat(entity.direzione()));
        }
        entity.stati().setIsMoving(false);

    }

    /**
     * aggiorna le collisioni
     * 
     * @param entity
     */
    private static void aggiornaCollisioni(Entity entity) {
        collisioneY = Map.checkCollisionY(entity);
        collisioneX = Map.checkCollisionX(entity);
    }

}
