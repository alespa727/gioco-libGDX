package progetto.gameplay.entities.components.specific.entity;

import progetto.gameplay.entities.components.base.IteratableComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.world.Map;
import progetto.gameplay.world.graph.node.Node;

public class NodeTrackerComponent extends IteratableComponent {
    private final Entity owner;

    private Node lastNode;
    private Node node;

    public NodeTrackerComponent(Entity owner) {
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
