package progetto.gameplay.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.base.ComponentManager;
import progetto.gameplay.entities.components.specific.NodeComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.systems.base.System;
import progetto.gameplay.world.Map;

public class NodeTrackerSystem extends System {
    @Override
    public void update(float delta, Array<Entity> list) {
        for (Entity e : list) {
            ComponentManager componentManager = e.componentManager;
            if (componentManager.contains(NodeComponent.class)){
                NodeComponent nodeComponent = componentManager.get(NodeComponent.class);
                if (nodeComponent.lastNode != null && nodeComponent.node != null) {
                    nodeComponent.lastNode.setWalkable(true);
                }
                if (nodeComponent.node != null) {
                    nodeComponent.lastNode = nodeComponent.node;
                }
                nodeComponent.node = Map.getGraph().getClosestNode(e.getPhysics().getPosition().x, e.getPhysics().getPosition().y);
                if (nodeComponent.node != null) {
                    nodeComponent.node.setWalkable(false);
                }
            }

        }

    }
}
