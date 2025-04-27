package progetto.entity.entities.specific.notliving;

import com.badlogic.gdx.graphics.Texture;
import progetto.entity.Engine;
import progetto.entity.entities.base.EntityConfig;
import progetto.entity.entities.base.EntityInstance;

public class Casa extends GameObject {
    /**
     * Crea un'entità a partire da un'istanza salvata (es. caricata da un file).
     *
     * @param instance l'entità salvata {@link EntityInstance}
     * @param manager  il gestore delle entità {@link Engine}
     * @param radius
     */
    public Casa(EntityInstance instance, Engine manager, float radius) {
        super(instance, manager, radius);
    }

    /**
     * Crea un'entità a partire da una configurazione personalizzata.
     *
     * @param config  configurazione dell'entità {@link EntityConfig}
     * @param manager il gestore delle entità {@link Engine}
     * @param radius
     */
    public Casa(EntityConfig config, Engine manager, float radius) {
        super(config, manager, radius);
        this.texture = new Texture("entities/casa.png");
    }

}
