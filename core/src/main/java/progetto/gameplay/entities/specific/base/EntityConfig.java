package progetto.gameplay.entities.specific.base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import progetto.gameplay.entities.components.base.Component;

/**
 * Contiene tutte le informazioni di base per configurare un'entità.
 * Questa classe viene usata per creare, salvare o duplicare entità.
 */
public class EntityConfig extends Component {

    // === Identificatori e descrizione ===

    /**
     * Identificatore univoco dell'entità
     */
    public int id;

    /**
     * Nome dell'entità (es. "Goblin", "Player")
     */
    public String nome;

    /**
     * Descrizione dell'entità, utile per testi o debugging
     */
    public String descrizione;

    // === Posizione e dimensioni ===

    /**
     * Posizione X iniziale dell'entità
     */
    public float x;

    /**
     * Posizione Y iniziale dell'entità
     */
    public float y;

    /**
     * Raggio dell'entità, usato per collisioni e hitbox
     */
    public float radius;

    /**
     * Direzione iniziale in cui guarda o si muove l'entità (Vector2)
     */
    public Vector2 direzione;

    // === Stato e comportamento ===

    /**
     * Indica se l'entità è viva o no
     */
    public boolean isAlive;

    /**
     * Indica se l'entità è attualmente in collisione
     */
    public boolean inCollisione;

    /**
     * Indica se l'entità si sta muovendo
     */
    public boolean isMoving;

    // === Statistiche ===

    /**
     * Punti vita dell'entità
     */
    public float hp;

    /**
     * Velocità di movimento
     */
    public float speed;

    /**
     * Danno che infligge quando attacca
     */
    public float attackdmg;

    // === Grafica ===

    /**
     * Immagine o sprite principale dell'entità (Texture di LibGDX)
     */
    public transient Texture img;

    /**
     * Larghezza dell'immagine da disegnare a schermo
     */
    public float imageWidth;

    /**
     * Altezza dell'immagine da disegnare a schermo
     */
    public float imageHeight;

    // === Costruttori ===

    /**
     * Costruttore vuoto, utile quando si vuole inizializzare i dati manualmente.
     */
    public EntityConfig() {
    }

    /**
     * Crea una copia di un'altra configurazione.
     *
     * @param config la configurazione da copiare (EntityConfig)
     */
    public EntityConfig(EntityConfig config) {
        this.id = config.id;
        this.x = config.x;
        this.y = config.y;
        this.img = config.img;
        this.radius = config.radius;
        this.direzione = config.direzione;
        this.nome = config.nome;
        this.descrizione = config.descrizione;
        this.isAlive = config.isAlive;
        this.inCollisione = config.inCollisione;
        this.isMoving = config.isMoving;
        this.hp = config.hp;
        this.speed = config.speed;
        this.attackdmg = config.attackdmg;
        this.imageWidth = config.imageWidth;
        this.imageHeight = config.imageHeight;
    }

    @Override
    public String toString() {
        return "EntityConfig{" +
            "id=" + id +
            ", nome='" + nome + '\'' +
            ", descrizione='" + descrizione + '\'' +
            ", posizione=(" + x + ", " + y + ")" +
            ", radius=" + radius +
            ", direzione=" + (direzione != null ? direzione.toString() : "null") +
            ", isAlive=" + isAlive +
            ", inCollisione=" + inCollisione +
            ", isMoving=" + isMoving +
            ", hp=" + hp +
            ", speed=" + speed +
            ", attackdmg=" + attackdmg +
            ", imageSize=(" + imageWidth + " x " + imageHeight + ")" +
            ", img=" + (img != null ? img.toString() : "null") +
            '}';
    }
}
