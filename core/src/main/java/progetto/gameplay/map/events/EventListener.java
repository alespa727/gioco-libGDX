package progetto.gameplay.map.events;

import com.badlogic.gdx.physics.box2d.*;
import progetto.gameplay.entities.types.CombatEntity;
import progetto.gameplay.entities.types.Enemy;
import progetto.gameplay.entities.types.entity.Entity;
import progetto.gameplay.entities.types.Player;
import progetto.gameplay.manager.entity.EntityManager;

public class EventListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        // Definizioni caratteristiche fisiche
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Salvataggio corpi
        Object dataA = fixtureA.getBody().getUserData();
        Object dataB = fixtureB.getBody().getUserData();

        // Gestione eventi della mappa, controllando entrambe le configurazioni:
        if (dataA instanceof Entity && dataB instanceof MapEvent) {
            ((MapEvent) dataB).trigger((Entity) dataA);
        } else if (dataB instanceof Entity && dataA instanceof MapEvent) {
            ((MapEvent) dataA).trigger((Entity) dataB);
        }
        // ---------------------------------------------
        // Gestione del range del player
        // ---------------------------------------------
        // Se "dataA" è un Player e "dataB" è un CombatEntity (ma non un Player)
        // e se il filtro del fixtureB non indica il RANGE, aggiungi dataB al player.
        boolean isPlayerA = dataA instanceof Player;
        boolean isPlayerB = dataB instanceof Player;
        boolean isCombatB = dataB instanceof CombatEntity;
        boolean isNotRangeB = (fixtureB.getFilterData().categoryBits != EntityManager.RANGE);

        if (isPlayerA && !isPlayerB && isCombatB && isNotRangeB) {
            ((Player) dataA).addEntity((CombatEntity) dataB);
        }

        // ---------------------------------------------
        // Gestione del contatto tra Enemy e Player nel range
        // ---------------------------------------------
        // Se "dataA" è un Enemy e il filtro del fixtureA indica RANGE,
        // mentre "dataB" è un Player e il filtro del fixtureB non indica RANGE,
        // l'enemy aggiunge il player alla sua lista di entità.
        boolean isRangeA = (fixtureA.getFilterData().categoryBits == EntityManager.RANGE);
        boolean isRangeB = (fixtureB.getFilterData().categoryBits == EntityManager.RANGE);

        if (dataA instanceof Enemy && isRangeA && dataB instanceof Player && !isRangeB) {
            ((Enemy) dataA).addEntity((Player) dataB);
        }

    }

    @Override
    public void endContact(Contact contact) {
        // Recupera i fixture coinvolti nel contatto
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Salva i dati utente associati ai corpi (userData)
        Object dataA = fixtureA.getBody().getUserData();
        Object dataB = fixtureB.getBody().getUserData();

        // ----------------------------------------------------
        // Gestione del range del player (Rimozione entità)
        // Se dataA è un Player e dataB è un CombatEntity (ma non un Player)
        // e se il filtro del fixtureB non indica RANGE, rimuovo la CombatEntity dal Player.
        boolean isPlayerA = dataA instanceof Player;
        boolean isPlayerB = dataB instanceof Player;
        boolean isCombatB = dataB instanceof CombatEntity;
        boolean isNotRangeB = fixtureB.getFilterData().categoryBits != EntityManager.RANGE;

        if (isPlayerA && !isPlayerB && isCombatB && isNotRangeB) {
            ((Player) dataA).removeEntity((CombatEntity) dataB);
        }

        // ----------------------------------------------------
        // Gestione del contatto tra Enemy e Player nel range (Rimozione Player)
        // Se dataA è un Enemy associato a un fixture con filtro RANGE
        // e dataB è un Player (con filtro NON RANGE), rimuovo il Player dall'Enemy
        boolean isRangeA = fixtureA.getFilterData().categoryBits == EntityManager.RANGE;
        boolean isRangeB = fixtureB.getFilterData().categoryBits == EntityManager.RANGE;

        if (dataA instanceof Enemy && isRangeA && dataB instanceof Player && !isRangeB) {
            Enemy enemy1 = (Enemy) dataA;
            Player player = (Player) dataB;
            enemy1.removeEntity(player);
        }

        // ----------------------------------------------------
        // Gestione degli eventi della mappa
        // Se dataA è un Entity e dataB è un MapEvent, triggero l'evento
        // e disattivo il MapEvent

        // Gestione eventi della mappa, controllando entrambe le configurazioni:
        if (dataA instanceof Entity && dataB instanceof MapEvent) {
            ((MapEvent) dataB).setActive(false);
        } else if (dataB instanceof Entity && dataA instanceof MapEvent) {
            ((MapEvent) dataA).setActive(false);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
