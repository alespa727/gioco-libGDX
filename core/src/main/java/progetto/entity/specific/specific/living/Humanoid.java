package progetto.entity.specific.specific.living;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import progetto.entity.components.specific.humanoid.*;
import progetto.skills.base.SkillSet;
import progetto.entity.specific.base.Entity;
import progetto.entity.specific.base.EntityConfig;
import progetto.entity.specific.base.EntityInstance;
import progetto.rendering.entity.EntityManager;

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
     * @param entityManager Il gestore delle entità del gioco.
     */
    public Humanoid(HumanoidInstances instance, EntityManager entityManager) {
        super(instance, entityManager);
        addComponent(new EntityMovementComponent(this));
        addComponent(new HumanStatsComponent(instance.speed, instance.maxHealth));
        addComponent(new EntityPathFinderComponent(this));
        addComponent(new SkillSet());
        addComponent(new SpeedLimiterComponent(this));
        addComponent(new HumanStatesComponent(this));
        addComponent(new HumanoidDrawerComponent(this));
        getComponent(HumanoidDrawerComponent.class).setAwake(false);
    }

    /**
     * Costruttore per creare un'entità umanoide utilizzando una configurazione di entità.
     * Aggiunge i componenti necessari per la gestione del movimento, salute, abilità e velocità.
     *
     * @param config La configurazione dell'umanoide (velocità, salute, ecc.).
     * @param manager Il gestore delle entità del gioco.
     */
    public Humanoid(EntityConfig config, EntityManager manager) {
        super(config, manager);
        addComponent(new EntityMovementComponent(this));
        getComponent(EntityMovementComponent.class).setAwake(false);
        addComponent(new HumanStatsComponent(config.speed, config.hp));
        addComponent(new EntityPathFinderComponent(this));
        addComponent(new SkillSet());
        addComponent(new SpeedLimiterComponent(this));
        addComponent(new HumanStatesComponent(this));
        addComponent(new HumanoidDrawerComponent(this));
        getComponent(HumanoidDrawerComponent.class).setAwake(false);
    }

    // METODI ASTRATTI

    @Override
    public void create() {
        if (getPhysics().getBody() == null) {
            despawn();
        }
    }

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

    public boolean searchPathIdle(Entity target) {
        return getPathFinder().render(target.getPosition().x, target.getPosition().y, manager.delta);
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
     * @return Il componente {@link EntityMovementComponent} dell'umanoide.
     */
    public EntityMovementComponent getMovementManager() {
        return getComponent(EntityMovementComponent.class);
    }

    /**
     * Restituisce il gestore del percorso dell'umanoide.
     *
     * @return Il componente {@link EntityPathFinderComponent} dell'umanoide.
     */
    public EntityPathFinderComponent getPathFinder() {
        return getComponent(EntityPathFinderComponent.class);
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
        if (getState().isAlive()){
            getComponent(HumanoidDrawerComponent.class).draw(batch, elapsedTime);
            getComponent(HumanoidDrawerComponent.class).update();
        } else {
            getComponent(DespawnComponent.class).draw(batch, getTextures().play(this, "default", elapsedTime),
                getPosition().x - getConfig().imageWidth / 2,
                getPosition().y - getConfig().imageHeight / 2,
                getConfig().imageWidth, getConfig().imageHeight);
        }

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
