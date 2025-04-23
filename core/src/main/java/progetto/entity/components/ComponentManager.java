package progetto.entity.components;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import progetto.entity.components.base.Component;

/**
 * Classe che gestisce i {@link Component} associati a un'entità.
 * <p>
 * Ogni componente è identificato dalla sua classe. Può essere aggiunto, rimosso,
 * recuperato oppure verificato se presente.
 * </p>
 * Questa classe è spesso usata internamente da una {@code Entity}.
 */
public class ComponentManager {

    /**
     * Mappa dei componenti dove la chiave è la classe del componente
     * e il valore è l'istanza effettiva.
     */
    private final ArrayMap<Class<? extends Component>, Component> components;

    /**
     * Costruttore che inizializza il manager con una mappa già esistente.
     *
     * @param components mappa dei componenti da usare
     */
    public ComponentManager(ArrayMap<Class<? extends Component>, Component> components) {
        this.components = components;
    }

    /**
     * Costruttore di default: inizializza una mappa vuota.
     */
    public ComponentManager() {
        components = new ArrayMap<>();
    }

    /**
     * Ritorna tutti i componenti attualmente presenti nel manager.
     *
     * @return array di oggetti {@code Component}
     */
    public Object[] components() {
        return components.values;
    }

    /**
     * Aggiunge un componente al manager.
     * Se un componente della stessa classe era già presente, viene sovrascritto.
     *
     * @param component componente da aggiungere {@link Component}
     */
    public void add(Component component) {
        Class<? extends Component> componentClass = component.getClass();
        components.put(componentClass, component);
    }

    /**
     * Aggiunge un array di componenti al manager.
     *
     * @param components array di componenti da aggiungere
     */
    public void add(Component[] components) {
        for (Component component : components) {
            add(component);
        }
    }

    /**
     * Ottiene un componente a partire dalla sua classe.
     *
     * @param componentClass classe del componente da recuperare {@link Class}
     * @param <T>            tipo del componente
     * @return il componente trovato {@link Component}
     * @throws IllegalArgumentException se il componente non è presente
     */
    public <T extends Component> T get(Class<T> componentClass) {
        Component component = components.get(componentClass);
        if (component == null) {
            throw new IllegalArgumentException("Component " + componentClass.getSimpleName() + " non trovato");
        }
        return componentClass.cast(component);
    }

    /**
     * Controlla se un certo componente è presente nel manager.
     *
     * @param componentClass classe del componente da cercare
     * @return {@code true} se il componente è presente, altrimenti {@code false}
     */
    public boolean contains(Class<? extends Component> componentClass) {
        return components.containsKey(componentClass);
    }

    /**
     * Controlla se sono presenti tutti i componenti specificati.
     *
     * @param components array delle classi dei componenti richiesti
     * @return {@code true} se tutti i componenti sono presenti, altrimenti {@code false}
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
     * Rimuove un componente dalla mappa.
     *
     * @param componentClass classe del componente da rimuovere {@link Component}
     * @param <T>            tipo del componente da rimuovere
     */
    public <T extends Component> void remove(Class<T> componentClass) {
        components.removeKey(componentClass);
    }

}
