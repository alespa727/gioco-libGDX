package progetto.gameplay.entity.types.living;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import progetto.gameplay.entity.components.humanoid.*;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.entity.skills.Skill;
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
     * Restituisce una skill specifica dal set di abilità.
     *
     * @param skillclass La classe dell'abilità desiderata.
     * @return La skill richiesta.
     */
    public Skill getSkill(Class<? extends Skill> skillclass) {
        return getComponent(SkillSet.class).getSkill(skillclass);
    }

    public HumanStatesComponent getStates() {
        return getComponent(HumanStatesComponent.class);
    }

    /**
     * Verifica se l'umanoide è invulnerabile.
     *
     * @return true se l'umanoide è invulnerabile, false altrimenti.
     */
    public boolean isInvulnerable() {
        return getStates().isInvulnerable();
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
     * Restituisce il componente che gestisce le statistiche di salute dell'umanoide.
     *
     * @return Il componente {@link HumanStatsComponent} dell'umanoide.
     */
    public HumanStatsComponent getStats() {
        return getComponent(HumanStatsComponent.class);
    }

    /**
     * Imposta un buff di salute che moltiplica la salute massima.
     *
     * @param multiplier Il moltiplicatore per la salute massima.
     */
    public void setHealthBuff(float multiplier) {
        getStats().setHealth(getMaxHealth() * multiplier);
    }

    /**
     * Verifica se l'umanoide ha un buff di salute attivo.
     *
     * @return true se ci sono buff di salute, false altrimenti.
     */
    public boolean hasAnyHealthBuff() {
        return getStates().hasAnyBuffs();
    }

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
     * Rigenera la salute dell'umanoide a un valore specificato.
     *
     * @param amount La quantità di salute da ripristinare.
     */
    public void regenHealthTo(float amount) {
        getStats().setHealth(amount);
    }

    /**
     * Infligge danno all'umanoide, riducendo la sua salute.
     *
     * @param damage Il danno da infliggere.
     */
    public void inflictDamage(float damage) {
        getStates().setHasBeenHit(true);
        getStats().setHealth(getStats().getHealth() - damage);
    }

    /**
     * Verifica se l'umanoide è stato colpito.
     *
     * @return true se l'umanoide è stato colpito, false altrimenti.
     */
    public boolean hasBeenHit() {
        return getStates().hasBeenHit();
    }

    /**
     * Imposta lo stato di "colpito" dell'umanoide.
     *
     * @param hasBeenHit true se l'umanoide è stato colpito, false altrimenti.
     */
    public void setHasBeenHit(boolean hasBeenHit) {
        getStates().hasBeenHit();
    }

    // --- GESTIONE MOVIMENTO ---

    /**
     * Restituisce la velocità attuale dell'umanoide.
     *
     * @return La velocità dell'umanoide.
     */
    public float getSpeed() {
        return getStats().getMaxSpeed();
    }

    /**
     * Imposta un moltiplicatore per la velocità dell'umanoide.
     *
     * @param speedMultiplier Il moltiplicatore della velocità.
     */
    public void setSpeedMultiplier(float speedMultiplier) {
        getStats().setSpeedMultiplier(speedMultiplier);
    }

    /**
     * Restituisce il gestore del movimento dell'umanoide.
     *
     * @return Il componente {@link EntityMovementManager} dell'umanoide.
     */
    public EntityMovementManager movement() {
        return getComponent(EntityMovementManager.class);
    }

    // --- GESTIONE COMBATTIMENTO ---

    /**
     * Metodo astratto per la gestione dei cooldown delle abilità.
     *
     * @param delta Il delta di tempo.
     */
    public abstract void cooldown(float delta);

    // --- GESTIONE VITA & MORTE ---

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
        getStates().setHasBeenHit(false);
    }

    /**
     * Metodo astratto per gestire la scomparsa dell'umanoide dal gioco.
     *
     * @return L'istanza dell'entità che si sta rimuovendo.
     */
    public abstract EntityInstance despawn();

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

    // --- METODI DI RENDERING ---

    /**
     * Disegna l'umanoide sullo schermo utilizzando il batch.
     *
     * @param batch Il batch di disegno.
     * @param elapsedTime Il tempo trascorso dall'ultimo frame.
     */
    @Override
    public void draw(SpriteBatch batch, float elapsedTime) {
        if (getStates().hasBeenHit()) {
            batch.setColor(1, 0, 0, 0.6f);
        }
        batch.draw(getTextures().getAnimation(this).getKeyFrame(elapsedTime, true),
            getPosition().x - getConfig().imageWidth / 2,
            getPosition().y - getConfig().imageHeight / 2,
            getConfig().imageWidth, getConfig().imageHeight);
        batch.setColor(Color.WHITE);
    }

    /**
     * Restituisce il gestore del percorso dell'umanoide.
     *
     * @return Il componente {@link EntityPathFinder} dell'umanoide.
     */
    public EntityPathFinder getPathFinder() {
        return getComponent(EntityPathFinder.class);
    }

    /**
     * Disegna il percorso dell'umanoide.
     *
     * @param shapeRenderer Il renderer delle forme per disegnare il percorso.
     */
    public void drawPath(ShapeRenderer shapeRenderer) {
        getPathFinder().drawPath(shapeRenderer);
    }
}
