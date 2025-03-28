package io.github.ale.screens.game.entityType.combatEntity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.entityType.abstractEntity.EntityConfig;
import io.github.ale.screens.game.entityType.livingEntity.LivingEntity;
import io.github.ale.screens.game.maps.Map;

public abstract class CombatEntity extends LivingEntity {
    private final Cooldown knockback = new Cooldown(0.5f);
    private final Cooldown damage = new Cooldown(.273f);
    private final Cooldown attack;
    private final Cooldown patrolling = new Cooldown(5f);
    private boolean isPatrolling = false;
    private Body range;
    private boolean isAttacking = false;

    private final short RANGE = 0x0010;

    public CombatEntity(EntityConfig config, EntityManager manager, float attackcooldown) {
        super(config, manager);
        attack = new Cooldown(attackcooldown);
        damage.reset();
        attack.reset(0f);
        stati().setImmortality(false);
        createRange(1f, 1f);
    }

    public void createRange(float width, float height){
        BodyDef range = new BodyDef();
        range.type = BodyDef.BodyType.KinematicBody;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height, new Vector2(0, 0), 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        this.range = manager.world.createBody(range);
        this.range.createFixture(fixtureDef);
        this.range.setUserData("range");
    }

    public void adjustRange(){
        range.setTransform(body.getPosition(), 0);
    }

    public void despawn(){
        super.despawn();
        manager.world.destroyBody(range);
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

    @Override
    public void hit(Vector2 origine, float damage) {
        Vector2 attackDirection = new Vector2(origine.x - coordinateCentro().x, origine.y - coordinateCentro().y);
        body.applyForceToCenter(attackDirection.scl(4f), true);
        statistiche().inflictDamage(damage, false);
        knockback.reset();
    }

    public void knockback(float x, float y){
    }

    @Override
    public void updateEntity(float delta) {
        super.updateEntity(delta);
        // Adjust the range based on direction
        adjustRange();

    }

    public void attackCooldown(float delta) {
        attack.update(delta);
        if (attack.isReady) {
            if (isAttacking) {
                attack();
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
