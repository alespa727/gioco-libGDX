package progetto.gameplay.entity.components.warrior;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import progetto.factories.BodyFactory;
import progetto.gameplay.entity.components.IteratableComponent;
import progetto.gameplay.entity.types.living.combat.Warrior;
import progetto.gameplay.manager.entity.EntityManager;

public class DirectionalRangeComponent extends IteratableComponent {

    public final Warrior owner;

    public Body directionalRange;
    public BodyDef bodyDef = new BodyDef();
    public FixtureDef fixtureDef;
    public Shape shape;

    private float rangeRadius;

    public DirectionalRangeComponent(Warrior owner) {
        this.owner = owner;
        createRange(1.5f);
    }

    @Override
    public void update(float delta) {
        Body body = owner.getPhysics().getBody();
        if (body == null) {
            return;
        }
        if (directionalRange != null) {
            directionalRange.setTransform(body.getPosition(), owner.getDirection().angleRad() - 60 *MathUtils.degreesToRadians);
        }
    }

    public void setDirectionalRange(Body directionalRange) {
        this.directionalRange = directionalRange;
    }


    public void createRange(float radius) {
        this.rangeRadius = radius;

        bodyDef = BodyFactory.createBodyDef(BodyDef.BodyType.KinematicBody, 0, 0);

        shape = new PolygonShape();
        Vector2[] vertices = new Vector2[5];
        vertices[0] = new Vector2(0, 0);

        for (int i = 2; i < 6; i++) {
            float angle = (float) (i / 6.0 * 100 * MathUtils.degreesToRadians);
            vertices[i - 1] = new Vector2(
                radius * MathUtils.cos(angle),
                radius * MathUtils.sin(angle)
            );
        }

        ((PolygonShape) shape).set(vertices);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = EntityManager.RANGE;
    }

    public Body getDirectionalRange() {
        return directionalRange;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    public Shape getShape() {
        return shape;
    }

    public float getRangeRadius() {
        return rangeRadius;
    }
}
