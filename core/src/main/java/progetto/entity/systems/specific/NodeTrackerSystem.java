package progetto.entity.systems.specific;

import progetto.entity.components.ComponentManager;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.movement.NodeComponent;
import progetto.entity.entities.base.Entity;
import progetto.entity.systems.base.AutomaticSystem;
import progetto.entity.components.base.ComponentFilter;
import progetto.world.map.Map;

public class NodeTrackerSystem extends AutomaticSystem {

    public NodeTrackerSystem() {
        super(ComponentFilter.all(NodeComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.shouldRender()) return;
        ComponentManager componentManager = entity.components;
        if (componentManager.contains(NodeComponent.class)) {
            NodeComponent nodeComponent = componentManager.get(NodeComponent.class);
            if (nodeComponent.lastNode != null && nodeComponent.node != null) {
                nodeComponent.lastNode.setWalkable(true);
            }
            if (nodeComponent.node != null) {
                nodeComponent.lastNode = nodeComponent.node;
            }
            nodeComponent.node = Map.getGraph().getClosestNode(entity.components.get(PhysicsComponent.class).getPosition().x, entity.components.get(PhysicsComponent.class).getPosition().y);
            if (nodeComponent.node != null) {
                nodeComponent.node.setWalkable(false);
            }
        }
    }
}
