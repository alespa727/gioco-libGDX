package progetto.gameplay.entity.types.living.combat;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import progetto.gameplay.entity.types.living.Humanoid;
import progetto.gameplay.entity.types.living.HumanoidInstances;
import progetto.gameplay.entity.types.Entity;
import progetto.factories.BodyFactory;
import progetto.utils.Cooldown;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.manager.ManagerEntity;

public abstract class Warriors extends Humanoid {

    // === COSTANTI ===
    private static final float DAMAGE_COOLDOWN_TIME = 0.273f;
    private static final float KNOCKBACK_COOLDOWN_TIME = 0.22f;

    // === ATTRIBUTI DI COMBATTIMENTO ===
    private final Cooldown damage = new Cooldown(DAMAGE_COOLDOWN_TIME);
    private final Cooldown knockback = new Cooldown(KNOCKBACK_COOLDOWN_TIME);
    private Vector2 hitDirection = new Vector2();

    // === RANGE ===
    public Body directionalRange;
    public Body circularRange;

    private float rangeRadius;
    public BodyDef bodyDef = new BodyDef();
    public FixtureDef fixtureDef;
    public Shape shape;

    // === COSTRUTTORI ===

    public Warriors(HumanoidInstances instance, ManagerEntity managerEntity) {
        super(instance, managerEntity);
        initCombatState();
        createRange(1.5f);
    }

    public Warriors(EntityConfig config, ManagerEntity manager) {
        super(config, manager);
        initCombatState();
    }

    private void initCombatState() {
        hitDirection.setZero();
        knockback.reset();
        damage.reset();
    }

    // === RANGE ===

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
        fixtureDef.filter.categoryBits = ManagerEntity.RANGE;
    }

    public void adjustRange() {
        if (directionalRange != null) {
            directionalRange.setTransform(body.getPosition(), direzione().angleRad() - 60 *MathUtils.degreesToRadians);
        }
    }

    public float rangeRadius() {
        return rangeRadius;
    }

    public Body getDirectionalRange() {
        return directionalRange;
    }

    // === COMBATTIMENTO ===

    public abstract void attack();

    public void hit(Entity entity, float damage, float hitForce) {
        if (!invulnerable){
            hitDirection = new Vector2(entity.body.getPosition()).sub(body.getPosition()).nor().scl(-1*hitForce);
            inflictDamage(damage);
            knockback.reset();
        }
    }

    public void knockback(Vector2 force) {
        knockback.update(delta);
        if (!knockback.isReady) {
            body.applyLinearImpulse(force, body.getWorldCenter(), true);
        }
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

    // === AGGIORNAMENTO ===

    @Override
    public void updateEntity(float delta) {
        super.updateEntity(delta);
        adjustRange();
        knockback(hitDirection);
    }
}
