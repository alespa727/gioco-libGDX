package progetto.world.events.base;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import progetto.ECS.entities.Entity;
import progetto.factories.BodyFactory;
import progetto.world.WorldManager;

/**
 * Evento nella mappa
 * <p>
 * Gestire l'evento tramite {@link #trigger(Entity)} e {@link #update(float delta)}
 * </p>
 * <p>
 * Se vuoi che l'evento si attivi subito dopo la creazione, chiama {@link #trigger(Entity)} e poi {@link #destroy()}
 * direttamente nel costruttore.
 * </p>
 */
public abstract class MapEvent {

    protected Vector2 position; // Posizione fisica dell'evento
    protected float radius; // Raggio di attivazione evento
    protected boolean isActive; // Switch di attivazione dell'evento
    protected boolean shouldRemove=false;
    private Body body;

    /**
     * Crea un evento nella mappa che può essere attivato quando il player entra nel suo raggio.
     * <p>
     * L'evento ha una posizione e un raggio. Viene subito creata la zona in cui può essere attivato.
     * </p>
     *
     * @param position posizione centrale dell’evento
     * @param radius   raggio entro cui l’evento può attivarsi
     */
    public MapEvent(Vector2 position, float radius) {
        this.position = position;
        this.radius = radius;
        this.createZone();
    }

    public MapEvent(){}

    public float getX(){
        return position.x;
    }

    public float getY(){
        return position.y;
    }

    /**
     * Restituisce se l'evento è attivo
     */
    public boolean isActive() {
        return isActive;
    }

    public boolean getShouldbeRemove() {
        return shouldRemove;
    }

    /**
     * @param status stato dell'evento
     */
    public void setActive(boolean status) {
        this.isActive = status;
    }

    public void setBody(Body body) {
        this.body = body;
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
        body = BodyFactory.createBody(this, bodyDef, fixtureDef);
    }

    /**
     * Aggiorna l'evento
     */
    public abstract void update(float delta);

    /**
     * Attiva l'evento
     *
     * @param entity entità che lo ha triggerato
     *               <p>
     *               Gestire l'evento a seconda dell'entità
     *               </p>
     */
    public abstract void trigger(Entity entity);  // Da implementare per ogni evento specifico

    /**
     * Rimuove l'evento
     */
    public void destroy() {
        WorldManager.destroyBody(body);
        shouldRemove=true;
    }
}
