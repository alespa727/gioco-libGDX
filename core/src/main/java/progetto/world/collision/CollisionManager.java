package progetto.world.collision;

import com.badlogic.gdx.physics.box2d.*;
import progetto.ECS.EntityEngine;
import progetto.ECS.components.specific.general.BulletComponent;
import progetto.ECS.components.specific.sensors.InRangeListComponent;
import progetto.ECS.entities.Entity;
import progetto.ECS.entities.specific.living.combat.Warrior;
import progetto.ECS.entities.specific.living.combat.boss.Boss;
import progetto.ECS.entities.specific.living.combat.enemy.BaseEnemy;
import progetto.ECS.entities.specific.notliving.Bullet;
import progetto.core.game.player.Player;
import progetto.world.events.base.MapEvent;
import progetto.world.events.specific.ChangeMap;
import progetto.world.map.Map;


public class CollisionManager implements ContactListener {

    /**
     * Eseguito quando due oggetti iniziano a toccarsi.
     *
     * Funzioni:
     * - Distrugge proiettili che colpiscono la mappa.
     * - Attiva eventi (es. cambio mappa) quando il player li tocca.
     * - Applica danno a nemici colpiti da proiettili.
     */
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

        if (dataA instanceof MapEvent && dataB instanceof Entity) {
            ((MapEvent) dataA).trigger((Entity) dataB);
            return;
        } else if (dataB instanceof MapEvent && dataA instanceof Entity) {
            ((MapEvent) dataB).trigger((Entity) dataA);
            return;
        }

        if (dataA instanceof Player && (dataB instanceof BaseEnemy || dataB instanceof Boss)) {
            ((Player) dataA).components.get(InRangeListComponent.class).inRange.add((Warrior) dataB);
        }
        if (dataB instanceof Player && (dataA instanceof BaseEnemy || dataA instanceof Boss)) {
            ((Player) dataB).components.get(InRangeListComponent.class).inRange.add((Warrior) dataA);
        }

        boolean isRangeA = (fixtureA.getFilterData().categoryBits == EntityEngine.RANGE);
        boolean isRangeB = (fixtureB.getFilterData().categoryBits == EntityEngine.RANGE);

        if (dataA instanceof BaseEnemy && isRangeA && dataB instanceof Player && !isRangeB) {
            if (((BaseEnemy) dataA).contains(InRangeListComponent.class)) {
                ((BaseEnemy) dataA).get(InRangeListComponent.class).inRange.add((Warrior) dataB);
            }
        }

        if (dataB instanceof BaseEnemy && isRangeB && dataA instanceof Player && !isRangeA) {
            if (((BaseEnemy) dataB).contains(InRangeListComponent.class)) {
                ((BaseEnemy) dataB).get(InRangeListComponent.class).inRange.add((Warrior) dataA);
            }
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

    /**
     * Eseguito quando due oggetti smettono di toccarsi.
     *
     * Funzioni:
     * - Rimuove nemici dalla lista "in range" del player.
     * - Disattiva eventi (es. cambio mappa) quando il player si allontana.
     */
    @Override
    public void endContact(Contact contact) {
        // Recupera i fixture coinvolti nel contatto
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Salva i dati utente associati ai corpi (userData)
        Object dataA = fixtureA.getBody().getUserData();
        Object dataB = fixtureB.getBody().getUserData();


        if (dataA instanceof Player && (dataB instanceof BaseEnemy || dataB instanceof Boss)) {
            ((Player) dataA).components.get(InRangeListComponent.class).inRange.removeValue((Warrior) dataB, false);
        }
        if (dataB instanceof Player && (dataA instanceof BaseEnemy || dataA instanceof Boss)) {
            ((Player) dataB).components.get(InRangeListComponent.class).inRange.removeValue((Warrior) dataA, false);
        }

        boolean isRangeA = fixtureA.getFilterData().categoryBits == EntityEngine.RANGE;
        boolean isRangeB = fixtureB.getFilterData().categoryBits == EntityEngine.RANGE;

        if (dataA instanceof BaseEnemy baseEnemy1 && isRangeA && dataB instanceof Player player && !isRangeB) {
            baseEnemy1.removeEntity(player);
        }
        // Gestione eventi della mappa
        if (dataA instanceof Player && dataB instanceof ChangeMap) {
            ((MapEvent) dataB).setActive(false);
            return;
        } else if (dataB instanceof Player && dataA instanceof ChangeMap) {
            ((MapEvent) dataA).setActive(false);
            return;
        }

        if (dataA instanceof MapEvent && dataB instanceof Entity) {
            ((MapEvent) dataA).trigger((Entity) dataB);
            return;
        } else if (dataB instanceof MapEvent && dataA instanceof Entity) {
            ((MapEvent) dataB).trigger((Entity) dataA);
            return;
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
