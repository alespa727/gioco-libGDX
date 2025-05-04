package progetto.world.events.base;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import progetto.factories.BodyFactory;

public abstract class RectangleMapEvent extends MapEvent {

    protected float width;
    protected float height;

    public RectangleMapEvent(Vector2 position, float width, float height) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.createZone();
    }

    @Override
    public void createZone() {
        float width = this.width;
        float height = this.height;
        BodyDef bodyDef = BodyFactory.createBodyDef(BodyDef.BodyType.KinematicBody, getX() + width / 2, getY() + height / 2);
        Shape boxShape = BodyFactory.createPolygonShape(width / 2, height / 2);

        FixtureDef fixtureDef = BodyFactory.createFixtureDef(boxShape, 1f, 0.1f, 0.1f);
        fixtureDef.isSensor = true;

        setBody(BodyFactory.createBody(this, bodyDef, fixtureDef));
    }
}
