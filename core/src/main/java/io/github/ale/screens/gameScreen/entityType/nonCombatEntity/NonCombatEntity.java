package io.github.ale.screens.gameScreen.entityType.nonCombatEntity;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import io.github.ale.screens.gameScreen.entityType.EntityManager;
import io.github.ale.screens.gameScreen.entityType.abstractEntity.EntityConfig;
import io.github.ale.screens.gameScreen.entityType.livingEntity.LivingEntity;
import io.github.ale.screens.gameScreen.entityType.nonCombatEntity.states.NpcStates;

public abstract class NonCombatEntity extends LivingEntity {

    private DefaultStateMachine<NonCombatEntity, NpcStates> stateMachine;

    public NonCombatEntity(EntityConfig config, EntityManager manager) {
        super(config, manager);
        stateMachine = new DefaultStateMachine<>(this);
    }

    public DefaultStateMachine<NonCombatEntity, NpcStates> statemachine(){ return stateMachine;}

    @Override
    public void updateEntityType() {

    }

    @Override
    public void create() {

    }
}
