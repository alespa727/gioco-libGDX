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
    private final Cooldown attack;
    private final Cooldown patrolling = new Cooldown(5f);
    private boolean isPatrolling = false;
    private Body range;
    private boolean isAttacking = false;

    private Vector2 hitDirection;

    private Cooldown knockback;

    public CombatEntity(EntityConfig config, EntityManager manager, float attackcooldown) {
        super(config, manager);
        hitDirection = new Vector2();
        attack = new Cooldown(attackcooldown);
        knockback = new Cooldown(0.1f);
        knockback.reset();
        damage.reset();
        attack.reset(0f);
        stati().setImmortality(false);
        createRange();
    }

    public void createRange(){
        BodyDef range = new BodyDef();
        range.type = BodyDef.BodyType.KinematicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        fixtureDef.filter.categoryBits = EntityManager.RANGE;

        this.range = manager.world.createBody(range);
        this.range.createFixture(fixtureDef);
        this.range.setUserData(this);
    }

    public void adjustRange(){
        range.setTransform(body.getPosition(), 0);
    }

    public void despawn(){
        super.despawn();
        manager.world.destroyBody(range);
    }

    public float rangeRadius(){
        return range.getFixtureList().get(0).getShape().getRadius();
    }
    public abstract void attack();

    public boolean isPatrolling() {
        return isPatrolling;
    }

    public Cooldown getAttackCooldown() {
        return attack;
    }

    public void setIsAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    public void hit(CombatEntity entity, float damage) {
        hitDirection = new Vector2(entity.body.getPosition()).sub(body.getPosition()).scl(-1);
        statistiche().inflictDamage(damage, false);
        knockback.reset();
    }

    public void knockback(Vector2 force){
        knockback.update(delta);
        if (!knockback.isReady)
            body.applyLinearImpulse(force.scl(body.getMass()), body.getWorldCenter(), true);
    }


    @Override
    public void updateEntity(float delta) {
        super.updateEntity(delta);
        // Adjust the range based on direction
        adjustRange();

        knockback(hitDirection.nor());
    }

    public void attackCooldown() {
        attack.update(delta);
        if (attack.isReady) {
            if (isAttacking) {
                System.out.println("Attacking");
                attack();
                attack.reset();
            }
        }
    }

    public void patrollingCooldown(float delta) {
        patrolling.update(delta);
        if (patrolling.isReady) {
            patrolling.reset();
            isPatrolling=true;
        }else{
            isPatrolling=false;
        }

    }

    public void damageCooldown(float delta) {
        if (statistiche().gotDamaged) {
            damage.update(delta);
            body.setLinearDamping(0f);
            if (damage.isReady) {
                statistiche().gotDamaged = false;
                damage.reset();
            }
        }
    }

    public void drawRange(ShapeRenderer renderer) {
        renderer.setColor(0, 0, 0, 0.5f);  // Set color for range rectangle
    }
}
