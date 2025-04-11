package progetto.gameplay.map.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import progetto.gameplay.manager.ManagerWorld;
import progetto.gameplay.entity.types.Entity;
import progetto.factories.BodyFactory;

/**
 * Evento nella mappa
 * <p>
 * Gestire l'evento tramite {@link #trigger(Entity)} e {@link #update()}
 * </p>
 * <p>
 * Se vuoi che l'evento si attivi subito dopo la creazione, chiama {@link #trigger(Entity)} e poi {@link #destroy()}
 * direttamente nel costruttore.
 * </p>
 */
public abstract class MapEvent {

    private Body body;
    protected final Vector2 position; // Posizione fisica dell'evento
    protected final float radius; // Raggio di attivazione evento
    protected boolean isActive; // Switch di attivazione dell'evento

    /**
     * Crea un evento nella mappa che può essere attivato quando il player entra nel suo raggio.
     * <p>
     * L'evento ha una posizione e un raggio. Viene subito creata la zona in cui può essere attivato.
     * </p>
     * @param position posizione centrale dell’evento
     * @param radius raggio entro cui l’evento può attivarsi
     */
    public MapEvent(Vector2 position, float radius) {
        this.position = position;
        this.radius = radius;
        this.createZone();
    }

    /**
     * Restituisce se l'evento è attivo
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * @param status stato dell'evento
     */
    public void setActive(boolean status) {
        this.isActive = status;
    }

    /**
     * Crea la zona dell'evento
     */
    public void createZone() {
        // Definizione corpo
        BodyDef bodyDef = BodyFactory.createBodyDef(BodyDef.BodyType.KinematicBody, position.x, position.y);
        bodyDef.fixedRotation = true; // Rotazione fissa

        // Definizione forma
        Shape shape = BodyFactory.createCircle(radius);

        // Definizione caratteristiche fisiche
        FixtureDef fixtureDef = BodyFactory.createFixtureDef(shape, 0, 0, 0);
        fixtureDef.isSensor = true; // Gestione manuale fisica

        // Creo il corpo
        body = BodyFactory.createBody(this, bodyDef, fixtureDef, shape);
    }

    /**
     * Aggiorna l'evento
     */
    public abstract void update();

    /**
     * Attiva l'evento
     *
     * @param entity entità che lo ha triggerato
     * <p>
     * Gestire l'evento a seconda dell'entità
     * </p>
     */
    public abstract void trigger(Entity entity);  // Da implementare per ogni evento specifico

    /**
     * Rimuove l'evento
     */
    public void destroy() {
        ManagerWorld.destroyBody(body);
    }
}
