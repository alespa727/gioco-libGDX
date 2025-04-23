package progetto.entity.components.specific.movement;

import progetto.entity.components.base.Component;
import progetto.world.graph.node.Node;

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
