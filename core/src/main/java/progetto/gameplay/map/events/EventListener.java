package progetto.gameplay.map.events;

import com.badlogic.gdx.physics.box2d.*;
import progetto.gameplay.entity.types.notliving.Bullet;
import progetto.gameplay.entity.types.living.combat.Warriors;
import progetto.gameplay.entity.types.living.combat.enemy.Enemy;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.living.combat.player.Player;
import progetto.gameplay.manager.ManagerEntity;

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
        boolean isCombatB = dataB instanceof Warriors;
        boolean isNotRangeB = (fixtureB.getFilterData().categoryBits != ManagerEntity.RANGE);

        if (isPlayerA && !isPlayerB && isCombatB && isNotRangeB) {
            ((Player) dataA).addEntity((Warriors) dataB);
        }

        // ---------------------------------------------
        // Gestione del contatto tra Enemy e Player nel range
        // ---------------------------------------------
        // Se "dataA" è un Enemy e il filtro del fixtureA indica RANGE,
        // mentre "dataB" è un Player e il filtro del fixtureB non indica RANGE,
        // l'enemy aggiunge il player alla sua lista di entità.
        boolean isRangeA = (fixtureA.getFilterData().categoryBits == ManagerEntity.RANGE);
        boolean isRangeB = (fixtureB.getFilterData().categoryBits == ManagerEntity.RANGE);

        if (dataA instanceof Enemy && isRangeA && dataB instanceof Player && !isRangeB) {
            ((Enemy) dataA).addEntity((Player) dataB);
        }

        if (dataA instanceof Warriors && !isRangeA && dataB instanceof Bullet) {
            if (((Bullet) dataB).getOwner() == dataA) return;
            if(((Warriors) dataA).isInvulnerable()) return;
            ((Warriors) dataA).hit((Entity) dataB, ((Bullet) dataB).damage,2);
            ((Bullet) dataB).despawn();
        }
        if (dataB instanceof Warriors && !isRangeA && dataA instanceof Bullet) {
            if (((Bullet) dataA).getOwner() == dataB) return;
            if(((Warriors) dataB).isInvulnerable()) return;
            ((Warriors) dataB).hit((Entity) dataA, ((Bullet) dataA).damage, 2);
            ((Bullet) dataA).despawn();
        }

        if ("map".equals(dataB) && dataA instanceof Bullet) {
            ((Bullet) dataA).despawn();
        }
        if ("map".equals(dataA) && dataB instanceof Bullet) {
            ((Bullet) dataB).despawn();
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
        boolean isCombatB = dataB instanceof Warriors;
        boolean isNotRangeB = fixtureB.getFilterData().categoryBits != ManagerEntity.RANGE;

        if (isPlayerA && !isPlayerB && isCombatB && isNotRangeB) {
            ((Player) dataA).removeEntity((Warriors) dataB);
        }

        // ----------------------------------------------------
        // Gestione del contatto tra Enemy e Player nel range (Rimozione Player)
        // Se dataA è un Enemy associato a un fixture con filtro RANGE
        // e dataB è un Player (con filtro NON RANGE), rimuovo il Player dall'Enemy
        boolean isRangeA = fixtureA.getFilterData().categoryBits == ManagerEntity.RANGE;
        boolean isRangeB = fixtureB.getFilterData().categoryBits == ManagerEntity.RANGE;

        if (dataA instanceof Enemy enemy1 && isRangeA && dataB instanceof Player player && !isRangeB) {
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
