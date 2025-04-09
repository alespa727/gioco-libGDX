package progetto.gameplay.entity.types.living;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.entity.behaviors.EntityPathFinder;
import progetto.gameplay.entity.skills.Skill;
import progetto.gameplay.entity.skills.SkillSet;
import progetto.gameplay.manager.ManagerEntity;
import progetto.gameplay.entity.behaviors.manager.entity.movement.EntityMovementManager;

public abstract class Humanoid extends Entity {

    // --- ATTRIBUTI PRINCIPALI ---
    private final SkillSet skillset;
    private final float speed, maxHealth;
    private final EntityPathFinder entityPathFinder;
    private final EntityMovementManager movement;
    private float health;
    private float speedMultiplier;
    private boolean hasBeenHit = false;
    private boolean hasAnyBuff = false;

    protected boolean invulnerable = false;


    public Humanoid(HumanoidInstances instance, ManagerEntity managerEntity) {
        super(instance, managerEntity);
        skillset = new SkillSet();
        movement = new EntityMovementManager(this);

        speed = instance.speed;
        maxHealth = instance.maxHealth;
        health = maxHealth;
        speedMultiplier = 1f;

        this.entityPathFinder = new EntityPathFinder(this);
    }

    // --- COSTRUTTORE ---
    public Humanoid(EntityConfig config, ManagerEntity manager) {
        super(config, manager);
        skillset = new SkillSet();
        movement = new EntityMovementManager(this);

        speed = config.speed;
        maxHealth = config.hp;
        health = maxHealth;
        speedMultiplier = 1f;

        this.entityPathFinder = new EntityPathFinder(this);
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void move(){
        movement.update();
    }

    public void searchPath(Entity target){
        pathfinder().renderPath(target.getPosition().x, target.getPosition().y, delta);
    }

    // --- GESTIONE SALUTE ---
    public void checkIfHaveBuff(){
        hasAnyBuff = health < maxHealth;
    }

    public void setHealthBuff(float multiplier) {
        this.health = maxHealth*multiplier;
    }

    public boolean hasAnyHealthBuff(){
        return hasAnyBuff;
    }

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
        if (!invulnerable){
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
        checkIfHaveBuff();
    }

    // --- METODI DI RENDERING ---
    @Override
    public void draw(SpriteBatch batch, float elapsedTime) {
        if (hasBeenHit) {
            batch.setColor(1, 0, 0, 0.6f);
        }
        batch.draw(getTextures().getAnimation(this).getKeyFrame(elapsedTime, true), getPosition().x - config().imageWidth/2, getPosition().y - config().imageHeight/2, config().imageWidth, config().imageHeight);
        batch.setColor(Color.WHITE);
    }

    public EntityPathFinder pathfinder() {
        return entityPathFinder;
    }

    public void drawPath(ShapeRenderer shapeRenderer) {
        entityPathFinder.drawPath(shapeRenderer);
    }
}
