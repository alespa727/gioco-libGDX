package io.github.ale.screens.gameplay.map.events;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.ale.screens.gameplay.entities.types.entity.Entity;
import io.github.ale.utils.BodyBuilder.BodyBuilder;

public abstract class MapEvent {
    protected Vector2 position; // Posizione fisica dell'evento
    protected float radius; // Raggio di attivazione evento
    protected boolean active; // Switch di attivazione dell'evento

    /**
     * Crea l'evento fisicamente
     */
    public MapEvent(Vector2 position, float radius, World world) {
        this.position = position;
        this.radius = radius;
        this.createZone(world);
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
    public void createZone(World world) {
        // Definizione corpo
        BodyDef bodyDef = BodyBuilder.createBodyDef(BodyDef.BodyType.KinematicBody, position.x , position.y);
        bodyDef.fixedRotation = true; // Rotazione fissa

        // Definizione forma
        Shape shape = BodyBuilder.createCircle(radius);

        // Definizione caratteristiche fisiche
        FixtureDef fixtureDef = BodyBuilder.createFixtureDef(shape, 0, 0, 0);
        fixtureDef.isSensor = true; // Gestione manuale fisica

        // Creo il corpo
        BodyBuilder.createBody(world, this, bodyDef, fixtureDef, shape);
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
