package io.github.ale.screens.gameScreen.entity.abstractEntity.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.pathfinding.Node;

public class EntityMovementManager {
    public boolean fermo;
    Vector2 direction;
    public boolean sulNodo;
    private Node lastNode;
    private Node node;
    private static final float REACHED_THRESHOLD = 1/16f; 

    public void setGoal(Node start, Node node) {
        this.lastNode=start;
        this.node = node;
        sulNodo=true;
        fermo=false;
    }

    public void setFermo(Entity e) {
        
        
        if(node.equals(lastNode) && fermo==false){
            direction.scl(0.5f);
            e.direzione().set(direction);
        }
            
        //System.out.println(e.direzione());
        fermo = true;
    }

    public void update(Entity entity) {
        if (node == null) {
            return;
        }
        if(lastNode!=null){
            direction = new Vector2(node.x - lastNode.x, node.y - lastNode.y);
            if (!direction.epsilonEquals(0, 0)) {
                entity.direzione().set(direction);
            }
        }
        
        fermo = false;
        Node targetNode = node;
        Vector2 targetPosition = new Vector2(targetNode.getX(), targetNode.getY());
        moveTowards(entity, targetPosition);

        if (entity.coordinateCentro().dst(targetPosition) < REACHED_THRESHOLD) {
            sulNodo=true;
            lastNode=node;
        }else sulNodo=false;
    }

    private void moveTowards(Entity entity, Vector2 target) {
        Vector2 direzione = new Vector2(target).sub(entity.coordinateCentro()).nor(); 
        float speed = entity.statistiche().getSpeed() * Gdx.graphics.getDeltaTime();
        Vector2 movement = direzione.scl(speed);
        entity.setX(entity.getX() + movement.x);
        entity.setY(entity.getY() + movement.y);
    }
}
