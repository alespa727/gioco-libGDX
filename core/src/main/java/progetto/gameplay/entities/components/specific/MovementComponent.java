package progetto.gameplay.entities.components.specific;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.components.specific.base.Cooldown;
import progetto.gameplay.world.graph.node.Node;

public class MovementComponent extends Component {
    private boolean isReady = true;
    private final Array<Node> path = new Array<>();
    public Cooldown cooldown = new Cooldown(2);
    public int stepIndex = 0;

    public void setPath(DefaultGraphPath<Node> path) {
        if (isReady) {
            for (Node node : this.path) {
                node.setWalkable(true);
            }
            this.path.clear();
            for (Node node : path) {
                this.path.add(node);
                node.setWalkable(false);
            }
        }
    }

    public Array<Node> getPath(){
        return path;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }


    public void drawPath(ShapeRenderer shapeRenderer) {
        if (path.isEmpty()) {
            return;
        }
        shapeRenderer.setColor(Color.RED);
        Node previousNode = null;
        for (Node node : path) {
            if (previousNode != null) {
                shapeRenderer.rectLine(previousNode.getX(), previousNode.getY(), node.getX(), node.getY(), 0.1f);
            }
            previousNode = node;
        }

        shapeRenderer.setColor(Color.BLACK);
    }
}
