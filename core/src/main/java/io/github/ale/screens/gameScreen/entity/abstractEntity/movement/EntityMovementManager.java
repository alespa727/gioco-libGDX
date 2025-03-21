package io.github.ale.screens.gameScreen.entity.abstractEntity.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.pathfinding.Node;

public class EntityMovementManager {
    public boolean fermo;
    private Vector2 direction;
    public boolean sulNodo;
    private Node lastNode;
    private Node node;
    private final float REACHED_THRESHOLD = 1/16f; 

    public void setGoal(Node start, Node node) {
        this.lastNode=start;
        this.node = node;
        sulNodo=true;
        fermo=false;
        direction = new Vector2();
    }

    public void setFermo(Entity e) {
        
        
        if(node.equals(lastNode) && fermo==false){
            direction.scl(0.5f);
            e.direzione().set(direction);
                       
            //System.out.println(e.direzione());
            fermo = true;
        }
 
    }

    public void update(Entity entity) {
        if (node == null) {
            return;
        }
        if(lastNode!=null){
            direction = new Vector2(node.x - lastNode.x, node.y - lastNode.y);
            if (!direction.epsilonEquals(0, 0)) {
                entity.setDirezione(direction);
            }
        }
        
        fermo = false;
        Node targetNode = node;
        Vector2 targetPosition = new Vector2(targetNode.getX(), targetNode.getY());
        towards(entity, targetPosition);
    }

    private void towards(Entity entity, Vector2 target) {
        Vector2 direzione = new Vector2(target).sub(entity.coordinateCentro()).nor(); 
        float speed = entity.statistiche().getSpeed() * Gdx.graphics.getDeltaTime();
        Vector2 movement = direzione.scl(speed);
        if(!entity.manager.ispathclear(entity, node)){
            movement.scl(0.9f);
        }
        entity.setX(entity.getX() + movement.x);
        entity.setY(entity.getY() + movement.y);
        
        if (entity.coordinateCentro().dst(target) < REACHED_THRESHOLD) {
            sulNodo=true;
            lastNode=node;
        }else sulNodo=false;
    }
}

