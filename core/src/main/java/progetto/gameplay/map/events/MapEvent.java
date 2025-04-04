package progetto.gameplay.map.events;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import progetto.gameplay.WorldManager;
import progetto.gameplay.entities.types.entity.Entity;
import progetto.utils.BodyBuilder;

public abstract class MapEvent {
    protected Vector2 position; // Posizione fisica dell'evento
    protected float radius; // Raggio di attivazione evento
    protected boolean active; // Switch di attivazione dell'evento

    /**
     * Crea l'evento fisicamente
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
        return active;
    }

    /**
     * Attiva l'evento
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Crea la zona dell'evento
     */
    public void createZone() {
        // Definizione corpo
        BodyDef bodyDef = BodyBuilder.createBodyDef(BodyDef.BodyType.KinematicBody, position.x , position.y);
        bodyDef.fixedRotation = true; // Rotazione fissa

        // Definizione forma
        Shape shape = BodyBuilder.createCircle(radius);

        // Definizione caratteristiche fisiche
        FixtureDef fixtureDef = BodyBuilder.createFixtureDef(shape, 0, 0, 0);
        fixtureDef.isSensor = true; // Gestione manuale fisica

        // Creo il corpo
        WorldManager.addBody(this, bodyDef, fixtureDef);
        shape.dispose();
    }

    /**
     * Aggiorna l'evento
     */
    public abstract void update();

    /**
     * Attiva l'evento
     */
    public abstract void trigger(Entity entity);  // Da implementare per ogni evento specifico
}
