package progetto.gameplay.entities.types;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import progetto.utils.BodyBuilder;

public class Bullet{

    private BodyDef bodyDef;
    private FixtureDef fixtureDef;
    private Body body;
    private Shape shape;


    public Bullet(float x, float y, Vector2 velocity) {
        bodyDef = BodyBuilder.createBodyDef(BodyDef.BodyType.KinematicBody, x, y);
        bodyDef.fixedRotation = true;
        shape = BodyBuilder.createCircle(0.1f);
        fixtureDef = BodyBuilder.createFixtureDef(shape, 1f, 0, 0);
        fixtureDef.isSensor = true;
        body = BodyBuilder.createBody(this, bodyDef, fixtureDef, shape);
        body.setLinearVelocity(velocity );
    }
}
