package progetto.world.graph.node;

import com.badlogic.gdx.ai.pfa.Connection;

public class NodeConnection implements Connection<Node> {
    private final Node fromNode;
    private final Node toNode;
    private final float cost;

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
        if (!toNode.isWalkable() || !fromNode.isWalkable()) return 100f;
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
