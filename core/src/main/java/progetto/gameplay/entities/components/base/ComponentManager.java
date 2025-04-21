package progetto.gameplay.entities.components.base;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class ComponentManager {
    private final ArrayMap<Class<? extends Component>, Component> components;

    public ComponentManager(ArrayMap<Class<? extends Component>, Component> components) {
        this.components = components;
    }

    public Object[] components() {
        return components.values;
    }

    public ComponentManager() {
        components = new ArrayMap<>();
    }

    /**
     * @param component componente da aggiungere {@link Component}
     */
    public void add(Component component) {
        Class<? extends Component> componentClass = component.getClass();
        components.put(componentClass, component);
    }

    public void add(Component[] components) {
        for (Component component : components) {
            add(component);
        }
    }

    /**
     * @param componentClass classe del componente che si vuole {@link Class}
     * @return componete richiesto {@link Component}
     * @param <T> tipo di componente trovato
     */
    public <T extends Component> T get(Class<T> componentClass) {
        Component component = components.get(componentClass);
        if (component == null) {
            throw new IllegalArgumentException("Component " + componentClass.getSimpleName() + " non trovato");
        }
        return componentClass.cast(component);
    }

    /**
     * Verifica se esiste un determinato componente nalla mappa
     * @param componentClass classe del componente
     * @return esistenza del componente
     */
    public boolean contains(Class<? extends Component> componentClass) {
        return components.containsKey(componentClass);
    }

    /**
     * Verifica se esistono determinati componenti nalla mappa
     * @param components classi del componente
     * @return esistenza del componente
     */
    public final boolean contains(Array<Class<? extends Component>> components) {
        for (int i = 0; i < components.size; i++) {
            Class<? extends Component> component = components.get(i);
            if (!contains(component)) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param componentClass componente da rimuovere {@link Component}
     * @param <T> tipo di componente da rimuovere
     */
    public <T extends Component> void remove(Class<T> componentClass) {
        components.removeKey(componentClass);
    }

}
