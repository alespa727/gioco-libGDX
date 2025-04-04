package progetto.gameplay.entities.types;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import progetto.gameplay.WorldManager;
import progetto.utils.Cooldown;
import progetto.gameplay.entities.types.entity.EntityConfig;
import progetto.gameplay.manager.entity.EntityManager;

public abstract class CombatEntity extends LivingEntity {
    private final Cooldown damage = new Cooldown(.273f);
    private final Cooldown knockback;
    private Body range;
    private Vector2 hitDirection;

    private float rangeRadius;

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
        this.rangeRadius = radius;
        BodyDef range = new BodyDef();
        range.type = BodyDef.BodyType.KinematicBody;

        PolygonShape shape = new PolygonShape();

        Vector2[] vertices = new Vector2[5];
        vertices[0] = new Vector2(0,0);
        for (int i = 2; i < 6; i++) {
            float angle = (float) (i  / 6.0 * 145 * MathUtils.degreesToRadians); // convert degrees to radians
            vertices[i-1] = new Vector2( radius * ((float)Math.cos(angle)), radius * ((float)Math.sin(angle)));
        }
        shape.set(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = EntityManager.RANGE;

        this.range = WorldManager.getInstance().createBody(range);
        this.range.createFixture(fixtureDef);
        this.range.setUserData(this);
    }

    public void adjustRange() {
        range.setTransform(body.getPosition(), direzione().angleRad()-90*MathUtils.degreesToRadians);
    }

    public float rangeRadius() {
        return rangeRadius;
    }

    public Body getRange() {
        return range;
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
        WorldManager.getInstance().destroyBody(range);
    }
}
