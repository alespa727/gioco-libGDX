package progetto.ECS.entities.specific.living;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import progetto.ECS.EntityEngine;
import progetto.ECS.components.specific.base.StateComponent;
import progetto.ECS.components.specific.base.StatusComponent;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.ECS.components.specific.general.AttributeComponent;
import progetto.ECS.components.specific.general.skills.SkillSet;
import progetto.ECS.components.specific.graphics.CustomAnimationComponent;
import progetto.ECS.components.specific.graphics.DrawableComponent;
import progetto.ECS.components.specific.movement.MovementComponent;
import progetto.ECS.components.specific.movement.PathFinderComponent;
import progetto.ECS.entities.Entity;
import progetto.ECS.entities.specific.EntityConfig;
import progetto.ECS.entities.specific.BaseEntity;

/**
 * Classe astratta che rappresenta un'entità umanoide nel gioco.
 * Estende {@link Entity} e implementa la logica per la gestione del movimento, combattimento,
 * salute e interazione con altre entità.
 */
public abstract class Humanoid extends BaseEntity {

    /**
     * Costruttore per creare un entità umanoide utilizzando un'istanza preconfigurata.
     * Aggiunge i componenti necessari per la gestione del movimento, salute, abilità e velocità.
     *
     * @param instance L'istanza dell'umanoide da usare per la configurazione.
     * @param entityEngine   Il gestore delle entità del gioco.
     */
    public Humanoid(HumanoidInstances instance, EntityEngine entityEngine) {
        super(instance, entityEngine);

        String[] string = new String[1];
        string[0] = "default";
        Texture[] img = new Texture[1];
        img[0] = instance.config.img;

        add(
            new MovementComponent(),
            new AttributeComponent(instance.speed, instance.maxHealth),
            new PathFinderComponent(this),
            new SkillSet(),
            new StatusComponent(),
            new DrawableComponent(),
            new CustomAnimationComponent(string, img)
        );

        this.components.get(MovementComponent.class).setAwake(false);
    }

    /**
     * Costruttore per creare un'entità umanoide utilizzando una configurazione di entità.
     * Aggiunge i componenti necessari per la gestione del movimento, salute, abilità e velocità.
     *
     * @param config  La configurazione dell'umanoide (velocità, salute, ecc.).
     * @param manager Il gestore delle entità del gioco.
     */
    public Humanoid(EntityConfig config, EntityEngine manager) {
        super(config, manager);

        String[] string = new String[1];
        string[0] = "default";
        Texture[] img = new Texture[1];
        img[0] = config.img;

        add(
            new MovementComponent(),
            new AttributeComponent(config.speed, config.hp),
            new PathFinderComponent(this),
            new SkillSet(),
            new StatusComponent(),
            new DrawableComponent(),
            new CustomAnimationComponent(string, img)
        );

        this.components.get(MovementComponent.class).setAwake(false);
    }

    // METODI ASTRATTI

    @Override
    public void create() {
        if (components.get(PhysicsComponent.class).getBody() == null) {
            unregister();
        }
    }

    // --- METODI DI ACCESSO AI COMPONENTI ---

    /**
     * Restituisce il set di abilità associato all'umanoide.
     *
     * @return Il {@link SkillSet} dell'umanoide.
     */
    public final SkillSet getSkillset() {
        return components.get(SkillSet.class);
    }

    /**
     * Restituisce il componente che gestisce gli stati dell'umanoide.
     *
     * @return Il componente {@link StatusComponent} dell'umanoide.
     */
    public StatusComponent getHumanStates() {
        return components.get(StatusComponent.class);
    }

    /**
     * Restituisce il componente che gestisce le statistiche di salute dell'umanoide.
     *
     * @return Il componente {@link AttributeComponent} dell'umanoide.
     */
    public AttributeComponent getStats() {
        return components.get(AttributeComponent.class);
    }

    /**
     * Inizia a cercare il percorso verso un obiettivo (un'altra entità).
     *
     * @param target L'entità target verso cui l'umanoide deve dirigersi.
     */
    public void searchPath(Entity target) {
        getPathFinder().renderPath(target.get(PhysicsComponent.class).getPosition().x, target.get(PhysicsComponent.class).getPosition().y, entityEngine.delta);
    }

    public boolean searchPathIdle(Entity target) {
        return getPathFinder().renderPath(target.get(PhysicsComponent.class).getPosition().x, target.get(PhysicsComponent.class).getPosition().y, entityEngine.delta);
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
        getHumanStates().hasBeenHit = true;
        getStats().health = getStats().health - damage;
    }

    /**
     * Fa resuscitare l'umanoide, ripristinando la sua salute e stato.
     */
    public void respawn() {
        StateComponent state = this.components.get(StateComponent.class);
        state.setAlive(true);
        getHumanStates().hasBeenHit = false;
    }

    /**
     * Restituisce il gestore del movimento dell'umanoide.
     *
     * @return Il componente {@link MovementComponent} dell'umanoide.
     */
    public MovementComponent getMovementManager() {
        return components.get(MovementComponent.class);
    }

    /**
     * Restituisce il gestore del percorso dell'umanoide.
     *
     * @return Il componente {@link PathFinderComponent} dell'umanoide.
     */
    public PathFinderComponent getPathFinder() {
        return components.get(PathFinderComponent.class);
    }

    // --- METODI DI RENDERING ---

    /**
     * Disegna il percorso dell'umanoide.
     *
     * @param shapeRenderer Il renderer delle forme per disegnare il percorso.
     */
    public void drawPath(ShapeRenderer shapeRenderer) {
        getMovementManager().drawPath(shapeRenderer);
    }
}
