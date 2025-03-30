package io.github.ale.screens.game.entityType.enemy;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.cooldown.Cooldown;
import io.github.ale.screens.game.entities.skill.skillist.enemy.Slash;
import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.entityType.enemy.enemyStates.EnemyStates;
import io.github.ale.screens.game.entityType.entity.EntityConfig;
import io.github.ale.screens.game.entityType.combat.CombatEntity;

public abstract class Enemy extends CombatEntity {

    public float viewDistance;
    public float pursueMaxDistance;

    public Cooldown attack;

    public DefaultStateMachine<Enemy, EnemyStates> statemachine;

    public Enemy(EntityConfig config, EntityManager manager, float attackcooldown) {
        super(config, manager);
        attack = new Cooldown(attackcooldown);
        attack.reset(0f);
        statemachine = new DefaultStateMachine<>(this);
        viewDistance = 9f;
        pursueMaxDistance = 13f;
        statemachine.setInitialState(EnemyStates.PATROLLING);
        skillset().add(new Slash(this, "", "", 10));
    }

    @Override
    public void updateEntityType(float delta) {
        statemachine.update();
    }

    @Override
    public void hit(CombatEntity entity, float damage) {
        super.hit(entity, damage);
        movement().reset();
    }

    @Override
    public void cooldown(float delta) {
        damageCooldown(delta);
    }

    public abstract void attack();
}
