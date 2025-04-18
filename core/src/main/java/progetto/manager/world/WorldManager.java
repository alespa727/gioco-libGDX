package progetto.manager.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.gameplay.world.events.base.MapEvent;

public class WorldManager {
    private static World instance;
    private static Queue<Body> bodyToDestroy;

    public static void init(){
        if (instance == null) {
            instance = new World(new Vector2(0, 0), true);
            bodyToDestroy = new Queue<>();
        }
    }

    public static World getInstance() {
        if (instance == null) {
            System.err.println("AHGDASUYGDASDAGSDASID");
            instance = new World(new Vector2(0, 0), true);
            bodyToDestroy = new Queue<>();
        }
        return instance;
    }

    public static void clearMap() {
        Array<Body> bodies = new Array<>();
        instance.getBodies(bodies);
        for (Body body : bodies) {
            if ("map".equals(body.getUserData()) || body.getUserData() instanceof MapEvent) {
                instance.destroyBody(body);
            }
        }
    }

    public static void destroyBody(Body body) {
        if (body != null) {
            bodyToDestroy.addFirst(body);
        }
    }

    public static void update() {
        if (bodyToDestroy.notEmpty()) {
            instance.destroyBody(bodyToDestroy.removeFirst());
        }
    }
}
