package io.github.ale.screens.game.entityType.nonCombatEntity;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.entityType.abstractEntity.EntityConfig;
import io.github.ale.screens.game.entityType.livingEntity.LivingEntity;
import io.github.ale.screens.game.entityType.nonCombatEntity.states.NpcStates;

public abstract class NonCombatEntity extends LivingEntity {

    private DefaultStateMachine<NonCombatEntity, NpcStates> stateMachine;

    public NonCombatEntity(EntityConfig config, EntityManager manager) {
        super(config, manager);
        stateMachine = new DefaultStateMachine<>(this);
    }

    public DefaultStateMachine<NonCombatEntity, NpcStates> statemachine(){ return stateMachine;}

    @Override
    public void create() {

    }
}
