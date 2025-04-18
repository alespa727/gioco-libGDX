package progetto.gameplay.entities.specific.base;

// Importazioni

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ArrayMap;
import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.components.base.IteratableComponent;
import progetto.gameplay.entities.components.specific.base.*;
import progetto.graphics.animations.CustomAnimation;
import progetto.graphics.animations.DefaultAnimationSet;
import progetto.manager.entities.EntityManager;

/**
 * Classe base per ogni entità del gioco (giocatori, nemici, boss, ecc.).
 * Contiene posizione, direzione, stato, fisica e animazioni.
 */
public abstract class Entity {

    /** Gestore generale delle entità (dove è registrata questa entità). {@link EntityManager} */
    public final EntityManager manager;

    /** Configurazione iniziale dell'entità. {@link EntityConfig} */
    private final EntityConfig config;

    // Componenti principali dell'entità
    public Color color;

    private boolean awake = true;

    /**
     * Gestione delle animazioni
     */
    private final CustomAnimation textures;

    private final ArrayMap<Class<? extends Component>, Component> components;

    /**
     * Crea un'entità a partire da un'istanza salvata (es. caricata da un file).
     * @param instance l'entità salvata {@link EntityInstance}
     * @param manager il gestore delle entità {@link EntityManager}
     */
    public Entity(EntityInstance instance, EntityManager manager) {
        this.config = instance.config;
        this.manager = manager;
        String[] string = new String[1];
        string[0] = "default";
        Texture[] img = new Texture[1];
        img[0] = config.img;
        this.textures = new CustomAnimation(string, img);
        this.color = new Color(1f, 1f, 1f, 1.0f);

        components = new ArrayMap<>();
        addComponent(new ZLevelComponent(0));
        addComponent(new StateComponent());
        addComponent(new PhysicsComponent(this, instance.coordinate));
        addComponent(new NodeTrackerComponent(this));
        addComponent(new DirectionComponent(config.direzione));

        getComponent(PhysicsComponent.class).createBody();
    }

    /**
     * Crea un'entità a partire da una configurazione personalizzata.
     * @param config configurazione dell'entità {@link EntityConfig}
     * @param manager il gestore delle entità {@link EntityManager}
     */
    public Entity(EntityConfig config, EntityManager manager) {
        this.config = config;
        this.manager = manager;
        String[] string = new String[1];
        string[0] = "default";
        Texture[] img = new Texture[1];
        img[0] = config.img;
        this.textures = new CustomAnimation(string, img);
        this.color = new Color(1f, 1f, 1f, 1.0f);
        this.color = new Color(1f, 1f, 1f, 1.0f);

        components = new ArrayMap<>();
        addComponent(new ZLevelComponent(0));
        addComponent(new StateComponent());
        addComponent(new PhysicsComponent(this));
        addComponent(new NodeTrackerComponent(this));
        addComponent(new DirectionComponent(config.direzione));

        getComponent(PhysicsComponent.class).createBody();
    }

    public void setAwake(boolean awake) {
        this.awake = awake;
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

    public boolean containsComponent(Class<? extends Component> componentClass) {
        return components.containsKey(componentClass);
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


    public final int getZ() {
        return getComponent(ZLevelComponent.class).getZ();
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

    public void setColor(Color color){this.color = color;}

    /**
     * Disegna l'entità sullo schermo.
     * @param batch il disegnatore
     * @param tempoTrascorso tempo passato per l’animazione
     */
    public abstract void draw(SpriteBatch batch, float tempoTrascorso);

    /**
     * Restituisce le texture e animazioni associate.
     * @return immagini {@link DefaultAnimationSet}
     */
    public CustomAnimation getTextures() {
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
