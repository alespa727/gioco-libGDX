package io.github.ale.screens.gameplay.entities.types.combat;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import io.github.ale.utils.Cooldown;
import io.github.ale.screens.gameplay.entities.types.entity.EntityConfig;
import io.github.ale.screens.gameplay.entities.types.mobs.LivingEntity;
import io.github.ale.screens.gameplay.manager.entity.EntityManager;

public abstract class CombatEntity extends LivingEntity {
    private final Cooldown damage = new Cooldown(.273f);
    private final Cooldown knockback;
    private Body range;
    private Vector2 hitDirection;

    // Costruttore
    public CombatEntity(EntityConfig config, EntityManager manager) {
        super(config, manager);
        hitDirection = new Vector2();
        knockback = new Cooldown(0.22f);
        knockback.reset();
        damage.reset();
        stati().setImmortality(false);
    }

    // --- GESTIONE RANGE ---

    public void createRange(float radius) {
        BodyDef range = new BodyDef();
        range.type = BodyDef.BodyType.KinematicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = EntityManager.RANGE;

        this.range = manager.world.createBody(range);
        this.range.createFixture(fixtureDef);
        this.range.setUserData(this);
        this.range.setFixedRotation(true);
    }

    public void adjustRange() {
        range.setTransform(body.getPosition(), body.getLinearVelocity().angleRad());
    }

    public float rangeRadius() {
        return range.getFixtureList().get(0).getShape().getRadius();
    }

    // --- GESTIONE COMBATTIMENTO ---

    public abstract void attack();

    public void hit(CombatEntity entity, float damage) {
        hitDirection = new Vector2(entity.body.getPosition()).sub(body.getPosition()).scl(-1);
        inflictDamage(damage);
        knockback.reset();
    }

    public void knockback(Vector2 force) {
        knockback.update(delta);
        if (!knockback.isReady)
            body.applyLinearImpulse(force.scl(body.getMass()), body.getWorldCenter(), true);
    }

    public void damageCooldown(float delta) {
        if (hasBeenHit()) {
            damage.update(delta);
            if (damage.isReady) {
                setHasBeenHit(false);
                damage.reset();
            }
        }
    }

    // --- GESTIONE MOVIMENTO & COMPORTAMENTO ---

    @Override
    public void updateEntity(float delta) {
        super.updateEntity(delta);
        adjustRange();
        knockback(hitDirection.nor());
    }

    // --- GESTIONE DISTRUZIONE ---

    public void despawn() {
        super.despawn();
        manager.world.destroyBody(range);
    }
}
