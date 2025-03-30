package io.github.ale.screens.game.map.events;

import com.badlogic.gdx.physics.box2d.*;
import io.github.ale.screens.game.entities.player.Player;
import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.entityType.combat.CombatEntity;
import io.github.ale.screens.game.entityType.enemy.Enemy;
import io.github.ale.screens.game.entityType.entity.Entity;
import io.github.ale.screens.game.entityType.mobs.LivingEntity;

public class EventListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object dataA = fixtureA.getBody().getUserData();
        Object dataB = fixtureB.getBody().getUserData();

        if (dataA instanceof Entity && dataB instanceof MapEvent) {
            ((MapEvent) dataB).trigger((Entity) dataA);
        }

        boolean isPlayerA = dataA instanceof Player;
        boolean isPlayerB = dataB instanceof Player;
        boolean isCombatB = dataB instanceof CombatEntity;
        boolean isNotRangeB = fixtureB.getFilterData().categoryBits != EntityManager.RANGE;
        if (!isPlayerB && isPlayerA && isCombatB && isNotRangeB) {
            ((Player) dataA).addEntity((CombatEntity) dataB);
            System.out.println("Added combat entity");
        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object dataA = fixtureA.getBody().getUserData();
        Object dataB = fixtureB.getBody().getUserData();

        boolean isPlayerA = dataA instanceof Player;
        boolean isPlayerB = dataB instanceof Player;
        boolean isCombatB = dataB instanceof CombatEntity;
        boolean isNotRangeB = fixtureB.getFilterData().categoryBits != EntityManager.RANGE;
        if (!isPlayerB && isPlayerA && isCombatB && isNotRangeB) {
            ((Player) dataA).removeEntity((CombatEntity) dataB);
            System.out.println("Removed combat entity");
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
