package io.github.ale.screens.game.entities.entityTypes.neutral;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import io.github.ale.screens.game.manager.EntityManager;
import io.github.ale.screens.game.entities.entityTypes.entity.EntityConfig;
import io.github.ale.screens.game.entities.entityTypes.mobs.LivingEntity;
import io.github.ale.screens.game.entities.entityTypes.neutral.states.NpcStates;

public abstract class NonCombatEntity extends LivingEntity {

    private final DefaultStateMachine<NonCombatEntity, NpcStates> stateMachine;

    public NonCombatEntity(EntityConfig config, EntityManager manager) {
        super(config, manager);
        stateMachine = new DefaultStateMachine<>(this);
    }

    public DefaultStateMachine<NonCombatEntity, NpcStates> statemachine(){ return stateMachine;}

    @Override
    public void create() {

    }
}
