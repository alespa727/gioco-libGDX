package io.github.ale.screens.gameScreen.entity.abstractEntity.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

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
                e.setDirezione(new Vector2(1f, -1f));
            }
            if (e.getX() > x && e.getY() > y) {
                e.setDirezione(new Vector2(-1f, -1f));
            }
            if (e.getX() < x && e.getY() < y) {

                e.setDirezione(new Vector2(1f, 1f));
            }
            if (e.getX() > x && e.getY() < y) {
                e.setDirezione(new Vector2(-1f, 1f));
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
            if (e.direzione().x == -1f) {
                e.setX(e.getX() - speed);
            } else if (e.direzione().x == 1f) {
                e.setX(e.getX() + speed);
            }
        }

        if (!collisioneY) {
            if (e.direzione().y == 1f) {
                e.setY(e.getY() + speed);
            } else if (e.direzione().y == -1f) {
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
            if (!entity.direzione().epsilonEquals(1f, 0)) {
                entity.direzione().set(1f, 0);
            }
        } else {
            if (!entity.direzione().epsilonEquals(-1f, 0)) {
                entity.direzione().set(-1f, 0);
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
            if (!entity.direzione().epsilonEquals(0, 1f)) {
                entity.direzione().set(0, 1f);
            }
        } else {
            if (!entity.direzione().epsilonEquals(0, -1f)) {
                entity.direzione().set(0, -1f);
            }
        }
    }

    /**
     * setta direzione e variabile di stato a fermo
     * 
     * @param entity
     */
    private static void setFermo(Entity entity) {
        if (entity.direzione().x==1f || entity.direzione().x==-1f) {
            entity.direzione().x=entity.direzione().x/2;
        }
        if (entity.direzione().y==1f || entity.direzione().y==-1f) {
            entity.direzione().y=entity.direzione().y/2;
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
