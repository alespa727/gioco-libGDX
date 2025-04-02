package io.github.ale.screens.game.entities.types.neutral;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import io.github.ale.screens.game.entities.types.entity.EntityConfig;
import io.github.ale.screens.game.entities.types.mobs.LivingEntity;
import io.github.ale.screens.game.entities.types.neutral.states.NpcStates;
import io.github.ale.screens.game.manager.entity.EntityManager;

public abstract class NonCombatEntity extends LivingEntity {

    private final DefaultStateMachine<NonCombatEntity, NpcStates> stateMachine;

    public NonCombatEntity(EntityConfig config, EntityManager manager) {
        super(config, manager);
        stateMachine = new DefaultStateMachine<>(this);
    }

    public DefaultStateMachine<NonCombatEntity, NpcStates> statemachine() {
        return stateMachine;
    }

    @Override
    public void create() {

    }
}
