package progetto.gameplay.entity.components.entity;

import progetto.gameplay.entity.components.IteratableComponent;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.map.Map;
import progetto.gameplay.map.graph.node.Node;

public class NodeTracker extends IteratableComponent {
    private final Entity owner;

    private Node lastNode;
    private Node node;

    public NodeTracker(Entity owner) {
        this.owner = owner;
    }

    public void dispose() {
        if (lastNode != null) {
            lastNode.setWalkable(true);
        }
        if (node != null) {
            node.setWalkable(true);
        }

    }

    @Override
    public void update(float delta) {
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
}
