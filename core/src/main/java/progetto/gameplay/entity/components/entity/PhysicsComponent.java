package progetto.gameplay.entity.components.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import progetto.factories.BodyFactory;
import progetto.gameplay.entity.components.Component;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.manager.ManagerWorld;
import progetto.utils.TerminalCommand;

public class PhysicsComponent extends Component {
    protected Entity owner;

    private final Vector2 position;

    public Body body;
    public BodyDef bodyDef;
    public FixtureDef fixtureDef;
    public Shape shape;

    public PhysicsComponent(Entity owner) {
        this.owner = owner;
        this.position = new Vector2(owner.getConfig().x, owner.getConfig().y);
    }

    public PhysicsComponent(Entity owner, Vector2 position) {
        this.owner = owner;
        this.position = position;
    }

    // === Corpo e fisica ===

    public void createBody() {
        bodyDef = BodyFactory.createBodyDef(BodyDef.BodyType.DynamicBody, position.x, position.y);
        shape = BodyFactory.createCircle(owner.getConfig().radius);
        fixtureDef = BodyFactory.createFixtureDef(shape, 25f, .8f, .1f);
    }

    public void initBody() {
        body = BodyFactory.createBody(owner, this.bodyDef, this.fixtureDef);
        body.setAngularDamping(5f);
        body.setLinearDamping(5f);
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Vector2 getPosition() {
        if (body != null) {
            return body.getPosition();
        }
        return position;
    }

    public Vector2 getVelocity() {
        return body.getLinearVelocity();
    }

    public void teleport(Vector2 pos) {
        body.setTransform(pos, body.getAngle());
    }

    public void setActive(boolean active) {
        if (body != null) {
            body.setActive(active);
        }
    }

}
