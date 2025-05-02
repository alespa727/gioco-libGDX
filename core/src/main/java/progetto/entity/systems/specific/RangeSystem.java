package progetto.entity.systems.specific;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import progetto.entity.Engine;
import progetto.entity.components.base.ComponentFilter;
import progetto.entity.components.specific.base.StateComponent;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.combat.AttackRangeComponent;
import progetto.entity.components.specific.movement.DirectionComponent;
import progetto.entity.entities.base.Entity;
import progetto.entity.systems.base.IteratingSystem;
import progetto.factories.BodyFactory;

public class RangeSystem extends IteratingSystem {

    public RangeSystem() {
        super(ComponentFilter.all(AttackRangeComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.get(StateComponent.class).shouldBeUpdated()) return;
        if (entity.components.contains(AttackRangeComponent.class)) {
            AttackRangeComponent range = entity.components.get(AttackRangeComponent.class);
            Body body = entity.components.get(PhysicsComponent.class).getBody();
            if (body == null) {
                return;
            }
            if (range.fixtureDef == null) {
                createRange(range);
                range.directionalRange = BodyFactory.createBody(entity, range.getBodyDef(), range.getFixtureDef());
            }

            if (entity.components.get(AttackRangeComponent.class).directionalRange != null) {
                entity.components.get(AttackRangeComponent.class).directionalRange.setTransform(body.getPosition(), entity.get(DirectionComponent.class).direction.angleRad() - 60 * MathUtils.degreesToRadians);
            }
        }
    }

    public void createRange(AttackRangeComponent rangeC) {
        rangeC.rangeRadius = 1.5f;

        rangeC.bodyDef = BodyFactory.createBodyDef(BodyDef.BodyType.KinematicBody, 0, 0);

        rangeC.shape = new PolygonShape();
        Vector2[] vertices = new Vector2[5];
        vertices[0] = new Vector2(0, 0);

        for (int i = 2; i < 6; i++) {
            float angle = (float) (i / 6.0 * 100 * MathUtils.degreesToRadians);
            vertices[i - 1] = new Vector2(
                1.5f * MathUtils.cos(angle),
                1.5f * MathUtils.sin(angle)
            );
        }

        ((PolygonShape) rangeC.shape).set(vertices);

        rangeC.fixtureDef = new FixtureDef();
        rangeC.fixtureDef.shape = rangeC.shape;
        rangeC.fixtureDef.isSensor = true;
        rangeC.fixtureDef.filter.categoryBits = Engine.RANGE;
    }
}
