package io.github.ale.screens.game.map.events;

import com.badlogic.gdx.physics.box2d.*;
import io.github.ale.screens.game.entities.player.Player;
import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.entityType.combat.CombatEntity;
import io.github.ale.screens.game.entityType.entity.Entity;

public class EventListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        // Definizioni caratteristiche fisiche
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Salvataggio corpi
        Object dataA = fixtureA.getBody().getUserData();
        Object dataB = fixtureB.getBody().getUserData();

        // Gestione eventi della mappa
        if (dataA instanceof Entity && dataB instanceof MapEvent) {
            ((MapEvent) dataB).trigger((Entity) dataA);
            ((MapEvent) dataB).setActive(true);
        }

        // Gestione range del player
        boolean isPlayerA = dataA instanceof Player;
        boolean isPlayerB = dataB instanceof Player;
        boolean isCombatB = dataB instanceof CombatEntity;
        boolean isNotRangeB = fixtureB.getFilterData().categoryBits != EntityManager.RANGE; // Controllo del filtro con cui può interagire

        if (!isPlayerB && isPlayerA && isCombatB && isNotRangeB) {
            ((Player) dataA).addEntity((CombatEntity) dataB); // Aggiungo entità alla lista di entità del player
        }

    }

    @Override
    public void endContact(Contact contact) {
        // Definizioni caratteristiche fisiche
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Salvataggio corpi
        Object dataA = fixtureA.getBody().getUserData();
        Object dataB = fixtureB.getBody().getUserData();

        // Gestione range del player
        boolean isPlayerA = dataA instanceof Player;
        boolean isPlayerB = dataB instanceof Player;
        boolean isCombatB = dataB instanceof CombatEntity;
        boolean isNotRangeB = fixtureB.getFilterData().categoryBits != EntityManager.RANGE; // Controllo del filtro con cui può interagire
        if (!isPlayerB && isPlayerA && isCombatB && isNotRangeB) {
            ((Player) dataA).removeEntity((CombatEntity) dataB); // Rimuovo entità dalla lista del player
        }

        // Gestione eventi della mappa
        if (dataA instanceof Entity && dataB instanceof MapEvent) {
            ((MapEvent) dataB).trigger((Entity) dataA);
            ((MapEvent) dataB).setActive(false);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
