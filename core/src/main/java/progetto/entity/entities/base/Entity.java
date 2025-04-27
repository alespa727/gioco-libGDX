package progetto.entity.entities.base;

// Importazioni

import com.badlogic.gdx.utils.Array;
import progetto.entity.Engine;
import progetto.entity.components.ComponentManager;
import progetto.entity.components.base.Component;
import progetto.entity.components.specific.ai.StateComponent;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.general.ConfigComponent;
import progetto.entity.components.specific.graphics.ColorComponent;
import progetto.entity.components.specific.graphics.ZLevelComponent;
import progetto.entity.components.specific.movement.DirectionComponent;
import progetto.entity.components.specific.movement.NodeComponent;

/**
 * Classe base per ogni entità del gioco (giocatori, nemici, boss, ecc.).
 * Contiene posizione, direzione, stato, fisica e animazioni.
 */
public abstract class Entity {

    /**
     * Variabile globale per avere un id unico per ogni entità
     */
    private static int nextId = 0;

    /**
     * Id identificativo dell'entità
     */
    public final int id;

    /**
     * Gestore generale delle entità (dove è registrata questa entità). {@link Engine}
     */
    public final Engine engine;

    /**
     * Gestore componenti
     */
    public ComponentManager components;

    /**
     * Crea un'entità a partire da un'istanza salvata (es. Caricata da un file).
     *
     * @param instance l'entità salvata {@link EntityInstance}
     * @param engine   il gestore delle entità {@link Engine}
     */
    public Entity(EntityInstance instance, Engine engine) {
        this.engine = engine;
        this.id = nextId++;

        this.components = new ComponentManager();
        add(
            new ConfigComponent(instance.config, id),
            new ZLevelComponent(0),
            new StateComponent(),
            new PhysicsComponent(this, instance.coordinate),
            new NodeComponent(),
            new DirectionComponent(),
            new ColorComponent()
        );

        if (contains(PhysicsComponent.class)) {
            get(PhysicsComponent.class).createBody();
        }
    }

    /**
     * Crea un'entità a partire da una configurazione personalizzata.
     *
     * @param config configurazione dell'entità {@link EntityConfig}
     * @param engine il gestore delle entità {@link Engine}
     */
    public Entity(EntityConfig config, Engine engine) {
        this.engine = engine;
        this.id = nextId++;

        this.components = new ComponentManager();
        add(
            new ConfigComponent(config, id),
            new ZLevelComponent(0),
            new StateComponent(),
            new PhysicsComponent(this, config),
            new NodeComponent(),
            new DirectionComponent(),
            new ColorComponent()
        );

        if (contains(PhysicsComponent.class)) {
            get(PhysicsComponent.class).createBody();
        }

        System.out.println(config.id);
    }

    /**
     * @param componentClass classe del componente che si vuole {@link Class}
     * @param <T>            tipo di componente trovato
     * @return componete richiesto {@link Component}
     */
    public <T extends Component> T get(Class<T> componentClass) {
        Component component = components.get(componentClass);
        if (component == null) {
            throw new IllegalArgumentException("Component " + componentClass.getSimpleName() + " non trovato");
        }
        return componentClass.cast(component);
    }

    /**
     * Aggiunge uno o più componenti all'entità.
     *
     * @param components array di componenti da aggiungere
     */
    public void add(Component... components) {
        this.components.add(components);
    }


    public boolean contains(Array<Class<? extends Component>> components) {
        if (components.size == 0) {
            return false;
        }
        return this.components.contains(components);
    }

    public boolean contains(Class<? extends Component> components) {
        return this.components.contains(components);
    }

    /**
     * Rimuove l'entità dal mondo e restituisce un oggetto che la rappresenta.
     *
     * @return l'entità salvabile {@link EntityInstance}
     */
    public abstract EntityInstance unregister();

    /**
     * Viene chiamato dopo la creazione per inizializzare comportamenti specifici.
     */
    public abstract void create();

    @Override
    public String toString() {
        EntityConfig config = get(ConfigComponent.class).getConfig();
        return getClass().getSimpleName() + "{" +
            "posizione=" + get(PhysicsComponent.class).getPosition() +
            ", direzione=" + get(DirectionComponent.class).direction +
            ", isAlive=" + get(StateComponent.class).isAlive() +
            ", shouldRender=" + get(StateComponent.class).shouldBeUpdated() +
            ", id=" + config.id +
            ", nome='" + config.nome + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Entity other)) {
            return false;
        }
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
