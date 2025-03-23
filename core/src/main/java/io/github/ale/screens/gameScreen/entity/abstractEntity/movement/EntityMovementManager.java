package io.github.ale.screens.gameScreen.entity.abstractEntity.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.gameScreen.entity.livingEntity.LivingEntity;
import io.github.ale.screens.gameScreen.pathfinding.Node;

public class EntityMovementManager {
    public boolean fermo;
    private Vector2 direction;
    public boolean searchingfornext;
    private Node lastNode;
    private Node node;
    private final float REACHED_THRESHOLD = 1/16f; 

    public void setGoal(Node start, Node node) {
        this.lastNode=start;
        this.node = node;
        searchingfornext=true;
        fermo=false;
        direction = new Vector2();
    }

    public void setFermo(LivingEntity e) {
        
        if(node.equals(lastNode) && fermo==false){
            direction.scl(0.5f);
            e.direzione().set(direction);
                       
            //System.out.println(e.direzione());
            searchingfornext = true;
        }
 
    }

    public void update(LivingEntity entity) {
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

    private void towards(LivingEntity entity, Vector2 target) {
        Vector2 direzione = new Vector2(target).sub(entity.coordinateCentro()).nor(); 
        float speed = entity.statistiche().speed() * Gdx.graphics.getDeltaTime();
        Vector2 movement = direzione.scl(speed);
        /*if(!entity.manager.ispathclear(entity, node)){
            movement.scl(0.75f);
        }*/
        entity.setX(entity.getX() + movement.x);
        entity.setY(entity.getY() + movement.y);
        
        if (entity.coordinateCentro().dst(target) < REACHED_THRESHOLD) {
            searchingfornext=true;
            lastNode=node;
        }else searchingfornext=false;
    }
}

