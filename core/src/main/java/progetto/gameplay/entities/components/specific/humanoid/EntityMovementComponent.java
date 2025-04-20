package progetto.gameplay.entities.components.specific.humanoid;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.base.IteratableComponent;
import progetto.gameplay.entities.components.specific.base.Cooldown;
import progetto.gameplay.entities.specific.specific.living.Humanoid;
import progetto.gameplay.world.Map;
import progetto.gameplay.world.graph.node.Node;

public class EntityMovementComponent extends IteratableComponent {

    // Da rifare per entità non volanti

    final Humanoid owner;
    public final Array<Vector2> path;
    public boolean isReady = true;

    public final Cooldown cooldown;
    public Vector2 direction;

    public int stepIndex = 0;

    public EntityMovementComponent(Humanoid entity) {
        this.path = new Array<>();
        this.owner = entity;
        float offset = MathUtils.random(-0.3f, 0.3f);
        this.cooldown = new Cooldown(1.5f+offset);
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady() {
        isReady = true;
    }

    public void setPath(DefaultGraphPath<Node> path) {
        this.path.clear();
        for (Node node : path) {
            this.path.add(new Vector2(node.getX(), node.getY()));
        }
        this.cooldown.reset();
        this.isReady = false;
    }

    @Override
    public void update(float delta) {
        cooldown.update(owner.manager.delta);
        if (stepIndex > path.size - 1 || cooldown.isReady) {
            stepIndex = 0;
            this.isReady = true;
            cooldown.reset();
        }
        if (path.size == 0) {
            return;
        }
        direzione();
        towards(path.get(stepIndex));
    }

    public void direzione() {
        if (stepIndex == 0) return;

        direction = new Vector2(path.get(stepIndex).x - path.get(stepIndex - 1).x, path.get(stepIndex).y - path.get(stepIndex - 1).y);
        if (!direction.epsilonEquals(0, 0)) {
            owner.getDirection().set(direction);
        }

    }

    private void towards(Vector2 target) {
        Body body = owner.getPhysics().getBody();
        if (body == null) {
            return;
        }

        if (owner.getPosition().dst(target) < 8/16f) {
            body.setLinearDamping(20f);
            stepIndex++;
            for (Vector2 node: path){
                if (Map.getGraph().getClosestNode(node.x, node.y)!=null && Map.getGraph().getClosestNode(node.x, node.y).isWalkable()){
                    this.isReady = true;
                    break;
                }
            }
        } else {
            body.setLinearDamping(3f);
        }

        Vector2 movementDirection = new Vector2(target).sub(owner.getPosition()).nor();
        float speed = owner.getMaxSpeed();
        Vector2 movement = movementDirection.scl(speed);
        Vector2 force = new Vector2(movement).scl(speed);
        body.applyLinearImpulse(force, owner.getPosition(), true);
    }
}

