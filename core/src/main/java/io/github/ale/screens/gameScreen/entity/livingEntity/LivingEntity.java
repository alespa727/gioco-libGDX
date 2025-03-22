package io.github.ale.screens.gameScreen.entity.livingEntity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.gameScreen.entity.EntityManager;
import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.abstractEntity.caratteristiche.Skill;
import io.github.ale.screens.gameScreen.entity.skill.SkillSet;
import io.github.ale.screens.gameScreen.maps.Map;

public abstract class LivingEntity extends Entity{
    private final SkillSet skillset;

    private float dx, dy, x = 0, y = 0, angolo;
    private final Cooldown knockback = new Cooldown(.5f);
    private final Cooldown damage = new Cooldown(.273f);
    private final Cooldown attack = new Cooldown(1f);

    public LivingEntity(EntityConfig config, EntityManager manager) {
        super(config, manager);
        skillset = new SkillSet(this);
        damage.reset();
    }

    public abstract void cooldown();

    public void damagecooldown(){
        if(statistiche().gotDamaged){
            damage.update(Gdx.graphics.getDeltaTime());
            if(damage.isReady){
                statistiche().gotDamaged = false;
                damage.reset();
            }
        }
    }

    public SkillSet skillset() {
        return skillset;
    }


    public Skill getSkill(Class<? extends Skill> skillclass){
        return skillset.getSkill(skillclass);
    }

    
    /**
     * disegna l'hitbox del player
     * 
     * @param renderer
     */
    @Override
    public void drawHitbox(ShapeRenderer renderer) {
        if (stati().inCollisione()) {
            renderer.setColor(Color.RED);
        }
        renderer.rect(hitbox().x, hitbox().y, hitbox().width, hitbox().height);
        renderer.setColor(Color.BLACK);
        renderer.rect(range.x, range.y, range.width, range.height);
        renderer.setColor(Color.BLACK);
    }

    @Override
    public void updateEntity() {
        delta = Gdx.graphics.getDeltaTime();
        
        if(direzione().x > 0)range.x = coordinateCentro().x+ (float) Math.ceil(direzione().x)-getSize().width/2;
        else range.x = coordinateCentro().x+ (float) Math.floor(direzione().x)-getSize().width/2;
        if(direzione().y > 0) range.y = coordinateCentro().y+ (float) Math.ceil(direzione().y)-getSize().height/2;
        else range.y = coordinateCentro().y+ (float) Math.floor(direzione().y)-getSize().height/2;

        cooldown();
        limiti();
        adjustHitbox();
        knockback();
    }

    
    public void hit(float angolo, float damage) {
        statistiche().inflictDamage(damage, false);
        dx = (float) Math.cos(Math.toRadians(angolo)) * 6f;
        dy = (float) Math.sin(Math.toRadians(angolo)) * 6f;
        knockback.reset();
        this.angolo = angolo;
        knockback();
    }

    protected void knockback() {
        delta = Gdx.graphics.getDeltaTime();
        if (!knockback.isReady) {
            knockback.update(delta);
            dx *= 0.9;
            dy *= 0.9;
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


}
