package io.github.ale.screens.gameScreen.entity.abstractEntity.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.pathfinding.Node;

public class EntityMovementManager {

    public int count;
    boolean flag = false;
    Vector2 lastdirezione = new Vector2();

    public EntityMovementManager() {
        count=1000;
    }

    public void resetCount() {
        count = 0;
        flag=false;
    }

    public void update(DefaultGraphPath<Node> path, Array<Vector2> direzioni, Entity e) {
        if (path == null || path.getCount() == 0 || path.getCount() < count+1) {
            return; // Nessun percorso valido o percorso completato
        }

        if(count == path.getCount()-1) {
            return;
        }

        if(Math.abs(e.getX() +e.getSize().getWidth()/2 - path.get(path.getCount()-1).getX()) < 1f && Math.abs(e.getY() +e.getSize().getHeight()/2 - path.get(path.getCount()-1).getY()) < 1f) {
            if(!flag) {
                e.setDirezione(new Vector2(lastdirezione.x/2, lastdirezione.y/2));
                flag=true;
            }
            
            return;
        }

        Vector2 target = new Vector2(path.get(count+1).getX(), path.get(count+1).getY());
        float angolo = (float) Math.atan2(target.y - e.getY() - e.getSize().getHeight() / 2,
                target.x - e.getX() - e.getSize().getWidth() / 2);

        float vx = (float) (Math.cos(angolo) * e.statistiche().getSpeed() * Gdx.graphics.getDeltaTime());
        float vy = (float) (Math.sin(angolo) * e.statistiche().getSpeed() * Gdx.graphics.getDeltaTime());
        
        e.setX(e.getX() + vx);
        e.setY(e.getY() + vy);

        e.setDirezione(angleToRoundedDirection(angolo * 180 / (float) Math.PI));

        lastdirezione = direzioni.get(count);

        if(Math.abs(e.getX() +e.getSize().getWidth()/2 - path.get(count+1).getX()) < 0.01f && Math.abs(e.getY() +e.getSize().getHeight()/2 - path.get(count+1).getY()) < 0.01f) {
            System.out.println("AAA");
            count++;
        }
    }

    public Vector2 angleToRoundedDirection(float degrees) {
        float radians = (float) Math.toRadians(degrees);
        float x = (float) Math.cos(radians);
        float y = (float) Math.sin(radians);

        if (x > 0.5f) {
            x = 1;
        } else if (x < -0.5f) {
            x = -1;
        } else {
            x = 0;
        }

        if (y > 0.5f) {
            y = 1;
        } else if (y < -0.5f) {
            y = -1;
        } else {
            y = 0;
        }

        return new Vector2(x, y);
    }
}
