package io.github.ale.screens.game.pathfinding.graph;

import com.badlogic.gdx.ai.pfa.Connection;

public class NodeConnection implements Connection<Node> {
    private final Node fromNode;
    private final Node toNode;
    private final float cost;
    public boolean isOccupied = false;

    /**
     * conessione fra nodi
     */
    public NodeConnection(Node fromNode, Node toNode, float cost) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.cost = cost;
    }

    /**
     * restituisce il costo del nodo
     */
    @Override
    public float getCost() {
        if(!toNode.isWalkable()) return 10f;
        return cost;
    }

    @Override
    public Node getFromNode() {
        return fromNode;
    }

    @Override
    public Node getToNode() {
        return toNode;
    }

}
