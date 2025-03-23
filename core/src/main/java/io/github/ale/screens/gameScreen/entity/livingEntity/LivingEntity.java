package io.github.ale.screens.gameScreen.entity.livingEntity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import io.github.ale.screens.gameScreen.entity.EntityManager;
import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entity.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entity.abstractEntity.caratteristiche.Skill;
import io.github.ale.screens.gameScreen.entity.abstractEntity.movement.EntityMovementManager;
import io.github.ale.screens.gameScreen.entity.abstractEntity.stats.Stats;
import io.github.ale.screens.gameScreen.entity.enemy.abstractEnemy.states.States;
import io.github.ale.screens.gameScreen.entity.skill.SkillSet;
import io.github.ale.screens.gameScreen.pathfinding.Pathfinder;

public abstract class LivingEntity extends Entity{
    private final SkillSet skillset;
    protected Rectangle range;
    private Stats statistiche;
    public final Pathfinder pathfinder;
    public DefaultStateMachine<LivingEntity, States> statemachine;
    
    private final EntityMovementManager movement;

    public LivingEntity(EntityConfig config, EntityManager manager) {
        super(config, manager);
        inizializzaStatistiche(config.hp, config.speed, config.attackdmg);
        skillset = new SkillSet(this);
        movement = new EntityMovementManager();
        statemachine = new DefaultStateMachine<>(this);
        this.pathfinder = new Pathfinder(this);
    }

    
    public EntityMovementManager movement() {
        return movement;
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
        renderer.circle(coordinateCentro().x, coordinateCentro().y, 0.3f, 10);
    }

    /**
     * disegna il nemico
     * 
     * @param batch
     * @param elapsedTime
     */
    @Override
    public void draw(SpriteBatch batch, float elapsedTime) {
        elapsedTime += Gdx.graphics.getDeltaTime();

        graphics().setAnimation(this);
        if(statistiche().gotDamaged){
            batch.setColor(1, 0, 0, 0.6f);
        }
        
        batch.draw(graphics().getAnimazione().getKeyFrame(elapsedTime, true), getX(), getY(), getSize().width, getSize().height);
        
        
        batch.setColor(Color.WHITE);
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

    
    public Stats statistiche() {
        return this.statistiche;
    }

    
    public final void inizializzaStatistiche(float hp, float speed, float attackdmg) {
        this.statistiche = new Stats(hp, speed, attackdmg);
    }

    
    public final SkillSet skillset() {
        return skillset;
    }


    public Skill getSkill(Class<? extends Skill> skillclass){
        return skillset.getSkill(skillclass);
    }
    
    public Rectangle range() {
        return range;
    }

    
    public void kill() {
        statistiche().inflictDamage(statistiche().health(), stati().immortality());
        stati().setIsAlive(false);
    }
    
    public boolean checkIfDead() {
        if (statistiche().health() <= 0) {
            this.stati().setIsAlive(false);
            despawn();
        }
        return this.stati().isAlive();
    }

    public void respawn() {
        stati().setIsAlive(config().isAlive);
        this.statistiche().gotDamaged = false;
    }

    public StateMachine<LivingEntity, States> statemachine(){return statemachine;}
}
