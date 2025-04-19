package progetto.gameplay.entities.components.base;

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

    public boolean contains(Class<? extends Component> componentClass) {
        return components.containsKey(componentClass);
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
