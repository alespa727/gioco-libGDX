package progetto.gameplay.entities.components.specific.movement;

import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.world.graph.node.Node;

public class NodeComponent extends Component {
    public Node lastNode;
    public Node node;

    public void dispose() {
        if (lastNode != null) {
            lastNode.setWalkable(true);
        }
        if (node != null) {
            node.setWalkable(true);
        }
    }

}
