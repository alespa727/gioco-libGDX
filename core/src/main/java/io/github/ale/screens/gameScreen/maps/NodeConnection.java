package io.github.ale.screens.gameScreen.maps;

import com.badlogic.gdx.ai.pfa.Connection;

public class NodeConnection implements Connection<Node> {
    private Node fromNode;
    private Node toNode;
    private float cost;

    public NodeConnection(Node fromNode, Node toNode, float cost) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.cost = cost;
    }

    @Override
    public float getCost() {
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
