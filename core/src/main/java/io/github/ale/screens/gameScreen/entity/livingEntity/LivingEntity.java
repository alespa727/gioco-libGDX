package io.github.ale.screens.gameScreen.entity.livingEntity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.ale.screens.gameScreen.entity.EntityManager;
import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.abstractEntity.caratteristiche.Skill;
import io.github.ale.screens.gameScreen.entity.skill.SkillSet;

public abstract class LivingEntity extends Entity{
    private final SkillSet skillset;

    public LivingEntity(EntityConfig config, EntityManager manager) {
        super(config, manager);
        skillset = new SkillSet(this);
    }

    public abstract void cooldown();
    public abstract void hit(float angle, float damage);
    
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
    }

    
    public final SkillSet skillset() {
        return skillset;
    }


    public Skill getSkill(Class<? extends Skill> skillclass){
        return skillset.getSkill(skillclass);
    }


}
