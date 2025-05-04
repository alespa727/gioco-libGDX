package progetto.ECS.systems.specific;

import progetto.ECS.components.ComponentManager;
import progetto.ECS.components.base.ComponentFilter;
import progetto.ECS.components.specific.base.StateComponent;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.ECS.components.specific.movement.NodeComponent;
import progetto.ECS.entities.Entity;
import progetto.ECS.systems.base.IteratingSystem;
import progetto.world.map.Map;

public class NodeTrackerSystem extends IteratingSystem {

    public NodeTrackerSystem() {
        super(ComponentFilter.all(NodeComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.get(StateComponent.class).shouldBeUpdated()) return;
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
