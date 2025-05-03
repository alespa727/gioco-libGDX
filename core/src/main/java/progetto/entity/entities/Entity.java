package progetto.entity.entities;

import com.badlogic.gdx.utils.Array;
import progetto.entity.EntityEngine;
import progetto.entity.components.ComponentManager;
import progetto.entity.components.base.Component;
import progetto.entity.components.specific.base.StateComponent;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.general.ConfigComponent;
import progetto.entity.components.specific.movement.DirectionComponent;
import progetto.entity.entities.specific.EntityConfig;
import progetto.entity.entities.specific.EntityInstance;

/**
 * Un contenitore di un numero intero e componenti, dati puri senza logica.
 */
public abstract class Entity {

    /**
     * Variabile globale per avere un id unico per ogni entità.
     */
    private static int nextId = 0;

    /**
     * Id identificativo dell'entità.
     */
    public final int id;

    /**
     * Gestore generale delle entità (dove è registrata questa entità). {@link EntityEngine}
     */
    public final EntityEngine entityEngine;

    /**
     * Gestore componenti.
     */
    public ComponentManager components;

    /**
     * Crea un'entità a partire da un'istanza salvata (es. Caricata da un file).
     *
     * @param entityEngine il gestore delle entità {@link EntityEngine}
     */
    public Entity(EntityEngine entityEngine) {
        this.entityEngine = entityEngine;
        this.id = nextId++;
        this.components = new ComponentManager();
    }

    public boolean shouldAddToSystems(){
        return components.shouldBeAddedToSystems();
    }

    /**
     * Restituisce un componente specifico.
     *
     * @param componentClass classe del componente che si vuole {@link Class}
     * @param <T> tipo di componente trovato
     * @return componente richiesto {@link Component}
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

    /**
     * Verifica se l'entità contiene tutti i componenti richiesti.
     *
     * @param components array di classi di componenti
     * @return true se tutti presenti
     */
    public boolean contains(Array<Class<? extends Component>> components) {
        return components.size > 0 && this.components.contains(components);
    }

    /**
     * Verifica se l'entità contiene un componente specifico.
     *
     * @param component classe del componente
     * @return true se presente
     */
    public boolean contains(Class<? extends Component> component) {
        return this.components.contains(component);
    }

    /**
     * Rimuove un componente dall'entità.
     *
     * @param component classe del componente da rimuovere
     */
    public void remove(Class<? extends Component> component) {
        this.components.remove(component);
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
        if (!(obj instanceof Entity other)) return false;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}