package io.github.ale.screens.gameScreen.entityType.livingEntity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.ale.screens.gameScreen.entityType.EntityManager;
import io.github.ale.screens.gameScreen.entityType.abstractEntity.Entity;
import io.github.ale.screens.gameScreen.entityType.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entities.skill.skillist.Skill;
import io.github.ale.screens.gameScreen.entityType.livingEntity.movement.EntityMovementManager;
import io.github.ale.screens.gameScreen.entityType.abstractEntity.stats.Stats;
import io.github.ale.screens.gameScreen.entities.skill.SkillSet;
import io.github.ale.screens.gameScreen.pathfinding.Pathfinder;

public abstract class LivingEntity extends Entity{
    private final SkillSet skillset;

    private Stats statistiche;
    private final Pathfinder pathfinder;
    private final EntityMovementManager movement;

    public LivingEntity(EntityConfig config, EntityManager manager) {
        super(config, manager);
        inizializzaStatistiche(config.hp, config.speed, config.attackdmg);
        skillset = new SkillSet();
        movement = new EntityMovementManager();
        this.pathfinder = new Pathfinder(this);
    }

    public final void inizializzaStatistiche(float hp, float speed, float attackdmg) {
        this.statistiche = new Stats(hp, speed, attackdmg);
    }

    public abstract void cooldown(float delta);
    public abstract void hit(float angle, float damage);

    public Pathfinder pathfinder(){
        return pathfinder;
    }
    public EntityMovementManager movement() {
        return movement;
    }
    public Stats statistiche() {
        return this.statistiche;
    }
    public final SkillSet skillset() {
        return skillset;
    }
    public Skill getSkill(Class<? extends Skill> skillclass){
        return skillset.getSkill(skillclass);
    }
    public void drawPath(ShapeRenderer shapeRenderer) {
        pathfinder.drawPath(shapeRenderer);
    }

    public void kill() {
        statistiche().inflictDamage(statistiche().health(), stati().immortality());
        stati().setIsAlive(false);
    }

    public void respawn() {
        stati().setIsAlive(config().isAlive);
        this.statistiche().gotDamaged = false;
    }

    public void despawn(){
        super.despawn();
        pathfinder.dispose();
    }

    @Override
    public void updateEntity(float delta) {
        cooldown(delta);
        limiti();
    }

    public void checkIfDead() {
        if (statistiche().health() <= 0) {
            this.stati().setIsAlive(false);
            despawn();
        }
    }

    @Override
    public void draw(SpriteBatch batch, float elapsedTime) {
        graphics().setAnimation(this);
        if(statistiche().gotDamaged){
            batch.setColor(1, 0, 0, 0.6f);
        }
        batch.draw(graphics().getAnimazione().getKeyFrame(elapsedTime, true), getX(), getY(), getSize().width, getSize().height);
        batch.setColor(Color.WHITE);
    }

    /**
     * disegna l'hitbox del player
     */
    @Override
    public void drawHitbox(ShapeRenderer renderer) {
        if (stati().inCollisione()) {
            renderer.setColor(Color.RED);
        }
        renderer.rect(hitbox().x, hitbox().y, hitbox().width, hitbox().height);
        renderer.setColor(Color.BLACK);
        renderer.circle(coordinateCentro().x, coordinateCentro().y, 0.3f, 10);
    }

}
