package progetto.gameplay.entity.components;

import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.map.Map;
import progetto.gameplay.map.graph.node.Node;

public class NodeTracker {
    private final Entity owner;

    private Node lastNode;
    private Node node;

    public NodeTracker(Entity owner) {
        this.owner = owner;
    }

    public void updateNode() {
        if (lastNode != null && node != null) {
            lastNode.setWalkable(true);
        }
        if (node != null) {
            lastNode = node;
        }
        node = Map.getGraph().getClosestNode(owner.getPhysics().getPosition().x, owner.getPhysics().getPosition().y);
        if (node != null) {
            node.setWalkable(false);
        }
    }

    public void dispose() {
        if (lastNode != null) {
            lastNode.setWalkable(true);
        }
        if (node != null) {
            node.setWalkable(true);
        }

    }
}
