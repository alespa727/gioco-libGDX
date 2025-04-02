package io.github.ale.screens.game.entities.types.mobs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.ale.screens.game.entities.types.entity.Entity;
import io.github.ale.screens.game.entities.types.entity.EntityConfig;
import io.github.ale.screens.game.entities.types.mobs.pathfinder.EntityPathFinder;
import io.github.ale.screens.game.entities.skills.Skill;
import io.github.ale.screens.game.entities.skills.SkillSet;
import io.github.ale.screens.game.manager.entity.EntityManager;
import io.github.ale.screens.game.manager.entity.EntityMovementManager;

public abstract class LivingEntity extends Entity {

    // --- ATTRIBUTI PRINCIPALI ---
    private final SkillSet skillset;
    private final float speed, maxHealth;
    private final EntityPathFinder entityPathFinder;
    private final EntityMovementManager movement;
    private float health;
    private float speedMultiplier;
    private boolean hasBeenHit = false;

    // --- COSTRUTTORE ---
    public LivingEntity(EntityConfig config, EntityManager manager) {
        super(config, manager);
        skillset = new SkillSet();
        movement = new EntityMovementManager(this);

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

    public boolean hasBeenHit() {
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
        if (body.getLinearVelocity().len() > speed) {
            body.applyLinearImpulse(body.getLinearVelocity().scl(-1), body.getWorldCenter(), true);
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
        skillset.update();
    }

    // --- METODI DI RENDERING ---
    @Override
    public void draw(SpriteBatch batch, float elapsedTime) {
        skillset.draw(batch, elapsedTime);
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
        renderer.circle(getPosition().x, getPosition().y, 0.3f, 10);
    }

    public EntityPathFinder pathfinder() {
        return entityPathFinder;
    }

    public void drawPath(ShapeRenderer shapeRenderer) {
        entityPathFinder.drawPath(shapeRenderer);
    }
}
