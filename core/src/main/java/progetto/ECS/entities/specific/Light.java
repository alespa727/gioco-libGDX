package progetto.ECS.entities.specific;

import progetto.ECS.EntityEngine;
import progetto.ECS.entities.Entity;

public class Light extends Entity {
    /**
     * Crea un'entità a partire da un'istanza salvata (es. Caricata da un file).
     *
     * @param entityEngine il gestore delle entità {@link EntityEngine}
     */
    public Light(EntityEngine entityEngine) {
        super(entityEngine);
    }

    /**
     * Rimuove l'entità dal mondo e restituisce un oggetto che la rappresenta.
     *
     * @return l'entità salvabile {@link EntityInstance}
     */
    @Override
    public EntityInstance unregister() {
        return null;
    }

    /**
     * Viene chiamato dopo la creazione per inizializzare comportamenti specifici.
     */
    @Override
    public void create() {

    }
}
