package io.github.ale.screens.game.entityType.enemy;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.entityType.enemy.enemyStates.EnemyStates;
import io.github.ale.screens.game.entityType.entity.EntityConfig;
import io.github.ale.screens.game.entityType.combat.CombatEntity;

public abstract class Enemy extends CombatEntity {

    public float viewDistance;
    public float pursueMaxDistance;

    public DefaultStateMachine<Enemy, EnemyStates> statemachine;

    public Enemy(EntityConfig config, EntityManager manager, float attackcooldown) {
        super(config, manager, attackcooldown);
        statemachine = new DefaultStateMachine<>(this);
        viewDistance = 9f;
        pursueMaxDistance = 13f;
        statemachine.setInitialState(EnemyStates.PATROLLING);
    }

    public DefaultStateMachine<Enemy, EnemyStates> statemachine(){ return statemachine;}

    @Override
    public void updateEntityType(float delta) {
        statemachine.update();
    }

    public void evade(float x, float y) {
        float directionX = x - coordinateCentro().x;
        float directionY = y - coordinateCentro().y;

        Vector2 oppositeDirection = new Vector2(-directionX, -directionY);

        pathfinder().renderPath(coordinateCentro().x + oppositeDirection.x, coordinateCentro().y + oppositeDirection.y, delta);
    }

    @Override
    public void cooldown(float delta) {
        damageCooldown(delta);
        attackCooldown();
        patrollingCooldown(delta);
    }
}
