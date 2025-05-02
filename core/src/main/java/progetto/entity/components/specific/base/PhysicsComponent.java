package progetto.entity.components.specific.base;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import progetto.entity.components.base.Component;
import progetto.entity.components.specific.general.ConfigComponent;
import progetto.entity.entities.Entity;
import progetto.entity.entities.specific.EntityConfig;
import progetto.factories.BodyFactory;

public class PhysicsComponent extends Component {
    protected final Entity owner;

    private final Vector2 position;

    public Body body;
    public BodyDef bodyDef;
    public FixtureDef fixtureDef;
    public Shape shape;

    public PhysicsComponent(Entity owner, EntityConfig config) {
        this.owner = owner;
        this.position = new Vector2(config.x, config.y);
    }

    public PhysicsComponent(Entity owner, Vector2 position) {
        this.owner = owner;
        this.position = position;
    }

    public PhysicsComponent() {
        this.owner = null;
        this.position = null;
    }

    // === Corpo e fisica ===

    public void createBody() {
        bodyDef = BodyFactory.createBodyDef(BodyDef.BodyType.DynamicBody, position.x, position.y);
        shape = BodyFactory.createCircle(owner.get(ConfigComponent.class).getConfig().radius);
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

    /**
     * Sposta istantaneamente l'entità in una nuova posizione.
     *
     * @param pos nuova posizione
     */
    public void teleport(Vector2 pos) {
        if(body != null) {
            body.setTransform(pos, body.getAngle());
        }
    }

    public void setActive(boolean active) {
        if (body != null) {
            body.setActive(active);
        }
    }

}
