package io.github.ale.screens.gameScreen.entityType.combatEntity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.math.Rectangle;
import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.gameScreen.entityType.EntityManager;
import io.github.ale.screens.gameScreen.entityType.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entityType.livingEntity.LivingEntity;
import io.github.ale.screens.gameScreen.maps.Map;

public abstract class CombatEntity extends LivingEntity{

    private float dx, dy, x = 0, y = 0, angolo;
    private final Cooldown knockback = new Cooldown(1f);
    private final Cooldown damage = new Cooldown(.273f);
    private final Cooldown attack;
    protected Rectangle range;
    private boolean isAttacking = false;


    public CombatEntity(EntityConfig config, EntityManager manager, float attackcooldown) {
        super(config, manager);
        attack = new Cooldown(attackcooldown);
        damage.reset();
        range = new Rectangle();
        attack.reset(0f);
    }

    @Override
    public void updateEntity() {
        delta = Gdx.graphics.getDeltaTime();

        if(direzione().x > 0)range.x = coordinateCentro().x+ (float) Math.ceil(direzione().x)-range.width/2;
        else range.x = coordinateCentro().x+ (float) Math.floor(direzione().x)-range.width/2;
        if(direzione().y > 0) range.y = coordinateCentro().y+ (float) Math.ceil(direzione().y)-range.height/2;
        else range.y = coordinateCentro().y+ (float) Math.floor(direzione().y)-range.height/2;

        cooldown();
        limiti();
        adjustHitbox();
        knockback();
    }

    public abstract void attack();

    public void attackcooldown(){
        attack.update(delta);
        if(attack.isReady){
            if (isAttacking) {
                System.err.println("ATTACCO!");
                attack();
                attack.reset();
            }
        }
    }

    public Rectangle range(){
        return range;
    }

    public Cooldown getAttackCooldown(){
        return attack;
    }

    public void damagecooldown(){
        if(statistiche().gotDamaged){
            damage.update(Gdx.graphics.getDeltaTime());
            if(damage.isReady){
                statistiche().gotDamaged = false;
                damage.reset();
            }
        }
    }

    @Override
    public void hit(float angolo, float damage) {
        statistiche().inflictDamage(damage, false);
        dx = (float) Math.cos(Math.toRadians(angolo)) * 5f;
        dy = (float) Math.sin(Math.toRadians(angolo)) * 5f;
        knockback.reset();
        this.angolo = angolo;
        knockback();
    }

    protected void knockback() {
        delta = Gdx.graphics.getDeltaTime();
        if (!knockback.isReady) {
            knockback.update(delta);
            dx *= 0.975f;
            dy *= 0.975f;
            if (!Map.checkCollisionX(this, 0.1f, angolo)) {
                x = dx * delta;
                setX(getX() + x);
            }
            if (!Map.checkCollisionY(this, 0.1f, angolo)) {
                y = dy * delta;
                setY(getY() + y);
            }
        } else {
            x = 0;
            y = 0;
        }
    }

    public void setIsAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    public void drawRange(ShapeRenderer renderer) {
        renderer.setColor(0, 0, 0, 0.5f);
        renderer.rect(range.x, range.y, range.width, range.height);
        renderer.setColor(1, 1, 1, 1);
    }

}
