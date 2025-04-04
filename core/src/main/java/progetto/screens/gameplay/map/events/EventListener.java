package progetto.screens.gameplay.map.events;

import com.badlogic.gdx.physics.box2d.*;
import progetto.screens.gameplay.entities.types.combat.CombatEntity;
import progetto.screens.gameplay.entities.types.enemy.Enemy;
import progetto.screens.gameplay.entities.types.entity.Entity;
import progetto.screens.gameplay.entities.types.player.Player;
import progetto.screens.gameplay.manager.entity.EntityManager;

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

        boolean isRangeA = fixtureA.getFilterData().categoryBits == EntityManager.RANGE;
        boolean isRangeB = fixtureB.getFilterData().categoryBits == EntityManager.RANGE;
        if (dataA instanceof Enemy && isRangeA && dataB instanceof Player && !isRangeB) {
            Enemy enemy1 = (Enemy) dataA;
            Player player = (Player) dataB;

            enemy1.addEntity(player);
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

        boolean isRangeA = fixtureA.getFilterData().categoryBits == EntityManager.RANGE;
        boolean isRangeB = fixtureB.getFilterData().categoryBits == EntityManager.RANGE;
        if (dataA instanceof Enemy && isRangeA && dataB instanceof Player && !isRangeB) {
            Enemy enemy1 = (Enemy) dataA;
            Player player = (Player) dataB;

            enemy1.removeEntity(player);
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
