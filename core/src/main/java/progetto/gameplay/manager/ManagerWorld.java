package progetto.gameplay.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.factories.BodyFactory;
import progetto.gameplay.entity.components.BodyComponent;
import progetto.gameplay.entity.types.notliving.Bullet;
import progetto.gameplay.map.events.MapEvent;

public class ManagerWorld {
    private static World instance;
    private static Queue<BodyComponent> bodyToCreate;
    private static Queue<Body> bodyToDestroy;

    public static void init(){
        if (instance == null) {
            instance = new World(new Vector2(0, 0), true);
            bodyToCreate = new Queue<>();
            bodyToDestroy = new Queue<>();
        }
    }

    public static World getInstance() {
        if (instance == null) {
            instance = new World(new Vector2(0, 0), true);
            bodyToCreate = new Queue<>();
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
            if (body.getUserData() instanceof Bullet){
                ((Bullet) body.getUserData()).despawn();
            }
        }
    }

    public static void destroyBody(Body body) {
        bodyToDestroy.addFirst(body);
    }

    public static void update() {
        if (bodyToDestroy.size > 0) {
            instance.destroyBody(bodyToDestroy.removeFirst());
        }
        if (bodyToCreate.size > 0) {
            BodyComponent bodyComponent = bodyToCreate.removeFirst();
            BodyFactory.createBody(bodyComponent.userdata, bodyComponent.bodyDef, bodyComponent.fixtureDef, bodyComponent.shape);
        }
    }
}
