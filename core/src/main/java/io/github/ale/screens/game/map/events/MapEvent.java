package io.github.ale.screens.game.map.events;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.ale.screens.game.entityType.entity.Entity;

public abstract class MapEvent {
    protected Vector2 position;
    protected float radius;

    public MapEvent(Vector2 position, float radius, World world) {
        this.position = position;
        this.radius = radius;
        this.createZone(world);
    }

    public void createZone(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = true;

        Shape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(this);
    }

    public abstract void trigger(Entity entity);  // Da implementare per ogni evento specifico
}
