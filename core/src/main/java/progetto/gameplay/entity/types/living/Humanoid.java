package progetto.gameplay.entity.types.living;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import progetto.gameplay.entity.components.humanoid.*;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.entity.skills.SkillSet;
import progetto.gameplay.manager.entity.ManagerEntity;

/**
 * Classe astratta che rappresenta un'entità umanoide nel gioco.
 * Estende {@link Entity} e implementa la logica per la gestione del movimento, combattimento,
 * salute e interazione con altre entità.
 */
public abstract class Humanoid extends Entity {

    /**
     * Costruttore per creare un entità umanoide utilizzando un'istanza preconfigurata.
     * Aggiunge i componenti necessari per la gestione del movimento, salute, abilità e velocità.
     *
     * @param instance L'istanza dell'umanoide da usare per la configurazione.
     * @param managerEntity Il gestore delle entità del gioco.
     */
    public Humanoid(HumanoidInstances instance, ManagerEntity managerEntity) {
        super(instance, managerEntity);
        addComponent(new EntityMovementManager(this));
        addComponent(new HumanStatsComponent(instance.speed, instance.maxHealth));
        addComponent(new EntityPathFinder(this));
        addComponent(new SkillSet());
        addComponent(new SpeedLimiter(this));
        addComponent(new HumanStatesComponent(this));
    }

    /**
     * Costruttore per creare un'entità umanoide utilizzando una configurazione di entità.
     * Aggiunge i componenti necessari per la gestione del movimento, salute, abilità e velocità.
     *
     * @param config La configurazione dell'umanoide (velocità, salute, ecc.).
     * @param manager Il gestore delle entità del gioco.
     */
    public Humanoid(EntityConfig config, ManagerEntity manager) {
        super(config, manager);
        addComponent(new EntityMovementManager(this));
        getComponent(EntityMovementManager.class).setAwake(false);
        addComponent(new HumanStatsComponent(config.speed, config.hp));
        addComponent(new EntityPathFinder(this));
        addComponent(new SkillSet());
        addComponent(new SpeedLimiter(this));
        addComponent(new HumanStatesComponent(this));
    }

    // METODI ASTRATTI

    /**
     * Metodo astratto per la gestione dei cooldown delle abilità.
     *
     * @param delta Il delta di tempo.
     */
    public abstract void cooldown(float delta);

    /**
     * Metodo astratto per gestire la scomparsa dell'umanoide dal gioco.
     *
     * @return L'istanza dell'entità che si sta rimuovendo.
     */
    public abstract EntityInstance despawn();


    // --- METODI DI ACCESSO AI COMPONENTI ---

    /**
     * Restituisce il set di abilità associato all'umanoide.
     *
     * @return Il {@link SkillSet} dell'umanoide.
     */
    public final SkillSet getSkillset() {
        return getComponent(SkillSet.class);
    }

    /**
     * Restituisce il componente che gestisce gli stati dell'umanoide.
     *
     * @return Il componente {@link HumanStatesComponent} dell'umanoide.
     */
    public HumanStatesComponent getHumanStates() {
        return getComponent(HumanStatesComponent.class);
    }

    /**
     * Restituisce il componente che gestisce le statistiche di salute dell'umanoide.
     *
     * @return Il componente {@link HumanStatsComponent} dell'umanoide.
     */
    public HumanStatsComponent getStats() {
        return getComponent(HumanStatsComponent.class);
    }

    /**
     * Inizia a cercare il percorso verso un obiettivo (un'altra entità).
     *
     * @param target L'entità target verso cui l'umanoide deve dirigersi.
     */
    public void searchPath(Entity target) {
        getPathFinder().renderPath(target.getPosition().x, target.getPosition().y, manager.delta);
    }

    // --- GESTIONE SALUTE ---

    /**
     * Restituisce la salute attuale dell'umanoide.
     *
     * @return La salute dell'umanoide.
     */
    public float getHealth() {
        return getStats().getHealth();
    }

    /**
     * Restituisce la salute massima dell'umanoide.
     *
     * @return La salute massima dell'umanoide.
     */
    public float getMaxHealth() {
        return getStats().getMaxHealth();
    }


    /**
     * Restituisce la velocità attuale dell'umanoide.
     *
     * @return La velocità dell'umanoide.
     */
    public float getSpeed() {
        return getPhysics().getBody().getLinearVelocity().len();
    }

    /**
     * Restituisce la velocità massima dell'umanoide.
     *
     * @return La velocità dell'umanoide.
     */
    public float getMaxSpeed() {
        return getStats().getMaxSpeed();
    }


    /**
     * Infligge danno all'umanoide, riducendo la sua salute.
     *
     * @param damage Il danno da infliggere.
     */
    public void inflictDamage(float damage) {
        getHumanStates().setHasBeenHit(true);
        getStats().setHealth(getStats().getHealth() - damage);
    }

    /**
     * Uccide l'umanoide, infliggendo danno pari alla sua salute massima.
     */
    public void kill() {
        inflictDamage(getMaxHealth());
        setDead();
    }

    /**
     * Fa resuscitare l'umanoide, ripristinando la sua salute e stato.
     */
    public void respawn() {
        setAlive();
        getHumanStates().setHasBeenHit(false);
    }

    /**
     * Restituisce il gestore del movimento dell'umanoide.
     *
     * @return Il componente {@link EntityMovementManager} dell'umanoide.
     */
    public EntityMovementManager getMovementManager() {
        return getComponent(EntityMovementManager.class);
    }

    /**
     * Restituisce il gestore del percorso dell'umanoide.
     *
     * @return Il componente {@link EntityPathFinder} dell'umanoide.
     */
    public EntityPathFinder getPathFinder() {
        return getComponent(EntityPathFinder.class);
    }

    // --- METODI DI RENDERING ---

    /**
     * Disegna l'umanoide sullo schermo utilizzando il batch.
     *
     * @param batch Il batch di disegno.
     * @param elapsedTime Il tempo trascorso dall'ultimo frame.
     */
    @Override
    public void draw(SpriteBatch batch, float elapsedTime) {
        if (getHumanStates().hasBeenHit()) {
            batch.setColor(1, 0, 0, 0.6f);
        }
        batch.draw(getTextures().getAnimation(this).getKeyFrame(elapsedTime, true),
            getPosition().x - getConfig().imageWidth / 2,
            getPosition().y - getConfig().imageHeight / 2,
            getConfig().imageWidth, getConfig().imageHeight);
        batch.setColor(Color.WHITE);
    }


    // --- METODI DI AGGIORNAMENTO ---

    /**
     * Metodo chiamato per aggiornare lo stato dell'umanoide.
     * Viene eseguito ogni frame.
     *
     * @param delta Il delta di tempo.
     */
    @Override
    public void updateEntity(float delta) {
        cooldown(delta);
    }

    // --- METODI DI DEBUG ---

    /**
     * Disegna il percorso dell'umanoide.
     *
     * @param shapeRenderer Il renderer delle forme per disegnare il percorso.
     */
    public void drawPath(ShapeRenderer shapeRenderer) {
        getPathFinder().drawPath(shapeRenderer);
    }
}
