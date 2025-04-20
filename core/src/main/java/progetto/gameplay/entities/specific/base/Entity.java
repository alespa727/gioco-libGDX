package progetto.gameplay.entities.specific.base;

// Importazioni

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.components.base.ComponentManager;
import progetto.gameplay.entities.components.base.IteratableComponent;
import progetto.gameplay.entities.components.specific.*;
import progetto.gameplay.entities.components.specific.base.*;
import progetto.graphics.animations.CustomAnimation;
import progetto.graphics.animations.DefaultAnimationSet;
import progetto.manager.entities.Engine;

/**
 * Classe base per ogni entità del gioco (giocatori, nemici, boss, ecc.).
 * Contiene posizione, direzione, stato, fisica e animazioni.
 */
public abstract class Entity {

    /** Gestore generale delle entità (dove è registrata questa entità). {@link Engine} */
    public final Engine manager;

    /** Configurazione iniziale dell'entità. {@link EntityConfig} */
    private final EntityConfig config;

    private boolean awake = true;

    /**
     * Gestione delle animazioni
     */
    private final CustomAnimation textures;

    public ComponentManager componentManager;

    /**
     * Crea un'entità a partire da un'istanza salvata (es. caricata da un file).
     * @param instance l'entità salvata {@link EntityInstance}
     * @param manager il gestore delle entità {@link Engine}
     */
    public Entity(EntityInstance instance, Engine manager) {
        this.config = instance.config;
        this.manager = manager;
        String[] string = new String[1];
        string[0] = "default";
        Texture[] img = new Texture[1];
        img[0] = config.img;
        this.textures = new CustomAnimation(string, img);

        Component[] components = new Component[]{
            new ZLevelComponent(0),
            new StateComponent(),
            new PhysicsComponent(this, instance.coordinate),
            new NodeComponent(),
            new DirectionComponent(),
            new ColorComponent(),
        };

        componentManager = new ComponentManager();
        componentManager.add(components);
        componentManager.get(PhysicsComponent.class).createBody();
    }

    /**
     * Crea un'entità a partire da una configurazione personalizzata.
     * @param config configurazione dell'entità {@link EntityConfig}
     * @param manager il gestore delle entità {@link Engine}
     */
    public Entity(EntityConfig config, Engine manager) {
        this.config = config;
        this.manager = manager;
        String[] string = new String[1];
        string[0] = "default";
        Texture[] img = new Texture[1];
        img[0] = config.img;
        this.textures = new CustomAnimation(string, img);

        Component[] components = new Component[]{
            new ZLevelComponent(0),
            new StateComponent(),
            new PhysicsComponent(this),
            new NodeComponent(),
            new DirectionComponent(),
            new ColorComponent(),
        };

        componentManager = new ComponentManager();
        componentManager.add(components);
        componentManager.get(PhysicsComponent.class).createBody();
    }

    public void setAwake(boolean awake) {
        this.awake = awake;
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
        return componentManager.get(PhysicsComponent.class);
    }

    /**
     * Restituisce la direzione in cui si sta muovendo l'entità.
     * @return direzione {@link DirectionComponent}
     */
    public final Vector2 getDirection() {
        return componentManager.get(DirectionComponent.class).direction;
    }

    /**
     * Restituisce lo stato dell'entità.
     * @return stato {@link StateComponent}
     */
    public final StateComponent getState() {
        return componentManager.get(StateComponent.class);
    }


    public final int getZ() {
        return componentManager.get(ZLevelComponent.class).getZ();
    }

    /**
     * Restituisce la posizione dell'entità.
     * @return posizione corrente
     */
    public Vector2 getPosition() {
        return getPhysics().getPosition();
    }

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

    public void componentManager(float delta) {
        if (awake) {
            for (int i = 0; i < componentManager.components().length; i++) {
                Component c = (Component) componentManager.components()[i];
                if (c instanceof IteratableComponent && c.isAwake()) {
                    ((IteratableComponent) c).update(delta);
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
