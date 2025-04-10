package progetto.gameplay.entity.types;

// Importazioni
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import progetto.gameplay.entity.components.DirectionComponent;
import progetto.gameplay.entity.components.NodeTracker;
import progetto.gameplay.entity.components.PhysicsComponent;
import progetto.gameplay.entity.components.StateComponent;
import progetto.gameplay.entity.types.living.HumanoidTextures;
import progetto.gameplay.manager.entity.ManagerEntity;

/**
 * Classe base per ogni entità del gioco (giocatori, nemici, boss, ecc.).
 * Contiene posizione, direzione, stato, fisica e animazioni.
 */
public abstract class Entity {

    /** Gestore generale delle entità (dove è registrata questa entità). {@link ManagerEntity} */
    public final ManagerEntity manager;

    /** Configurazione iniziale dell'entità. {@link EntityConfig} */
    private final EntityConfig config;

    // Componenti principali dell'entità
    protected final PhysicsComponent physics;      // Gestisce posizione e corpo fisico
    protected final DirectionComponent direction;  // Direzione di movimento
    protected final StateComponent state;          // Informazioni come "è vivo", "va disegnato", ecc.
    protected final NodeTracker nodeTracker;       // Tiene traccia del nodo della mappa (per pathfinding)
    private final HumanoidTextures textures;       // Gestisce immagini e animazioni

    /**
     * Crea un'entità a partire da un'istanza salvata (es. caricata da un file).
     * @param instance l'entità salvata {@link EntityInstance}
     * @param manager il gestore delle entità {@link ManagerEntity}
     */
    public Entity(EntityInstance instance, ManagerEntity manager) {
        this.config = instance.config;
        this.manager = manager;
        this.physics = new PhysicsComponent(this, instance.coordinate);
        this.nodeTracker = new NodeTracker(this);
        this.physics.createBody();
        this.direction = new DirectionComponent(config.direzione);
        this.state = new StateComponent();
        this.textures = new HumanoidTextures(config.img);
    }

    /**
     * Crea un'entità a partire da una configurazione personalizzata.
     * @param config configurazione dell'entità {@link EntityConfig}
     * @param manager il gestore delle entità {@link ManagerEntity}
     */
    public Entity(EntityConfig config, ManagerEntity manager) {
        this.config = config;
        this.manager = manager;
        this.physics = new PhysicsComponent(this);
        this.nodeTracker = new NodeTracker(this);
        this.physics.createBody();
        this.direction = new DirectionComponent(config.direzione);
        this.state = new StateComponent();
        this.textures = new HumanoidTextures(config.img);
    }

    /**
     * Aggiorna il comportamento base dell'entità.
     * @param delta tempo trascorso dall'ultimo frame
     */
    public abstract void updateEntity(float delta);

    /**
     * Aggiorna il comportamento specifico di questo tipo di entità.
     * @param delta tempo trascorso dall'ultimo frame
     */
    public abstract void updateEntityType(float delta);

    /**
     * Viene chiamato dopo la creazione per inizializzare comportamenti specifici.
     */
    public abstract void create();

    /**
     * Rimuove l'entità dal mondo e restituisce un oggetto che la rappresenta.
     * @return l'entità salvabile {@link EntityInstance}
     */
    public abstract EntityInstance despawn();

    /**
     * Imposta l'entità come "caricata", permettendole di aggiornarsi.
     */
    public void load() {
        state.setLoaded(true);
    }

    /**
     * Restituisce la configurazione dell'entità.
     * @return configurazione {@link EntityConfig}
     */
    public final EntityConfig getConfig() {
        return new EntityConfig(config);
    }

    /**
     * Restituisce il componente fisico dell'entità.
     * @return fisica {@link PhysicsComponent}
     */
    public PhysicsComponent getPhysics() {
        return physics;
    }

    /**
     * Restituisce la direzione in cui si sta muovendo l'entità.
     * @return direzione {@link DirectionComponent}
     */
    public final Vector2 getDirection() {
        return direction.getDirection();
    }

    /**
     * Restituisce lo stato dell'entità.
     * @return stato {@link StateComponent}
     */
    public final StateComponent getState() {
        return state;
    }

    /**
     * Restituisce la posizione dell'entità.
     * @return posizione corrente
     */
    public Vector2 getPosition() {
        return physics.getPosition();
    }

    /**
     * Sposta istantaneamente l'entità in una nuova posizione.
     * @param position nuova posizione
     */
    public void teleport(Vector2 position) {
        physics.teleport(position);
    }

    /**
     * Aggiorna il nodo corrente nella mappa (utile per il pathfinding).
     */
    public void updateNode() {
        nodeTracker.updateNode();
    }

    /**
     * Disegna l'entità sullo schermo.
     * @param batch il disegnatore
     * @param tempoTrascorso tempo passato per l’animazione
     */
    public void draw(SpriteBatch batch, float tempoTrascorso) {
        batch.draw(
            textures.getAnimation(this).getKeyFrame(tempoTrascorso, true),
            getPosition().x - config.imageWidth / 2,
            getPosition().y - config.imageWidth / 2,
            config.imageWidth,
            config.imageHeight
        );
        batch.setColor(Color.WHITE);
    }

    /**
     * Restituisce le texture e animazioni associate.
     * @return immagini {@link HumanoidTextures}
     */
    public HumanoidTextures getTextures() {
        return textures;
    }

    /**
     * Aggiorna l'entità se è attiva e pronta.
     * @param delta tempo trascorso dall’ultimo frame
     */
    public void render(float delta) {
        if (state.isLoaded()) {
            updateEntityType(delta);
            updateEntity(delta);
        }
    }

    /**
     * Indica se l’entità deve essere disegnata sullo schermo.
     * @return true se va disegnata
     */
    public boolean shouldRender() {
        return state.shouldRender();
    }

    /**
     * Imposta se l’entità va disegnata o no.
     * @param shouldRender true per disegnarla
     */
    public void setShouldRender(boolean shouldRender) {
        physics.setActive(shouldRender);
        state.setShouldRender(shouldRender);
    }

    /**
     * Imposta l’entità come viva.
     */
    public void setAlive() {
        state.setAlive(true);
    }

    /**
     * Imposta l’entità come morta.
     */
    public void setDead() {
        state.setAlive(false);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "posizione=" + getPosition() +
            ", direzione=" + getDirection() +
            ", isAlive=" + getState().isAlive() +
            ", shouldRender=" + shouldRender() +
            ", id=" + getConfig().id +
            ", nome='" + getConfig().nome + '\'' +
            '}';
    }
}
