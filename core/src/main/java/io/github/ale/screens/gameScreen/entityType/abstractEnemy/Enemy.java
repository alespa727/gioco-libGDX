package io.github.ale.screens.gameScreen.entityType.abstractEnemy;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.gameScreen.entityType.EntityManager;
import io.github.ale.screens.gameScreen.entityType.abstractEnemy.enemyStates.EnemyStates;
import io.github.ale.screens.gameScreen.entityType.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entityType.combatEntity.CombatEntity;

public abstract class Enemy extends CombatEntity {

    public DefaultStateMachine<Enemy, EnemyStates> statemachine;

    public Enemy(EntityConfig config, EntityManager manager, float attackcooldown) {
        super(config, manager, attackcooldown);
        this.range = new Rectangle(0, 0, 1.5f, 1.5f);
        statemachine = new DefaultStateMachine<>(this);
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
        attackCooldown(delta);
        patrollingCooldown(delta);
    }
}
