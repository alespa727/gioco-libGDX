package progetto.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.entity.components.specific.combat.AttackRangeComponent;
import progetto.entity.entities.specific.living.combat.Warrior;
import progetto.world.events.base.MapEvent;
import progetto.world.map.Map;

import java.util.concurrent.Semaphore;

public class WorldManager {
    private static World instance;
    private static Queue<Body> bodyToDestroy;

    public static void init() {
        if (instance == null) {
            instance = new World(new Vector2(0, 0), true);
            bodyToDestroy = new Queue<>();
        }
    }

    public static World getInstance() {
        if (instance == null) {
            instance = new World(new Vector2(0, 0), true);
            bodyToDestroy = new Queue<>();
        }
        return instance;
    }

    public static void clearMap() {
        Array<Body> bodies = new Array<>();
        instance.getBodies(bodies);
        for (Body body : bodies) {
            if (body.getUserData() instanceof Map) {
                instance.destroyBody(body);
            }

            if (body.getUserData() instanceof Warrior warrior && warrior.components.contains(AttackRangeComponent.class)) {
                AttackRangeComponent range = warrior.components.get(AttackRangeComponent.class);
                if (range.getDirectionalRange() != null) {
                    instance.destroyBody(range.getDirectionalRange());
                    range.directionalRange = null;
                }
            }
        }
    }

    public static void destroyBody(Body body) {
        if (body != null) {
            bodyToDestroy.addFirst(body);
        }
    }

    public static void update() {
        while (bodyToDestroy.notEmpty()) {
            instance.destroyBody(bodyToDestroy.removeFirst());
        }
    }
}
