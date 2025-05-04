package progetto.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.ECS.components.specific.combat.AttackRangeComponent;
import progetto.ECS.entities.specific.living.combat.Warrior;
import progetto.world.map.Map;

/**
 * Gestisce il mondo fisico (Box2D) del gioco.
 * Si occupa di creare, distruggere e pulire i corpi fisici.
 */
public class WorldManager {
    private static World instance; // Istanza del mondo fisico
    private static Queue<Body> bodyToDestroy; // Corpi in coda per la distruzione

    /**
     * Inizializza il mondo fisico e la coda di distruzione se non sono già stati creati.
     */
    public static void init() {
        if (instance == null) {
            instance = new World(new Vector2(0, 0), true);
            bodyToDestroy = new Queue<>();
        }
    }

    /**
     * Restituisce l'istanza del mondo fisico, creandola se necessario.
     *
     * @return l'istanza di World
     */
    public static World getInstance() {
        if (instance == null) {
            instance = new World(new Vector2(0, 0), true);
            bodyToDestroy = new Queue<>();
        }
        return instance;
    }

    /**
     * Pulisce la mappa.
     */
    public static void clearMap() {
        Array<Body> bodies = new Array<>();
        instance.getBodies(bodies);
        for (Body body : bodies) {
            if (body.getUserData() instanceof Map) {
                instance.destroyBody(body);
            }

            if (body.getUserData() instanceof Warrior warrior && warrior.components.contains(AttackRangeComponent.class)) {
                AttackRangeComponent range = warrior.components.get(AttackRangeComponent.class);
                if (range.getDirectionalRange() != null) {
                    instance.destroyBody(range.getDirectionalRange());
                    range.directionalRange = null;
                }
            }
        }
    }

    public static void clear(){
        Array<Body> bodies = new Array<>();
        instance.getBodies(bodies);
        for (Body body : bodies) {
            instance.destroyBody(body);
        }
    }

    /**
     *Aggiunge un corpo fisico alla coda di distruzione.
     *
     * @param body il corpo fisico da distruggere
     */
    public static void destroyBody(Body body) {
        if (body != null) {
            bodyToDestroy.addFirst(body);
        }
    }

    /**
     * Distrugge tutti i corpi fisici che sono stati messi in coda per la distruzione.
     * Deve essere chiamato nel thread principale durante l'update, OBBLIGATORIAMENTE solamente dopo aver aggiornato il mondo e fuori da il loop.
     */
    public static void update() {
        while (bodyToDestroy.notEmpty()) {
            instance.destroyBody(bodyToDestroy.removeFirst());
        }
    }
}
