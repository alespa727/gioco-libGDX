package io.github.ale.screens.gameScreen.entity.abstractEntity.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.pathfinding.Node;

public class EntityMovementManager {

    public EntityMovementManager() {

    }

    public void update(DefaultGraphPath<Node> path, Array<Vector2> direzioni, Entity e) {
        if (path == null || path.getCount() == 0 || path.getCount() <= 1) {
            return; // Nessun percorso valido o percorso completato
        }
        
        if (direzioni.get(0).epsilonEquals(1, 1) 
        || direzioni.get(0).epsilonEquals(-1, 1)
        || direzioni.get(0).epsilonEquals(1, -1)
        || direzioni.get(0).epsilonEquals(-1, -1)) {
            e.statistiche().setSpeedBuff(1/1.41f);
        }else{
            e.statistiche().setSpeedBuff(1f);
        }

        if (Math.abs(e.getX() + e.getSize().getWidth() / 2 - path.get(1).getX()) < 0.1f) {
           
        } else if (e.getX() + e.getSize().getWidth() / 2 < path.get(1).getX()) {
            System.out.println("DESTRA");
            e.direzione().x=1f;
            e.setX(e.getX() + e.statistiche().getSpeed() * Gdx.graphics.getDeltaTime());
        } else if (e.getX() + e.getSize().getWidth() / 2 > path.get(1).getX()) {
            System.out.println("SINISTRA");
            e.direzione().x=-1f;
            e.setX(e.getX() - e.statistiche().getSpeed() * Gdx.graphics.getDeltaTime());
        }

        if (Math.abs(e.getY() + e.getSize().getHeight() / 2 - path.get(1).getY())<0.1f) {
            
        }else if (e.getY() + e.getSize().getHeight() / 2 < path.get(1).getY()) {
            System.out.println("SU");
            e.direzione().y=1f;
            e.setY(e.getY() + e.statistiche().getSpeed() * Gdx.graphics.getDeltaTime());
        } else if (e.getY() + e.getSize().getHeight() / 2 > path.get(1).getY()) {
            System.out.println("GIU");
            e.direzione().y=-1f;
            e.setY(e.getY() - e.statistiche().getSpeed() * Gdx.graphics.getDeltaTime());
        }
    }
}
