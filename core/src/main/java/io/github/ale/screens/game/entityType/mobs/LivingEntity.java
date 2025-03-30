package io.github.ale.screens.game.entityType.mobs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.entityType.entity.Entity;
import io.github.ale.screens.game.entityType.entity.EntityConfig;
import io.github.ale.screens.game.entities.skill.skillist.Skill;
import io.github.ale.screens.game.entityType.mobs.movement.EntityMovementManager;
import io.github.ale.screens.game.entities.skill.SkillSet;
import io.github.ale.screens.game.entityType.mobs.movement.EntityPathFinder;

public abstract class LivingEntity extends Entity {

    // --- ATTRIBUTI PRINCIPALI ---
    private final SkillSet skillset;
    private final float speed, maxHealth;
    private float health;
    private float speedMultiplier;
    private boolean hasBeenHit = false;

    private final EntityPathFinder entityPathFinder;
    private final EntityMovementManager movement;

    // --- COSTRUTTORE ---
    public LivingEntity(EntityConfig config, EntityManager manager) {
        super(config, manager);
        skillset = new SkillSet();
        movement = new EntityMovementManager();

        speed = config.speed;
        maxHealth = config.hp;
        health = maxHealth;
        speedMultiplier = 1f;

        this.entityPathFinder = new EntityPathFinder(this);
    }

    // --- GESTIONE SALUTE ---
    public float health() {
        return health;
    }

    public void regenHealthTo(float amount) {
        health = amount;
    }

    public void regenHealth() {
        health = maxHealth;
    }

    public void inflictDamage(float damage) {
        hasBeenHit = true;
        this.health -= damage;
    }

    public boolean hasBeenHit(){
        return hasBeenHit;
    }

    public void setHasBeenHit(boolean hasBeenHit) {
        this.hasBeenHit = hasBeenHit;
    }

    // --- GESTIONE MOVIMENTO ---
    public float speed() {
        return speed * speedMultiplier;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public EntityMovementManager movement() {
        return movement;
    }

    public void limitSpeed() {
        if (Math.abs(body.getLinearVelocity().x) > speed) {
            body.setLinearVelocity(new Vector2(speed * Math.signum(body.getLinearVelocity().x), body.getLinearVelocity().y));
        }
        if (Math.abs(body.getLinearVelocity().y) > speed) {
            body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, speed * Math.signum(body.getLinearVelocity().y)));
        }
    }

    // --- GESTIONE COMBATTIMENTO ---
    public abstract void cooldown(float delta);

    public final SkillSet skillset() {
        return skillset;
    }

    public Skill getSkill(Class<? extends Skill> skillclass) {
        return skillset.getSkill(skillclass);
    }

    // --- GESTIONE VITA & MORTE ---
    public void checkIfDead() {
        if (health <= 0) {
            this.stati().setIsAlive(false);
            despawn();
        }
    }

    public void kill() {
        if (!stati().immortality()) inflictDamage(maxHealth);
        stati().setIsAlive(false);
    }

    public void respawn() {
        stati().setIsAlive(config().isAlive);
        hasBeenHit = false;
    }

    public void despawn() {
        super.despawn();
        entityPathFinder.dispose();
    }

    // --- METODI DI AGGIORNAMENTO ---
    @Override
    public void updateEntity(float delta) {
        cooldown(delta);
        limitSpeed();
    }

    // --- METODI DI RENDERING ---
    @Override
    public void draw(SpriteBatch batch, float elapsedTime) {
        graphics().setAnimation(this);
        if (hasBeenHit) {
            batch.setColor(1, 0, 0, 0.6f);
        }
        batch.draw(graphics().getAnimazione().getKeyFrame(elapsedTime, true), getX(), getY(), config().imageWidth, config().imageHeight);
        batch.setColor(Color.WHITE);
    }

    @Override
    public void drawHitbox(ShapeRenderer renderer) {
        if (stati().inCollisione()) {
            renderer.setColor(Color.RED);
        }
        renderer.setColor(Color.BLACK);
        renderer.circle(coordinateCentro().x, coordinateCentro().y, 0.3f, 10);
    }

    public EntityPathFinder pathfinder() {
        return entityPathFinder;
    }

    public void drawPath(ShapeRenderer shapeRenderer) {
        entityPathFinder.drawPath(shapeRenderer);
    }
}
