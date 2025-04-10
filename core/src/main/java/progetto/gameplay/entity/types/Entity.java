package progetto.gameplay.entity.types;

// Importazioni
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ArrayMap;
import progetto.gameplay.entity.components.*;
import progetto.gameplay.entity.components.entity.DirectionComponent;
import progetto.gameplay.entity.components.entity.NodeTracker;
import progetto.gameplay.entity.components.entity.PhysicsComponent;
import progetto.gameplay.entity.components.entity.StateComponent;
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
    private final ArrayMap<Class<? extends Component>, Component> components;
    protected boolean awake = true;
    private final HumanoidTextures textures;       // Gestisce immagini e animazioni

    /**
     * Crea un'entità a partire da un'istanza salvata (es. caricata da un file).
     * @param instance l'entità salvata {@link EntityInstance}
     * @param manager il gestore delle entità {@link ManagerEntity}
     */
    public Entity(EntityInstance instance, ManagerEntity manager) {
        this.config = instance.config;
        this.manager = manager;
        this.textures = new HumanoidTextures(config.img);

        components = new ArrayMap<>();
        addComponent(new StateComponent());
        addComponent(new PhysicsComponent(this, instance.coordinate));
        addComponent(new NodeTracker(this));
        addComponent(new DirectionComponent(config.direzione));

        getComponent(PhysicsComponent.class).createBody();
    }

    /**
     * Crea un'entità a partire da una configurazione personalizzata.
     * @param config configurazione dell'entità {@link EntityConfig}
     * @param manager il gestore delle entità {@link ManagerEntity}
     */
    public Entity(EntityConfig config, ManagerEntity manager) {
        this.config = config;
        this.manager = manager;
        this.textures = new HumanoidTextures(config.img);

        components = new ArrayMap<>();
        addComponent(new StateComponent());
        addComponent(new PhysicsComponent(this));
        addComponent(new NodeTracker(this));
        addComponent(new DirectionComponent(config.direzione));

        getComponent(PhysicsComponent.class).createBody();
    }

    /**
     * @param component componente da aggiungere {@link Component}
     */
    public void addComponent(Component component) {
        Class<? extends Component> componentClass = component.getClass();
        components.put(componentClass, component);
    }

    /**
     * @param componentClass classe del componente che si vuole {@link Class}
     * @return componete richiesto {@link Component}
     * @param <T> tipo di componente trovato
     */
    public <T extends Component> T getComponent(Class<T> componentClass) {
        Component component = components.get(componentClass);
        if (component == null) {
            throw new IllegalArgumentException("Component " + componentClass.getSimpleName() + " non trovato");
        }
        return componentClass.cast(component);
    }

    /**
     *
     * @param componentClass componente da rimuovere {@link Component}
     * @param <T> tipo di componente da rimuovere
     */
    public <T extends Component> void removeComponent(Class<T> componentClass) {
        components.removeKey(componentClass);
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
        getState().setLoaded(true);
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
        return getComponent(PhysicsComponent.class);
    }

    /**
     * Restituisce la direzione in cui si sta muovendo l'entità.
     * @return direzione {@link DirectionComponent}
     */
    public final Vector2 getDirection() {
        return getComponent(DirectionComponent.class).getDirection();
    }

    /**
     * Restituisce lo stato dell'entità.
     * @return stato {@link StateComponent}
     */
    public final StateComponent getState() {
        return getComponent(StateComponent.class);
    }

    /**
     * Restituisce la posizione dell'entità.
     * @return posizione corrente
     */
    public Vector2 getPosition() {
        return getPhysics().getPosition();
    }

    /**
     * Sposta istantaneamente l'entità in una nuova posizione.
     * @param position nuova posizione
     */
    public void teleport(Vector2 position) {
        getPhysics().teleport(position);
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
        if (getState().isLoaded()) {
            updateEntityType(delta);
            updateEntity(delta);
        }
    }

    public void updateComponents(float delta) {
        if (awake) {
            for (Component component : components.values()) {
                if (component instanceof IteratableComponent && component.isAwake()) {
                    ((IteratableComponent) component).update(delta);
                }
            }
        }
    }

    /**
     * Indica se l’entità deve essere disegnata sullo schermo.
     * @return true se va disegnata
     */
    public boolean shouldRender() {
        return getState().shouldRender();
    }

    /**
     * Imposta se l’entità va disegnata o no.
     * @param shouldRender true per disegnarla
     */
    public void setShouldRender(boolean shouldRender) {
        getPhysics().setActive(shouldRender);
        getState().setShouldRender(shouldRender);
    }

    /**
     * Imposta l’entità come viva.
     */
    public void setAlive() {
        getState().setAlive(true);
    }

    /**
     * Imposta l’entità come morta.
     */
    public void setDead() {
        getState().setAlive(false);
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
