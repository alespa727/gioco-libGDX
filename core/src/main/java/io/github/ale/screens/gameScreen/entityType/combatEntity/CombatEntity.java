package io.github.ale.screens.gameScreen.entityType.combatEntity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.math.Rectangle;
import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.gameScreen.entityType.EntityManager;
import io.github.ale.screens.gameScreen.entityType.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entityType.livingEntity.LivingEntity;
import io.github.ale.screens.gameScreen.maps.Map;

public abstract class CombatEntity extends LivingEntity {

    private float dx, dy, x = 0, y = 0, angolo;
    private final Cooldown knockback = new Cooldown(0.5f);
    private final Cooldown damage = new Cooldown(.273f);
    private final Cooldown attack;
    private final Cooldown patrolling = new Cooldown(5f);
    private boolean isPatrolling = false;
    protected Rectangle range;
    private boolean isAttacking = false;

    public CombatEntity(EntityConfig config, EntityManager manager, float attackcooldown) {
        super(config, manager);
        attack = new Cooldown(attackcooldown);
        damage.reset();
        range = new Rectangle();
        attack.reset(0f);
        stati().setImmortality(false);
    }


    public abstract void attack();

    public Rectangle range() {
        return range;
    }

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
    public void hit(float angolo, float damage) {
        statistiche().inflictDamage(damage, stati().immortality());
        dx = (float) Math.cos(Math.toRadians(angolo));
        dy = (float) Math.sin(Math.toRadians(angolo));
        knockback.reset();
        this.angolo = angolo;
    }

    @Override
    public void updateEntity(float delta) {
        super.updateEntity(delta);

        // Adjust the range based on direction
        if (direzione().x > 0) {
            range.x = coordinateCentro().x + (float) Math.ceil(direzione().x) - range.width / 2;
        } else {
            range.x = coordinateCentro().x + (float) Math.floor(direzione().x) - range.width / 2;
        }

        if (direzione().y > 0) {
            range.y = coordinateCentro().y + (float) Math.ceil(direzione().y) - range.height / 2;
        } else {
            range.y = coordinateCentro().y + (float) Math.floor(direzione().y) - range.height / 2;
        }

        // Apply knockback
        knockbackUpdate(delta);
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

    protected void knockbackUpdate(float delta) {
        float knockbackSpeed = 4f;

        if (!knockback.isReady) {
            knockback.update(delta);
            dx *= 0.95f;
            dy *= 0.95f;

            // Check for collisions and apply knockback force
            if (!Map.checkCollisionX(this, 0.1f, angolo)) {
                x = dx * knockbackSpeed * delta;
                setX(getX() + x);
            }

            if (!Map.checkCollisionY(this, 0.1f, angolo)) {
                y = dy * knockbackSpeed * delta;
                setY(getY() + y);
            }
        } else {
            x = 0;
            y = 0;
        }
    }

    public void drawRange(ShapeRenderer renderer) {
        renderer.setColor(0, 0, 0, 0.5f);  // Set color for range rectangle
        renderer.rect(range.x, range.y, range.width, range.height);  // Draw range
    }
}
