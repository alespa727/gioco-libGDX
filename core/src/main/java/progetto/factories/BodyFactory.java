package progetto.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import progetto.gameplay.manager.ManagerWorld;
import progetto.utils.TerminalCommand;

public class BodyFactory {
    public static BodyDef createBodyDef(BodyDef.BodyType type, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(x, y);
        return bodyDef;
    }
    public static Shape createCircle(float radius) {
        Shape shape = new CircleShape();
        shape.setRadius(radius);
        return shape;
    }

    public static Shape createPolygonShape(float width, float height) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        return shape;
    }

    public static Shape createChainShape(Vector2[] points) {
        ChainShape shape = new ChainShape();
        shape.createChain(points);
        return shape;
    }

    public static FixtureDef createFixtureDef(Shape shape, float density, float friction, float restitution) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        return fixtureDef;
    }

    public static Body createBody(Object userdata, BodyDef bodyDef, FixtureDef fixtureDef) {
        TerminalCommand.printError("Creando una body per: " + userdata.getClass().getSimpleName() + bodyDef + fixtureDef);
        Shape shape = fixtureDef.shape;
        Body body = ManagerWorld.getInstance().createBody(bodyDef);
        body.setTransform(bodyDef.position.x, bodyDef.position.y, 0f);
        body.createFixture(fixtureDef);
        body.setUserData(userdata);
        fixtureDef.shape = null;
        shape.dispose();
        return body;
    }
}
