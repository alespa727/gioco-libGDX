package progetto.gameplay;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.gameplay.map.events.MapEvent;

public class WorldManager{
    private static World instance;
    private static Queue<BodyDef> bodyToCreate;
    private static Queue<FixtureDef> fixtureToCreate;

    public static void init(){
        if (instance == null) {
            instance = new World(new Vector2(0, 0), true);
            bodyToCreate = new Queue<>();
        }
    }

    public static World getInstance() {
        if (instance == null) {
            instance = new World(new Vector2(0, 0), true);
            bodyToCreate = new Queue<>();
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

    public static void update() {
        if (bodyToCreate.notEmpty()){
            instance.createBody(bodyToCreate.removeFirst()).createFixture(fixtureToCreate.removeFirst());
        }
    }
}
