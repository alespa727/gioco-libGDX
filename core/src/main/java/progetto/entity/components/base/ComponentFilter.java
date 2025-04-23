package progetto.entity.components.base;

import com.badlogic.gdx.utils.Array;
import progetto.entity.entities.base.Entity;

/**
 * Serve per filtrare le entità in base ai componenti che hanno o non hanno.
 * <p>
 * Si usa per dire: "Voglio solo le entità che hanno questi componenti" oppure
 * "Non voglio le entità che hanno questi componenti".
 * </p>
 */
public class ComponentFilter {

    /**
     * Tipo di filtro: REQUIRE = devono avere i componenti, EXCLUDE = non devono averli.
     */
    enum ComponentFilterType {
        EXCLUDE,
        REQUIRE
    }

    /**
     * Lista dei componenti usati dal filtro (da richiedere o escludere).
     */
    private final Array<Class<? extends Component>> required;

    /**
     * Tipo del filtro: REQUIRE o EXCLUDE.
     */
    private ComponentFilterType type = ComponentFilterType.REQUIRE;

    /**
     * Costruttore privato. Crea un filtro con una lista di componenti.
     *
     * @param required componenti da controllare
     */
    @SafeVarargs
    private ComponentFilter(Class<? extends Component>... required) {
        this.required = new Array<>(required);
    }

    /**
     * Cambia il tipo del filtro (REQUIRE o EXCLUDE).
     *
     * @param type nuovo tipo del filtro
     */
    private void setType(ComponentFilterType type) {
        this.type = type;
    }

    /**
     * Crea un filtro che richiede che l'entità abbia tutti i componenti dati.
     *
     * @param required componenti richiesti
     * @return filtro che accetta solo entità con quei componenti
     */
    @SafeVarargs
    public static ComponentFilter all(Class<? extends Component>... required){
        ComponentFilter filter = new ComponentFilter(required);
        filter.setType(ComponentFilterType.REQUIRE);
        return filter;
    }

    /**
     * Crea un filtro che esclude le entità che hanno uno di questi componenti.
     *
     * @param excluded componenti da escludere
     * @return filtro che rifiuta le entità con quei componenti
     */
    @SafeVarargs
    public static ComponentFilter exclude(Class<? extends Component>... excluded){
        ComponentFilter filter = new ComponentFilter(excluded);
        filter.setType(ComponentFilterType.EXCLUDE);
        return filter;
    }

    /**
     * Controlla se l'entità rispetta le regole del filtro.
     *
     * @param e entità da controllare
     * @return true se l'entità passa il filtro, false altrimenti
     */
    public boolean matches(Entity e) {
        boolean hasComponents = e.contains(required);

        return switch (type) {
            case REQUIRE -> hasComponents;
            case EXCLUDE -> !hasComponents;
        };
    }

    /**
     * Restituisce i componenti usati dal filtro.
     *
     * @return array dei componenti richiesti o esclusi
     */
    public Array<Class<? extends Component>> getRequired() {
        return required;
    }
}
