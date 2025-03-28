package io.github.ale.screens.game.entityType.livingEntity.movement;

import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.game.entityType.livingEntity.LivingEntity;
import io.github.ale.screens.game.pathfinding.Node;

public class EntityMovementManager {
    public boolean fermo;
    private Vector2 direction;
    public boolean searchingfornext;
    private Node lastNode;
    private Node node;
    private final float REACHED_THRESHOLD = 4f/16f;

    public void setGoal(Node start, Node node) {
        this.lastNode=start;
        this.node = node;
        searchingfornext=true;
        fermo=false;
        direction = new Vector2();
    }

    public void reset(){
        searchingfornext=true;
        lastNode=node;
        node=null;
    }

    public Node getGoal(){
        return node;
    }

    public Node getLastNode(){
        return lastNode;
    }

    public void update(LivingEntity entity, float delta) {
        if (node == null) {
            return;
        }
        if(lastNode != null){
            direction = new Vector2(node.x - lastNode.x, node.y - lastNode.y);
            if (!direction.epsilonEquals(0, 0)) {
                entity.setDirezione(direction);
            }
        }

        fermo = false;
        Node targetNode = node;
        Vector2 targetPosition = new Vector2(targetNode.getX(), targetNode.getY());
        towards(entity, targetPosition, delta);
    }

    private void towards(LivingEntity entity, Vector2 target, float delta) {
        Vector2 movementDirection = new Vector2(target).sub(entity.coordinateCentro()).nor();
        float speed = entity.statistiche().speed();
        Vector2 movement = movementDirection.scl(speed);
        entity.body.setLinearVelocity(movement.x, movement.y);

        if (entity.coordinateCentro().dst(target) < REACHED_THRESHOLD) {
            searchingfornext = true;
            lastNode = node; // Safely mark the current node as reached
        }else searchingfornext=false;

    }
}

