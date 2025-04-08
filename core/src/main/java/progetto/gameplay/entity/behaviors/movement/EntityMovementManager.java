package progetto.gameplay.entity.behaviors.manager.entity.movement;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import progetto.utils.Cooldown;
import progetto.gameplay.entity.types.living.Humanoid;
import progetto.gameplay.map.Map;
import progetto.gameplay.map.graph.node.Node;

public class EntityMovementManager {

    final Humanoid owner;
    final Array<Vector2> path;
    boolean isReady = true;

    final Cooldown cooldown;
    Vector2 direction;

    int steps = 0;

    public EntityMovementManager(Humanoid entity) {
        this.path = new Array<>();
        this.owner = entity;
        float offset = MathUtils.random(-0.3f, 0.3f);
        this.cooldown = new Cooldown(1.5f+offset);
    }

    public boolean isReady() {
        return isReady;
    }

    public void setPath(DefaultGraphPath<Node> path) {
        this.path.clear();
        for (Node node : path) {
            this.path.add(new Vector2(node.getX(), node.getY()));
        }
        this.cooldown.reset();
        this.isReady = false;
    }

    public void update() {
        cooldown.update(owner.delta);
        if (steps > path.size - 1 || cooldown.isReady) {
            steps = 0;
            this.isReady = true;
            cooldown.reset();
        }
        if (path.size == 0) {
            return;
        }
        direzione();
        towards(path.get(steps));
    }

    public void direzione() {
        if (steps == 0) return;

        direction = new Vector2(path.get(steps).x - path.get(steps - 1).x, path.get(steps).y - path.get(steps - 1).y);
        if (!direction.epsilonEquals(0, 0)) {
            owner.setDirezione(direction);
        }

    }

    private void towards(Vector2 target) {
        if (owner.getPosition().dst(target) < 8/16f) {
            owner.body.setLinearDamping(20f);
            steps++;
            for (Vector2 node: path){
                if (!Map.getGraph().getClosestNode(node.x, node.y).isWalkable()){
                    this.isReady = true;
                    break;
                }
            }
        } else {
            owner.body.setLinearDamping(3f);
        }

        Vector2 movementDirection = new Vector2(target).sub(owner.getPosition()).nor();
        float speed = owner.getSpeed();
        Vector2 movement = movementDirection.scl(speed);
        Vector2 force = new Vector2(movement).scl(speed).scl(owner.body.getMass());
        owner.body.applyForceToCenter(force.scl(2f), true);
    }
}

