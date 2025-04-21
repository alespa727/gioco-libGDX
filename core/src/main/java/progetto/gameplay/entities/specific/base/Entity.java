package progetto.gameplay.entities.specific.base;

// Importazioni

import com.badlogic.gdx.math.Vector2;
import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.components.base.ComponentManager;
import progetto.gameplay.entities.components.specific.ai.StateComponent;
import progetto.gameplay.entities.components.specific.base.PhysicsComponent;
import progetto.gameplay.entities.components.specific.general.ConfigComponent;
import progetto.gameplay.entities.components.specific.graphics.ColorComponent;
import progetto.gameplay.entities.components.specific.graphics.ZLevelComponent;
import progetto.gameplay.entities.components.specific.movement.DirectionComponent;
import progetto.gameplay.entities.components.specific.movement.NodeComponent;
import progetto.manager.entities.Engine;

/**
 * Classe base per ogni entità del gioco (giocatori, nemici, boss, ecc.).
 * Contiene posizione, direzione, stato, fisica e animazioni.
 */
public abstract class Entity {

    /** Variabile globale per avere un id unico per ogni entità */
    private static int nextId = 0;

    /** Id identificativo dell'entità */
    public final int id;

    /** Gestore generale delle entità (dove è registrata questa entità). {@link Engine} */
    public final Engine engine;

    /** Gestore componenti */
    public ComponentManager components;

    /**
     * Crea un'entità a partire da un'istanza salvata (es. Caricata da un file).
     * @param instance l'entità salvata {@link EntityInstance}
     * @param engine il gestore delle entità {@link Engine}
     */
    public Entity(EntityInstance instance, Engine engine) {
        this.engine = engine;
        this.id = nextId++;

        this.components = new ComponentManager();
        addComponents(
            new ConfigComponent(instance.config, id),
            new ZLevelComponent(0),
            new StateComponent(),
            new PhysicsComponent(this, instance.coordinate),
            new NodeComponent(),
            new DirectionComponent(),
            new ColorComponent()
        );

        getComponent(PhysicsComponent.class).createBody();
    }

    /**
     * Crea un'entità a partire da una configurazione personalizzata.
     * @param config configurazione dell'entità {@link EntityConfig}
     * @param engine il gestore delle entità {@link Engine}
     */
    public Entity(EntityConfig config, Engine engine) {
        this.engine = engine;
        this.id = nextId++;

        this.components = new ComponentManager();
        addComponents(
            new ConfigComponent(config, id),
            new ZLevelComponent(0),
            new StateComponent(),
            new PhysicsComponent(this, config),
            new NodeComponent(),
            new DirectionComponent(),
            new ColorComponent()
        );

        getComponent(PhysicsComponent.class).createBody();
        System.out.println(config.id);
    }

    /**
     * Restituisce la configurazione dell'entità.
     * @return configurazione {@link EntityConfig}
     */
    public final EntityConfig getConfig() {
        return new EntityConfig(getComponent(ConfigComponent.class).getConfig());
    }

    /**
     * Restituisce la posizione dell'entità.
     * @return posizione corrente
     */
    public Vector2 getPosition() {
        return getComponent(PhysicsComponent.class).getPosition();
    }

    /**
     * Restituisce la direzione in cui si sta muovendo l'entità.
     * @return direzione {@link DirectionComponent}
     */
    public final Vector2 getDirection() {
        return getComponent(DirectionComponent.class).direction;
    }

    /**
     * Indica se l’entità deve essere disegnata sullo schermo.
     * @return true se va disegnata
     */
    public boolean shouldRender() {
        return getComponent(StateComponent.class).shouldRender();
    }

    /**
     * Imposta se l’entità va disegnata o no.
     * @param shouldRender true per disegnarla
     */
    public void setShouldRender(boolean shouldRender) {
        getComponent(PhysicsComponent.class).setActive(shouldRender);
        getComponent(StateComponent.class).setShouldRender(shouldRender);
    }

    /**
     * Rimuove l'entità dal mondo e restituisce un oggetto che la rappresenta.
     * @return l'entità salvabile {@link EntityInstance}
     */
    public abstract EntityInstance unregister();

    /**
     * Viene chiamato dopo la creazione per inizializzare comportamenti specifici.
     */
    public abstract void create();

    /**
     * @param componentClass classe del componente che si vuole {@link Class}
     * @return componete richiesto {@link Component}
     * @param <T> tipo di componente trovato
     */
    public <T extends Component> T getComponent(Class<T> componentClass) {
        Component component = components.get(componentClass);
        if (component == null) {
            throw new IllegalArgumentException("Component " + componentClass.getSimpleName() + " non trovato");
        }
        return componentClass.cast(component);
    }

    /**
     * Aggiunge uno o più componenti all'entità.
     * @param components array di componenti da aggiungere
     */
    public void addComponents(Component... components) {
        this.components.add(components);
    }

    @Override
    public String toString() {
        EntityConfig config = getConfig();
        return getClass().getSimpleName() + "{" +
            "posizione=" + getPosition() +
            ", direzione=" + getDirection() +
            ", isAlive=" + getComponent(StateComponent.class).isAlive() +
            ", shouldRender=" + shouldRender() +
            ", id=" + config.id +
            ", nome='" + config.nome + '\'' +
            '}';
    }
}
