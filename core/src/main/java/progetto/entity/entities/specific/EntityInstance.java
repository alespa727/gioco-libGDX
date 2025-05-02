package progetto.entity.entities.specific;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import progetto.core.Core;
import progetto.core.ResourceManager;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.general.ConfigComponent;
import progetto.entity.components.specific.movement.DirectionComponent;
import progetto.entity.entities.Entity;

/**
 * Rappresenta una "fotografia" di un'entità in un certo momento.
 * Utile per salvare lo stato o ricreare un'entità più tardi.
 */
public class EntityInstance {

    /**
     * Nome della classe dell'entità (es. "Player", "Enemy", ecc.)
     */
    public final String type;
    /**
     * Posizione corrente dell'entità nel mondo ({@link Vector2})
     */
    public final Vector2 coordinate;
    /**
     * Direzione verso cui l'entità è rivolta o si sta muovendo ({@link Vector2})
     */
    public final Vector2 direction;
    /**
     * {@link EntityConfig} contenente tutte le informazioni sull'entità (statistiche, immagine, stato, ecc.)
     */
    public EntityConfig config;

    /**
     * Costruisce un'istanza a partire da una {@link Entity}.
     * Salva tipo, configurazione, posizione e direzione.
     *
     * @param e entità da cui creare l'istanza
     */
    public EntityInstance(Entity e) {
        this.type = e.getClass().getSimpleName(); // Nome del tipo (classe)

        this.config = e.get(ConfigComponent.class).getConfig();       // Clona la configurazione
        this.coordinate = e.get(PhysicsComponent.class).getPosition(); // Clona la posizione
        this.direction = e.get(DirectionComponent.class).direction; // Clona la direzione
    }

    public EntityInstance() {
        this.coordinate = new Vector2();
        this.direction = new Vector2();
        this.type = "null";
    }

    public void loadTexture() {
        config.img = ResourceManager.get().get("entities/" + type + ".png", Texture.class);
    }

    @Override
    public String toString() {
        return "EntityInstance{" +
            "type='" + type + '\'' +
            ", config=" + config.toString() +
            ", coordinate=" + coordinate +
            ", direction=" + direction +
            '}';
    }
}

