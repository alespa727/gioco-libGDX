package io.github.ale.screens.game.entityType.neutral;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.entityType.entity.EntityConfig;
import io.github.ale.screens.game.entityType.mobs.LivingEntity;
import io.github.ale.screens.game.entityType.neutral.states.NpcStates;

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
