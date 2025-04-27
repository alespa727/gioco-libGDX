package progetto.world;

import com.badlogic.gdx.physics.box2d.*;
import progetto.entity.Engine;
import progetto.entity.components.specific.general.BulletComponent;
import progetto.entity.components.specific.sensors.InRangeListComponent;
import progetto.entity.entities.base.Entity;
import progetto.entity.entities.specific.living.combat.Warrior;
import progetto.entity.entities.specific.living.combat.boss.Boss;
import progetto.entity.entities.specific.living.combat.enemy.Enemy;
import progetto.entity.entities.specific.notliving.Bullet;
import progetto.player.Player;
import progetto.world.events.base.MapEvent;
import progetto.world.events.specific.ChangeMap;
import progetto.world.map.Map;

public class CollisionManager implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        // Definizioni caratteristiche fisiche
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Salvataggio corpi
        Object dataA = fixtureA.getBody().getUserData();
        Object dataB = fixtureB.getBody().getUserData();

        if (dataA instanceof Bullet && dataB instanceof Map) {
            ((Bullet) dataA).unregister();
        }
        if (dataB instanceof Bullet && dataA instanceof Map) {
            ((Bullet) dataB).unregister();
        }


        // Gestione eventi della mappa, controllando entrambe le configurazioni:
        if (dataA instanceof Player && dataB instanceof ChangeMap) {
            ((MapEvent) dataB).setActive(true);
            ((MapEvent) dataB).trigger((Entity) dataA);
            return;
        } else if (dataB instanceof Player && dataA instanceof ChangeMap) {
            ((MapEvent) dataA).setActive(true);
            ((MapEvent) dataA).trigger((Entity) dataB);
            return;
        }
        // ---------------------------------------------
        // Gestione del range del player
        // ---------------------------------------------
        if (dataA instanceof Player && (dataB instanceof Enemy || dataB instanceof Boss)) {
            ((Player) dataA).components.get(InRangeListComponent.class).inRange.add((Warrior) dataB);
        }
        if (dataB instanceof Player && (dataA instanceof Enemy || dataA instanceof Boss)) {
            ((Player) dataB).components.get(InRangeListComponent.class).inRange.add((Warrior) dataA);
        }

        // ---------------------------------------------
        // Gestione del contatto tra Enemy e Player nel range
        // ---------------------------------------------
        // Se "dataA" è un Enemy e il filtro del fixtureA indica RANGE,
        // mentre "dataB" è un Player e il filtro del fixtureB non indica RANGE,
        // l'enemy aggiunge il player alla sua lista di entità.
        boolean isRangeA = (fixtureA.getFilterData().categoryBits == Engine.RANGE);
        boolean isRangeB = (fixtureB.getFilterData().categoryBits == Engine.RANGE);

        if (dataA instanceof Enemy && isRangeA && dataB instanceof Player && !isRangeB) {
            ((Enemy) dataA).addEntity((Player) dataB);
        }

        if (dataA instanceof Warrior && !isRangeA && dataB instanceof Bullet) {
            if (((Warrior) dataA).getHumanStates().isInvulnerable()) return;

            // ACCETTA anche sottoclassi di targetClass
            if (!((Bullet) dataB).getTargetClass().isAssignableFrom(dataA.getClass())) return;

            ((Warrior) dataA).hit((Entity) dataB, ((Bullet) dataB).components.get(BulletComponent.class).damage, 2);
            ((Bullet) dataB).unregister();
        }

        if (dataB instanceof Warrior && !isRangeA && dataA instanceof Bullet) {
            if (((Warrior) dataB).getHumanStates().isInvulnerable()) return;

            // ACCETTA anche sottoclassi di targetClass
            if (!((Bullet) dataA).getTargetClass().isAssignableFrom(dataB.getClass())) return;

            ((Warrior) dataB).hit((Entity) dataA, ((Bullet) dataA).components.get(BulletComponent.class).damage, 2);
            ((Bullet) dataA).unregister();
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
        if (dataA instanceof Player && (dataB instanceof Enemy || dataB instanceof Boss)) {
            ((Player) dataA).components.get(InRangeListComponent.class).inRange.removeValue((Warrior) dataB, false);
        }
        if (dataB instanceof Player && (dataA instanceof Enemy || dataA instanceof Boss)) {
            ((Player) dataB).components.get(InRangeListComponent.class).inRange.removeValue((Warrior) dataA, false);
        }

        // ----------------------------------------------------
        // Gestione del contatto tra Enemy e Player nel range (Rimozione Player)
        // Se dataA è un Enemy associato a un fixture con filtro RANGE
        // e dataB è un Player (con filtro NON RANGE), rimuovo il Player dall'Enemy
        boolean isRangeA = fixtureA.getFilterData().categoryBits == Engine.RANGE;
        boolean isRangeB = fixtureB.getFilterData().categoryBits == Engine.RANGE;

        if (dataA instanceof Enemy enemy1 && isRangeA && dataB instanceof Player player && !isRangeB) {
            enemy1.removeEntity(player);
        }

        // ----------------------------------------------------
        // Gestione degli eventi della mappa
        // Se dataA è un Entity e dataB è un MapEvent, triggero l'evento
        // e disattivo il MapEvent

        // Gestione eventi della mappa, controllando entrambe le configurazioni:
        if (dataA instanceof Player && dataB instanceof ChangeMap) {
            ((MapEvent) dataB).setActive(false);
        } else if (dataB instanceof Player && dataA instanceof ChangeMap) {
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
