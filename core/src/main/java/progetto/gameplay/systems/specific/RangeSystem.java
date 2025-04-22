package progetto.gameplay.systems.specific;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import progetto.factories.BodyFactory;
import progetto.gameplay.entities.components.base.ComponentManager;
import progetto.gameplay.entities.components.specific.base.PhysicsComponent;
import progetto.gameplay.entities.components.specific.combat.AttackRangeComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.systems.base.System;
import progetto.manager.entities.Engine;

public class RangeSystem extends System {
    @Override
    public void update(float delta, Array<Entity> list) {
        for (Entity e : list) {
            if (!e.shouldRender()) continue;
            ComponentManager componentManager = e.components;
            if (e.components.contains(AttackRangeComponent.class)) {
                AttackRangeComponent range = e.components.get(AttackRangeComponent.class);
                Body body = e.components.get(PhysicsComponent.class).getBody();
                if (body == null) {
                    return;
                }
                if (range.fixtureDef == null) {
                    createRange(range);
                    range.directionalRange = BodyFactory.createBody(e, range.getBodyDef(), range.getFixtureDef());
                }

                if (e.components.get(AttackRangeComponent.class).directionalRange != null) {
                    e.components.get(AttackRangeComponent.class).directionalRange.setTransform(body.getPosition(), e.getDirection().angleRad() - 60 * MathUtils.degreesToRadians);
                }


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
