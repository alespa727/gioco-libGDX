package progetto.gameplay.entity.types;

import com.badlogic.gdx.math.Vector2;

public class EntityInstance {

    // === Identificatore del tipo ===
    public final String type;

    // === Identificatori e descrizione ===
    public final int id;
    public final String nome;
    public final String descrizione;

    // === Configurazione e stato ===
    public final EntityConfig config;
    public final Vector2 coordinate;
    public final Vector2 direzione;
    public final boolean isRendered;

    // === Costruttore ===
    public EntityInstance(Entity e) {
        this.type = e.getClass().getSimpleName();
        System.out.println(type); // Debug: stampa il tipo dell'entità

        this.id = e.id;
        this.nome = e.nome;
        this.descrizione = e.descrizione;
        this.config = e.config();
        this.coordinate = e.getPosition();
        this.direzione = e.direzione();
        this.isRendered = e.isRendered();
    }
}
