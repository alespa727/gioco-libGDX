package progetto.gameplay.entities.specific.specific.notliving;

import com.badlogic.gdx.graphics.Texture;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.base.EntityInstance;
import progetto.manager.entities.Engine;

public class Sword extends GameObject {
    /**
     * Crea un'entità a partire da una configurazione personalizzata.
     *
     * @param config  configurazione dell'entità {@link EntityConfig}
     * @param manager il gestore delle entità {@link Engine}
     */
    public Sword(EntityConfig config, Engine manager, float radius) {
        super(config, manager, radius);
        this.texture = new Texture("entities/Finn/attack/sword.png");
    }

    /**
     * Crea un'entità a partire da un'istanza salvata (es. caricata da un file).
     *
     * @param instance l'entità salvata {@link EntityInstance}
     * @param manager  il gestore delle entità {@link Engine}
     */
    public Sword(EntityInstance instance, Engine manager, float radius) {
        super(instance, manager, radius);
        this.texture = new Texture("entities/Finn/attack/sword.png");
    }

    /**
     * Viene chiamato dopo la creazione per inizializzare comportamenti specifici.
     */
    @Override
    public void create() {

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
}
