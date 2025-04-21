package progetto.gameplay.entities.specific.base;

// Importazioni

import com.badlogic.gdx.graphics.Texture;
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

    /** Variabile globale per avere un id unico per ogni entità */
    private static int nextId = 0;

    /** Id identificativo dell'entità */
    public final int id;

    /** Gestore generale delle entità (dove è registrata questa entità). {@link Engine} */
    public final Engine manager;

    /** Gestione delle animazioni */
    private final CustomAnimation textures;

    /** Gestore componenti */
    public ComponentManager components;

    /**
     * Crea un'entità a partire da un'istanza salvata (es. caricata da un file).
     * @param instance l'entità salvata {@link EntityInstance}
     * @param manager il gestore delle entità {@link Engine}
     */
    public Entity(EntityInstance instance, Engine manager) {
        Component[] components = new Component[]{
            new ConfigComponent(instance.config, nextId),
            new ZLevelComponent(0),
            new StateComponent(),
            new PhysicsComponent(this, instance.coordinate),
            new NodeComponent(),
            new DirectionComponent(),
            new ColorComponent(),
        };

        this.components = new ComponentManager();
        this.components.add(components);
        this.components.get(PhysicsComponent.class).createBody();

        nextId++;
        this.id = nextId;
        this.manager = manager;
        String[] string = new String[1];
        string[0] = "default";
        Texture[] img = new Texture[1];
        img[0] = instance.config.img;
        this.textures = new CustomAnimation(string, img);
    }

    /**
     * Crea un'entità a partire da una configurazione personalizzata.
     * @param config configurazione dell'entità {@link EntityConfig}
     * @param manager il gestore delle entità {@link Engine}
     */
    public Entity(EntityConfig config, Engine manager) {
        Component[] components = new Component[]{
            new ConfigComponent(config, nextId),
            new ZLevelComponent(0),
            new StateComponent(),
            new PhysicsComponent(this, config),
            new NodeComponent(),
            new DirectionComponent(),
            new ColorComponent(),
        };

        this.components = new ComponentManager();
        this.components.add(components);
        this.components.get(PhysicsComponent.class).createBody();

        nextId++;
        this.id = nextId;
        System.out.println(config.id);
        this.manager = manager;
        String[] string = new String[1];
        string[0] = "default";
        Texture[] img = new Texture[1];
        img[0] = config.img;
        this.textures = new CustomAnimation(string, img);


    }

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
     * Restituisce la configurazione dell'entità.
     * @return configurazione {@link EntityConfig}
     */
    public final EntityConfig getConfig() {
        return new EntityConfig(components.get(ConfigComponent.class).getConfig());
    }

    /**
     * Restituisce il componente fisico dell'entità.
     * @return fisica {@link PhysicsComponent}
     */
    public PhysicsComponent getPhysics() {
        return components.get(PhysicsComponent.class);
    }

    /**
     * Restituisce la direzione in cui si sta muovendo l'entità.
     * @return direzione {@link DirectionComponent}
     */
    public final Vector2 getDirection() {
        return components.get(DirectionComponent.class).direction;
    }

    /**
     * Restituisce lo stato dell'entità.
     * @return stato {@link StateComponent}
     */
    public final StateComponent getState() {
        return components.get(StateComponent.class);
    }


    public final int getZ() {
        return components.get(ZLevelComponent.class).getZ();
    }

    /**
     * Restituisce la posizione dell'entità.
     * @return posizione corrente
     */
    public Vector2 getPosition() {
        return getPhysics().getPosition();
    }

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
        }
    }

    public void update(float delta) {
        for (int i = 0; i < components.components().length; i++) {
            Component c = (Component) components.components()[i];
            if (c instanceof IteratableComponent && c.isAwake()) {
                ((IteratableComponent) c).update(delta);
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
