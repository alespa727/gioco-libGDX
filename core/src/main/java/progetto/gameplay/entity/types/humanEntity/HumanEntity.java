package progetto.gameplay.entity.types.humanEntity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import progetto.gameplay.entity.types.abstractEntity.Entity;
import progetto.gameplay.entity.types.abstractEntity.EntityConfig;
import progetto.gameplay.entity.types.abstractEntity.EntityInstance;
import progetto.gameplay.manager.entity.movement.EntityPathFinder;
import progetto.gameplay.entity.skills.Skill;
import progetto.gameplay.entity.skills.SkillSet;
import progetto.gameplay.manager.entity.EntityManager;
import progetto.gameplay.manager.entity.movement.EntityMovementManager;

public abstract class HumanEntity extends Entity {

    // --- ATTRIBUTI PRINCIPALI ---
    private final SkillSet skillset;
    private final float speed, maxHealth;
    private final EntityPathFinder entityPathFinder;
    private final EntityMovementManager movement;
    private float health;
    private float speedMultiplier;
    private boolean hasBeenHit = false;


    public HumanEntity(HumanInstance instance, EntityManager entityManager) {
        super(instance, entityManager);
        skillset = new SkillSet();
        movement = new EntityMovementManager(this);

        speed = instance.speed;
        maxHealth = instance.maxHealth;
        health = maxHealth;
        speedMultiplier = 1f;

        this.entityPathFinder = new EntityPathFinder(this);
    }

    // --- COSTRUTTORE ---
    public HumanEntity(EntityConfig config, EntityManager manager) {
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
    public float getHealth() {
        return health;
    }

    public float getMaxHealth(){
        return maxHealth;
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
    public float getSpeed() {
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

    public final SkillSet getSkillset() {
        return skillset;
    }

    public Skill getSkill(Class<? extends Skill> skillclass) {
        return skillset.getSkill(skillclass);
    }

    // --- GESTIONE VITA & MORTE ---
    public void checkIfDead() {
        if (health <= 0) {
            setDead();
            despawn();
        }
    }

    public void kill() {
        if (!isInvulnerable()){
            inflictDamage(maxHealth);
            setDead();
        }
    }

    public void respawn() {
        setAlive();
        hasBeenHit = false;
    }

    public abstract EntityInstance despawn();

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
        if (hasBeenHit) {
            batch.setColor(1, 0, 0, 0.6f);
        }
        batch.draw(getTextures().getAnimation(this).getKeyFrame(elapsedTime, true), getX(), getY(), config().imageWidth, config().imageHeight);
        batch.setColor(Color.WHITE);
    }

    public EntityPathFinder pathfinder() {
        return entityPathFinder;
    }

    public void drawPath(ShapeRenderer shapeRenderer) {
        entityPathFinder.drawPath(shapeRenderer);
    }
}
