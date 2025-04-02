package io.github.ale.screens.game.map.events;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.ale.screens.game.entities.entityTypes.entity.Entity;

public abstract class MapEvent{
    protected Vector2 position; // Posizione fisica dell'evento
    protected float radius; // Raggio di attivazione evento
    protected boolean active; // Switch di attivazione dell'evento

    /**Crea l'evento fisicamente*/
    public MapEvent(Vector2 position, float radius, World world) {
        this.position = position;
        this.radius = radius;
        this.createZone(world);
    }

    /**Attiva l'evento*/
    public void setActive(boolean active) {
        this.active = active;
    }

    /**Restituisce se l'evento è attivo*/
    public boolean isActive() {
        return active;
    }

    /**Crea la zona dell'evento*/
    public void createZone(World world) {
        // Definizione corpo
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody; // Tipo di corpo
        bodyDef.position.set(position); // Posizione corpo
        bodyDef.fixedRotation = true; // Rotazione fissa

        // Definizione forma
        Shape shape = new CircleShape();
        shape.setRadius(radius); // Raggio

        // Definizione caratteristiche fisiche
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true; // Gestione manuale fisica

        // Creo il corpo
        Body body = world.createBody(bodyDef); // Crea e aggiunge al mondo
        body.createFixture(fixtureDef); // Allega le caratteristiche fisiche
        body.setUserData(this); // Aggiunge come proprietario dell'evento la reference all'evento
    }

    /**Aggiorna l'evento*/
    public abstract void update();
    /**Attiva l'evento*/
    public abstract void trigger(Entity entity);  // Da implementare per ogni evento specifico
}
