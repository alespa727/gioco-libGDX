package progetto.gameplay;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Queue;
import progetto.gameplay.entities.types.Enemy;
import progetto.gameplay.entities.types.entity.Entity;

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

    public static void addBody(BodyDef bodyDef, FixtureDef fixtureDef) {
        bodyToCreate.addLast(bodyDef);
        fixtureToCreate.addLast(fixtureDef);
    }

    public static void update() {
        if (bodyToCreate.notEmpty()){
            instance.createBody(bodyToCreate.removeFirst()).createFixture(fixtureToCreate.removeFirst());
        }
    }
}
