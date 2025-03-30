package io.github.ale.screens.game.entityType.combat;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.entityType.entity.EntityConfig;
import io.github.ale.screens.game.entityType.mobs.LivingEntity;

public abstract class CombatEntity extends LivingEntity {
    private final Cooldown damage = new Cooldown(.273f);
    private Body range;
    private Vector2 hitDirection;
    private final Cooldown knockback;

    // Costruttore
    public CombatEntity(EntityConfig config, EntityManager manager) {
        super(config, manager);
        hitDirection = new Vector2();
        knockback = new Cooldown(0.22f);
        knockback.reset();
        damage.reset();
        stati().setImmortality(false);
        createRange();
    }

    // --- GESTIONE RANGE ---

    public void createRange() {
        BodyDef range = new BodyDef();
        range.type = BodyDef.BodyType.KinematicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = EntityManager.RANGE;

        this.range = manager.world.createBody(range);
        this.range.createFixture(fixtureDef);
        this.range.setUserData(this);
    }

    public void adjustRange() {
        range.setTransform(body.getPosition(), 0);
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
